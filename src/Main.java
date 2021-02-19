import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static Input sc = new Input();
    private static Art art = new Art();
    private static Method m = new Method();
    private static FileReader fileReader;
    static {
        try {
            fileReader = new FileReader("src", "gameLog.txt", "gameLog.txt");
        } catch (IOException e) {
            System.out.println("Error creating file reader");
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
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
                if (input.equalsIgnoreCase("q")) break;
                switch (input) {
                    case "B":
                        battle(player);
                        break;
                    case "S":
                        art.shopMenu(player);
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

    public static boolean checkRevive(Player player){
        if(player.getInventory().get("Revive") != null) {
            player.useItem("Revive");
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
        String battleEffect = "normal";
        do {
            String youEffect = player.getState();
            takeEffect(player);

            if(player.getHealth() <= 0){
                // if poison kills you
                break;
            }
            art.hud(player);
            Thread.sleep(600);
            System.out.println("Your current condition : " + youEffect);
            System.out.println("Current condition on enemy : " + battleEffect);
            Thread.sleep(600);

            System.out.println("Enemy health : \033[0;31m" + enemy.getHealth());

            String turn = sc.getInput("\033[0;38mYour move", options, false);
            String result = handleTurn(turn, enemy, player);
            if(result.equalsIgnoreCase("item")){
                System.out.println(); // so the turn counts if an item was used
            } else if (result.equalsIgnoreCase("special")) {
                battleEffect = specialOptions(player);
            } else if (result.equalsIgnoreCase("attacking")){
                attackMade(player, enemy);
            } else {
                // did not use an item, so either scanned enemy or viewed and exited inventory
                if(youEffect.equalsIgnoreCase("poison")) {  // temp for undoing the poison damage when you actually did not make a turn
                    player.updateStat("Health", player.getHealth() + 5);
                }
                fight(player, enemy);
                return; // return needed to prevent a bubble effect with scanning enemy and finishing the fight, caused multiple victories. finally figured this out
            }

            if(enemy.getHealth() <= 0){
                break;
            } else {
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
            } else if (didUseItem(player)) {
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

    public static boolean didUseItem(Player player) throws InterruptedException {
        boolean itemUsed = false;
        String option = sc.getInput("What would you like to use?", player.getInventoryItems(),true);
        if(!option.equalsIgnoreCase("nothing")){
            itemUsed = true;
            player.useItem(option);
        }
        return itemUsed;
    }


    public static void attackMade(Player attacker, Enemy defender) throws InterruptedException {
        int damage = m.calcDamage(attacker.getStats().get("Attack"), defender.getDefense());
        System.out.println("\033[0;37mEnemy attempts to dodge");
        Thread.sleep(600);
        boolean enemyDodge = m.blocked(defender.getSpeed());
        if(!enemyDodge){
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
            poisonDamage = (int) (enemy.getMaxHealth() * .03);
            System.out.println("\033[0;32mEnemy takes poison damage\033[0;38m");
            enemy.setHealth(enemy.getHealth() - poisonDamage);
        } else if (currentAilment.equalsIgnoreCase("shield")){
            extraDefenseMult = 2;
        } else if (currentAilment.equalsIgnoreCase("confuse")){
            confuseChance = 50;
        }

        int damage = m.calcDamage(enemy.getAttack(), player.getStats().get("Defense") * extraDefenseMult);
        damage /= extraDefenseMult;
        System.out.println("\033[0;37mYou attempt to dodge");
        Thread.sleep(600);
        boolean youDodge = m.blocked(player.getStats().get("Speed") * extraDodgeMult);
        Thread.sleep(600);

        if(!youDodge){
            if(ranConfuseHit <= confuseChance && currentAilment.equalsIgnoreCase("confuse")){
                System.out.println("Enemy has hit himself for " + enemy.getAttack() / 2);
                Thread.sleep(600);

                enemy.setHealth(enemy.getHealth() - enemy.getAttack() / 2);
            } else {
                System.out.println("Enemy hits you for " + damage + " damage");
                if(infectChance <= infect){
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

    public static String specialOptions(Player player) throws InterruptedException {
        List<String> options = new ArrayList<>(Arrays.asList("s", "p", "r", "c"));
        String result = "normal";
        art.specials();
        boolean success = m.successfulCast(player.getStats().get("Magic"));
        String specialChoice = sc.getInput("This is the way to fight! How will you win this?", options, false);
        if(success){
            System.out.println("Successful cast!");
            Thread.sleep(600);
            result = handleSpecialTurn(specialChoice);
            return result;
        } else {
            Thread.sleep(600);
            System.out.println("You struggle to remember how to do this...");
            Thread.sleep(600);
            return result;
        }
    }

    private static String handleSpecialTurn(String specialTurn){
        if (specialTurn.equalsIgnoreCase("s")){
            return "sand";
        } else if (specialTurn.equalsIgnoreCase("p")){
            return "poison";
        } else if (specialTurn.equalsIgnoreCase("r")){
            return "shield";
        } else {
            return "confuse";
        }
        // s => increase dodge rest of fight
        // p => turn based damage rest of fight
        // r => increase defense for rest of fight
        // c => chance enemy hits self rest of fight
    }

    public static void takeEffect(Player player) throws InterruptedException {
        String currentStatus = player.getState();
        if(currentStatus.equalsIgnoreCase("poison")){
            int damage = (int) (player.getMaxHealth() * .03);
            System.out.printf("\033[0;32mYou take %d poison damage\033[0;38m%n", damage);
            Thread.sleep(600);
            player.updateStat("Health", player.getHealth() - damage);
        }
    }
}
