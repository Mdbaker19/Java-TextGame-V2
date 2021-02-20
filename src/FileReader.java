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
        writeToLog("Your stats : " + statList.toString());
        writeToLog("___________________________________________________________________________________________________________");
    }

    public Path getLogFilePath() {
        return logFilePath;
    }
}