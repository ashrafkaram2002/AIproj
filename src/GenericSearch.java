import java.util.*;
import java.util.function.Function;

public abstract class GenericSearch {

    // Abstract methods that must be implemented by specific search problems
    public abstract boolean goalTest(String state);
    public abstract LinkedList<Node> expand(Node node);
    public abstract String getInitialState();

    // GENERAL-SEARCH function that takes the queuing function as an argument
    public Node generalSearch(Function<Queue<Node>, Node> qingFunction) {
        // Initialize the search queue with the root node (initial state)
        Queue<Node> nodes = new LinkedList<>();
        nodes.add(new Node(getInitialState(), null, null, 0, 0));

        // Start the search loop
        while (!nodes.isEmpty()) {
            // Remove the front node using the queuing function
            Node node = qingFunction.apply(nodes);

            // Check if the current node's state is the goal state
            if (goalTest(node.getState())) {
                return node; // Return the solution node if the goal state is reached
            }

            // Expand the node and add the resulting nodes to the queue
            nodes.addAll(expand(node));
        }

        // Return null if no solution was found
        return null;
    }
}
