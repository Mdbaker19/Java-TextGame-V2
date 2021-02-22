import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private Method m = new Method();
    private final double levelUpMultiplier = 1.17;

    private String name;
    private String type;
    private boolean specialUsed = false;
    private boolean thiefSpecial = false;
    private List<String> state;
    private HashMap<String, Integer> stats;
    private HashMap<String, Integer> inventory = new HashMap<>();
    private int wallet;
    private int victories;
    private int level;
    private int exp;
    private int expRequirement;
    private int maxHealth;
    private int mp;
    private int maxMp;


    public Player(String name){
        this.name = name;
        this.wallet = 30;
        this.victories = 1;
        this.level = 1;
        this.exp = 0;
        this.expRequirement = 100;
        this.state = new ArrayList<>();
        inventory.put("Antidote", 1);
        inventory.put("Potion", 1);
    }

    public Player(String name, String type, List<String> state, HashMap<String, Integer> stats, HashMap<String, Integer> inventory,
                  int wallet, int victories, int level, int exp, int expRequirement, int maxHealth, int mp, int maxMp) {
        this.name = name;
        this.type = type;
        this.state = state;
        this.stats = stats;
        this.inventory = inventory;
        this.wallet = wallet;
        this.victories = victories;
        this.level = level;
        this.exp = exp;
        this.expRequirement = expRequirement;
        this.maxHealth = maxHealth;
        this.mp = mp;
        this.maxMp = maxMp;
    }

    public void initialStats(String type) {
        HashMap<String, Integer> stats = new HashMap<>();
        if (type.equalsIgnoreCase("thief")) {
            stats.put("Health", 130);
            stats.put("Attack", 15);
            stats.put("Defense", 11);
            stats.put("Magic", 11);
            stats.put("Speed", 32);
            this.mp = 25;
        } else if (type.equalsIgnoreCase("knight")) {
            stats.put("Health", 175);
            stats.put("Attack", 18);
            stats.put("Defense", 18);
            stats.put("Magic", 7);
            stats.put("Speed", 9);
            this.mp = 10;
        } else {
            stats.put("Health", 95);
            stats.put("Attack", 9);
            stats.put("Defense", 9);
            stats.put("Magic", 30);
            stats.put("Speed", 18);
            this.mp = 100;
        }
        this.stats = stats;
        this.maxMp = this.mp;
        this.maxHealth = stats.get("Health");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHealth(){
        return this.stats.get("Health");
    }
    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
    public HashMap<String, Integer> getStats() {
        return stats;
    }
    public void setStats(HashMap<String, Integer> stats) {
        this.stats = stats;
    }

    public void viewStats() {
        System.out.println("Here are your stats");
        System.out.println("LVL : " +this.getLevel());
        System.out.println("EXP: " + this.getExp() +"/"+this.getExpRequirement());
        System.out.println("Max HP : " + this.getMaxHealth());
        for(Map.Entry<String, Integer> entry : this.getStats().entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("Mp : " + this.getMp() + "/" + this.getMaxMp());
        System.out.println(this.getWallet()+" ¢");
        System.out.println("Inventory contains : ");
    }


    public void updateStat(String stat, int value){
        HashMap<String, Integer> curr = this.getStats();
        curr.put(stat, value);
        this.setStats(curr);
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public HashMap<String, Integer> getInventory() {
        return inventory;
    }
    public List<String> getInventoryItems(){
        List<String> items = new ArrayList<>();
        items.add("nothing");
        HashMap<String, Integer> inventory = this.getInventory();
        for(Map.Entry<String, Integer> entry : inventory.entrySet()){
            items.add(entry.getKey() +" "+entry.getValue());
        }
        return items;
    }

    public void useItem(String item, Enemy enemy) throws InterruptedException {
        int max = this.getMaxHealth();
        int curr = this.getHealth();
        int currMp = this.getMp();
        int maxMp = this.getMaxMp();
        int recoveredMp = (int) (maxMp * .35);
        List<String> currState = this.getState();
        if(item.equalsIgnoreCase("potion")){
            if(curr + 25 > max){
                int healing = Math.abs(max - curr);
                System.out.printf("Potion healing for %d hp%n", healing);
                this.updateStat("Health", this.getHealth() + healing);
            } else {
                System.out.println("Potion healing for 25 hp");
                this.updateStat("Health", this.getHealth() + 25);
            }
        } else if(item.equalsIgnoreCase("antidote")){
            System.out.println("Antidote cures your poison");
            currState.remove("poison");
        } else if(item.equalsIgnoreCase("clock")){
            System.out.println("A Clock rings in the distance and wakes you up");
            currState.remove("sleep");
        } else if(item.equalsIgnoreCase("bomb")){
            int damage = (int) (enemy.getMaxHealth() * .2);
            System.out.println("You duck down and throw a bomb.. ");
            Thread.sleep(200);
            System.out.printf("You deal %d%n", damage);
            enemy.setHealth(enemy.getHealth() - damage);
        } else if(item.equalsIgnoreCase("ether")){

            if(currMp + recoveredMp > maxMp){
                int recover = Math.abs(maxMp - currMp);
                System.out.printf("Ether recovers for %d mp%n", recover);
                this.setMp(this.getMp() + recover);
            } else {
                System.out.printf("Ether recovers for %d mp%n", recoveredMp);
                this.setMp(this.getMp() + recoveredMp);
            }
        } else if(item.equalsIgnoreCase("hPotion")){

            if(curr + 55 > max){
                int healing = Math.abs(max - curr);
                System.out.printf("Potion healing for %d hp%n", healing);
                this.updateStat("Health", this.getHealth() + healing);
            } else {
                System.out.println("High Potion healing for 55 hp");
                this.updateStat("Health", this.getHealth() + 55);
            }
        } else if(item.equalsIgnoreCase("mega")){
            int amount = (int) (this.getMaxHealth() * .35);
            if(curr + amount > max){
                int healing = Math.abs(max - curr);
                System.out.printf("Potion healing for %d hp%n", healing);
                this.updateStat("Health", this.getHealth() + healing);
            } else {
                System.out.printf("Mega Potion healing for %d hp%n", amount);
                this.updateStat("Health", this.getHealth() + amount);
            }
        }
        this.setState(currState);
        removeItem(m.cap(item));
    }

    public void setInventory(HashMap<String, Integer> inventory) {
        this.inventory = inventory;
    }

    public void addItemToInventory(String item){
        HashMap<String, Integer> curr = getInventory();
        if(curr.containsKey(item)) {
            curr.put(item, curr.get(item) + 1);
        } else {
            curr.put(item, 1);
        }
    }

    public void removeItem(String item){
        HashMap<String, Integer> curr = getInventory();
        if(curr.get(item) > 1) {
            curr.put(item, curr.get(item) - 1);
        } else {
            curr.remove(item);
        }
        this.setInventory(curr);
    }

    public void viewInventory(){
        for(Map.Entry<String, Integer> entry : getInventory().entrySet()){
            System.out.println(entry.getKey() +" : " + entry.getValue());
        }
    }

    public int getVictories() {
        return victories;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }

    public int getLevel() {
        return level;
    }

    public void levelUp(){
        HashMap<String, Integer> curr = this.getStats();
        int yourHealth = this.getHealth();
        curr.replaceAll((k, v) -> (int) (v * levelUpMultiplier));
        this.setMaxHealth((int) (this.getMaxHealth() * levelUpMultiplier));
        curr.put("Health", yourHealth); // so you do not heal on level up
        this.setStats(curr);
        this.setLevel(this.getLevel() + 1);
        int currMpMax = this.getMaxMp();
        this.setMp(currMpMax);
        this.setMaxMp((int) (currMpMax * 1.15));
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) throws InterruptedException {
        int requirement = this.getExpRequirement();
        if(exp >= requirement){
            exp = Math.abs(requirement - (exp));
            this.levelUp();
            this.setExpRequirement();
            System.out.println("You leveled Up!");
            Thread.sleep(600);
        }
        this.exp = exp;
    }

    public int getExpRequirement() {
        return expRequirement;
    }

    public void setExpRequirement() {
        this.expRequirement = (int) (this.getExpRequirement() * 1.4);
    }

    public List<String> getState() {
        return state;
    }

    public void setState(List<String> state) {
        this.state = state;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(int maxMp) {
        this.maxMp = maxMp;
    }

    public boolean isSpecialUsed() {
        return specialUsed;
    }

    public void setSpecialUsed(boolean specialUsed) {
        this.specialUsed = specialUsed;
    }

    public boolean isThiefSpecial() {
        return thiefSpecial;
    }

    public void setThiefSpecial(boolean thiefSpecial) {
        this.thiefSpecial = thiefSpecial;
    }
}
