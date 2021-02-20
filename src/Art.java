import java.util.ArrayList;
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
        System.out.printf("%-20s%s%n", " ", "[G]amble");
        System.out.printf("%-20s%s%n", " ", "[V]iew Stats");
        System.out.printf("%-20s%s%n", " ", "[Q]uit");
        System.out.printf("|%-60s|%n\033[0;38m", " ");
        mainOptions.add("B");
        mainOptions.add("V");
        mainOptions.add("S");
        mainOptions.add("G");
        mainOptions.add("Q");
    }

    public void welcome(){
        for(int i = 0; i < 4; i++){
            System.out.printf("%-5s|%-219s-|%n", " ", "-");
        }
        System.out.printf("%-5s|%-105s %s %-106s|%n"," ", " ", "Welcome", " ");
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
        System.out.println("The gambler is a place to try your luck for some extra coin or save your money and gain some interest on it over every victory in battle");
        System.out.println("Rock paper scissors gives 3x money, Number guesser gives 5x and Coin toss is 2x");
        System.out.println("Good luck player");
    }
    public void gamePlayInfo(){
        System.out.println("Special contains a few different options, all of which last through the battle until a new one is successfully cast.");
        System.out.println("Magic Summon calls an ally into battle that deals 3% of enemy health per turn and has a 20% chance to block for you");
        System.out.println("Sand increases you dodge rate by 2x");
        System.out.println("Poison causes 5% enemy damage per turn");
        System.out.println("If you are poisoned you are indefinitely until an antidote is used");
        System.out.println("Normal enemies have a 5% chance to poison you, a more venomous enemy, 'cough' has 10x the chance to poison you");
        System.out.println("Some enemies are smarter than others and it is impossible to dodge them but, they can not poison you");
        System.out.println("Some enemies can put you to sleep, you need a clock to wake up or try you luck every turn with a 50% chance to wake up");
        System.out.println("Rob the enemy to steal 50% of their coin reward");
        System.out.println("Confuse the enemy to have a 35% chance of them hitting themselves");
        System.out.println("Defend to reduce damage by 1/2");
    }

    public void shopMenu(Player player) throws InterruptedException {
        do {
            System.out.printf("\033[0;32m|%-69s|%n", " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[P]otion", " ", 10, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[H]igh Potion", " ", 30, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[M]ega Potion", " ", 65, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[A]ntidote", " ", 10, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[C]lock", " ", 15, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[B]omb", " ", 35, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[R]evive", " ", 100, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[W]eapon", " ", 50, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[G]uard", " ", 50, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[S]tudy", " ", 50, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[F]ade", " ", 50, " ");
            System.out.printf("|%-20s%-20s%-5s%-24s|%n", " ", "[I]nfo", " ", " ");
            System.out.printf("|%-20s%-20s%-5s%-24s|%n", " ", "[E]xit", " ", " ");
            System.out.printf("|%-69s|%n\033[0;38m", " ");
            Thread.sleep(600);

            String option = sc.getInput("Welcome "+player.getName()+", you have "+player.getWallet()+" ¢, What would you like?");
            Thread.sleep(600);

            if(option.equalsIgnoreCase("e")) break;
            handleChoice(player, option);
        } while (true);
        System.out.println("Thank you "+player.getName());
    }


    public void handleChoice(Player player, String choice) throws InterruptedException {
        String item;
        int savings = player.getWallet();
        if (choice.equalsIgnoreCase("p") && savings >= 10) {
            int howMany = sc.getNum("How many would you like?", player.getWallet(), 10);
            if(howMany < 2) {
                item = " Potion";
            } else {
                item = " " +howMany + " Potions";
            }
            quantity(howMany, 10, player, "Potion");

        } else if (choice.equalsIgnoreCase("h") && savings >= 30) {
            int howMany = sc.getNum("How many would you like?", player.getWallet(), 30);
            if(howMany < 2) {
                item = " High Potion";
            } else {
                item = " " +howMany + " High Potions";
            }
            quantity(howMany, 30, player, "Hpotion");

        } else if (choice.equalsIgnoreCase("a") && savings >= 10) {
            int howMany = sc.getNum("How many would you like?", player.getWallet(), 10);
            if(howMany < 2) {
                item = " Antidote";
            } else {
                item = " " +howMany + " Antidotes";
            }
            quantity(howMany, 10, player, "Antidote");

        } else if (choice.equalsIgnoreCase("c") && savings >= 15) {
            int howMany = sc.getNum("How many would you like?", player.getWallet(), 15);
            if(howMany < 2) {
                item = " Clock";
            } else {
                item = " " +howMany + " Clocks";
            }
            quantity(howMany, 15, player, "Clock");

        } else if (choice.equalsIgnoreCase("r") && savings >= 100) {
            int howMany = sc.getNum("How many would you like?", player.getWallet(), 100);
            if(howMany < 2) {
                item = " Revive";
            } else {
                item = " " +howMany + " Revives";
            }
            quantity(howMany, 100, player, "Revive");

        } else if (choice.equalsIgnoreCase("b") && savings >= 35) {
            int howMany = sc.getNum("How many would you like?", player.getWallet(), 35);
            if(howMany < 2) {
                item = " Bomb";
            } else {
                item = " " +howMany + " Bombs";
            }
            quantity(howMany, 35, player, "Bomb");

        } else if (choice.equalsIgnoreCase("m") && savings >= 65) {
            int howMany = sc.getNum("How many would you like?", player.getWallet(), 65);
            if(howMany < 2) {
                item = " Mega Potion";
            } else {
                item = " " +howMany + " Mega Potions";
            }
            quantity(howMany, 65, player, "Mega");

        } else if ((choice.equalsIgnoreCase("w") || choice.equalsIgnoreCase("f") || choice.equalsIgnoreCase("g") || choice.equalsIgnoreCase("s")) && savings >= 50) {
            int howMany = sc.getNum("How many would you like?", player.getWallet(), 50);
            item = " Booster";
            statBooster(howMany, 50, player, choice);

        } else if (choice.equalsIgnoreCase("i")) {
            info();
            return;
        } else {
            System.out.println("Do not have it or you can not afford it.");
            Thread.sleep(600);
            return;
        }
        System.out.println("Sounds good, here is your" + item);
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
        System.out.printf("\033[0;34m|%-67s|%n", " ");
        System.out.printf("|%-20s%-22s\033[0;31m%-5d / %-17d\033[0;34m|%n", " ", "Health : " , player.getHealth(), player.getMaxHealth());
        System.out.printf("|\033[0;32m%-20s%-19s%-28s\033[0;34m|%n", " ", "Status: ", player.getState());
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[A]ttack", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[S]pecial", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[I]nventory", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[C]heck Enemy", " ");
        System.out.printf("|%-67s|%n\033[0;38m", " ");
    }

    public void enemyHud(Enemy enemy, String status){
        System.out.println();
        System.out.printf("\033[0;32m|%-20s%-22s\033[0;31m%-5d / %-17d\033[0;32m|%n", " ", "Health : " , enemy.getHealth(), enemy.getMaxHealth());
        System.out.printf("|%-20s%-19s%-28s|%n\033[0;38m", " ", "Status: ", status);
        System.out.println();
    }

    public void specials(){
        System.out.printf("\033[0;35m|%-67s|%n", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[M]agic Summon", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[S]and", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[P]oison", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[R]ob", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[C]onfuse", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[D]efend", " ");
        System.out.printf("|%-67s|%n\033[0;38m", " ");
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
        System.out.println("Potion heals for 25, High Potion heals for 55, Mega Potion heals 35% max health, Antidote cures poison, Revive cures death, Bomb deals 20% damage, Each of the stat boosters give a +1 permanent boost to that stat");
        System.out.println("Weapon boosts attack, Guard boost defense, Fade boost speed, Study boosts magic");
        System.out.println("When an item is used, it takes your turn and then the enemy takes their turn even if you did not need to heal.. you should not heal if you do not need to");
    }
}