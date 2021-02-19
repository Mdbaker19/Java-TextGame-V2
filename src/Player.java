import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private Method m = new Method();
    private final double levelUpMultiplier = 1.1;

    private String name;
    private String type;
    private String state;
    private HashMap<String, Integer> stats;
    private HashMap<String, Integer> inventory = new HashMap<>();
    private int wallet;
    private int victories;
    private int level;
    private int exp;
    private int expRequirement;
    private int maxHealth;


    public Player(String name){
        this.name = name;
        this.wallet = 30;
        this.victories = 1;
        this.level = 1;
        this.exp = 0;
        this.expRequirement = 100;
        this.state = "normal";
        inventory.put("Antidote", 1);
        inventory.put("Potion", 1);
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

    public HashMap<String, Integer> getStats() {
        return stats;
    }
    public void viewStats() {
        System.out.println("Here are your stats for your class");
        System.out.println("Max health : " + this.getMaxHealth());
        for(Map.Entry<String, Integer> entry : this.getStats().entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("Current level is " +this.getLevel());
        System.out.println("Current EXP: " + this.getExp() +"/"+this.getExpRequirement());
    }
    public int getHealth(){
        return this.stats.get("Health");
    }

    public void initialStats(String type) {
        HashMap<String, Integer> stats = new HashMap<>();
        if (type.equalsIgnoreCase("thief")) {
            stats.put("Health", 120);
            stats.put("Attack", 10);
            stats.put("Defense", 10);
            stats.put("Magic", 18);
            stats.put("Speed", 28);
        } else if (type.equalsIgnoreCase("knight")) {
            stats.put("Health", 155);
            stats.put("Attack", 18);
            stats.put("Defense", 15);
            stats.put("Magic", 8);
            stats.put("Speed", 10);
        } else {
            stats.put("Health", 110);
            stats.put("Attack", 8);
            stats.put("Defense", 10);
            stats.put("Magic", 28);
            stats.put("Speed", 18);
        }
        this.stats = stats;
        this.maxHealth = stats.get("Health");
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setStats(HashMap<String, Integer> stats) {
        this.stats = stats;
    }

    public void updateStat(String stat, int value){ // mainly for healing
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
            this.setState("normal");
        } else if(item.equalsIgnoreCase("bomb")){
            int damage = (int) (enemy.getMaxHealth() * .2);
            System.out.println("You duck down and throw a bomb.. ");
            Thread.sleep(200);
            System.out.printf("You deal %d%n", damage);
            enemy.setHealth(enemy.getHealth() - damage);
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

        removeItem(m.cap(item));
    }

    public void revive(String item){
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
        this.expRequirement = (int) (this.getExpRequirement() * 1.35);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
