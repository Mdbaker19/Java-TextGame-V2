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
        fight(player, enemy);
    }


    public static void fight(Player player, Enemy enemy) throws InterruptedException {
        List<String> options = new ArrayList<>(Arrays.asList("a", "i", "s", "c"));
        List<String> battleEffects = enemy.getAilments();
        do {
            String youEffect = player.getState();
            takeEffect(player);

            if (player.getHealth() <= 0) {
                // if poison kills you
                break;
            }
            art.enemyHud(enemy, battleEffects);
            Thread.sleep(600);
            art.hud(player);

            String turn = sc.getInput("\033[0;38mYour move", options, false);
            String result = handleTurn(turn, enemy, player);
            if (result.equalsIgnoreCase("item")) {
                System.out.println(); // so the turn counts if an item was used or you are asleep
            } else if (result.equalsIgnoreCase("special")) {
                String specialReturn = specialOptions(player, enemy);
                if(!specialReturn.equalsIgnoreCase("normal") && !battleEffects.contains(specialReturn)){
                    battleEffects.add(specialReturn);
                }
            } else if (result.equalsIgnoreCase("attacking")) {
                attackMade(player, enemy);
            } else {
                // did not use an item, so either scanned enemy or viewed and exited inventory
                if (youEffect.equalsIgnoreCase("poison")) {  // temp for undoing the poison damage when you actually did not make a turn
                    player.updateStat("Health", player.getHealth() + (int) (player.getMaxHealth() * .05));
                }
                fight(player, enemy);
                return; // return needed to prevent a bubble effect with scanning enemy and finishing the fight, caused multiple victories. finally figured this out
            }

            enemy.setAilments(battleEffects);

            if (enemy.getHealth() <= 0) {
                break;
            }
            if (battleEffects.contains("cursed")) {
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
                enemyTurn(enemy, player, battleEffects);
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
        boolean asleep = player.getState().equalsIgnoreCase("sleep");
        int ranWakeUp = 0;
        if (asleep) {
            System.out.println("zzz……");
            ranWakeUp = (int) Math.floor(Math.random() * 100);
        }

        if(ranWakeUp >= 50){
            System.out.println("You fall over, as you hit the ground you wake up");
            player.setState("normal");
            asleep = false;
        }

        if(turn.equalsIgnoreCase("i")){
            if (player.getInventory().isEmpty()) {
                System.out.println("You do not have any items");
                Thread.sleep(600);
                return "";
            } else if (didUseItem(player, enemy)) {
                return "item";
            }
        } else if (asleep){
            System.out.println("You are dreaming of a fight that you are losing");
            return "item";
        } else if (turn.equalsIgnoreCase("a")){
            return "attacking";
        } else if (turn.equalsIgnoreCase("s")){
            return "special";
        } else if (turn.equalsIgnoreCase("c")){
            enemy.showStats(player.getVictories());
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


    public static void attackMade(Player attacker, Enemy enemy) throws InterruptedException {
        int damage = m.calcDamage(attacker.getStats().get("Attack"), enemy.getDefense());
        System.out.println("\033[0;37mEnemy attempts to dodge");
        boolean enemyDodge = m.blocked(enemy.getSpeed());
        if(!enemyDodge){
            if (m.criticalHit(attacker.getStats().get("Speed"))) {
                System.out.println("Critical Hit!");
                damage*=1.5;
            }
            System.out.println("You hit for " + damage + " damage");
            Thread.sleep(600);
            enemy.setHealth(enemy.getHealth() - damage);
        } else {
            System.out.println("He is too quick, you have missed your attack");
        }
    }
    public static void enemyTurn(Enemy enemy, Player player, List<String> currentAilments) throws InterruptedException {

        int extraDefenseMult = 1;
        int extraDodgeMult = 1;
        int poisonDamage = 0;
        int confuseChance = 0;
        int ranConfuseHit = (int) Math.floor(Math.random() * 100);

        int infectChance = (int) Math.floor(Math.random() * 100);
        int infect = enemy.getInfect();

        int castChance = (int) Math.floor(Math.random() * 100);

        Thread.sleep(600);

        if (currentAilments.contains("sand")) {
            extraDodgeMult = 2;
        }
        if (currentAilments.contains("poison")) {
            poisonDamage = (int) (enemy.getMaxHealth() * .05);
            if (poisonDamage < 1) {
                poisonDamage = 1;
            }
            System.out.println("\033[0;32mEnemy takes poison damage\033[0;38m");
            enemy.setHealth(enemy.getHealth() - poisonDamage);
        }
        if (currentAilments.contains("shield")) {
            extraDefenseMult = 2;
        }
        if (currentAilments.contains("confuse")) {
            confuseChance = 35;
        }

        if (enemy.isCaster() && castChance < 35) {
            System.out.println("The monster begins to glow as the world fades to darkness…");
            player.setState("sleep");
        } else {
            int damage = m.calcDamage(enemy.getAttack(), player.getStats().get("Defense"));
            damage /= extraDefenseMult;
            System.out.println("\033[0;37mYou attempt to dodge");
            boolean youDodge = m.blocked(player.getStats().get("Speed") * extraDodgeMult);
            Thread.sleep(600);

            int randomSummonBlock = (int) Math.floor(Math.random() * 100);

            if (ranConfuseHit <= confuseChance && currentAilments.contains("confuse")) {
                int hitSelf = enemy.getAttack() / 2;
                if(hitSelf < 1){
                    hitSelf = 1;
                }
                System.out.println("Enemy has hit himself for " + hitSelf);
                Thread.sleep(600);
                enemy.setHealth(enemy.getHealth() - hitSelf);

            } else if (enemy.getAccuracy() > 10) {
                System.out.println("This one sees through your tricks");
                if (m.criticalHit(enemy.getSpeed())) {
                    System.out.println("Critical Hit!");
                    damage *= 1.5;
                }
                System.out.println("Enemy hits you for " + damage + " damage");
                player.updateStat("Health", player.getHealth() - damage);
            } else if (!youDodge) {
                if (randomSummonBlock < 20 && currentAilments.contains("cursed")) {
                    System.out.println("Your summon stumbles.. it falls in front of you and takes the hit");
                } else {
                    if (m.criticalHit(enemy.getSpeed())) {
                        System.out.println("Critical Hit!");
                        damage *= 1.5;
                    }
                    System.out.println("Enemy hits you for " + damage + " damage");
                    if (infectChance <= infect) {
                        Thread.sleep(600);
                        System.out.println("You begin to slowly die");
                        Thread.sleep(600);
                        player.setState("poison");
                    }
                    player.updateStat("Health", player.getHealth() - damage);
                }
            } else {
                System.out.println("You have successfully dodged the attack!");
                Thread.sleep(600);
            }
        }
    }

    public static String specialOptions(Player player, Enemy enemy) throws InterruptedException {
        List<String> options = new ArrayList<>(Arrays.asList("s", "p", "r", "c", "d", "m"));
        String result = "normal";
        art.specials();
        boolean success = m.successfulCast(player.getStats().get("Magic"));
        String specialChoice = sc.getInput("This is the way to fight! How will you win this?", options, false);
        if(success){
            System.out.println("Successful cast!");
            Thread.sleep(600);
            result = handleSpecialTurn(specialChoice, enemy, player);
        } else {
            Thread.sleep(600);
            System.out.println("You struggle to remember how to do this...");
            Thread.sleep(600);
        }
        return result;
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
