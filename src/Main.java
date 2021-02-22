import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final Input sc = new Input();
    private static final Art art = new Art();
    private static final Method m = new Method();
    private static final Converter converter = new Converter();
    private static FileReader fileReader;
    private static FileReader saveLoadWriter;
    private static Gambler gambler = new Gambler();
    private static final ArrayList<String> beginOptions = new ArrayList<>(Arrays.asList("A", "S", "G", "L"));
    private static final ArrayList<String> classOptions = new ArrayList<>(Arrays.asList("knight", "mage", "thief"));
    static {
        try {
            fileReader = new FileReader("src", "gameLog.txt", "gameLog.txt");
        } catch (IOException e) {
            System.out.println("Error creating file reader" + e);
        }
    }
    static {
        try {
            saveLoadWriter = new FileReader("src", "gameStates.txt", "gameStates.txt");
        } catch (IOException e){
            System.out.println("Error accessing save states" + e);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        List<String> playerInfo = converter.formatInfo(saveLoadWriter.getFileLines());

        art.welcome();
        art.welcomeOptions();
        do{
            System.out.println();
            String choice = sc.getInput("Welcome player, please input an option", beginOptions,false).toUpperCase();
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
                case "L":
                    if(playerInfo == null){
                        System.out.println("No save file detected");
                        break;
                    }
                    Player loadedPlayer = converter.makePlayer(playerInfo);
                    System.out.println("Loading Save file for " + loadedPlayer.getName());
                    Thread.sleep(1000);
                    gambler = converter.getGambler(saveLoadWriter.getFileLines());
                    decision(loadedPlayer);
                    return;
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
        String type = sc.getInput("What is your Class choice?", classOptions, true);
        player.setType(m.cap(type));
        player.initialStats(player.getType());
        Thread.sleep(600);
        System.out.printf("Good choice %s, you are a great %s%n", player.getName(), player.getType());
        decision(player);
    }


    public static void decision(Player player) throws IOException, InterruptedException {
        List<String> playerInfo = converter.formatInfo(saveLoadWriter.getFileLines());
        do {
            if(player.getVictories() % 14 == 0){
                System.out.println("\033[0;31mBoss fight coming up");
            }
            if (player.getHealth() <= 0) {
                if(!death(player)){
                    return;
                }
            }
            if(player.getHealth() > 0){
                art.mainScreen();
                String input = sc.getInput("What would you like to do?", art.getMainOptions(), false).toUpperCase();
                if (input.equalsIgnoreCase("e") && sc.getInput("Are you sure? [Y]es / [N]o").equalsIgnoreCase("y")) {
                    break;
                }
                if(input.equalsIgnoreCase("p")){
                    boolean areYouSure = false;
                    if(playerInfo != null){
                        areYouSure = sc.getInput("A save file is detected, do you wish to overwrite? [Y]es / [N]o").equalsIgnoreCase("y");
                    }
                    if(areYouSure || playerInfo == null) {
                        pushUpWork(player, gambler, saveLoadWriter);
                        System.out.println("File saved in \033[0;35mgameStates\033[0;38m");
                    }
                }
                switch (input) {
                    case "B":
                        battle(player);
                        break;
                    case "S":
                        art.shopMenu(player);
                        break;
                    case "A":
                        gambler.account(player);
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
        fileReader.saveRecord(player, gambler);
    }

    public static void battle(Player player) throws InterruptedException {
        int wins = player.getVictories();
        System.out.printf("Battle # %d%n", wins);
        int randomizer = (int) Math.floor(Math.random() * 100);
        Enemy enemy = new Enemy(wins, randomizer);
        fight(player, enemy);
    }


    public static void fight(Player player, Enemy enemy) throws InterruptedException {
        List<String> options = new ArrayList<>(Arrays.asList("a", "i", "s", "c", "m"));
        List<String> battleEffects = enemy.getAilments();
        List<String> states = player.getState();
        states.remove("sleep");
        states.remove("shield");
        player.setState(states);
        player.setThiefSpecial(false);
        do {
            takeEffect(player);
            if (player.getHealth() <= 0) { // if poison kills you
                break;
            }
            art.enemyHud(enemy, battleEffects);
            Thread.sleep(700);
            art.hud(player);

            String turn = sc.getInput("\033[0;38mYour move", options, false);
            String result = handleTurn(turn, enemy, player);
            if (result.equalsIgnoreCase("item")) {
                System.out.println(); // so the turn counts if an item was used or you are asleep
            } else if (result.equalsIgnoreCase("magic")) {
                String magicReturn = magicOptions(player, enemy);
                if(magicReturn.equalsIgnoreCase("quit")){
                    undoPoison(player);
                    fight(player, enemy);
                    return;
                } else if(!magicReturn.equalsIgnoreCase("normal") && !battleEffects.contains(magicReturn)){
                    battleEffects.add(magicReturn);
                }
            } else if (result.equalsIgnoreCase("attacking")) {
                attackMade(player, enemy);
            } else if (result.equalsIgnoreCase("special")) {
                String whatItDoes = specialInfo(player);
                if(player.getMp() < 40){
                    System.out.println("You do not have enough MP for this");
                    undoPoison(player);
                    fight(player, enemy);
                    return;
                } else if (player.isSpecialUsed()){
                    System.out.println("You have already activated this");
                    undoPoison(player);
                    fight(player, enemy);
                    return;
                }
                if(sc.getInput(whatItDoes + " [Y]es / [N]o").equalsIgnoreCase("y")){
                    specialMove(player, enemy);
                } else {
                    undoPoison(player);
                    fight(player, enemy);
                    return;
                }
            } else {
                undoPoison(player);
                fight(player, enemy);
                return; // return needed to prevent a bubble effect with scanning enemy and finishing the fight, caused multiple victories. finally figured this out
            }

            enemy.setAilments(battleEffects);
            Thread.sleep(400);

            if (enemy.getHealth() <= 0) { // you killed enemy
                break;
            }
            if (enemy.isHealer()) {
                enemyHealerTurn(enemy, false);
            }

            if (battleEffects.contains("cursed")) {
                int zombieDamage = (int) (enemy.getMaxHealth() * .03);
                if(zombieDamage < 1){
                    zombieDamage = 1;
                }
                System.out.printf("Agheghegh… your summon attacks dealing %d damage%n", zombieDamage);
                enemy.setHealth(enemy.getHealth() - zombieDamage);
            }
            if (enemy.getHealth() <= 0) { // summon killed enemy
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
        List<String> playerStates = player.getState();
        boolean asleep = playerStates.contains("sleep");
        int ranWakeUp = 100;
        if (asleep) {
            System.out.println("zzz……");
            ranWakeUp = (int) Math.floor(Math.random() * 100);
        }

        if (turn.equalsIgnoreCase("c")){
            enemy.showStats(player.getVictories());
            return "";
        } else if(turn.equalsIgnoreCase("i")){
            if (player.getInventory().isEmpty()) {
                System.out.println("You do not have any items");
                Thread.sleep(400);
                return "";
            } else if (didUseItem(player, enemy)) {
                return "item";
            }
            return "";
        }

        if(ranWakeUp <= 35){
            System.out.println("You fall over, as you hit the ground you wake up");
            Thread.sleep(300);
            playerStates.remove("sleep");
            player.setState(playerStates);
            asleep = false;
        }

        if (asleep){
            System.out.println("You are dreaming of a fight that you are losing");
            return "item";
        } else if (turn.equalsIgnoreCase("a")){
            return "attacking";
        } else if (turn.equalsIgnoreCase("m")){
            return "magic";
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

    public static String specialInfo(Player player){
        String type = player.getType();
        System.out.println("Special Moves will use 40MP");
        String description = "";
        if (type.equalsIgnoreCase("knight")) {
            description = "Throw a spear at the enemy, guaranteed to deal double damage";
        } else if (type.equalsIgnoreCase("mage")){
            description = "Heal 50% of your health";
        } else {
            description = "Double your critical chance for the rest of the fight";
        }
        return description;
    }

    public static void specialMove(Player player, Enemy enemy) throws InterruptedException {
        String type = player.getType();
        int enemyDef = enemy.getDefense();
        if(enemy.getAilments().contains("fractured")) {
            enemyDef /= 2;
        }
        int damage = m.calcDamage(player.getStats().get("Attack"), enemyDef);

        if (type.equalsIgnoreCase("knight")) {
            if (m.criticalHit(player.getStats().get("Speed"), false)) {
                System.out.println("You have time to sharpen your spear a little more");
                Thread.sleep(300);
                damage*=1.5;
            }
            System.out.printf("Quickly you take the high ground and throw your spear dealing %d damage %n", damage * 2);
            enemy.setHealth(enemy.getHealth() - damage * 2);

        } else if (type.equalsIgnoreCase("mage")){
            mageSpecial(player);

        } else {
            player.setSpecialUsed(true);
            player.setThiefSpecial(true);
            System.out.println("You realize you have not been wearing your glasses and put them on, the world is so much clearer now");
            Thread.sleep(500);
            if (m.criticalHit(player.getStats().get("Speed"), true)) {
                System.out.println("Critical Hit!");
                damage*=1.5;
            }
            System.out.printf("You attack for the first time dealing %d damage%n", damage);
            enemy.setHealth(enemy.getHealth() - damage);
        }
        Thread.sleep(400);
        player.setMp(player.getMp() - 40);
    }


    public static void attackMade(Player player, Enemy enemy) throws InterruptedException {
        int enemyDef = enemy.getDefense();
        if(enemy.getAilments().contains("fractured")) {
            enemyDef /= 2;
        }
        int damage = m.calcDamage(player.getStats().get("Attack"), enemyDef);
        System.out.println("\033[0;37mEnemy attempts to dodge");
        int enemySpeed = enemy.getSpeed();
        boolean enemyDodge = false;
        if(enemy.getAilments().contains("slow")){
            enemyDodge = m.blocked(enemySpeed, true);
        } else {
            enemyDodge = m.blocked(enemySpeed, false);
        }
        if(!enemyDodge){
            if (m.criticalHit(player.getStats().get("Speed"), player.isThiefSpecial())) {
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
        List<String> playerStatus = player.getState();

        int extraDefenseMult = 1;
        int extraDodgeMult = 1;
        int poisonDamage = 0;
        int confuseChance = 0;
        int ranConfuseHit = (int) Math.floor(Math.random() * 100);
        int healChance = (int) Math.floor(Math.random() * 100);
        int castChance = (int) Math.floor(Math.random() * 100);
        int infectChance = (int) Math.floor(Math.random() * 100);

        int infect = enemy.getInfect();
        Thread.sleep(600);

        if(enemy.isCaster()){
            currentAilments.remove("confuse");
            currentAilments.remove("blind");
            currentAilments.remove("slow");
        }
        if (currentAilments.contains("blind")) {
            extraDodgeMult = 2;
        }
        if (currentAilments.contains("poison")) {
            poisonDamage = (int) (enemy.getMaxHealth() * .05);
            if (poisonDamage < 1) {
                poisonDamage = 1;
            }
            System.out.println("\033[0;32mEnemy takes poison damage\033[0;38m");
            enemy.setHealth(enemy.getHealth() - poisonDamage);
            if(enemy.getHealth() <= 0){
                return;
            }
        }
        if (playerStatus.contains("shield")) {
            extraDefenseMult = 2;
        }
        if (currentAilments.contains("confuse")) {
            confuseChance = 33;
        }

        if (ranConfuseHit <= confuseChance && currentAilments.contains("confuse")) {
            int hitSelf = enemy.getAttack() / 2;
            if(hitSelf < 1){
                hitSelf = 1;
            }
            System.out.println("Enemy has hit itself for " + hitSelf);
            Thread.sleep(600);
            enemy.setHealth(enemy.getHealth() - hitSelf);

        } else {

            if (enemy.isCaster() && castChance < 33) {
                System.out.println("The monster begins to glow as the world fades to darkness…");
                Thread.sleep(500);
                if(!playerStatus.contains("sleep")){
                    playerStatus.add("sleep");
                }
            } else if (enemy.isHealer() && healChance < 33) {
                enemyHealerTurn(enemy, true);
            } else {
                int damage = m.calcDamage(enemy.getAttack(), player.getStats().get("Defense"));
                if (enemy.isCaster()) {
                    damage = m.calcDamage(enemy.getMagic(), player.getStats().get("Defense"));
                }
                damage /= extraDefenseMult;
                boolean youDodge = false;
                if(!playerStatus.contains("sleep")){
                    System.out.println("\033[0;37mYou attempt to dodge");
                    youDodge = m.blocked(player.getStats().get("Speed") * extraDodgeMult, false);
                } else {
                    System.out.println("You are asleep……");
                }
                Thread.sleep(600);

                int randomSummonBlock = (int) Math.floor(Math.random() * 100);

                if (enemy.getAccuracy() > 10) {
                    System.out.println("This one sees through your tricks");
                    if (m.criticalHit(enemy.getSpeed(), false)) {
                        System.out.println("Critical Hit!");
                        damage *= 1.5;
                    }
                    System.out.println("Enemy hits you for " + damage + " damage");
                    player.updateStat("Health", player.getHealth() - damage);
                } else if (!youDodge) {
                    if (randomSummonBlock < 20 && currentAilments.contains("cursed")) {
                        System.out.println("Your summon stumbles.. it falls in front of you and takes the hit");
                        Thread.sleep(300);
                    } else {
                        if (m.criticalHit(enemy.getSpeed(), false)) {
                            System.out.println("Critical Hit!");
                            damage *= 1.5;
                        }
                        System.out.println("Enemy hits you for " + damage + " damage");
                        if (infectChance <= infect) {
                            poison(player);
                        }
                        player.updateStat("Health", player.getHealth() - damage);
                    }
                } else {
                    System.out.println("You have successfully dodged the attack!");
                    Thread.sleep(600);
                }
            }
        }
        player.setState(playerStatus);
    }

    public static String magicOptions(Player player, Enemy enemy) throws InterruptedException {
        List<String> options = new ArrayList<>(Arrays.asList("s", "p", "b", "c", "d", "m", "t", "q", "f"));
        String result = "normal";
        art.specials();
        boolean success = m.successfulCast(player.getStats().get("Magic"));
        String specialChoice = sc.getInput("This is the way to fight! How will you win this?", options, false);
        if(specialChoice.equalsIgnoreCase("q")){
            return "quit";
        }
        if(success){
            result = handleSpecialTurn(specialChoice, enemy, player);
            if(result.equalsIgnoreCase("notEnough")){
                System.out.println("You do not have enough mp to cast this");
                return "quit";
            }
            System.out.println("Successful cast!");
        } else {
            Thread.sleep(600);
            System.out.println("You struggle to remember how to do this...");
        }
        Thread.sleep(600);
        return result;
    }

    private static String handleSpecialTurn(String specialTurn, Enemy enemy, Player player){
        boolean enemyMagician = enemy.isCaster();
        int availableMp = player.getMp();
        boolean small = availableMp >= 15;
        boolean large = availableMp >= 25;
        if (specialTurn.equalsIgnoreCase("s")){
            int currWorth = enemy.getWorth();
            int stolenAmount = currWorth / 2;
            enemy.setWorth(currWorth - (int) (currWorth * .25));
            System.out.printf("You sneak up and steal %d ¢%n", stolenAmount);
            player.setWallet(player.getWallet() + stolenAmount);
            return "stolen";
        } else if ((specialTurn.equalsIgnoreCase("c") || specialTurn.equalsIgnoreCase("d")) && !large) {
            return "notEnough";
        } else if (!small) {
            return "notEnough";
        } else {
            if (specialTurn.equalsIgnoreCase("b")) {
                if (enemyMagician) {
                    System.out.println("This one is immune……");
                }
                player.setMp(availableMp - 15);
                return "blind";
            } else if (specialTurn.equalsIgnoreCase("f")) {
                if (enemyMagician) {
                    System.out.println("This one is immune……");
                }
                player.setMp(availableMp - 20);
                return "fractured";
            } else if (specialTurn.equalsIgnoreCase("p")) {
                if (enemy.getInfect() > 49) {
                    System.out.println("This one is immune……");
                }
                player.setMp(availableMp - 15);
                return "poison";
            } else if (specialTurn.equalsIgnoreCase("m")) {
                System.out.println("You summon an undead ally");
                player.setMp(availableMp - 15);
                return "cursed";
            } else if (specialTurn.equalsIgnoreCase("t")) {
                if (enemyMagician) {
                    System.out.println("This one is immune……");
                }
                player.setMp(availableMp - 15);
                return "slow";
            } else if (specialTurn.equalsIgnoreCase("c")){
                if (enemyMagician) {
                    System.out.println("This one is immune……");
                }
                player.setMp(availableMp - 25);
                return "confuse";
            } else if (specialTurn.equalsIgnoreCase("d")) {
                List<String> currState = player.getState();
                currState.add("shield");
                player.setState(currState);
                player.setMp(availableMp - 25);
                return "normal";
            }
        }
        return "quit";
    }

    public static void takeEffect(Player player) throws InterruptedException {
        List<String> currentStatus = player.getState();
        if(currentStatus.contains("poison")){
            int damage = (int) (player.getMaxHealth() * .05);
            System.out.printf("\033[0;32mYou take %d poison damage\033[0;38m%n", damage);
            Thread.sleep(600);
            player.updateStat("Health", player.getHealth() - damage);
        }
    }

    public static void poison(Player player) throws InterruptedException {
        List<String> status = player.getState();
        Thread.sleep(600);
        System.out.println("You begin to slowly die");
        Thread.sleep(600);
        if(!status.contains("poison")){
            status.add("poison");
        }
        player.setState(status);
    }

    public static void undoPoison(Player player) {
        if (player.getState().contains("poison")) {
            player.updateStat("Health", player.getHealth() + (int) (player.getMaxHealth() * .05));
        }
    }

    public static void enemyHealerTurn(Enemy enemy, boolean enemyTurn) {
        int turnHeal = 0;
        if(enemyTurn) {
            turnHeal = (int) (enemy.getMaxHealth() * .18);
            System.out.printf("You blink and it is gone…… you hear the clanging of bottles in the distance and chase it down, enemy heals for %d%n", turnHeal);
        } else {
            turnHeal = (int) (enemy.getMaxHealth() * .02);
            if(turnHeal < 1){
                turnHeal = 1;
            }
            System.out.printf("You chase it down as it runs and drinks a potion, healing %d%n", turnHeal);
        }

        if(enemy.getHealth() + turnHeal > enemy.getMaxHealth()){
            turnHeal = enemy.getMaxHealth() - enemy.getHealth();
        }
        if(turnHeal < 0){
            turnHeal = 0;
        }
        enemy.setHealth(enemy.getHealth() + turnHeal);
    }

    public static void mageSpecial(Player player){
        int maxHp = player.getMaxHealth();
        int currHealth = player.getHealth();
        int amount = maxHp / 2;
        if(currHealth + amount > maxHp){
            System.out.println("You fully heal");
            player.updateStat("Health", maxHp);
        } else {
            System.out.printf("You heal for %d %n", amount); // some other message later
            player.updateStat("Health", currHealth + amount);
        }
    }

    public static boolean death(Player player) throws InterruptedException, IOException {
        System.out.println("You are dead and we need to take care of that");
        Thread.sleep(600);
        if(player.getInventory().get("Revive") != null) {
            System.out.println("\033[0;34mYou have used your revive to come back with 25% of your max HP, good on you for buying one");
            player.revive("Revive");
            Thread.sleep(600);
            player.updateStat("Health", player.getMaxHealth() / 4);
            return true;
        } else {
            System.out.println("What!? You do not have any revives.. well...");
            Thread.sleep(600);
            System.out.println("\033[0;31mGame over");
            fileReader.saveRecord(player, gambler);
            return false;
        }
    }

    public static void pushUpWork(Player player, Gambler gambler, FileReader saveWriter) throws IOException {
        saveWriter.createSaveState(player, gambler);
    }

}
