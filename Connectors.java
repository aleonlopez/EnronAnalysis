import java.io.*;
import java.util.*;

/**
 * The Connectors class finds connectors in a graph represented by a set of people and edges.
 * It also provides methods to print the connectors, print person details, and perform graph operations.
 */
public class Connectors {
    private Set<String> people;
    private Set<String[]> edges;
    private List<String> connectors;
    private String outputFile;
    private Map<String, Set<String>> graph;

    /**
     * Constructs a Connectors object with the specified people, edges, and output file.
     *
     * @param people      the set of people in the graph
     * @param edges       the set of edges in the graph
     * @param outputFile  the path of the output file to write connectors to
     */
    public Connectors(Set<String> people, Set<String[]> edges, String outputFile) {
        this.people = people;
        this.edges = edges;
        this.connectors = new ArrayList<>();
        this.outputFile = outputFile;
        this.graph = new HashMap<>();
        buildGraph();
    }

    /**
     * Builds the graph representation using the set of edges.
     */
    private void buildGraph() {
        for (String[] edge : edges) {
            String person1 = edge[0];
            String person2 = edge[1];

            graph.putIfAbsent(person1, new HashSet<>());
            graph.putIfAbsent(person2, new HashSet<>());

            graph.get(person1).add(person2);
            graph.get(person2).add(person1);
        }
    }

    /**
     * Finds the connectors in the graph.
     */
    public void findConnectors() {
        for (String person : people) {
            Set<String> visited = new HashSet<>();
            Map<String, Integer> dfsNum = new HashMap<>();
            Map<String, Integer> back = new HashMap<>();

            dfs(person, person, visited, dfsNum, back);
        }
    }

    /**
     * Performs a depth-first search (DFS) on the graph to find connectors.
     *
     * @param vertex   the current vertex being visited
     * @param parent   the parent of the current vertex
     * @param visited  the set of visited vertices
     * @param dfsNum   a map storing the DFS number of each vertex
     * @param back     a map storing the back number of each vertex
     */
    private void dfs(String vertex, String parent, Set<String> visited, Map<String, Integer> dfsNum, Map<String, Integer> back) {
        visited.add(vertex);
        dfsNum.put(vertex, dfsNum.size() + 1);
        back.put(vertex, dfsNum.get(vertex));

        int children = 0;
        boolean isConnector = false;

        for (String neighbor : graph.get(vertex)) {
            if (neighbor.equals(parent)) {
                continue;
            }

            if (!visited.contains(neighbor)) {
                children++;
                dfs(neighbor, vertex, visited, dfsNum, back); // Recursive call for dfs

                if (dfsNum.get(vertex) <= back.get(neighbor)) {
                    isConnector = true;
                }

                back.put(vertex, Math.min(back.get(vertex), back.get(neighbor)));
            } else {
                back.put(vertex, Math.min(back.get(vertex), dfsNum.get(neighbor)));
            }
        }

        if ((parent.equals(vertex) && children > 1) || (!parent.equals(vertex) && isConnector)) {
            connectors.add(vertex); // Adding to the list of connectors
        }
    }

    /**
     * Prints the connectors to the console and writes them to the output file if specified.
     */
    public void printConnectors() {
        Set<String> uniqueConnectors = new HashSet<>(connectors);

        System.out.println("Connectors:");
        for (String connector : uniqueConnectors) {
            System.out.println(connector);
        }

        if (outputFile != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                writer.println("Connectors:");
                for (String connector : uniqueConnectors) {
                    writer.println(connector);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Prints the details of a person, including the number of messages sent, received, and team members.
     *
     * @param email  the email address of the person
     */
    public void printPersonDetails(String email) {
        if (!people.contains(email)) {
            System.out.println("Email address (" + email + ") not found in the dataset.");
            return;
        }

        int sentCount = getSentCount(email);
        int receivedCount = getReceivedCount(email);
        int teamCount = getTeamCount(email);

        System.out.println("* " + email + " has sent messages to " + sentCount + " others");
        System.out.println("* " + email + " has received messages from " + receivedCount + " others");
        System.out.println("* " + email + " is in a team with " + teamCount + " individuals");
    }

    /**
     * Returns the count of messages sent by a person.
     *
     * @param email  the email address of the person
     * @return the count of messages sent
     */
    private int getSentCount(String email) {
        int count = 0;
        for (String[] edge : edges) {
            if (edge[0].equals(email)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the count of messages received by a person.
     *
     * @param email  the email address of the person
     * @return the count of messages received
     */
    private int getReceivedCount(String email) {
        int count = 0;
        for (String[] edge : edges) {
            if (edge[1].equals(email)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the count of team members of a person.
     *
     * @param email  the email address of the person
     * @return the count of team members
     */
    private int getTeamCount(String email) {
        Set<String> team = new HashSet<>();
        for (String[] edge : edges) {
            if (edge[0].equals(email)) {
                team.add(edge[1]);
            } else if (edge[1].equals(email)) {
                team.add(edge[0]);
            }
        }
        return team.size();
    }
}
