import java.util.List;
import com.datastax.driver.core.*;

class Geospatial {

    private static final String KEYSPACE_NAME = "fingerprint_system";

    private StudentStats[] stats;

    class StudentStats {
        String studentName;
        float avgGrade;
        float attendancePercentage;
    }

    Geospatial(Session session) {
        session.execute("USE " + KEYSPACE_NAME);

        List<Row> grades = session.execute("SELECT * FROM grades").all();
        List<Row> lectures = session.execute("SELECT * FROM lectures").all();
        List<Row> students = session.execute("SELECT * FROM students").all();
        List<Row> student_course = session.execute("SELECT * FROM student_course").all();
        List<Row> attendance = session.execute("SELECT * FROM attendance").all();

        stats = new StudentStats[students.size()];
        for (int i = 0; i < stats.length; i++) {
            stats[i] = new StudentStats();
            stats[i].studentName = students.get(i).getString("fname") + " "
                    + students.get(i).getString("lname");
        }

        for (int i = 0; i < stats.length; i++) {
            int fid = students.get(i).getInt("fid");
            int count = 0;
            stats[i].avgGrade = 0.0f;

            for (Row grade : grades) {
                if (grade.getInt("fid") == fid) {
                    stats[i].avgGrade += grade.getInt("value");
                    count++;
                }
            }

            if (count != 0) {
                stats[i].avgGrade /= (float)count;
            }
        }

        int[] course_by_lecture = new int[lectures.size()];
        for (Row value : lectures) {
            int lecture = value.getInt("lid");
            course_by_lecture[lecture] = value.getInt("cid");
        }

        for (int i = 0; i < stats.length; i++) {
            int fid = students.get(i).getInt("fid");
            stats[i].attendancePercentage = 0.0f;

            int overall = 0;
            for (Row lecture : lectures) {
                int lecture_course = lecture.getInt("cid");
                for (Row row : student_course) {
                    if (fid == row.getInt("fid") && lecture_course == row.getInt("cid")) {
                        overall++;
                        break;
                    }
                }
            }

            int attended = 0;
            for (Row attended_lecture : attendance) {
                if (fid != attended_lecture.getInt("fid")) { continue; }
                int lecture_course = course_by_lecture[attended_lecture.getInt("lid")];
                for (Row row : student_course) {
                    if (lecture_course == row.getInt("cid")) {
                        attended++;
                        break;
                    }
                }
            }

            if (overall > 0) {
                stats[i].attendancePercentage = 100.0f * (float) attended / (float) overall;
            }
        }

        System.out.println("Students stats:");
        System.out.println("Name\t | Avg. Grade\t | Attendance");
        System.out.println("======================================");
        for (StudentStats stat : stats) {
            System.out.printf("%s \t | %.1f \t\t\t | %.1f",
                    stat.studentName,
                    stat.avgGrade,
                    stat.attendancePercentage);
            System.out.println("%");
        }
        System.out.println();

        Plotter plotter = new Plotter();
        plotter.PlotStudentsStats(stats);
    }

    void SearchClosest(String name) {
        float originX = 0.0f, originY = 0.0f;
        for (StudentStats stat : stats) {
            if (stat.studentName.equals(name)) {
                originX = stat.avgGrade;
                originY = stat.attendancePercentage;
                break;
            }
        }

        String closestName = name;
        float closestValue = Float.MAX_VALUE;
        for (StudentStats stat : stats) {
            if (!stat.studentName.equals(name)) {
                float dx = stat.avgGrade - originX;
                float dy = (stat.attendancePercentage - originY) / 10.0f;
                float dist = dx * dx + dy * dy;

                if (dist < closestValue) {
                    closestName = stat.studentName;
                    closestValue = dist;
                }
            }
        }

        System.out.printf("Closest student to %s is %s [%.2f]", name, closestName, closestValue);
        System.out.println();
    }

}