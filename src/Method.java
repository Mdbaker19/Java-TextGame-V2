
public class Method {
    public Method(){}
    public String cap(String input){
        return input.substring(0, 1).toUpperCase()+input.substring(1);
    }

    public boolean blocked(int speed, boolean slowed){
        if(speed >= 80){
            speed = 80;
        }
        if(slowed){
            speed /= 2;
        }
        System.out.println("Chance is : " + speed + "%\033[0;38m");
        int ran = (int) Math.floor(Math.random() * 100);
        return speed > ran;
    }

    public int calcDamage(int attackerAttack, int defenderDefense){ // 20 attack against 20 defense result is 16 dmg
        if(defenderDefense < 80){
            double guard = (100 - defenderDefense) * .0076;               // needs fixing when defense is > 100
            int damage = (int) Math.floor(attackerAttack * guard);     // trying new formula
            if(damage < 5) damage = 5;
            return damage;
        } else {
            int multiplier = 12;
            int damage = attackerAttack * multiplier / defenderDefense;
            for (int i = Math.max(attackerAttack, defenderDefense); i >= 18; i -= 18) { // just trying anything that helps when it is scaled
                damage *= 1.18;
            }
//        System.out.println("damage is : " + damage);
//        System.out.println();
            return damage;
        }
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

    public boolean criticalHit(int speed, boolean thiefSpecial){
        int ran = (int) Math.floor(Math.random() * 100);
        if(thiefSpecial){
            ran /= 2;
        }
        if(speed >= 75){
            return ran < 22;
        } else if(speed >= 50){
            return ran < 18;
        } else {
            return ran < speed * .35;
        }
    }

    public static void main(String[] args) {
        Method m = new Method();
        System.out.println("100, 98");
        m.calcDamage(100, 98);
        System.out.println("100, 918");
        m.calcDamage(100, 918);
        System.out.println("1000, 983");
        m.calcDamage(1000, 983);
        System.out.println("110, 9");
        m.calcDamage(110, 9);
        System.out.println("10, 9");
        m.calcDamage(10, 9);
        System.out.println("20 and 20");
        m.calcDamage(20, 20);
        System.out.println("45 and 57");
        m.calcDamage(45, 57);
        System.out.println("57 and 45");
        m.calcDamage(57, 45);
    }
}
