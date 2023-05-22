import java.io.*;
import java.util.*;

public class Connectors {
    private Set<String> people;
    private Set<String[]> edges;
    private List<String> connectors;
    private String outputFile;
    private Map<String, Set<String>> graph;

    public Connectors(Set<String> people, Set<String[]> edges, String outputFile) {
        this.people = people;
        this.edges = edges;
        this.connectors = new ArrayList<>();
        this.outputFile = outputFile;
        this.graph = new HashMap<>();
        buildGraph();
    }

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

    public void findConnectors() {
        for (String person : people) {
            Set<String> visited = new HashSet<>();
            Map<String, Integer> dfsNum = new HashMap<>();
            Map<String, Integer> back = new HashMap<>();

            dfs(person, person, visited, dfsNum, back);
        }
    }

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
                dfs(neighbor, vertex, visited, dfsNum, back);

                if (dfsNum.get(vertex) <= back.get(neighbor)) {
                    isConnector = true;
                }

                back.put(vertex, Math.min(back.get(vertex), back.get(neighbor)));
            } else {
                back.put(vertex, Math.min(back.get(vertex), dfsNum.get(neighbor)));
            }
        }

        if ((parent.equals(vertex) && children > 1) || (!parent.equals(vertex) && isConnector)) {
            connectors.add(vertex);
        }
    }

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

    private int getSentCount(String email) {
        int count = 0;
        for (String[] edge : edges) {
            if (edge[0].equals(email)) {
                count++;
            }
        }
        return count;
    }

    private int getReceivedCount(String email) {
        int count = 0;
        for (String[] edge : edges) {
            if (edge[1].equals(email)) {
                count++;
            }
        }
        return count;
    }

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
