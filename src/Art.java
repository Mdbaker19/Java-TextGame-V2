import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Art {
    private List<String> mainOptions = new ArrayList<>();
    private Input sc = new Input();
    public Art(){}

    public void mainScreen(){
        mainOptions = new ArrayList<>();
        System.out.printf("\033[0;33m|%-60s|%n", " ");
        System.out.printf("%-20s%s%n", " ", "[B]attle");
        System.out.printf("%-20s%s%n", " ", "[S]hop");
        System.out.printf("%-20s%s%n", " ", "[A]ccount");
        System.out.printf("%-20s%s%n", " ", "[V]iew Stats");
        System.out.printf("%-20s%s%n", " ", "[E]nd Game");
        System.out.printf("|%-60s|%n\033[0;38m", " ");
        mainOptions.add("B");
        mainOptions.add("S");
        mainOptions.add("A");
        mainOptions.add("V");
        mainOptions.add("E");
    }

    public void welcome(){
        for(int i = 0; i < 4; i++){
            System.out.printf("%-5s|%-219s-|%n", " ", "-");
        }
        System.out.printf("%-5s|-%-102s %s %-103s-|%n"," ", " ", "JAVENTURE……", " ");
        for(int i = 0; i < 4; i++){
            System.out.printf("%-5s|%-219s-|%n", " ", "-");
        }
    }
    public void welcomeOptions(){
        System.out.printf("%-86s%-25s%-25s%-25s%n", " ", "[A]bout", "[S]tart", "[G]amePlay Help");
    }
    public void about(){
        System.out.println("An RPG game with 3 different classes to choose from, all with different base stats.");
        System.out.println("To play the game enter the options as they are presented ( the letter in [ ] or the text after the option 'x' : ");
        System.out.println("Fight in battles of random enemies to gain exp to level up and coins to spend in the shop");
        System.out.println("In battle you can use a standard attack, check the enemy stats or try for a special attack that it's success rate is based of your current magic stat");
        System.out.println("Enemies will progressively get harder and harder with better rewards as well.");
        System.out.println("If you happen to die and have a revive it is automatically used to give you an extra fighting chance");
        System.out.println("The account is a place to try your luck for some extra coin in the gambler or save your money and gain some interest on it over every victory in battle");
        System.out.println("Rock paper scissors gives 3x money, Number guesser gives 5x and Coin toss is 2x");
        System.out.println("Good luck player");
    }
    public void gamePlayInfo(){
        System.out.println("Leveling up recovers MP to the previous Max MP, HP does not recover on level up");
        System.out.println("Special contains a few different options, all of which last through the battle if it was a successfully cast.");
        System.out.println("Magic Summon calls an ally into battle that deals 3% of enemy health per turn and has a 20% chance to block for you");
        System.out.println("Blind increases you dodge rate by 2x");
        System.out.println("Poison causes 5% enemy damage per turn");
        System.out.println("If you are poisoned you are indefinitely until an antidote is used");
        System.out.println("Normal enemies have a 5% chance to poison you, a more venomous enemy, 'cough' has 10x the chance to poison you");
        System.out.println("Some enemies are smarter than others and it is impossible to dodge them but, they can not poison you");
        System.out.println("Some enemies can put you to sleep, you need a clock to wake up or try you luck every turn with a 35% chance to wake up");
        System.out.println("Steal the enemy to steal 50% of their coin reward, reduces coin reward by 15% each time");
        System.out.println("Confuse the enemy to have a 35% chance of them hitting themselves");
        System.out.println("Defend to reduce damage by 1/2");
        System.out.println("Time to reduce enemy dodge by 1/2");
    }

    public void shopMenu(Player player) throws InterruptedException {
        do {
            System.out.printf("\033[0;32m|%-69s|%n", " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[P]otion", " ", 10, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[H]igh Potion", " ", 30, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[M]ega Potion", " ", 65, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[A]ntidote", " ", 10, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[E]ther", " ", 25, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[C]lock", " ", 15, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[B]omb", " ", 35, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[R]evive", " ", 100, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[W]eapon", " ", 95, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[G]uard", " ", 95, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[S]tudy", " ", 95, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[F]ade", " ", 95, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[L]earn", " ", 95, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[D]evelop", " ", 95, " ");
            System.out.printf("|%-20s%-20s%-5s%-24s|%n", " ", "[I]nfo", " ", " ");
            System.out.printf("|%-20s%-20s%-5s%-24s|%n", " ", "[Q]uit", " ", " ");
            System.out.printf("|%-69s|%n\033[0;38m", " ");
            Thread.sleep(600);

            String option = sc.getInput("Welcome "+player.getName()+", you have "+player.getWallet()+" ¢, What would you like?");
            Thread.sleep(600);

            if(option.equalsIgnoreCase("q")) break;
            handleChoice(player, option);
        } while (true);
        System.out.println("Thank you "+player.getName());
    }


    public void handleChoice(Player player, String choice) throws InterruptedException {
        HowMany simplified = null;
        int savings = player.getWallet();
        if (choice.equalsIgnoreCase("p") && savings >= 10) {
            simplified = new HowMany(10, "Potion", player);
            quantity(simplified.getAmount(), 10, player, "Potion");

        } else if (choice.equalsIgnoreCase("h") && savings >= 30) {
            simplified = new HowMany(30, "High Potion", player);
            quantity(simplified.getAmount(), 30, player, "Hpotion");

        } else if (choice.equalsIgnoreCase("a") && savings >= 10) {
            simplified = new HowMany(10, "Antidote", player);
            quantity(simplified.getAmount(), 10, player, "Antidote");

        } else if (choice.equalsIgnoreCase("c") && savings >= 15) {
            simplified = new HowMany(15, "Clock", player);
            quantity(simplified.getAmount(), 15, player, "Clock");

        } else if (choice.equalsIgnoreCase("r") && savings >= 105) {
            simplified = new HowMany(105, "Revive", player);
            quantity(simplified.getAmount(), 105, player, "Revive");

        } else if (choice.equalsIgnoreCase("b") && savings >= 35) {
            simplified = new HowMany(35, "Bomb", player);
            quantity(simplified.getAmount(), 35, player, "Bomb");

        } else if (choice.equalsIgnoreCase("m") && savings >= 65) {
            simplified = new HowMany(65, "Mega Potion", player);
            quantity(simplified.getAmount(), 65, player, "Mega");

        } else if (choice.equalsIgnoreCase("e") && savings >= 25) {
            simplified = new HowMany(25, "Ether", player);
            quantity(simplified.getAmount(), 25, player, "Ether");

        } else if ((choice.equalsIgnoreCase("d") || choice.equalsIgnoreCase("w") || choice.equalsIgnoreCase("f") || choice.equalsIgnoreCase("g") || choice.equalsIgnoreCase("s") || choice.equalsIgnoreCase("l")) && savings >= 95) {
            simplified = new HowMany(95, "Booster", player);
            statBooster(simplified.getAmount(), 95, player, choice);

        } else if (choice.equalsIgnoreCase("i")) {
            info();
            return;
        }
        if(simplified != null){
            System.out.println("Sounds good, here is your " + simplified.getItemName());
        } else {
            System.out.println("Do not have it or you can not afford it.");
            Thread.sleep(600);
            return;
        }
        Thread.sleep(600);

    }

    private void quantity(int x, int price, Player player, String item){
        int total = x * price;
        player.setWallet(player.getWallet() - total);
        for(int i = 0; i < x; i++) {
            player.addItemToInventory(item);
        }
    }

    private void statBooster(int amount, int price, Player player, String statToUpgrade){
        if(statToUpgrade.equalsIgnoreCase("w")){
            statToUpgrade = "Attack";
        } else if(statToUpgrade.equalsIgnoreCase("g")){
            statToUpgrade = "Defense";
        } else if (statToUpgrade.equalsIgnoreCase("f")){
            statToUpgrade = "Speed";
        } else if (statToUpgrade.equalsIgnoreCase("s")){
            statToUpgrade = "Magic";
        } else if (statToUpgrade.equalsIgnoreCase("l")){
            int currMp = player.getMp();
            int currMaxMp = player.getMaxMp();
            player.setMp(currMp + 5);
            player.setMaxMp(currMaxMp + 5);
            int total = amount * price;
            player.setWallet(player.getWallet() - total);
            return;
        } else if (statToUpgrade.equalsIgnoreCase("d")){
            int currHp = player.getHealth();
            int currMaxHp = player.getMaxHealth();
            player.updateStat("Health", currHp + 5);
            player.setMaxHealth(currMaxHp + 5);
            int total = amount * price;
            player.setWallet(player.getWallet() - total);
            return;
        }
        int total = amount * price;
        player.setWallet(player.getWallet() - total);
        int currentStatValue = player.getStats().get(statToUpgrade);
        player.updateStat(statToUpgrade, currentStatValue + amount);
    }

    public List<String> getMainOptions(){
        return this.mainOptions;
    }

    public void hud(Player player){
        List<String> status = player.getState();
        System.out.printf("\033[0;34m|%-70s|%n", " ");
        System.out.printf("|%-20s%-25s\033[0;31m%-5d / %-17d\033[0;34m|%n", " ", "Health : " , player.getHealth(), player.getMaxHealth());
        System.out.printf("|%-20s%-25s\033[0;34m%-5d / %-17d\033[0;34m|%n", " ", "Mp : " , player.getMp(), player.getMaxMp());
        if(status.size() > 0){
            for(String s : status) {
                System.out.printf("|%-20s%-22s%-28s|%n", " ", "Condition: ", s);
            }
        } else {
            System.out.printf("|\033[0;32m%-20s%-25s%-25s\033[0;34m|%n", " ", "Status: ", "normal");
        }
        System.out.printf("|%-20s%-22s%-28s|%n", " ", "[A]ttack", " ");
        System.out.printf("|%-20s%-22s%-28s|%n", " ", "[S]pecial", " ");
        System.out.printf("|%-20s%-22s%-28s|%n", " ", "[I]nventory", " ");
        System.out.printf("|%-20s%-22s%-28s|%n", " ", "[C]heck Enemy", " ");
        System.out.printf("|%-70s|%n\033[0;38m", " ");
    }

    public void enemyHud(Enemy enemy, List<String> ailments){
        System.out.println();
        System.out.printf("\033[0;32m|%-70s|%n", " ");
        System.out.printf("\033[0;32m|%-20s%-25s\033[0;31m%-5d / %-17d\033[0;32m|%n", " ", "Health : " , enemy.getHealth(), enemy.getMaxHealth());
        if(ailments.size() > 0){
            for(String a : ailments) {
                System.out.printf("|%-20s%-25s%-25s|%n", " ", "Condition: ", a);
            }
        } else {
            System.out.printf("|%-20s%-25s%-25s|%n\033[0;38m", " ", "Status: ", "normal");
        }
        System.out.printf("\033[0;32m|%-70s|%n", " ");
        System.out.println("\033[0;38m");
    }

    public void specials(){
        System.out.printf("\033[0;35m|%-70s|%n", " ");
        System.out.printf("|%-20s%-22s%-3d%-25s|%n", " ", "[M]agic Summon", 15, " ");
        System.out.printf("|%-20s%-22s%-3d%-25s|%n", " ", "[B]lind", 15, " ");
        System.out.printf("|%-20s%-22s%-3d%-25s|%n", " ", "[P]oison", 15, " ");
        System.out.printf("|%-20s%-22s%-3d%-25s|%n", " ", "[S]teal", 0, " ");
        System.out.printf("|%-20s%-22s%-3d%-25s|%n", " ", "[C]onfuse", 25, " ");
        System.out.printf("|%-20s%-22s%-3d%-25s|%n", " ", "[D]efend", 25, " ");
        System.out.printf("|%-20s%-22s%-3d%-25s|%n", " ", "[T]ime", 15, " ");
        System.out.printf("|%-20s%-22s%-28s|%n", " ", "[Q]uit", " ");
        System.out.printf("|%-70s|%n\033[0;38m", " ");
    }

    public void victory(Player player, Enemy enemy) throws InterruptedException {
        int battleNumber = player.getVictories();
        int coins = enemy.getWorth();
        int exp = enemy.getExpValue();
        Thread.sleep(450);
        System.out.printf("\033[0;32mEnemy # %d is defeated. You have gained %d¢ and %d exp%n\033[0;38m", battleNumber, coins, exp);
        Thread.sleep(450);
        player.setExp(player.getExp() + exp);
        player.setWallet(player.getWallet() + coins);
    }

    private static void info(){
        System.out.println("Potion heals for 25, High Potion heals for 55, Mega Potion heals 35% max health, Antidote cures poison, Revive cures death, Bomb deals 20% of enemy Max HP");
        System.out.println("Ether recovers 35% mp");
        System.out.println("Each of the stat boosters give a +1 permanent boost to that stat");
        System.out.println("Weapon boosts attack, Guard boost defense, Fade boost speed, Study boosts magic");
        System.out.println("Learn and increase your max Mp by 5, Develop to increase your max Hp by 5");
        System.out.println("When an item is used, it takes your turn");
        System.out.println("Potions will heal at most up to your Max HP");
    }
}