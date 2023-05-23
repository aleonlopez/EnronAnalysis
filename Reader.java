import java.io.*;
import java.nio.file.*;
import java.util.regex.*;
import java.util.*;

/**
 * The Reader class reads email files from a given folder path, extracts sender and recipient information,
 * and stores the information in sets of people and edges.
 */
public class Reader {
    private Set<String> people;
    private Set<String[]> edges;

    /**
     * Constructs a Reader object with the specified folder path and initializes the people and edges sets.
     *
     * @param path the path of the folder containing email files
     */
    public Reader(String path) {
        this.people = new HashSet<>();
        this.edges = new HashSet<>();
        Path folder = Path.of(path);
        readEmails(folder);
    }

    /**
     * Recursively reads email files from the given folder and extracts sender and recipient information.
     *
     * @param folder the path of the folder to read
     */
    private void readEmails(Path folder) {
        try (DirectoryStream<Path> files = Files.newDirectoryStream(folder)) {
            for (Path file : files) {
                if (Files.isDirectory(file)) {
                    readEmails(file); // Recursively process subfolders
                } else {
                    Map<String, List<String>> emailData = extractEmailData(file);
                    List<String> senders = emailData.get("sender");
                    List<String> recipients = emailData.get("recipients");

                    if (!senders.isEmpty()) {
                        String sender = senders.get(0); // Get the first sender
                        for (String recipient : recipients) {
                            addEdge(sender, recipient);
                        }
                    } else {
                        System.out.println("No sender found in file: " + file);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extracts sender and recipient information from the given email file.
     * Uses Pattern Matcher and regex to find the emails given the line starting points: From, To, Cc, Bcc.
     *
     * @param emailFile the path of the email file to extract data from
     * @return a map containing the sender and recipient information
     */
    private Map<String, List<String>> extractEmailData(Path emailFile) {
        Map<String, List<String>> emailData = new HashMap<>();
        emailData.put("sender", new ArrayList<>());
        emailData.put("recipients", new ArrayList<>());

        try (BufferedReader br = new BufferedReader(new FileReader(emailFile.toString()))) {
            String line;
            boolean senderFound = false;

            Pattern pattern = Pattern.compile("(?i)((From|To|Cc|Bcc):\\s*\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}\\b|\\b[A-Za-z0-9._%+-]+@enron\\.com\\b)");

            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String match = matcher.group();
                    if (match.toLowerCase().startsWith("from:")) {
                        String sender = match.substring(5).trim();
                        emailData.get("sender").add(sender);
                        senderFound = true;
                    } else {
                        String recipient = match.trim();
                        emailData.get("recipients").add(recipient);
                    }
                }
            }

            // If no sender was found, add an empty sender to the emailData map
            if (!senderFound) {
                emailData.get("sender").add("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return emailData;
    }

    /**
     * Adds an edge between two people to the edges set.
     *
     * @param person1 the first person
     * @param person2 the second person
     */
    private void addEdge(String person1, String person2) {
        if (person1 != null && person2 != null && !person1.equals(person2)) {
            String[] edge = new String[]{person1, person2};
            if (!edges.contains(edge)) {
                edges.add(edge);
                people.add(person1);
                people.add(person2);
            }
        }
    }

    /**
     * Returns the set of people.
     *
     * @return the set of people
     */
    public Set<String> getPeople() {
        return people;
    }

    /**
     * Returns the set of edges.
     *
     * @return the set of edges
     */
    public Set<String[]> getEdges() {
        return edges;
    }
}
