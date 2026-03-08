package ers.group;
import java.io.*;

public class generateMasterFiles {
    private static final String KEY1 = "COMPUTER";
    private static final String KEY2 = "SCIENCE";

    public static String encrypt(String text) {
        if (text == null) return "";
        StringBuilder sb = new StringBuilder();
        int keyIndex = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == ',' || c == '|' || c == '\n' || c == '\r' || c == ';') {
                sb.append(c);
            } else {
                int shift = (KEY1.charAt(keyIndex % KEY1.length()) + KEY2.charAt(keyIndex % KEY2.length())) % 95;
                sb.append((char) (((c - 32 + shift) % 95) + 32));
                keyIndex++;
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String dir = "ERS-group/src/ers/group/master files/";

        String courses =
            "CS101,Programming 1,5,5,true,NONE,1,1\n" +
            "CS102,Computer Fundamentals,3,5,false,NONE,1,1\n" +
            "CS103,Discrete Mathematics,3,5,false,NONE,1,1\n" +
            "CS104,Introduction to IT Systems,2,5,false,NONE,1,1\n" +
            "CS105,Ethics in Computing,2,5,false,NONE,1,1\n" +
            "CS201,Programming 2,5,5,true,CS101,1,2\n" +
            "CS202,Object-Oriented Programming,5,5,true,CS105,1,2\n" +
            "CS203,Web Technologies,3,5,false,CS104,1,2\n" +
            "CS204,Linear Algebra for Computing,3,5,false,CS103,1,2\n" +
            "CS205,Human-Computer Interaction,2,5,false,NONE,1,2\n" +
            "CS301,Data Structures and Algorithms,5,5,true,CS201,2,1\n" +
            "CS302,Design and Analysis of Algorithms,5,5,true,CS202;CS204,2,1\n" +
            "CS303,Database Management Systems,3,5,true,CS202,2,1\n" +
            "CS304,Computer Networks,3,5,false,CS104,2,1\n" +
            "CS305,Advanced Programming Concepts,2,5,false,CS201;CS203,2,1\n" +
            "CS401,Operating Systems,5,5,true,CS301;CS301;CS302,2,2\n" +
            "CS402,Compiler Design,5,5,true,CS302,2,2\n" +
            "CS403,Information Security,3,5,false,CS304,2,2\n" +
            "CS404,Distributed Systems,3,5,false,CS301;CS304,2,2\n" +
            "CS405,Systems Integration Project,2,5,false,CS303;CS305,2,2";

        String teachers =
            "T-001,Alice Santos,CS101;CS201;CS305\n" +
            "T-002,Ben Cruz,CS102;CS205;CS104\n" +
            "T-003,Carla Reyes,CS103;CS204\n" +
            "T-004,Daniel Lopez,CS202;CS301;CS302\n" +
            "T-005,Elena Navarro,CS203;CS303\n" +
            "T-006,Francis Dela Cruz,CS304;CS403;CS404\n" +
            "T-007,Grace Lim,CS401;CS405\n" +
            "T-008,Henry Tan,CS202;CS402\n" +
            "T-009,Irene Velasco,CS105;CS304";

        String rooms =
            "R101,Lecture Room 1,false,5\n" +
            "R102,Lecture Room 2,false,5\n" +
            "R103,Lecture Room 3,false,5\n" +
            "R104,Lecture Room 4,false,5\n" +
            "R105,Lecture Room 5,false,5\n" +
            "LAB201,Computer Laboratory 1,true,5\n" +
            "LAB202,Computer Laboratory 2,true,5\n" +
            "LAB203,Computer Laboratory 3,true,5\n" +
            "NET301,Networking Laboratory,true,5\n" +
            "SYS401,Systems Laboratory,true,5";

        writeEncrypted(dir + "courseSubjects.txt", courses);
        writeEncrypted(dir + "teacher.txt", teachers);
        writeEncrypted(dir + "room.txt", rooms);
        System.out.println("All master files generated successfully!");
    }

    static void writeEncrypted(String path, String plaintext) throws Exception {
        File f = new File(path);
        PrintWriter pw = new PrintWriter(
            new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
        pw.print(encrypt(plaintext));
        pw.close();
        System.out.println("Written: " + f.getAbsolutePath());
    }
}