import javax.swing.*;
import java.awt.*;

class Plotter extends JFrame {

    private Geospatial.StudentStats[] Stats;

    void PlotStudentsStats(Geospatial.StudentStats[] stats) {
        Stats = stats;

        CartesianPanel panel = new CartesianPanel();
        add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Students Stats");
        setSize(700, 700);
        setVisible(true);
    }

    class CartesianPanel extends JPanel {
        static final int X_AXIS_FIRST_X_COORD = 50;
        static final int X_AXIS_SECOND_X_COORD = 600;
        static final int X_AXIS_Y_COORD = 600;

        static final int Y_AXIS_FIRST_Y_COORD = 50;
        static final int Y_AXIS_SECOND_Y_COORD = 600;
        static final int Y_AXIS_X_COORD = 50;

        static final int FIRST_LENGTH = 10;
        static final int SECOND_LENGTH = 5;

        static final int ORIGIN_COORDINATE_LENGTH = 6;

        static final int AXIS_STRING_DISTANCE = 20;

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw X Axis
            g2.drawLine(X_AXIS_FIRST_X_COORD, X_AXIS_Y_COORD,
                    X_AXIS_SECOND_X_COORD, X_AXIS_Y_COORD);
            // Draw Y Axis
            g2.drawLine(Y_AXIS_X_COORD, Y_AXIS_FIRST_Y_COORD,
                    Y_AXIS_X_COORD, Y_AXIS_SECOND_Y_COORD);

            // X Axis Arrow
            g2.drawLine(X_AXIS_SECOND_X_COORD - FIRST_LENGTH,
                    X_AXIS_Y_COORD - SECOND_LENGTH,
                    X_AXIS_SECOND_X_COORD, X_AXIS_Y_COORD);
            g2.drawLine(X_AXIS_SECOND_X_COORD - FIRST_LENGTH,
                    X_AXIS_Y_COORD + SECOND_LENGTH,
                    X_AXIS_SECOND_X_COORD, X_AXIS_Y_COORD);

            // Y Axis Arrow
            g2.drawLine(Y_AXIS_X_COORD - SECOND_LENGTH,
                    Y_AXIS_FIRST_Y_COORD + FIRST_LENGTH,
                    Y_AXIS_X_COORD, Y_AXIS_FIRST_Y_COORD);
            g2.drawLine(Y_AXIS_X_COORD + SECOND_LENGTH,
                    Y_AXIS_FIRST_Y_COORD + FIRST_LENGTH,
                    Y_AXIS_X_COORD, Y_AXIS_FIRST_Y_COORD);

            // Origin Point
            g2.fillOval(
                    X_AXIS_FIRST_X_COORD - (ORIGIN_COORDINATE_LENGTH / 2),
                    Y_AXIS_SECOND_Y_COORD - (ORIGIN_COORDINATE_LENGTH / 2),
                    ORIGIN_COORDINATE_LENGTH, ORIGIN_COORDINATE_LENGTH);

            // Draw text of axises
            g2.drawString("Average Grades", X_AXIS_SECOND_X_COORD - AXIS_STRING_DISTANCE / 2,
                    X_AXIS_Y_COORD + AXIS_STRING_DISTANCE);
            g2.drawString("Attendance", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                    Y_AXIS_FIRST_Y_COORD - AXIS_STRING_DISTANCE);
            g2.drawString("(0, 0)", X_AXIS_FIRST_X_COORD - AXIS_STRING_DISTANCE,
                    Y_AXIS_SECOND_Y_COORD + AXIS_STRING_DISTANCE);

            // Numerate Axis
            int xCoordNumbers = 11;
            int yCoordNumbers = 11;
            int xLength = (X_AXIS_SECOND_X_COORD - X_AXIS_FIRST_X_COORD)
                    / xCoordNumbers;
            int yLength = (Y_AXIS_SECOND_Y_COORD - Y_AXIS_FIRST_Y_COORD)
                    / yCoordNumbers;

            // Draw X Axis Numbers
            for (int i = 1; i < xCoordNumbers; i++) {
                g2.drawLine(X_AXIS_FIRST_X_COORD + (i * xLength),
                        X_AXIS_Y_COORD - SECOND_LENGTH,
                        X_AXIS_FIRST_X_COORD + (i * xLength),
                        X_AXIS_Y_COORD + SECOND_LENGTH);
                g2.drawString(Integer.toString(i),
                        X_AXIS_FIRST_X_COORD + (i * xLength) - 3,
                        X_AXIS_Y_COORD + AXIS_STRING_DISTANCE);
            }

            // Draw Y Axis Numbers
            for (int i = 1; i < yCoordNumbers; i++) {
                g2.drawLine(Y_AXIS_X_COORD - SECOND_LENGTH,
                        Y_AXIS_SECOND_Y_COORD - (i * yLength),
                        Y_AXIS_X_COORD + SECOND_LENGTH,
                        Y_AXIS_SECOND_Y_COORD - (i * yLength));
                g2.drawString(i * 10 + "%",
                        Y_AXIS_X_COORD - AXIS_STRING_DISTANCE * 2,
                        Y_AXIS_SECOND_Y_COORD - (i * yLength));
            }

            // Draw Points
            int OX = X_AXIS_FIRST_X_COORD - (ORIGIN_COORDINATE_LENGTH / 2);
            int OY = Y_AXIS_SECOND_Y_COORD - (ORIGIN_COORDINATE_LENGTH / 2);
            for (Geospatial.StudentStats stat : Stats) {
                g2.fillOval(OX + (int) (stat.avgGrade * 50.0f), OY - (int) (stat.attendancePercentage * 5.0f),
                        ORIGIN_COORDINATE_LENGTH, ORIGIN_COORDINATE_LENGTH);
                g2.drawString(stat.studentName, OX + (int) (stat.avgGrade * 50.0f),
                        OY - (int) (stat.attendancePercentage * 5.0f) - ORIGIN_COORDINATE_LENGTH);
            }
        }
    }

}