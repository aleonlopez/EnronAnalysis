# Enron Analysis
This project performs analysis on a collection of Enron email data to find connectors and provide insights into individual email details.

## Project Structure
The project consists of the three following classes:

- Reader: Reads and extracts email data from a given folder path.
- Connectors: Identifies the connectors in the email network and provides methods to print connectors and individual email details.
- EnronAnalysis: The main class that orchestrates the analysis process.

## Complexity Analysis
The complexity of the main operations in the project is as follows:

### Reading Emails:
- Time Complexity: O(n), where n is the total number of email files in the input folder. This is because the program reads each email file once.

### Building the Graph:
- Time Complexity: O(m), where m is the total number of edges in the email network. This is because the program iterates over all the edges and adds them to the graph data structure.

### Finding Connectors:
- Time Complexity: O(p * (v + e)), where p is the total number of people, v is the total number of vertices (people) in the graph, and e is the total number of edges (connections) in the graph. This is because the program performs a Depth-First Search (DFS) for each person in the graph, and the DFS algorithm has a time complexity of O(v + e).

### Printing Connectors:
- Time Complexity: O(c), where c is the number of unique connectors found. This is because the program prints each unique connector once.

### Printing Person Details:
- Time Complexity: O(e), where e is the total number of edges (connections) in the email network. This is because the program iterates over the edges to count the number of sent/received messages and the number of team members for a given email address.

## Space
The space complexity of the project is determined by the data structures used:

- The space complexity of the graph is O(v + e), where v is the total number of vertices (people) and e is the total number of edges (connections) in the graph.
- The space complexity of storing the connectors is O(c), where c is the number of unique connectors found.
- The space complexity of storing the email addresses, edges, and other data structures is proportional to their respective sizes.
