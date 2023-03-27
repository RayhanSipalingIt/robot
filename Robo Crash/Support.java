import java.util.Random;

public class Support extends Robot {    // class Support diturunkan dari class Robot
    private Random random = new Random();   // untuk random damage dan heal

    public Support(String name, String type, int maxHealth, int power, int position) {   // constructor awal
        super(name, type, maxHealth, power, position);
        super.setRange(4);  // class Support berjarak 5 petak
    }

    @Override
    public void attack(Robot enemy) {   // override method attack karena mau ditambahkan ability class Support
        super.attack(enemy);
        recovery();
    }

    private void recovery() {   // method ability class Support (pulihkan HP dan bar ultimate tiap menyerang)
        super.setHealth(super.getMaxHealth()*0.1);
        super.plusUltimateGauge();
        System.out.println(String.format("[Rejuvenation] %s memulihkan sedikit HP dan Ultimate Charge\n", super.getName()));
    }

    public void skill1(Robot ally) {   // method skill 1 class Support
        int totalBuff = super.getPower() + 500;
        System.out.println(String.format("[%s menggunakan Fiery Spirit terhadap %s]\n\n%s memperoleh buff power selama 2 giliran\n",
                        super.getName(), ally.getName(), ally.getName()));
        ally.setBuff(totalBuff, 2);     // berikan buff ke target ally
        super.checkBuff();
        super.plusUltimateGauge();
        recovery();  // panggil method ability tiap menggunakan skill atau ultimate
    }

    public void skill2(Robot enemy, Robot[] allies) {   // method skill 2 class Support
        System.out.println(String.format("[%s menggunakan Life Drain terhadap %s]\n", super.getName(), enemy.getName()));
        int totalDamage = super.getPower() + random.nextInt(200) + super.getBuffPower() + 300;
        enemy.takeDamage(totalDamage);
        System.out.println(String.format("Seluruh tim memulihkan HP sebanyak %s\n", (totalDamage / 2)));

        for (Robot ally : allies) { // heal seluruh rekan tim menggunakan loop
            if (ally.isDead()) {    // skip rekan apabila sudah mati
                continue;
            }
            ally.setHealth(totalDamage / 2);
        }

        super.checkBuff();
        super.plusUltimateGauge();
        recovery();  // panggil method ability tiap menggunakan skill atau ultimate
    }

    public void ultimate(Robot[] allies) {   // method ultimate class Support
        System.out.println(String.format("[%s menggunakan Salvation]\n\nHP seluruh tim kembali penuh\n", super.getName()));
        for (Robot ally : allies) { // pulihkan HP seluruh rekan tim hingga penuh 
            if (ally.isDead()) {    // bangkitkan rekan tim yang sudah mati
                System.out.println(String.format("%s berhasil dibangkitkan\n", ally.getName()));
            }
            ally.setHealth(ally.getMaxHealth());
        }
        super.checkBuff();
        resetUltimateGauge();
        recovery();  // panggil method ability tiap menggunakan skill atau ultimate
    }

    @Override
    public String infoSkill1() {    // method ambil info skill 1 class Support
        return String.format("[Fiery Spirit] memberikan buff power kepada satu rekan tim selama 2 giliran");
    }

    @Override
    public String infoSkill2() {    // method ambil info skill 2 class Support
        return String.format("[Life Drain] menyerap HP dari satu target, kemudian memulihkan HP seluruh tim berdasarkan sebagian dari HP yang diserap");
    }

    @Override
    public String infoUltimate() {    // method ambil info ultimate class Support
        return String.format(
                "\n[Salvation] memulihkan penuh HP seluruh tim, dan membangkitkan rekan tim yang telah mati");
    }

    @Override
    public String infoAbility() {    // method ambil info ability class Support
        return String.format("[Rejuvenation] memulihkan sebagian kecil HP dan Ultimate Charge setelah menyerang target");
    }
}