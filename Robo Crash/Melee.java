import java.util.Random;

public class Melee extends Robot {    // class Melee diturunkan dari class Robot
    private Random random = new Random();   // untuk random damage dan target ability

    public Melee(String name, String type, int maxHealth, int power, int position) {   // constructor awal
        super(name, type, maxHealth, power, position);
        super.setRange(1);  // class Melee hanya berjarak 1 petak
        super.gainShield(3);    // langsung mendapatkan 3 shield dari awal
    }

    @Override
    public void defend() {  // khusus Melee bisa tampung shield hingga 3, yang lain cuma 1
        gainShield(3);
        checkBuff();
        System.out.println(String.format("\n[%s membuat shield untuk bertahan]\n", super.getName()));
    }

    private void grantShieldRandom(Robot[] allies) {    // method ability class Melee (beri shield ke ally secara random)
        int index = random.nextInt(allies.length);  // acak target ally yang akan menerima shield
        allies[index].gainShield(1);
        System.out.println(String.format("[Shield of Aegis] %s memperoleh shield\n", allies[index].getName()));
    }

    public void skill1(Robot enemy, Robot[] allies) {   // method skill 1 class Melee
        System.out.println(String.format("[%s menggunakan Dual Slash terhadap %s]\n", super.getName(), enemy.getName()));
        int totalDamage = (super.getPower() + random.nextInt(51) + 50 + super.getBuffPower()) * 2;
        super.checkBuff();
        super.plusUltimateGauge();
        enemy.takeDamage(totalDamage);
        grantShieldRandom(allies);  // panggil method ability tiap menggunakan skill atau ultimate
    }

    public void skill2(Robot enemy, Robot[] allies) {   // method skill 2 class Melee
        System.out.println(
                String.format("[%s menggunakan Herculean Strike terhadap %s]\n", super.getName(), enemy.getName()));
        int totalDamage = super.getPower() + random.nextInt(100) + super.getBuffPower() + 500;
        super.checkBuff();
        super.plusUltimateGauge();
        enemy.takeDamage(totalDamage);
        grantShieldRandom(allies);  // panggil method ability tiap menggunakan skill atau ultimate
    }

    public void ultimate(Robot enemy, Robot[] allies) {   // method ultimate class Melee
        System.out.println(String.format("[%s menggunakan Fatal End terhadap %s]\n", super.getName(), enemy.getName()));
        int totalDamage = super.getPower() + ((super.getMaxHealth() - super.getHealth()) / 100) + super.getBuffPower() + 1000;
        super.checkBuff();
        resetUltimateGauge();
        enemy.takeDamage(totalDamage);
        grantShieldRandom(allies);  // panggil method ability tiap menggunakan skill atau ultimate
    }

    @Override
    public String infoSkill1() {    // method ambil info skill 1 class Melee
        return String.format("[Dual Slash] menyerang satu target dengan 2 kali tebasan");
    }

    @Override
    public String infoSkill2() {    // method ambil info skill 2 class Melee
        return String.format("[Herculean Strike] menyerang satu target dengan pukulan keras");
    }

    @Override
    public String infoUltimate() {    // method ambil info ultimate class Melee
        return String.format("[Fatal End] menyerang satu target dengan hantaman dahsyat (power meningkat berdasarkan jumlah HP User yang telah hilang)");
    }

    @Override
    public String infoAbility() {    // method ambil info ability class Melee
        return String.format("[Shield of Aegis] memberikan shield kepada satu rekan tim secara random");
    }

}