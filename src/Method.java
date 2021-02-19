
public class Method {
    public Method(){}
    public String cap(String input){
        return input.substring(0, 1).toUpperCase()+input.substring(1);
    }

    public boolean blocked(int speed){
        System.out.println("Chance is : " + speed + "%\033[0;38m");
        int ran = (int) Math.floor(Math.random() * 100);
        return speed > ran;
    }

    public int calcDamage(int attackerAttack, int defenderDefense){ // 20 attack against 20 defense result is 16 dmg
        if(defenderDefense >= 50) {
            defenderDefense = 50;
        }
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
        return magic * 2 > ran;
    }

    public boolean criticalHit(int speed){
        int ran = (int) Math.floor(Math.random() * 100);
        if(speed >= 50){
            return ran < 15;
        } else {
            return ran < speed * .3;
        }
    }
}
