package ers.group;

import java.io.*;
import java.util.*;

public class ScoreTabLogic {

    private final File file;

    public ScoreTabLogic(String filePath) {
        this.file = new File(filePath);
    }

    // ================== GPA Calculation ==================
    public double calculateGPA(String[] grades) {
        double sum = 0;
        int count = 0;
        for (String g : grades) {
            try {
                sum += Double.parseDouble(g.trim());
                count++;
            } catch (Exception ignored) {}
        }
        return count > 0 ? sum / count : 0;
    }

    // ================== Save Record ==================
    public void save(ScoreTabRecord record) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
            pw.println(record.toFileString());
        }
    }

    // ================== Search Record ==================
    public ScoreTabRecord search(String id, String semester) throws IOException {
        if (!file.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length < 13) continue;
                if (d[1].equals(id) && d[2].equals(semester)) {
                    ScoreTabRecord r = new ScoreTabRecord();
                    r.id = d[1];
                    r.name = d[0];
                    r.semester = d[2];
                    for (int i = 0; i < 5; i++) {
                        r.courses[i] = d[3 + i * 2];
                        r.grades[i] = d[4 + i * 2];
                        r.passed[i] = Double.parseDouble(d[4 + i * 2]) >= 3.0; // pass if grade >= 3.0
                    }
                    r.gpa = calculateGPA(r.grades);
                    return r;
                }
            }
        }
        return null;
    }

    // ================== Update Record ==================
    public boolean update(ScoreTabRecord updated) throws IOException {
        if (!file.exists()) return false;

        File temp = new File("temp_marksheet.txt");
        boolean updatedFlag = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file));
             PrintWriter pw = new PrintWriter(new FileWriter(temp))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length < 13) {
                    pw.println(line);
                    continue;
                }

                if (d[1].equals(updated.id) && d[2].equals(updated.semester)) {
                    pw.println(updated.toFileString());
                    updatedFlag = true;
                } else {
                    pw.println(line);
                }
            }
        }

        if (updatedFlag) {
            file.delete();
            temp.renameTo(file);
        } else {
            temp.delete();
        }

        return updatedFlag;
    }

    // ================== Load All Records ==================
    public List<ScoreTabRecord> loadAll() throws IOException {
        List<ScoreTabRecord> list = new ArrayList<>();
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length < 13) continue;

                ScoreTabRecord r = new ScoreTabRecord();
                r.id = d[1];
                r.name = d[0];
                r.semester = d[2];
                for (int i = 0; i < 5; i++) {
                    r.courses[i] = d[3 + i * 2];
                    r.grades[i] = d[4 + i * 2];
                    r.passed[i] = Double.parseDouble(d[4 + i * 2]) >= 3.0;
                }
                r.gpa = calculateGPA(r.grades);
                list.add(r);
            }
        }

        return list;
    }
}
