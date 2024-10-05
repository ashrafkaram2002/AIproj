import java.util.*;
import java.util.function.Function;

public class QueuingFunctions {

    // BFS (FIFO)
    public static Node bfsQingFunction(Queue<Node> queue) {
        return queue.poll();  // FIFO - Remove first element
    }

    // DFS (LIFO)
    public static Node dfsQingFunction(Queue<Node> queue) {
        return ((LinkedList<Node>) queue).removeLast();  // LIFO - Remove last element
    }

    // UCS (based on path cost)
    public static Node ucsQingFunction(Queue<Node> queue) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getPathCost));
        pq.addAll(queue);
        return pq.poll();  // Remove node with the lowest path cost
    }

    // Greedy Search (using heuristic1)
    public static Node greedyQingFunction1(Queue<Node> queue) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getHeuristicCost));
        pq.addAll(queue);
        return pq.poll();  // Remove node with the lowest heuristic cost
    }

    // A* Search (based on total cost = pathCost + heuristicCost)
    public static Node aStarQingFunction(Queue<Node> queue) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getTotalCost));
        pq.addAll(queue);
        return pq.poll();  // Remove node with the lowest total cost
    }
}
