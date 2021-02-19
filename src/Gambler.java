import java.util.ArrayList;
import java.util.Arrays;

public class Gambler {
    private Input sc = new Input();
    private MiniGamer miniGamer = new MiniGamer();
    private ArrayList<String> options = new ArrayList<>(Arrays.asList("S", "R", "W", "C", "U", "E"));
    private ArrayList<String> gameOptions = new ArrayList<>(Arrays.asList("R", "C", "N", "E"));

    private int storage;
    private int level;
    private double rate;
    private int checkNumber;

    public Gambler(){
        this.storage = 1;
        this.rate = 1.05;
        this.level = 1;
        this.checkNumber = 0;
    }

    public void gamble (Player player) throws InterruptedException {
        do {
            welcome();
            String choice = sc.getInput("What would you like to do here?", options, false).toUpperCase();
            if(choice.equalsIgnoreCase("e")){
                System.out.printf("Good luck out there %s%n", player.getName());
                Thread.sleep(200);
                break;
            }
            handleChoice(choice, player);
        } while (true);
    }

    private void handleChoice(String choice, Player player) throws InterruptedException {
        updateBankOnCheckNumber(player);
        switch (choice){
            case "S":
                stash(player);
                break;
            case "R":
                risk(player);
                break;
            case "W":
                take(player);
                break;
            case "C":
                check();
                break;
            case "U":
                upgrade(player);
                break;
        }
    }

    private void risk(Player player) throws InterruptedException {
        gameMenu();
        String choice = sc.getInput("What would you like to play?", gameOptions, false);
        if(choice.equalsIgnoreCase("e")){
            return;
        }
        int amount = sc.getNum("How much are we gambling here?", player.getWallet());
        games(player, choice, amount);
    }

    private void games(Player player, String choice, int bet) throws InterruptedException {
        boolean won = false;
        int mult = 1;
        if(choice.equalsIgnoreCase("n")){
            won = miniGamer.numberGame();
            mult = 5;
        } else if(choice.equalsIgnoreCase("c")){
            won = miniGamer.coinGame();
            mult = 2;
        } else if(choice.equalsIgnoreCase("r")){
            won = miniGamer.rockPaperScissor();
            mult = 3;
        }
        if(won){
            int total = bet * mult;
            System.out.printf("Nice one, you won %dx, a total of %d%n", mult, total);
            player.setWallet(player.getWallet() + total);
        } else {
            player.setWallet(player.getWallet() - bet);
            System.out.println("Try you luck next time");
        }
    }

    private void gameMenu(){
        System.out.printf("\033[0;35m|%-67s|%n", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[N]umber Guess", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[R]ock Paper Scissor", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[C]oin Toss", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[E]xit", " ");
        System.out.printf("|%-67s|%n\033[0;38m", " ");
    }

    private void stash(Player player) throws InterruptedException {
        int total = sc.getNum("How much are we saving? Current Wallet : " + player.getWallet(), player.getWallet());
        this.setStorage(this.getStorage() + total);
        player.setWallet(player.getWallet() - total);
        System.out.println("Thank you, deposited : " + total);
    }
    private void take(Player player) throws InterruptedException {
        int total = sc.getNum("How much are we taking? Current storage : " + this.getStorage(), this.getStorage());
        player.setWallet(player.getWallet() + total);
        this.setStorage(this.getStorage() - total);
        System.out.println("Thank you, withdrawn : " + total);
    }
    private void check(){
        System.out.printf("The current storage contains %d coin%n", this.getStorage());
    }
    private void upgrade(Player player){
        int cost = this.getLevel() * 25;
        int wallet = player.getWallet();
        System.out.printf("Current rate is this : %.3f%n", this.getRate());
        System.out.printf("Cost %d coin to increase%n", cost);
        if(wallet >= cost) {
            String choice = sc.getInput("Would you like to upgrade? [Y]es / [N]o");
            upgradeHandler(choice, cost, player);
        } else {
            System.out.println("You can not afford this service, come back later");
        }
    }

    private void upgradeHandler(String choice, int cost, Player player){
        if (choice.equalsIgnoreCase("y")){
            this.setLevel(this.getLevel() + 1);
            this.setRate(this.getRate() + .1);
            player.setWallet(player.getWallet() - cost);
        } else {
            System.out.println("No problem, spend wisely");
        }
    }

    private void updateBankOnCheckNumber(Player player){
        int past = this.getCheckNumber();
        int current = player.getVictories();
        int daysPast = current - past;
        System.out.println(daysPast + " day(s) have past");
        int value = this.getStorage();
        double rate = this.getRate();
        for(int i = 0; i < daysPast; i++){
            value*=rate;
        }
        this.setStorage(value);
        this.setCheckNumber(current);
    }

    private static void welcome(){
        System.out.printf("\033[0;36m|%-67s|%n", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "Welcome", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[S]tash", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[R]isk", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[W]ithdraw", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[C]heck", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[U]pgrade", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[E]xit", " ");
        System.out.printf("|%-67s|%n\033[0;38m", " ");
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(int checkNumber) {
        this.checkNumber = checkNumber;
    }
}
