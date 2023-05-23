import java.util.*;

/**
 * The EnronAnalysis class performs analysis on Enron email data.
 * It finds connectors in the email network and allows querying details of individuals.
 */
public class EnronAnalysis {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Insufficient arguments. Usage: java EnronAnalysis <input_path> <output_path>). <output_path> is optional.");
            return;
        }

        // Getting the paths to the folder and file to write to, if given
        String path = args[0];
        String outPath = args[1];

        if (path.isEmpty()) {
            System.out.println("Path not found for the input folder.");
            return;
        }

        Reader reader = new Reader(path);
        if (reader.getPeople().isEmpty() || reader.getEdges().isEmpty()) {
            System.out.println("No data found in the input folder.");
            return;
        }

        Connectors connectors = new Connectors(reader.getPeople(), reader.getEdges(), outPath);
        // Call to find and print connectors
        connectors.findConnectors();
        connectors.printConnectors();

        // Getting user input
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("");
            System.out.print("Email address of the individual (or EXIT to quit): ");
            String email = scanner.nextLine().trim();
            // If "EXIT", then quit the loop
            if (email.equalsIgnoreCase("EXIT")) {
                break;
            }
            // Call to print the details of the requested email
            connectors.printPersonDetails(email);
        }
        scanner.close();
    }
}
