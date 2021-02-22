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
    private int extraSpeed = 0;
    private boolean caster = false;
    private boolean healer = false;
    private List<String> ailments;
    public Enemy(int number, int randomizer) throws InterruptedException {
        if(number % 14 == 0){
            randomizer = 99;
        } else if (randomizer > 98){
            randomizer = 98;
        }
        double multiplier = number * .5;
        if (number > 35) {
          for(int i = 35; i < number; i++){
              multiplier*=1.3;
          }
        } else if(number >= 29){
            multiplier *= 1.35;
        } else if (number >= 15){
            multiplier *= 1.25;
        }
        this.expValue = (int) ((12 * number) * .85);
        this.worth = number * 7;
        this.infect = 5;
        if (randomizer <= 5) {  // weak nothing enemy, easy kill but less exp / $ for it so kind of a bad thing
            System.out.println("\033[0;30mI think it is dead already……");
            Thread.sleep(600);
            multiplier *= .25;
            this.expValue *= .75;
            this.worth *= .75;
        } else if (randomizer <= 20) {  // pre-emptive strike
            System.out.println("\033[0;34mEnemy caught off guard, you have the edge");
            Thread.sleep(600);
            multiplier *= .85;
        } else if (randomizer <= 32) {  // more $
            System.out.println("\033[0;36mSomething is different with this one, i think it is all that money he is holding");
            Thread.sleep(600);
            this.worth *= 3.5;
            multiplier*=1.1;
            this.expValue *= 1.2;
        } else if (randomizer <= 45) {  // heals every turn, more health and chance to heal self on it's turn
            System.out.println("\033[0;35mYou hear the clinging of bottles as a beast charges at you");
            Thread.sleep(600);
            this.healer = true;
            this.extraHealth = 3 * number;
            this.worth *= 1.5;
            this.expValue *= 1.7;
        } else if (randomizer <= 58) {  // attacks can not be dodged
            System.out.println("\033[0;32mWeird… this one literally looks sharper");
            Thread.sleep(600);
            this.accuracy = 100;
            this.worth *= 1.3;
            this.expValue *= 1.3;
            extraSpeed += 5;
        } else if (randomizer <= 71) {  // 50% to poison you, immune to poison
            System.out.println("\033[0;32m…Cough..Cough… what is this thing");
            Thread.sleep(600);
            this.infect *= 10;
            this.expValue *= 1.3;
        } else if (randomizer <= 83) {  // can put you to sleep, immune to some spells and 90% resist to physical
            System.out.println("\033[0;34mMmmmaagic");
            Thread.sleep(600);
            this.caster = true;
            this.worth *= 1.3;
        } else if (randomizer <= 98) {  // ambush, slightly stronger
            System.out.println("\033[0;31mLooks like this beast was waiting for you...");
            Thread.sleep(600);
            multiplier *= 1.35;
            this.expValue *= 1.65;
            this.worth *= 1.55;
        } else {    // boss fight
            System.out.println("\033[0;31mThis is it. \033[0;36mThe moment you have trained for");
            Thread.sleep(800);
            System.out.println("\033[0;31m……");
            Thread.sleep(500);
            System.out.println("\033[0;36m………\033[0;38m");
            Thread.sleep(500);
            multiplier*= 1.85;
            this.expValue *= 3.8;
            this.worth *= 2.8;
        }
        this.health = (int) Math.floor(13 * multiplier) + extraHealth;
        this.maxHealth = this.health;
        this.attack = (int) Math.floor(4 * multiplier);
        this.speed = (int) Math.floor(2 * multiplier) + extraSpeed;
        this.magic = (int) Math.floor(2 * multiplier); // for magic enemy damage
        this.defense = (int) Math.floor(3.2 * multiplier);
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
