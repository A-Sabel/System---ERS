package ers.group;

import java.io.*;

public class ScoreController {

    private final File file;

    public ScoreController(File file) {
        this.file = file;
    }

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

    public void save(ScoreRecord record) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
            pw.println(record.toFileString());
        }
    }

    public ScoreRecord search(String id, String year, String sem) throws IOException {
        if (!file.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].equals(id) && d[2].equals(year) && d[3].equals(sem)) {
                    ScoreRecord r = new ScoreRecord();
                    r.id = d[0];
                    r.name = d[1];
                    r.year = d[2];
                    r.semester = d[3];

                    for (int i = 0; i < 5; i++) {
                        r.courses[i] = d[4 + i * 3];
                        r.grades[i] = d[5 + i * 3];
                        r.passed[i] = Boolean.parseBoolean(d[6 + i * 3]);
                    }
                    r.gpa = Double.parseDouble(d[d.length - 1]);
                    return r;
                }
            }
        }
        return null;
    }

    public boolean update(ScoreRecord updated) throws IOException {
        if (!file.exists()) return false;

        File temp = new File("temp.txt");
        boolean updatedFlag = false;

        try (
            BufferedReader br = new BufferedReader(new FileReader(file));
            PrintWriter pw = new PrintWriter(new FileWriter(temp))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].equals(updated.id) &&
                    d[2].equals(updated.year) &&
                    d[3].equals(updated.semester)) {

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
}
