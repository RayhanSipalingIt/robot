public abstract class Robot {
    private String name, type;  // field nama dan tipe
    private int maxHealth, health, power, range, position;  // field maxHP, HP, damage, jarak serang dan bergerak, serta posisi robot
    private int buffPower, buffDuration, ultimateGauge, shieldCount = 0;    // field damage buff dan durasinya, bar ultimate (gak guna sih..), serta jumlah shield
    private boolean dead = false;   // field mati atau hidup
    private boolean active = true;  // field aktif atau non-aktif (penanda apakah robot bersatu atau tidak) | NOTE: sebenarnya hanya perlu di robot besar (CentralBrain)

    Robot(String name, String type, int maxHealth, int power, int position) {   // constructor awal
        this.name = name;
        this.type = type;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.power = power;
        this.position = position;
    }

    public String getName() {   // method ambil nama robot
        return name;
    }

    public int getPower() {   // method ambil power robot
        return power;
    }

    public int getMaxHealth() {   // method ambil max HP robot
        return maxHealth;
    }

    public int getHealth() {   // method ambil HP robot
        return health;
    }

    void setHealth(double heal) {   // method set HP robot (untuk heal)
        health += heal;
        if (health > maxHealth || dead) {   // jika HP melewati max HP atau robot sudah mati, set kembali ke max HP
            health = maxHealth;
            dead = false;
        }
    }

    public int getBuffPower() {   // method ambil nilai buff power robot
        return buffPower;
    }

    public int getBuffDuration() {   // method ambil durasi buff robot
        return buffDuration;
    }

    public int getPosition() {   // method ambil posisi robot
        return position;
    }

    public boolean isDead() {   // method ambil kondisi nyawa robot
        return dead;
    }

    public boolean isActive(){   // method ambil kondisi aktif robot
        return active;
    }

    void setActive(boolean active){   // method set kondisi aktif robot
        this.active = active;
    }

    public String getType() {   // method ambil tipe robot
        return type;
    }

    void setType(String type) {   // method set tipe robot
        this.type = type;
    }

    public int getRange() {   // method ambil jarak serang dan bergerak robot
        return range;
    }

    void setRange(int range) {   // method set jarak serang dan bergerak robot
        this.range = range;
    }

    public int getUltimateGauge() {   // method ambil nilai bar ultimate robot
        return ultimateGauge;
    }

    void plusUltimateGauge() {   // method tambah nilai bar ultimate robot | NOTE: tidak terpakai
        ultimateGauge += 10;
    }

    void resetUltimateGauge() {   // method reset nilai bar ultimate robot | NOTE: tidak berguna
        ultimateGauge = 0;
    }

    void setBuff(int buffPower, int buffDuration) {   // method tambah buff untuk robot
        this.buffPower += buffPower;
        this.buffDuration += buffDuration;
    }

    void checkBuff() {     // method cek kondisi buff sekaligus kurangi durasi buff robot
        if (buffDuration > 0) {
            buffDuration -= 1;
        } else {
            buffPower = 0;
        }
    }

    void gainShield(int shieldCount) {   // method tambah shield untuk robot
        this.shieldCount = shieldCount;
    }

    public int getShieldCount() {   // method ambil nilai shield robot
        return shieldCount;
    }

    boolean decreaseShield() {   // method kurangi nilai shield robot
        if (shieldCount > 0) {
            shieldCount -= 1;
            return true;
        } else {
            return false;
        }
    }

    public void move(int newPosition) {   // method bergerak (pindah posisi)
        position = newPosition;
    }

    public void attack(Robot enemy) {   // method basic attack
        decreaseShield();
        int totalDamage = power + buffPower;
        checkBuff();
        plusUltimateGauge();
        System.out.println(String.format("[%s menggunakan Basic Attack terhadap %s]\n", name, enemy.getName()));
        enemy.takeDamage(totalDamage);
    }

    public void defend() {   // method bertahan (buat shield)
        gainShield(1);
        checkBuff();
        System.out.println(String.format("\n[%s membuat shield untuk bertahan]\n", this.name));
    }

    public void heal() {   // method heal diri sendiri
        decreaseShield();
        double heal = (int) maxHealth * 0.25;
        setHealth(heal);
        checkBuff();
        System.out.println(String.format("\n[%s menggunakan heal, HP dipulihkan sebesar %s]\n", this.name, heal));
    }

    void takeDamage(int damage) {   // method ketika robot menerima damage
        if (decreaseShield()) { // robot tidak menerima damage apabila memiliki shield
            System.out.println(String.format("%s menggunakan shield untuk bertahan dari serangan", this.name));
        } else {
            this.health -= damage;
            System.out.println(String.format("%s menerima damage sebesar %s", this.name, damage));
        }

        if (health > 0) {
            System.out.println(String.format("HP %s tersisa %s\n", this.name, this.health));
        } else {    // robot dinyatakan mati apabila HP mencapai 0
            dead = true;
            System.out.println(String.format("!!! %s telah mati !!!\n", this.name));
        }
    }

    public abstract String infoSkill1();    // method abstrak info skill 1

    public abstract String infoSkill2();    // method abstrak info skill 2

    public abstract String infoUltimate();    // method abstrak info ultimate

    public abstract String infoAbility();    // method abstrak info ability
}