import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class FileReader {

    private String directoryName;
    private String fileName;
    private String logFileName;
    private Path directoryPath;
    private Path filePath;
    private Path logFilePath;
    private List<String> fileLines;
    private List<String> logFileLines;

    public FileReader(String directoryName, String fileName, String logFileName) throws IOException {
        this.directoryName = directoryName;
        this.fileName = fileName;
        this.logFileName = logFileName;
        this.directoryPath = Paths.get(directoryName);
        this.filePath = Paths.get(directoryName, fileName);
        this.logFilePath = Paths.get(directoryName, logFileName);

        if (Files.notExists(this.logFilePath)) {
            try {
                Files.createFile(this.logFilePath);

            } catch (IOException e) {
                throw new IOException("Unable to create the logfile (" + this.logFileName + ")!");

            }
        }
        if (Files.notExists(this.directoryPath)) {
            try {
                Files.createDirectories(this.directoryPath);
            } catch (IOException e) {
                Files.write(this.logFilePath, Arrays.asList(e.getMessage()), StandardOpenOption.APPEND);

                throw new IOException("Unable to create the data directory (" + this.directoryName + ")!");
            }
        }
        if (Files.notExists(this.filePath)) {
            try {
                Files.createFile(this.filePath);
            } catch (IOException e) {
                Files.write(this.logFilePath, Arrays.asList(e.getMessage()), StandardOpenOption.APPEND);

                throw new IOException("Unable to create the data file (" + this.fileName + ")!");
            }
        }

        this.fileLines = Files.readAllLines(this.filePath);
    }

    public void writeToLog(String message) throws IOException {
        try {
            Files.write(this.getLogFilePath(), Arrays.asList(message), StandardOpenOption.APPEND);
        } catch(IOException e) {
            Files.write(this.logFilePath, Arrays.asList(e.getMessage()), StandardOpenOption.APPEND);
            throw new IOException("Unable to write exception message to log file!");
        }
    }
    public void saveRecord(Player player, Gambler gambler) throws IOException {
        StringBuilder statList = new StringBuilder();
        for(Map.Entry<String, Integer> stat : player.getStats().entrySet()){
            statList.append(stat.getKey()).append(" ").append(stat.getValue()).append("\n");
        }
        writeToLog(player.getName() + " as a " + player.getType() + " won " + player.getVictories() + " battles at level " + player.getLevel() + " played at : " + new Date());
        writeToLog("You had " + gambler.getStorage() + " saved up");
        writeToLog("Wallet : " + player.getWallet());
        writeToLog("Your stats : " + statList.toString());
        writeToLog("Max Health : " + player.getMaxHealth());
        writeToLog("Mp : " + player.getMp() + "/" + player.getMaxMp());
        writeToLog("exp : " + player.getExp());
        writeToLog("exp Req : " + player.getExpRequirement());
        writeToLog("Rate : " + gambler.getRate());
        writeToLog("Gambler lvl : " + gambler.getLevel());
        writeToLog("Inventory : \n" + player.getInventory());
        writeToLog("%");
    }


    public void overWriteLog(String message) throws IOException {
        try {
            Files.write(this.getLogFilePath(), Arrays.asList(message));
        } catch(IOException e) {
            Files.write(this.logFilePath, Arrays.asList(e.getMessage()), StandardOpenOption.APPEND);
            throw new IOException("Unable to write exception message to log file!");
        }
    }

    public void createSaveState(Player player, Gambler gambler) throws IOException {
        StringBuilder statList = new StringBuilder();
        StringBuilder fullSave = new StringBuilder();
        for(Map.Entry<String, Integer> stat : player.getStats().entrySet()){
            statList.append(stat.getKey()).append(" ").append(stat.getValue()).append("\n");
        }
        fullSave.append(player.getName()).append(" as a ").append(player.getType()).append(" won ").append(player.getVictories()).append(" battles at level ").append(player.getLevel()).append(" played at : ").append(new Date()).append("\n");
        fullSave.append("You had ").append(gambler.getStorage()).append(" saved up").append("\n");
        fullSave.append("Wallet : ").append(player.getWallet()).append("\n");
        fullSave.append("Your stats : ").append(statList.toString());
        fullSave.append("Max Health : ").append(player.getMaxHealth()).append("\n");
        fullSave.append("Mp : ").append(player.getMp()).append("/").append(player.getMaxMp()).append("\n");
        fullSave.append("exp : ").append(player.getExp()).append("\n");
        fullSave.append("exp Req : ").append(player.getExpRequirement()).append("\n");
        fullSave.append("Rate : ").append(gambler.getRate()).append("\n");
        fullSave.append("Gambler lvl : ").append(gambler.getLevel()).append("\n");
        fullSave.append(player.getState()).append("\n");
        fullSave.append("Inventory : \n");
        for(Map.Entry<String, Integer> item : player.getInventory().entrySet()){
            fullSave.append(item.getKey()).append(" ").append(item.getValue()).append("\n");
        }
        overWriteLog(fullSave.toString());
    }

    public Path getLogFilePath() {
        return logFilePath;
    }

    public java.util.List<String> getFileLines() {
        return fileLines;
    }
}