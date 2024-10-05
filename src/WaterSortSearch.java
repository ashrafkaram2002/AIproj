import java.util.Queue; // Import for using Queue
import java.util.LinkedList; // Import for using LinkedList
import java.util.function.Function; // Import for using Function

// Import the QueuingFunctions class



public class WaterSortSearch extends GenericSearch {

    private String initialState;
    private int nodesExpanded = 0;

    // Constructor to set the initial state
    public WaterSortSearch(String initialState) {
        this.initialState = initialState;
    }

    // Define the goal test - all bottles must have uniform colors or be empty
    @Override
    public boolean goalTest(String state) {
        // Split the state into individual bottle descriptions
        String[] bottles = state.split(";");

        // Iterate through each bottle
        for (String bottle : bottles) {
            char[] layers = bottle.toCharArray();
            char firstColor = layers[0];

            // Check if all layers in the bottle are either the same color or empty
            for (char layer : layers) {
                if (layer != firstColor && layer != 'e') {
                    return false;  // Not all layers are the same or empty
                }
            }
        }

        return true;  // All bottles are uniform
    }

    // This method defines the initial state of the puzzle
    @Override
    public String getInitialState() {
        return this.initialState;
    }

    private boolean canPour(String fromBottle, String toBottle) {
        // Check if fromBottle has any liquid to pour
        if (fromBottle.indexOf('e') == 0) {
            return false;  // fromBottle is empty
        }

        // Check if toBottle has space and is either empty or has the same color as fromBottle's top layer
        char topFromColor = fromBottle.charAt(0);  // Top layer of fromBottle
        char topToColor = toBottle.charAt(0);  // Top layer of toBottle
        
        if (topToColor == 'e' || topFromColor == topToColor) {
            return true;  // Valid pour
        }

        return false;  // Invalid pour
    }

    // Perform the pour action from bottle i to bottle j
    private String[] pour(String[] bottles, int fromIndex, int toIndex) {
        String fromBottle = bottles[fromIndex];
        String toBottle = bottles[toIndex];

        // Find the number of layers to pour
        char topFromColor = fromBottle.charAt(0);
        int emptySpacesInTo = countEmptySpaces(toBottle);
        int sameColorLayersInFrom = countSameColorLayers(fromBottle, topFromColor);

        // Determine how many layers to pour (min of available space and available same-color layers)
        int layersToPour = Math.min(emptySpacesInTo, sameColorLayersInFrom);

        // Perform the pour: remove from fromBottle and add to toBottle
        fromBottle = removeLayers(fromBottle, layersToPour);
        toBottle = addLayers(toBottle, topFromColor, layersToPour);

        // Update the bottles array with the new bottle states
        bottles[fromIndex] = fromBottle;
        bottles[toIndex] = toBottle;

        return bottles;
    }

    // Helper function to count empty spaces in a bottle
    private int countEmptySpaces(String bottle) {
        int count = 0;
        for (char layer : bottle.toCharArray()) {
            if (layer == 'e') {
                count++;
            }
        }
        return count;
    }

    // Helper function to count how many consecutive same-color layers are at the top of a bottle
    private int countSameColorLayers(String bottle, char color) {
        int count = 0;
        for (char layer : bottle.toCharArray()) {
            if (layer == color) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    // Helper function to remove a specified number of layers from the top of a bottle
    private String removeLayers(String bottle, int layersToRemove) {
        for (int i = 0; i < layersToRemove; i++) {
            bottle = bottle.substring(1) + "e";  // Remove top layer and add empty space at the bottom
        }
        return bottle;
    }

    // Helper function to add a specified number of layers of a color to a bottle
    private String addLayers(String bottle, char color, int layersToAdd) {
        for (int i = 0; i < layersToAdd; i++) {
            bottle = color + bottle.substring(0, bottle.length() - 1);  // Add color to top and remove empty space at the bottom
        }
        return bottle;
    }

    // Expand function to generate all possible next states (i.e., pour operations)
    @Override
    public LinkedList<Node> expand(Node node) {
        LinkedList<Node> successors = new LinkedList<>();
        String currentState = node.getState();

        // Get a list of bottles from the current state
        String[] bottles = currentState.split(";");

        // Try pouring from every bottle to every other bottle
        for (int i = 0; i < bottles.length; i++) {
            for (int j = 0; j < bottles.length; j++) {
                if (i != j) { // Don't pour into the same bottle
                    // Check if it's valid to pour from bottle i to bottle j
                    if (canPour(bottles[i], bottles[j])) {
                        String[] newBottles = bottles.clone();
                        // Perform the pour action and create the new state
                        newBottles = pour(newBottles, i, j);

                        // Join the new state into a single string
                        String newState = String.join(";", newBottles);

                        // Create a new node for this state
                        Node newNode = new Node(newState, node, "pour_" + i + "_" + j, node.getPathCost() + 1, node.getDepth() + 1);

                        // Add this node to the list of successors
                        successors.add(newNode);
                    }
                }
            }
        }

        nodesExpanded++;  // Keep track of how many nodes were expanded
        return successors;
    }

    // Helper methods (canPour, pour, countEmptySpaces, countSameColorLayers, etc.) as explained before

    // Static solve method
    public static String solve(String initialState, String strategy, boolean visualize) {
        // Create an instance of WaterSortSearch
        WaterSortSearch problem = new WaterSortSearch(initialState);

        // Select the appropriate queuing function based on the strategy input
        Function<Queue<Node>, Node> queuingFunction = null;
        switch (strategy) {
            case "BF":
                queuingFunction = QueuingFunctions::bfsQingFunction;
                break;
            case "DF":
                queuingFunction = QueuingFunctions::dfsQingFunction;
                break;
            case "UC":
                queuingFunction = QueuingFunctions::ucsQingFunction;
                break;
            case "GR1":
                queuingFunction = QueuingFunctions::greedyQingFunction1;
                break;
            case "AS1":
                queuingFunction = QueuingFunctions::aStarQingFunction;
                break;
            case "AS2":
                queuingFunction = QueuingFunctions::aStarQingFunction;  // Another A* heuristic, if implemented
                break;
            default:
                return "Invalid strategy";  // Error handling for unrecognized strategies
        }

        // Call the general search algorithm
        Node result = problem.generalSearch(queuingFunction);

        // If a solution was found
        if (result != null) {
            String plan = result.getSolutionPath();  // Sequence of actions (pour operations)
            int pathCost = result.getPathCost();     // Total path cost (total number of layers poured)
            int nodesExpanded = problem.nodesExpanded;  // Number of nodes expanded during the search

            // If visualize is true, print the state at each step (optional)
            if (visualize) {
                visualizeSolution(result);
            }

            // Return the result in the format: plan;pathCost;nodesExpanded
            return plan + ";" + pathCost + ";" + nodesExpanded;
        } else {
            return "NOSOLUTION";  // No solution found
        }
    }

    // Visualize function to print the steps (optional)
    private static void visualizeSolution(Node node) {
        LinkedList<Node> path = new LinkedList<>();
        while (node != null) {
            path.addFirst(node);  // Collect nodes from the goal to the root
            node = node.getParent();
        }

        // Print each step (state) of the solution
        for (Node n : path) {
            System.out.println("State: " + n.getState() + " | Action: " + n.getAction());
        }
    }
}
