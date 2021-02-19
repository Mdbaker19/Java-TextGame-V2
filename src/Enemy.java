import java.util.List;

public class Enemy {
    private int health;
    private int maxHealth;
    private int attack;
    private int speed;
    private int magic;
    private int defense;
    private int worth;
    private int expValue;
    private int infect;
    private List<String> inventory;
    public Enemy(int number, int randomizer) throws InterruptedException {
        double multiplier = number * .42;
        this.expValue = 10 * number;
        this.worth = number * 5;
        this.infect = 5;
        Thread.sleep(600);
        if (randomizer < 5) {
            System.out.println("\033[0;30mI think it is dead already..");
            multiplier *= .15;
            this.expValue *= .55;
        } else if (randomizer < 25) {
            System.out.println("\033[0;34mEnemy caught off guard, you have the edge");
            multiplier *= .75;
            this.expValue *= .95;
        } else if (randomizer < 45) {
            System.out.println("\033[0;36mSomething is different with this one, i think it is all that money he is holding");
            this.worth *= 3.5;
        } else if (randomizer < 70 && randomizer > 60) {
            System.out.println("\033[0;32m..Cough..Cough.. what is this thing");
            this.infect *= 10;
        } else if (randomizer > 80 && randomizer < 95) {
            System.out.println("\033[0;31mLooks like this beast was waiting for you...");
            multiplier *= 1.4;
            this.expValue *= 1.55;
        } else if (randomizer > 95) {
            System.out.println("\033[0;31mThis is it. \033[0;36mThe moment you have trained for");
            System.out.println("\033[0;31m....");
            System.out.println("\033[0;36m....\033[0;38m");
            multiplier*= 2.5;
            this.expValue *= 3.5;
            this.worth *= 2.5;
        }
        Thread.sleep(600);
        this.health = (int) Math.floor(10 * multiplier);
        this.maxHealth = this.health;
        this.attack = (int) Math.floor(5 * multiplier);
        this.speed = (int) Math.floor(2 * multiplier);
        this.magic = (int) Math.floor(2 * multiplier);
        this.defense = (int) Math.floor(3 * multiplier);
    }

    public void showStats(int number){
        System.out.println("Enemy Level : " + number);
        System.out.println("Health : " + this.getHealth());
        System.out.println("Attack : " + this.getAttack());
        System.out.println("Defense : " + this.getDefense());
        System.out.println("Speed : " + this.getSpeed());
        System.out.println("Magic : " + this.getMagic());
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getInfect() {
        return infect;
    }

    public void setInfect(int infect) {
        this.infect = infect;
    }

    public int getExpValue() {
        return expValue;
    }

    public void setExpValue(int expValue) {
        this.expValue = expValue;
    }

    public int getWorth() {
        return worth;
    }

    public void setWorth(int worth) {
        this.worth = worth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public void setInventory(List<String> inventory) {
        this.inventory = inventory;
    }
}
