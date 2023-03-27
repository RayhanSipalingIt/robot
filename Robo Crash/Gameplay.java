import java.util.ArrayList;
import java.util.Scanner;

public class Gameplay { // class hanya untuk bermain (interface abal-abal)
    private char[] map; // field array char untuk map
    private ArrayList<Robot> allies = new ArrayList<Robot>();   // field simpan robot kecil (rekan tim)
    private ArrayList<Robot> enemies = new ArrayList<Robot>();  // field simpan robot lawan
    private CentralBrain largeRobot;    // deklarasi robot besar
    private final int validations[][] = { { 0, 9 }, { 11, 20 }, { 22, 31 }, { 33, 42 }, { 44, 53 }, { 55, 64 }, { 66, 75 }, { 77, 86 } };
    // array validations untuk menyimpan batas index yang boleh ditempati robot pada map

    public static void main(String[] args) throws InterruptedException {    // ini yah main method
        Gameplay play = new Gameplay();
        play.constructMap();
        play.constructRobot();
        play.mainMenu();
    }

    public void constructMap() {    // inisialisasi isi array char
        map = new char[88];
        for (int i = 0, j = 0; i < map.length; i++, j++) {
            if (j != 10) {
                map[i] = ' ';
            } else {
                map[i] = '\n';
                j = -1;
            }
        }
    }

    public void constructRobot() {  // deklarasi dan inisialisasi robot rekan tim dan robot lawan
        Melee OGHMA = new Melee("OGHMA", "Fighter", 20000, 1000, 36);
        Melee Marchosias = new Melee("Marchosias", "Fighter", 20000, 1000, 39);
        Ranged MK99 = new Ranged("MK99", "Marksman", 10000, 1500, 56);
        Ranged AA72 = new Ranged("AA72", "Marksman", 10000, 1500, 63);
        Support Croselle = new Support("Croselle", "Support", 5000, 500, 71);

        allies.add(OGHMA);
        allies.add(Marchosias);
        allies.add(MK99);
        allies.add(AA72);
        allies.add(Croselle);
        showSmallRobot();   // tampilkan robot kecil di map

        Melee Sandalphon = new Melee("Sandalphon", "Boss", 50000, 1000, 26);
        Ranged Metatron = new Ranged("Metatron", "Boss", 50000, 1000, 16);
        Support Uriel = new Support("Uriel", "Boss", 50000, 1000, 2);

        map[Sandalphon.getPosition()] = '1';    // tampilkan robot lawan di map
        map[Metatron.getPosition()] = '2';
        map[Uriel.getPosition()] = '3';
        enemies.add(Sandalphon);
        enemies.add(Metatron);
        enemies.add(Uriel);
    }

    public void combineRobot() {    // method robot bergabung
        if (cekDead()) {    // combine hanya bisa dilakukan apabila seluruh robot hidup
            hideSmallRobot();
            largeRobot = new CentralBrain("ANDRAS", "TITAN", 50000, 2000, allies.toArray(new Robot[0]));
            map[largeRobot.getPosition()] = 'T';
            largeRobot.combine();
            System.out.println("\n!!! Robot telah bergabung menjadi satu !!!\n");
        } else {
            System.out.println("!!! Terdapat robot yang telah mati, [Combine] tidak dapat dilakukan !!!");
        }
    }

    public void seperateRobot() {   // method robot terpisah
        showSmallRobot();
        largeRobot.separate();
        System.out.println("\n!!! Robot telah terbongkar !!!\n");
    }

    private boolean cekDead() {     // method cek apakah seluruh robot hidup
        for (Robot ally : allies) {
            if (ally.isDead()) {
                return false;   // kembalikan false apabila salah satu robot sudah mati
            }
        }
        return true;
    }

    private void hideSmallRobot() {     // method hilangkan robot kecil dari map (dipakai ketika CombineRobot)
        for (Robot ally : allies) {
            map[ally.getPosition()] = ' ';
        }
    }

    private void showSmallRobot() {     // method tampilkan robot kecil di map
        char huruf = 'A';   // iterasi huruf alfabet (yup bukan cuma angka bisa, alfabet pun bisa diiterasikan)
        for (Robot ally : allies) {
            map[ally.getPosition()] = huruf;
            huruf++;
        }
    }

