
public class Method {
    public Method(){}
    public String cap(String input){
        return input.substring(0, 1).toUpperCase()+input.substring(1);
    }

    public boolean blocked(int speed, boolean slowed, int attackersSpeed) throws InterruptedException {
        if(speed >= 80){
            speed = 80;
        }
        speed -= (attackersSpeed / 4);
        if (speed < 0){
            speed = 6;
        }
        if(slowed){
            speed /= 2;
        }
        System.out.println("Chance is : " + speed + "%\033[0;38m");
        Thread.sleep(200);
        int ran = (int) Math.floor(Math.random() * 100);
        return speed > ran;
    }

    public int calcDamage(int attackerAttack, int defenderDefense){ // 20 attack against 20 defense result is 16 dmg
        int damage;
        if(defenderDefense < 80){                                   // 200 attack 200 defense result is 60 dmg
            double guard = (100 - defenderDefense) * .0076;
            damage = (int) Math.floor(attackerAttack * guard);     // trying new formula for when defense is over 100
        } else {
            int multiplier = 12;
            damage = attackerAttack * multiplier / defenderDefense;
            for (int i = Math.max(attackerAttack, defenderDefense); i >= 18; i -= 18) { // just trying anything that helps when it is scaled
                damage *= 1.18;
            }
        }
//        System.out.println("damage is : " + (damage > 5 ? damage : 5));
//        System.out.println();
        return damage > 5 ? damage : 5;
    }

    public boolean successfulCast(int magic) { // or speed if it is steal
        int chance = (int) (magic * 1.5);
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

    public int minNum(int input){
        return input > 1 ? input : 1;
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
        System.out.println("200 and 200");
        m.calcDamage(200, 200);
    }
}
