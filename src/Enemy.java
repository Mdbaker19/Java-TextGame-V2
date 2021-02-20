import java.util.ArrayList;
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
    private int accuracy = 0;
    private int extraHealth = 0;
    private boolean caster = false;
    private boolean healer = false;
    private List<String> ailments;
    public Enemy(int number, int randomizer) throws InterruptedException {
        if(number % 12 == 0){
            randomizer = 99;
        } else if (randomizer > 98){
            randomizer = 98;
        }
        double multiplier = number * .5;
        if (number > 35) {
          for(int i = 35; i < number; i++){
              multiplier*=1.3;
          }
        } else if(number >= 25){
            multiplier *= 1.3;
        } else if (number >= 15){
            multiplier *= 1.2;
        }
        this.expValue = (int) ((12 * number) * .85);
        this.worth = number * 5;
        this.infect = 5;
        Thread.sleep(600);
        if (randomizer < 5) {
            System.out.println("\033[0;30mI think it is dead already……");
            multiplier *= .25;
            this.expValue *= .55;
        } else if (randomizer < 15) {
            System.out.println("\033[0;34mEnemy caught off guard, you have the edge");
            multiplier *= .85;
        } else if (randomizer < 30) {
            System.out.println("\033[0;36mSomething is different with this one, i think it is all that money he is holding");
            this.worth *= 3;
            multiplier*=1.1;
        } else if (randomizer < 45) {
            System.out.println("\033[0;35mYou hear the clinging of bottles as a beast charges at you");
            this.healer = true;
            this.extraHealth = 4 * number;
        } else if (randomizer < 58 && randomizer > 45) {
            System.out.println("\033[0;32mWeird… this one literally looks sharper");
            this.accuracy = 100;
            this.worth *= 1.3;
            this.expValue *= 1.3;
        } else if (randomizer < 71 && randomizer > 58) {
            System.out.println("\033[0;32m…Cough..Cough… what is this thing");
            this.infect *= 10;
        } else if (randomizer < 83 && randomizer > 71) {
            System.out.println("\033[0;34mMmmmaagic");
            this.caster = true;
            this.worth *= 1.3;
        } else if (randomizer > 83 && randomizer < 98) {
            System.out.println("\033[0;31mLooks like this beast was waiting for you...");
            multiplier *= 1.35;
            this.expValue *= 1.55;
            this.worth *= 1.35;
        } else if (randomizer > 98) {
            System.out.println("\033[0;31mThis is it. \033[0;36mThe moment you have trained for");
            Thread.sleep(300);
            System.out.println("\033[0;31m……");
            Thread.sleep(300);
            System.out.println("\033[0;36m………\033[0;38m");
            multiplier*= 2.25;
            this.expValue *= 3.5;
            this.worth *= 2.5;
        }
        Thread.sleep(600);
        this.health = (int) Math.floor(13 * multiplier) + extraHealth;
        this.maxHealth = this.health;
        this.attack = (int) Math.floor(4 * multiplier);
        this.speed = (int) Math.floor(2 * multiplier);
        this.magic = (int) Math.floor(2 * multiplier); // for magic enemy damage
        this.defense = (int) Math.floor(3 * multiplier);
        this.ailments = new ArrayList<>();
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

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public List<String> getAilments() {
        return ailments;
    }

    public void setAilments(List<String> ailments) {
        this.ailments = ailments;
    }

    public boolean isCaster() {
        return caster;
    }

    public void setCaster(boolean caster) {
        this.caster = caster;
    }

    public boolean isHealer() {
        return healer;
    }

    public void setHealer(boolean healer) {
        this.healer = healer;
    }
}
