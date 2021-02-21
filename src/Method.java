
public class Method {
    public Method(){}
    public String cap(String input){
        return input.substring(0, 1).toUpperCase()+input.substring(1);
    }

    public boolean blocked(int speed){
        if(speed >= 80){
            speed = 80;
        }
        System.out.println("Chance is : " + speed + "%\033[0;38m");
        int ran = (int) Math.floor(Math.random() * 100);
        return speed > ran;
    }

    public int calcDamage(int attackerAttack, int defenderDefense){ // 20 attack against 20 defense result is 16 dmg
        double guard = (100 - defenderDefense) * .01;
        int total = (int) Math.floor(attackerAttack * guard);
        if(total < 5){
            total = 5;
        }
        return total;
    }

    public boolean successfulCast(int magic){
        int chance = magic * 2;
        if(chance >= 100){
            chance = 100;
        }
        System.out.println("Chance is : " + chance + "%");
        int ran = (int) Math.floor(Math.random() * 100);
        return chance > ran;
    }

    public boolean criticalHit(int speed){
        int ran = (int) Math.floor(Math.random() * 100);
        if(speed >= 75){
            return ran < 22;
        } else if(speed >= 50){
            return ran < 18;
        } else {
            return ran < speed * .35;
        }
    }
}
