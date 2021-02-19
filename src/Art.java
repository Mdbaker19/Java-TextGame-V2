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
        System.out.printf("%-20s%s%n", " ", "[V]iew Stats");
        System.out.printf("%-20s%s%n", " ", "[Q]uit");
        System.out.printf("|%-60s|%n\033[0;38m", " ");
        mainOptions.add("B");
        mainOptions.add("V");
        mainOptions.add("S");
        mainOptions.add("Q");
    }

    public void shopMenu(Player player) throws InterruptedException {
        do {
            System.out.printf("\033[0;32m|%-69s|%n", " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[P]otion", " ", 10, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[H]igh Potion", " ", 30, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[A]ntidote", " ", 10, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[R]evive", " ", 100, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[S]harpen", " ", 50, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[G]uard", " ", 50, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[B]ooks", " ", 50, " ");
            System.out.printf("|%-20s%-20s%-5s%-4d%-20s|%n", " ", "[F]ade", " ", 50, " ");
            System.out.printf("|%-20s%-20s%-5s%-24s|%n", " ", "[I]nfo", " ", " ");
            System.out.printf("|%-20s%-20s%-5s%-24s|%n", " ", "[E]xit", " ", " ");
            System.out.printf("|%-69s|%n\033[0;38m", " ");
            Thread.sleep(600);

            String option = sc.getInput("Welcome "+player.getName()+", you have "+player.getWallet()+" coins, What would you like?");
            Thread.sleep(600);

            if(option.equalsIgnoreCase("e")) break;
            handleChoice(player, option);
        } while (true);
        System.out.println("Thank you "+player.getName());
    }


    public void handleChoice(Player player, String choice) throws InterruptedException {
        String item;
        int value = player.getWallet();
        if (choice.equalsIgnoreCase("p") && value >= 10) {
            int howMany = sc.getNum("How many would you like?", player.getWallet(), 10);
            if(howMany < 2) {
                item = " Potion";
            } else {
                item = " " +howMany + " Potions";
            }
            quantity(howMany, 10, player, "Potion");
        } else if (choice.equalsIgnoreCase("h") && value >= 30) {
            int howMany = sc.getNum("How many would you like?", player.getWallet(), 30);
            if(howMany < 2) {
                item = " High Potion";
            } else {
                item = " " +howMany + " High Potions";
            }
            quantity(howMany, 30, player, "Hpotion");
        } else if (choice.equalsIgnoreCase("a") && value >= 10) {
            int howMany = sc.getNum("How many would you like?", player.getWallet(), 10);
            if(howMany < 2) {
                item = " Antidote";
            } else {
                item = " " +howMany + " Antidotes";
            }
            quantity(howMany, 10, player, "Antidote");
        } else if (choice.equalsIgnoreCase("r") && value >= 100) {
            int howMany = sc.getNum("How many would you like?", player.getWallet(), 100);
            if(howMany < 2) {
                item = " Revive";
            } else {
                item = " " +howMany + " Revives";
            }
            quantity(howMany, 100, player, "Revive");
        } else if ((choice.equalsIgnoreCase("s") || choice.equalsIgnoreCase("f") || choice.equalsIgnoreCase("g") || choice.equalsIgnoreCase("b")) && value >= 50) {
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
        if(statToUpgrade.equalsIgnoreCase("s")){
            statToUpgrade = "Attack";
        } else if(statToUpgrade.equalsIgnoreCase("g")){
            statToUpgrade = "Defense";
        } else if (statToUpgrade.equalsIgnoreCase("f")){
            statToUpgrade = "Speed";
        } else if (statToUpgrade.equalsIgnoreCase("b")){
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
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[A]ttack", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[S]pecial", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[I]nventory", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[C]heck Enemy", " ");
        System.out.printf("|%-67s|%n\033[0;38m", " ");
    }

    public void specials(){
        System.out.printf("\033[0;35m|%-67s|%n", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[S]and", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[P]oison", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[R]aise Shield", " ");
        System.out.printf("|%-20s%-19s%-28s|%n", " ", "[C]onfuse", " ");
        System.out.printf("|%-67s|%n\033[0;38m", " ");
    }

    public void victory(Player player, Enemy enemy) throws InterruptedException {
        int battleNumber = player.getVictories();
        int coins = enemy.getWorth();
        int exp = enemy.getExpValue();
        player.setExp(player.getExp() + exp);
        player.setWallet(player.getWallet() + coins);
        Thread.sleep(600);
        System.out.printf("\033[0;32mEnemy # %d is defeated. You have gained %d coins and %d exp%n\033[0;38m", battleNumber, coins, exp);
        Thread.sleep(600);
    }

    private static void info(){
        System.out.println("Potion heals for 25, High Potion heals for 55, Antidote cures poison, Revive cures death, Each of the stat boosters give a +1 permanent boost to that stat");
        System.out.println("Sharpen boosts attack, Guard boost defense, Fade boost speed, Book boosts magic");
        System.out.println("When an item is used, it takes your turn and then the enemy takes their turn even if you did not need to heal.. you should not heal if you do not need to");
    }
}