    public void mainMenu() throws InterruptedException {    // tampilkan menu utama
        while (true) {
            System.out.println("~~~ ROBO CRASH [Definitely-Not-Gundam] ~~~");
            System.out.println("\n1. Battle\n2. Cek status robot");
            if (largeRobot == null || !largeRobot.isActive()) {
                System.out.println("3. Combine Robot");
            } else {
                System.out.println("3. Seperate Robot");
            }
            System.out.println("4. Exit\n");
            System.out.print("Pilih tindakan: ");
            int tindakan = new Scanner(System.in).nextInt();
            switch (tindakan) {
                case 1:
                    battle();   // panggil menu battle
                    break;
                case 2:
                    menuStatus();   // panggil menu status
                    break;
                case 3:
                    if (largeRobot == null || !largeRobot.isActive()) {
                        combineRobot();     // panggil method CombineRobot
                    } else {
                        seperateRobot();    // panggil method SeperateRobot
                    }
                    break;
                case 4:
                    System.out.println("\n!!! THANK YOU FOR PLAYING !!!");
                    return;
                default:
                    System.out.println("\n!!! Pilihan tidak valid, silahkan coba lagi !!!\n");
            }
            Thread.sleep(3000); // tambahkan delay biar gak terlalu spam
        }

    }

    private void updateMap(int changes, Robot player) {     // method perbarui posisi robot di map (dipanggil ketika robot berhasil move)
        map[changes] = map[player.getPosition()];
        map[player.getPosition()] = ' ';
        player.move(changes);
    }

    private void printMap() {   // method print array char di terminal sehingga berbentuk map
        for (char kotak : map) {
            System.out.print("|");
            System.out.print(kotak);
        }
    }

    private void robotList() {  // method print daftar robot rekan tim
        char huruf = 'A';
        for (Robot ally : allies) {
            System.out.print(String.format("[%s] %s", huruf, ally.getName()));
            if (ally.isDead()) {
                System.out.print(" [DEAD]\n");
            } else {
                System.out.println();
            }
            huruf++;
        }
    }

    private void enemyList() {  // method print daftar robot lawan
        for (int i = 0; i < enemies.size(); i++) {
            System.out.print(String.format("\n[%s] %s", (i + 1), enemies.get(i).getName()));
            if (enemies.get(i).isDead()) {
                System.out.print(" [DEAD]");
            }
        }
    }

    private void menuStatus() throws InterruptedException {  // menu status
        System.out.println("\n--- STATUS ROBOT ---");
        if (largeRobot == null || !largeRobot.isActive()) { // cek apakah robot besar aktif atau tidak
            robotList();    // pilih robot jika tidak aktif
            System.out.print("\nCek status robot (Tekan '0' untuk kembali): ");
            int index = pilihRobot();
            if (cekAlly(index)) {   // cek apakah pilihan robot valid
                statusRobot(allies.get(index));
            }
        } else {
            statusRobot(largeRobot);    // jika robot besar aktif, langsung ke tampilan status, tidak perlu pilih robot
        }
    }

    private void statusRobot(Robot ally) throws InterruptedException {  // method tampilan status robot yang dipilih
        System.out.println(String.format("<%s>", ally.getName()));
        System.out.println(String.format("HP: %s/%s | Ultimate: %s\n", ally.getHealth(), ally.getMaxHealth(),
                ally.getUltimateGauge()));
        System.out.println(
                String.format("Type: %s\nPower: %s\nRange: %s\nBuff: +%s power selama %s giliran\nShield: %s\n",
                        ally.getType(), ally.getPower(), ally.getRange(),
                        ally.getBuffPower(), ally.getBuffDuration(), ally.getShieldCount()));
        Thread.sleep(3000);
        System.out.print(String.format("Skill 1:\n%s\n\n", ally.infoSkill1()));
        System.out.print(String.format("Skill 2:\n%s\n\n", ally.infoSkill2()));
        System.out.print(String.format("Ultimate:\n%s\n\n", ally.infoUltimate()));
        System.out.print(String.format("Ability:\n%s\n\n", ally.infoAbility()));
    }

    private void battle() { // tampilkan menu battle
        System.out.println("\n   ~~~ BATTLE ~~~");
        printMap();     // tampilkan map dan seluruh posisi robot

        if (largeRobot == null || !largeRobot.isActive()) {     // cek apakah robot besar aktif
            System.out.println("\nPilih robot yang ingin digunakan:");
            robotList();
            System.out.print("\nRobot = ");
            int index = pilihRobot();
            if (cekAlly(index)) {       // cek apakah pilihan robot valid
                robotAction(allies.get(index));
            }
        } else {
            System.out.println();
            robotAction(largeRobot);    // jika robot besar aktif, langsung ke tampilan action, tidak perlu pilih robot
        }
    }

