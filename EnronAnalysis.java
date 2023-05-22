import java.util.*;

public class EnronAnalysis {
    public static void main(String[] args) {
        String path = args[0];
        String outPath = args[1];

        if (path.isEmpty()) {
            System.out.println("No path found for first input.");
        }

        Reader reader = new Reader(path);

        Connectors connectors = new Connectors(reader.getPeople(), reader.getEdges(),outPath);
        // call to find and print connectors
        connectors.findConnectors();
        connectors.printConnectors();
        // getting user input
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("");
            System.out.print("Email address of the individual (or EXIT to quit): ");
            String email = scanner.nextLine().trim();
            // if "EXIT" then
            if (email.equalsIgnoreCase("EXIT")) {
                break;
            }
            // call to print the details of the email requested
            connectors.printPersonDetails(email);
        }
        scanner.close();
    }
}
