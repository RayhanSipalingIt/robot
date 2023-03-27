import java.util.Random;

public class Ranged extends Robot {    // class Ranged diturunkan dari class Robot
    private Random random = new Random();   // untuk random damage
    private int maxStack = 5;   // field max stack ability
    private int stack = 0;  // field stack ability

    public Ranged(String name, String type, int maxHealth, int power, int position) {   // constructor awal
        super(name, type, maxHealth, power, position);
        super.setRange(3);  // class Ranged berjarak 3 petak
    }

    public int getStack() { // method ambil nilai stack ability
        return stack;
    }

    private void plusStack() {  // method tambah stack ability
        stack += 1;
        System.out.println("[Projectile Overload] stack +1\n");
    }

    private void resetStack() { // method reset stack ability
        stack = 0;
    }

    @Override
    public void attack(Robot enemy) {   // override method attack karena mau ditambahkan ability class Ranged
        super.attack(enemy);
        plusStack();
    }

    public void skill1(Robot enemy) {   // method skill 1 class Ranged
        super.decreaseShield();
        int totalDamage = super.getPower() + random.nextInt(201) + 500 + super.getBuffPower();
        if (stack < maxStack) {
            System.out
                    .println(String.format("[%s menggunakan Piercing Shot terhadap %s]\n", super.getName(),
                            enemy.getName()));
            plusStack();
        } else {    // gandakan damage skill apabila stack abilty penuh
            System.out
                    .println(String.format("[%s menggunakan Piercing Shot (Overload) terhadap %s]\n", super.getName(),
                            enemy.getName()));
            totalDamage = totalDamage * 2;
            resetStack();
        }
        super.checkBuff();
        super.plusUltimateGauge();
        enemy.takeDamage(totalDamage);
    }

    public void skill2(Robot[] enemies) {   // method skill 2 class Ranged
        super.decreaseShield();
        int totalDamage = super.getPower() + random.nextInt(201) + super.getBuffPower() + 100;
        if (stack < maxStack) {
            System.out.println(String.format("[%s menggunakan Megaton Missiles]\n", super.getName()));
            plusStack();
        } else {    // gandakan damage skill apabila stack abilty penuh
            System.out.println(String.format("[%s menggunakan Megaton Missiles (Overload)]\n", super.getName()));
            totalDamage = totalDamage * 2;
            resetStack();
        }

        for (Robot enemy : enemies) {   // berikan damage ke tiap musuh menggunakan loop
            if (enemy.isDead()) {   // skip musuh apabila sudah mati
                continue;
            }
            enemy.takeDamage(totalDamage);
        }

        super.checkBuff();
        super.plusUltimateGauge();
    }

    public void ultimate(Robot enemy) {   // method ultimate class Ranged
        super.decreaseShield();
        int totalDamage = super.getPower() + (enemy.getHealth() / 100) + super.getBuffPower() + 2000;
        if (stack < maxStack) {
            System.out
                    .println(String.format("[%s menggunakan Pralaya terhadap %s]\n", super.getName(), enemy.getName()));
            plusStack();
        } else {    // gandakan damage ultimate apabila stack abilty penuh
            System.out.println(String.format("[%s menggunakan Pralaya [Overload] terhadap %s]\n", super.getName(),
                    enemy.getName()));
            totalDamage = totalDamage * 2;
            resetStack();
        }
        super.checkBuff();
        super.resetUltimateGauge();
        enemy.takeDamage(totalDamage);
    }

    @Override
    public String infoSkill1() {    // method ambil info skill 1 class Ranged
        return String.format("[Piercing Shot] menyerang satu target dengan tembakan laser");
    }

    @Override
    public String infoSkill2() {    // method ambil info skill 2 class Ranged
        return String.format("[Megaton Missiles] menyerang seluruh target dengan serangan rudal");
    }

    @Override
    public String infoUltimate() {    // method ambil info ultimate class Ranged
        return String.format("[Pralaya] menyerang satu target dengan tembakan pemusnah (power meningkat berdasarkan sisa HP target)");
    }

    @Override
    public String infoAbility() {    // method ambil info ability class Ranged
        return String.format("[Projectile Overload] tiap serangan dari User akan menambahkan satu stack Projectile (max 5 stack)\nApabila Projectile mencapai 5 stack, serangan skill atau ultimate berikutnya akan menghasilkan damage 2x lipat\nStack saat ini = %s/5", stack);
    }
}