    private void robotAction(Robot player) {    // tampilkan menu action
        System.out.println(String.format("<%s>\n", player.getName()));

        System.out.println("[W] Move Up | [S] Move Down | [A] Move Left | [D] Move Right | [E] Defense");
        System.out.print("[X] Basic Attack | [1] Skill 1 | [2] Skill 2 | [3] Ultimate | [H] Heal\n\nPilih tindakan: ");
        String choose = new Scanner(System.in).next();

        switch (choose.toLowerCase()) {
            case "w", "s", "a", "d":
                cekMove(choose, player);    // panggil menu cek move
                break;
            case "e":
                player.defend();    // robot memakai defend
                break;
            case "h":
                player.heal();      // robot memakai heal
                break;
            case "x":
                cekBasicAtk(player);    // panggil menu cek basic attack
                break;
            case "1":
                cekSkill1(player);    // panggil method cek skill 1
                break;
            case "2":
                cekSkill2(player);    // panggil method cek skill 2
                break;
            case "3":
                cekUltimate(player);    // panggil method cek ultimate
                break;
            case "0":
                System.out.println("\n[Kembali ke menu utama]\n");
                break;
            default:
                System.out.println("\n!!! Pilihan tidak valid, kembali ke menu utama !!!\n");
        }
    }

    private void cekMove(String choose, Robot player) {     // menu cek move
        int changes = -1;
        String move = "";

        System.out.print("Jarak: ");
        int range = new Scanner(System.in).nextInt();

        switch (choose.toLowerCase()) {
            case "w":
                changes = player.getPosition() - 11 * range;    // rumus posisi baru apabila bergerak ke atas
                move = "atas";
                break;
            case "s":
                changes = player.getPosition() + 11 * range;    // rumus posisi baru apabila bergerak ke bawah
                move = "bawah";
                break;
            case "a":
                changes = player.getPosition() - range;    // rumus posisi baru apabila bergerak ke kiri
                move = "kiri";
                break;
            case "d":
                changes = player.getPosition() + range;    // rumus posisi baru apabila bergerak ke kanan
                move = "kanan";
                break;
        }

        if (range > player.getRange()) {    // cek apakah jumlah langkah melebihi batas gerak robot
            System.out.println(String.format("\n%s hanya dapat berjalan sebanyak %s petak\n", player.getName(), player.getRange()));
            return;
        } else if ((changes < 0 || changes >= 87)) {    // cek apakah posisi tujuan berada di luar map
            System.out.println("\nRobot tidak dapat bergerak keluar arena\n");
            return;
        } else if ("a".equals(choose) || "d".equals(choose)) {
            System.out.println("tes");
            for (int[] validation : validations) {      // cek apakah posisi baru melewati batas kiri kanan map (khusus jika bergerak ke kiri atau kanan)
                if ((player.getPosition() >= validation[0] && player.getPosition() <= validation[1])
                        && ((changes < validation[0]) || (changes > validation[1]))) {
                    System.out.println("\nRobot tidak dapat bergerak keluar arena\n");
                    return;
                }
            }
        }

        if (map[changes] != ' ') {  // cek apakah posisi baru masih kosong
            System.out.println("\nPetak sudah ditempati oleh robot lain\n");
        } else {
            updateMap(changes, player);
            System.out.println(String.format("\n[%s bergerak ke %s sebanyak %s petak]\n", player.getName(), move, range));
        }

    }

    private int pilihRobot() {  // menu pilih robot rekan tim
        String pilih = new Scanner(System.in).nextLine();
        int index = 5;
        switch (pilih.toLowerCase()) {
            case "a":
                index = 0;
                break;
            case "b":
                index = 1;
                break;
            case "c":
                index = 2;
                break;
            case "d":
                index = 3;
                break;
            case "e":
                index = 4;
                break;
            case "0":
                System.out.println("\n[Kembali ke menu utama]");
                break;
            default:
                System.out.println("\n!!! Pilihan tidak valid, kembali ke menu utama !!!");
        }
        System.out.println();
        return index;
    }

    private int pilihEnemy() {  // menu pilih target lawan
        System.out.print("\n\nTarget: ");
        int target = new Scanner(System.in).nextInt();
        System.out.println();
        return (target - 1);
    }

    private boolean cekAlly(int index) {    // method cek robot rekan tim
        if (index == 5) {
            return false;
        } else if (allies.get(index).isDead()) {
            System.out.println(String.format("!!! %s telah mati !!!\n", allies.get(index).getName()));
            return false;
        }
        return true;
    }

