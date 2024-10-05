public class Node {
    private String state;     // The current state of the puzzle (e.g., the arrangement of liquids in bottles)
    private Node parent;      // The parent node in the search tree
    private String action;    // The action that led to this state (e.g., "pour_0_3")
    private int pathCost;     // The cost to reach this node from the root node
    private int depth;        // The depth of this node in the search tree
    private int heuristicCost; // Heuristic value for informed search algorithms (optional)

    // Constructor for the Node class
    public Node(String state, Node parent, String action, int pathCost, int depth) {
        this.state = state;
        this.parent = parent;
        this.action = action;
        this.pathCost = pathCost;
        this.depth = depth;
        this.heuristicCost = 0; // Default value, can be updated later for heuristic-based searches
    }

    // Constructor that includes heuristic cost (for algorithms like A*)
    public Node(String state, Node parent, String action, int pathCost, int depth, int heuristicCost) {
        this(state, parent, action, pathCost, depth); // Call the main constructor
        this.heuristicCost = heuristicCost;
    }

    // Getters for the fields
    public String getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public String getAction() {
        return action;
    }

    public int getPathCost() {
        return pathCost;
    }

    public int getDepth() {
        return depth;
    }

    public int getHeuristicCost() {
        return heuristicCost;
    }

    // Setters for the heuristic cost
    public void setHeuristicCost(int heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    // Method to return the total cost (used for algorithms like A*)
    public int getTotalCost() {
        return pathCost + heuristicCost;
    }

    // Helper method to check if the node represents a goal state
    public boolean isGoalState(String goalState) {
        return this.state.equals(goalState);
    }

    // Method to reconstruct the solution path from the root to this node
    public String getSolutionPath() {
        StringBuilder solutionPath = new StringBuilder();
        Node currentNode = this;

        while (currentNode != null && currentNode.getParent() != null) {
            solutionPath.insert(0, currentNode.getAction() + ",");
            currentNode = currentNode.getParent();
        }

        return solutionPath.length() > 0 ? solutionPath.substring(0, solutionPath.length() - 1) : ""; // Remove last comma
    }
}
