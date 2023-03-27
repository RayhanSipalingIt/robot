public class CentralBrain extends Robot implements CombiningRobot { // class CentralBrain diturunkan dari class Robot dan mengimplementasikan interface CombiningRobot
    private Robot[] listRobot; // field simpan kumpulan robot kecil

    CentralBrain(String name, String type, int maxHealth, int power, Robot[] allies) {   // constructor awal
        super(name, type, maxHealth, power, allies[0].getPosition());
        this.listRobot = allies;
        super.setRange(5);  // Robot besar memiliki jarak 5
    }

    @Override
    public void combine() {     // method penggabungan robot
        for (Robot robot : listRobot) {
            robot.setActive(false);     // seluruh robot kecil dinonaktifkan | NOTE: sebenarnya gak berefek sih
        }
    }

    @Override
    public void separate() {    // method membongkar robot
        for (Robot robot : listRobot) {
            robot.setActive(true);     // seluruh robot kecil diaktifkan | NOTE: ya gak berefek sih
        }
        super.setActive(false);     // robot besar dinonaktifkan | NOTE: ini yang paling penting!!!
    }

    public void skill1(Robot enemy, Robot[] allies) {   // method skill 1 class CentralBrain
        System.out.println(String.format("<%s menggunakan senjata milik %s>\n", super.getName(), listRobot[0].getName()));
        ((Melee)listRobot[0]).ultimate(enemy, listRobot);   // memakai ultimate dari class Melee sebagai skill 1 (ini fitur yah :D)
    }

    public void skill2(Robot[] enemies) {   // method skill 2 class CentralBrain
        System.out.println(String.format("\n<%s menggunakan senjata milik %s>\n", super.getName(), listRobot[2].getName()));
        ((Ranged)listRobot[2]).skill2(enemies);   // memakai skill 2 dari class Ranged sebagai skill 2 (ini juga fitur :3)
    }

    public void ultimate(Robot[] enemies) {   // method ultimate class CentralBrain
        System.out.println(String.format("\n[%s memanggil Morning Star]\n", super.getName()));
        int totalDamage = super.getPower() + (super.getMaxHealth() / 100) + super.getBuffPower() + 10000;
        super.checkBuff();
        resetUltimateGauge();
        for (Robot enemy : enemies) {   // seluruh musuh menerima damage
            if (enemy.isDead()) {   // skip musuh apabila sudah mati
                continue;
            }
            enemy.takeDamage(totalDamage);
        }
    }

    @Override
    public String infoSkill1() {    // method ambil info skill 1 class CentralBrain
        return String.format("[%s: Fatal End] menyerang satu target dengan hantaman dahsyat (power meningkat berdasarkan jumlah HP User yang telah hilang)", listRobot[0].getName());
    }

    @Override
    public String infoSkill2() {    // method ambil info skill 2 class CentralBrain
        return String.format("[%s: Megaton Missiles] menyerang seluruh target dengan serangan rudal", listRobot[2].getName());
    }

    @Override
    public String infoUltimate() {    // method ambil info ultimate class CentralBrain
        return "[Morning Star] menyerang seluruh target dengan serangan cahaya dashyat (power meningkat berdasarkan Max HP User)";
    }

    @Override
    public String infoAbility() {    // method ambil info ability class CentralBrain
        return "[Robot ini tidak memiliki ability khusus (tentu bukan karena sang kreator malas)]";
    }

}
