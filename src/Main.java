import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static Input sc = new Input();
    private static Art art = new Art();
    private static Method m = new Method();
    private static FileReader fileReader;
    private static Gambler gambler = new Gambler();
    private static ArrayList<String> beginOptions = new ArrayList<>(Arrays.asList("A", "S", "G"));
    static {
        try {
            fileReader = new FileReader("src", "gameLog.txt", "gameLog.txt");
        } catch (IOException e) {
            System.out.println("Error creating file reader");
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        art.welcome();
        art.welcomeOptions();
        do{
            String choice = sc.getInput("…", beginOptions,false).toUpperCase();
            if(choice.equalsIgnoreCase("s")){
                break;
            }
            switch (choice){
                case "A":
                    art.about();
                    break;
                case "G":
                    art.gamePlayInfo();
                    break;
            }
        } while (true);
        start();
    }

    public static void start() throws IOException, InterruptedException {
        String name = sc.getInput("What is your name?");
        Player player = new Player(m.cap(name));
        choosePlayerClass(player);
    }

    public static void choosePlayerClass(Player player) throws IOException, InterruptedException {
        List<String> classOptions = new ArrayList<>();
        classOptions.add("Knight");
        classOptions.add("Mage");
        classOptions.add("Thief");
        String type = sc.getInput("What is your Class choice?", classOptions, true);
        player.setType(m.cap(type));
        player.initialStats(player.getType());
        Thread.sleep(600);
        System.out.printf("Good choice %s, you are a great %s%n", player.getName(), player.getType());
        decision(player);
    }


    public static void decision(Player player) throws IOException, InterruptedException {
        do {
            if (player.getHealth() <= 0) {
                System.out.println("You are dead and we need to take care of that");
                Thread.sleep(600);
                if(checkRevive(player)) {
                    System.out.println("You have used your revive to come back with 25% of your max HP, good on you for buying one");
                    Thread.sleep(600);
                    player.updateStat("Health", player.getMaxHealth() / 4);
                } else {
                    System.out.println("What!? You do not have any revives.. well...");
                    Thread.sleep(600);
                    System.out.println("Game over");
                    fileReader.saveRecord(player);
                    return;
                }
            }
            if(player.getHealth() > 0){
                art.mainScreen();
                String input = sc.getInput("What would you like to do?", art.getMainOptions(), false).toUpperCase();
                Thread.sleep(600);
                if (input.equalsIgnoreCase("q") && sc.getInput("Are you sure? [Y]es / [N]o").equalsIgnoreCase("y")) {
                    break;
                }
                switch (input) {
                    case "B":
                        battle(player);
                        break;
                    case "S":
                        art.shopMenu(player);
                        break;
                    case "G":
                        gambler.gamble(player);
                        break;
                    case "V":
                        player.viewStats();
                        player.viewInventory();
                        decision(player);
                        return;
                }
            }
        } while (true);
        System.out.println("Nice meeting you");
    }

    public static boolean checkRevive(Player player) throws InterruptedException {
        if(player.getInventory().get("Revive") != null) {
            player.revive("Revive");
            return true;
        }
        return false;
    }


    public static void battle(Player player) throws InterruptedException {
        int wins = player.getVictories();
        System.out.printf("Battle # %d%n", wins);
        int randomizer = (int) Math.floor(Math.random() * 100);
        Enemy enemy = new Enemy(wins, randomizer);
        fight(player, enemy, "normal");
    }


    public static void fight(Player player, Enemy enemy, String enemyStatus) throws InterruptedException {
        List<String> options = new ArrayList<>(Arrays.asList("a", "i", "s", "c"));
        String battleEffect = enemyStatus;
        do {
            String youEffect = player.getState();
            takeEffect(player);

            if (player.getHealth() <= 0) {
                // if poison kills you
                break;
            }
            art.enemyHud(enemy, battleEffect);
            Thread.sleep(600);
            art.hud(player);

            String turn = sc.getInput("\033[0;38mYour move", options, false);
            String result = handleTurn(turn, enemy, player);
            if (result.equalsIgnoreCase("item")) {
                System.out.println(); // so the turn counts if an item was used
            } else if (result.equalsIgnoreCase("special")) {
                battleEffect = specialOptions(player, enemy, battleEffect);
            } else if (result.equalsIgnoreCase("attacking")) {
                attackMade(player, enemy);
            } else {
                // did not use an item, so either scanned enemy or viewed and exited inventory
                if (youEffect.equalsIgnoreCase("poison")) {  // temp for undoing the poison damage when you actually did not make a turn
                    player.updateStat("Health", player.getHealth() + (int) (player.getMaxHealth() * .05));
                }
                fight(player, enemy, battleEffect);
                return; // return needed to prevent a bubble effect with scanning enemy and finishing the fight, caused multiple victories. finally figured this out
            }

            if (enemy.getHealth() <= 0) {
                break;
            }
            if (battleEffect.equalsIgnoreCase("cursed")) {
                int zombieDamage = (int) (enemy.getMaxHealth() * .03);
                if(zombieDamage < 1){
                    zombieDamage = 1;
                }
                System.out.printf("Agheghegh… your summon attacks dealing %d damage%n", zombieDamage);
                enemy.setHealth(enemy.getHealth() - zombieDamage);
            }
            if (enemy.getHealth() <= 0) {
                break;
            }
            else {
                enemyTurn(enemy, player, battleEffect);
            }

        } while (player.getHealth() > 0 && enemy.getHealth() > 0);

        if(enemy.getHealth() <= 0){
            art.victory(player, enemy);
            player.setVictories(player.getVictories() + 1);
        } else {
            System.out.println("Oof you lost...");
            Thread.sleep(600);
        }

    }


    public static String handleTurn(String turn, Enemy enemy, Player player) throws InterruptedException {
        if(turn.equalsIgnoreCase("i")){
            if (player.getInventory().isEmpty()) {
                System.out.println("You do not have any items");
                Thread.sleep(600);
                return "";
            } else if (didUseItem(player, enemy)) {
                return "item";
            }
        } else if (turn.equalsIgnoreCase("a")){
            return "attacking";
        } else if (turn.equalsIgnoreCase("c")){
            enemy.showStats(player.getVictories());
        } else if (turn.equalsIgnoreCase("s")){
            return "special";
        }
        return "";
    }

    public static boolean didUseItem(Player player, Enemy enemy) throws InterruptedException {
        boolean itemUsed = false;
        String option = sc.getInput("What would you like to use?", player.getInventoryItems(),true);
        if(!option.equalsIgnoreCase("nothing")){
            itemUsed = true;
            player.useItem(option, enemy);
        }
        return itemUsed;
    }


    public static void attackMade(Player attacker, Enemy defender) throws InterruptedException {
        int damage = m.calcDamage(attacker.getStats().get("Attack"), defender.getDefense());
        System.out.println("\033[0;37mEnemy attempts to dodge");
        boolean enemyDodge = m.blocked(defender.getSpeed());
        if(!enemyDodge){
            if (m.criticalHit(attacker.getStats().get("Speed"))) {
                System.out.println("Critical Hit!");
                damage*=1.5;
            }
            System.out.println("You hit for " + damage + " damage");
            Thread.sleep(600);
            defender.setHealth(defender.getHealth() - damage);
        } else {
            System.out.println("He is too quick, you have missed your attack");
        }
    }
    public static void enemyTurn(Enemy enemy, Player player, String currentAilment) throws InterruptedException {

        int extraDefenseMult = 1;
        int extraDodgeMult = 1;
        int poisonDamage = 0;
        int confuseChance = 0;
        int ranConfuseHit = (int) Math.floor(Math.random() * 100);

        int infectChance = (int) Math.floor(Math.random() * 100);
        int infect = enemy.getInfect();

        Thread.sleep(600);

        if (currentAilment.equalsIgnoreCase("sand")){
            extraDodgeMult = 2;
        } else if (currentAilment.equalsIgnoreCase("poison")){
            poisonDamage = (int) (enemy.getMaxHealth() * .05);
            if(poisonDamage < 1){
                poisonDamage = 1;
            }
            System.out.println("\033[0;32mEnemy takes poison damage\033[0;38m");
            enemy.setHealth(enemy.getHealth() - poisonDamage);
        } else if (currentAilment.equalsIgnoreCase("shield")){
            extraDefenseMult = 2;
        } else if (currentAilment.equalsIgnoreCase("confuse")){
            confuseChance = 35;
        }

        int damage = m.calcDamage(enemy.getAttack(), player.getStats().get("Defense"));
        damage /= extraDefenseMult;
        System.out.println("\033[0;37mYou attempt to dodge");
        boolean youDodge = m.blocked(player.getStats().get("Speed") * extraDodgeMult);
        Thread.sleep(600);

        int randomSummonBlock = (int) Math.floor(Math.random() * 100);

        if(!youDodge){
            if(ranConfuseHit <= confuseChance && currentAilment.equalsIgnoreCase("confuse")){
                System.out.println("Enemy has hit himself for " + enemy.getAttack() / 2);
                Thread.sleep(600);

                enemy.setHealth(enemy.getHealth() - enemy.getAttack() / 2);
            } else if (randomSummonBlock < 20 && currentAilment.equalsIgnoreCase("cursed")) {
                System.out.println("Your summon stumbles.. it falls in front of you and takes the hit");
            } else {
                if(m.criticalHit(enemy.getSpeed())){
                    System.out.println("Critical Hit!");
                    damage*=1.5;
                }
                System.out.println("Enemy hits you for " + damage + " damage");
                if(infectChance <= infect){
                    Thread.sleep(600);
                    System.out.println("You begin to slowly die");
                    Thread.sleep(600);
                    player.setState("poison");
                }
                player.updateStat("Health", player.getHealth() - damage);
            }
        } else if (enemy.getAccuracy() > 10) {
            System.out.println("This one sees through your tricks");
            if(m.criticalHit(enemy.getSpeed())){
                System.out.println("Critical Hit!");
                damage*=1.5;
            }
            System.out.println("Enemy hits you for " + damage + " damage");
            player.updateStat("Health", player.getHealth() - damage);
        } else {
            System.out.println("You have successfully dodged the attack!");
            Thread.sleep(600);

        }
    }

    public static String specialOptions(Player player, Enemy enemy, String currentEnemyStatus) throws InterruptedException {
        List<String> options = new ArrayList<>(Arrays.asList("s", "p", "r", "c", "d", "m"));
        String result = currentEnemyStatus;
        art.specials();
        boolean success = m.successfulCast(player.getStats().get("Magic"));
        String specialChoice = sc.getInput("This is the way to fight! How will you win this?", options, false);
        if(success){
            System.out.println("Successful cast!");
            Thread.sleep(600);
            result = handleSpecialTurn(specialChoice, enemy, player);
            return result;
        } else {
            Thread.sleep(600);
            System.out.println("You struggle to remember how to do this...");
            Thread.sleep(600);
            return result;
        }
    }

    private static String handleSpecialTurn(String specialTurn, Enemy enemy, Player player){
        if (specialTurn.equalsIgnoreCase("s")){
            return "sand";
        } else if (specialTurn.equalsIgnoreCase("p")){
            return "poison";
        } else if (specialTurn.equalsIgnoreCase("r")){
            int stolenAmount = enemy.getWorth() / 2;
            System.out.printf("You sneak up and steal %d ¢%n", stolenAmount);
            player.setWallet(player.getWallet() + stolenAmount);
            return "robbed";
        } else if (specialTurn.equalsIgnoreCase("d")){
            return "shield";
        } else if (specialTurn.equalsIgnoreCase("m")){
            System.out.println("You summon an undead ally");
            return "cursed";
        } else {
            return "confuse";
        }
    }

    public static void takeEffect(Player player) throws InterruptedException {
        String currentStatus = player.getState();
        if(currentStatus.equalsIgnoreCase("poison")){
            int damage = (int) (player.getMaxHealth() * .05);
            System.out.printf("\033[0;32mYou take %d poison damage\033[0;38m%n", damage);
            Thread.sleep(600);
            player.updateStat("Health", player.getHealth() - damage);
        }
    }
}
