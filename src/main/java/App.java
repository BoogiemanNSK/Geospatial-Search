import com.datastax.driver.core.*;

import java.util.Scanner;

public class App {

    private static final String REMOTE_HOST = "antandtim.tech";

    public static void main(String[] args) {
        Cluster cluster = Cluster.builder().addContactPoint(REMOTE_HOST).build();
        Session session = cluster.connect();
        Scanner in = new Scanner(System.in);

        Geospatial search = new Geospatial(session);

        System.out.println("Specify name of student to apply geospatial search:");
        String name = in.nextLine();
        System.out.println();

        search.SearchClosest(name);

        session.close();
        cluster.close();
    }

}