    private boolean cekEnemy(int index, int listSize) {    // method cek robot lawan
        if (!(index >= 0 && index < listSize)) {
            System.out.println("\n!!!Target tidak ditemukan !!!\n");
            return false;
        } else if (enemies.get(index).isDead()) {
            System.out.println("\n!!!! Target telah mati !!!\n");
            return false;
        } else {
            return true;
        }
    }

    private boolean cekRange(Robot player, Robot target) {  // method cek target serangan
        int targetPosition = target.getPosition();
        int boundLeft = player.getPosition() - player.getRange();
        int boundRight = player.getPosition() + player.getRange();

        for (int i = -(player.getRange()); i < player.getRange(); i++) {
            // cek apakah target berada dalam jangkauan serangan robot
            if (targetPosition >= (boundLeft + (i * 11)) && targetPosition <= (boundRight + (i * 11))) {
                return true;
            }
        }
        System.out.println("\nTarget berada di luar jangkauan User\n");
        return false;
    }

    private void cekBasicAtk(Robot player) {    // menu cek basic attack
        enemyList();
        int index = pilihEnemy();
        // cek apakah target valid dan berada dalam jangkauan serangan
        if (cekEnemy(index, enemies.size()) && cekRange(player, enemies.get(index))) {
            player.attack(enemies.get(index));
        }
    }

    private void cekSkill1(Robot player) {  // method cek skill 1
        if (player instanceof Support) {    // apabila robot merupakan Support
            robotList();
            System.out.print("\nRobot: ");
            int index = pilihRobot();
            // cek apakah target valid dan berada dalam jangkauan serangan
            if (cekAlly(index) && cekRange(player, allies.get(index))) {
                ((Support) player).skill1(allies.get(index));
            }
        } else {
            enemyList();
            int index = pilihEnemy();
            // cek apakah target valid dan berada dalam jangkauan serangan
            if (cekEnemy(index, enemies.size()) && cekRange(player, enemies.get(index))) {
                if (player instanceof Melee) {    // apabila robot merupakan Melee
                    ((Melee) player).skill1(enemies.get(index), allies.toArray(new Robot[0]));
                } else if (player instanceof Ranged) {    // apabila robot merupakan Ranged
                    ((Ranged) player).skill1(enemies.get(index));
                } else if (player instanceof CentralBrain) {    // apabila robot merupakan Robot besar
                    ((CentralBrain) player).skill1(enemies.get(index), allies.toArray(new Robot[0]));
                }
            }
        }
    }

    private void cekSkill2(Robot player) {  // method cek skill 2
        if (player instanceof Ranged) {    // apabila robot merupakan Ranged
            ((Ranged) player).skill2(enemies.toArray(new Robot[0]));
        } else if (player instanceof CentralBrain) {    // apabila robot merupakan Robot besar
            ((CentralBrain) player).skill2(enemies.toArray(new Robot[0]));
        } else {
            enemyList();
            int index = pilihEnemy();
            // cek apakah target valid dan berada dalam jangkauan serangan
            if (cekEnemy(index, enemies.size()) && cekRange(player, enemies.get(index))) {
                if (player instanceof Melee) {    // apabila robot merupakan Melee
                    ((Melee) player).skill2(enemies.get(index), allies.toArray(new Robot[0]));
                } else if (player instanceof Support) {    // apabila robot merupakan Support
                    ((Support) player).skill2(enemies.get(index), allies.toArray(new Robot[0]));
                }
            }
        }
    }

    private void cekUltimate(Robot player) {  // method cek ultimate
        if (player instanceof Support) {    // apabila robot merupakan Support
            ((Support) player).ultimate(allies.toArray(new Robot[0]));
        } else if (player instanceof CentralBrain) {    // apabila robot merupakan Robot besar
            ((CentralBrain) player).ultimate(enemies.toArray(new Robot[0]));
        } else {
            enemyList();
            int index = pilihEnemy();
            // cek apakah target valid dan berada dalam jangkauan serangan
            if (cekEnemy(index, enemies.size()) && cekRange(player, enemies.get(index))) {
                if (player instanceof Melee) {    // apabila robot merupakan Melee
                    ((Melee) player).ultimate(enemies.get(index), allies.toArray(new Robot[0]));
                } else if (player instanceof Ranged) {    // apabila robot merupakan Ranged
                    ((Ranged) player).ultimate(enemies.get(index));
                }
            }
        }
    }

}