import java.util.HashSet;
import java.util.Set;

public class PathFinder {
    private final GridWorld gridWorld;

    public PathFinder(GridWorld gridWorld) {
        this.gridWorld = gridWorld;
    }

    // Return a list of moves to get from the starting point of the grid to the ending
    public SinglyLinkedList<Move> findShortestPath() {
        // BFS setup
        GridIndex start = gridWorld.getStartingPoint();
        GridIndex end = gridWorld.getEndingPoint();
        //System.out.println("Start: " + start + " End: " + end);
        int rows = gridWorld.getRowCount();
        int cols = gridWorld.getColCount();

        // Previous moves to reconstruct the path
        Move[][] previousMoveData = new Move[rows][cols];

        // BFS queue and visited set
        LinkedQueue<GridIndex> queue = new LinkedQueue<>();
        queue.enqueue(start);

        //System.out.println("Starting Point: " + start);
        //System.out.println("Queue After Initialization: " + queue);
        while (!queue.isEmpty()) {
            GridIndex current = queue.dequeue();
            if (current.row == end.row && current.col == end.col) {
              //System.out.println("Ending point detected via manual comparison!");
              return reconstructPath(previousMoveData, current);
            }

            // Try all possible moves
            for (Move move : Move.values()) {
                GridIndex next = gridWorld.tryMove(current, move);
                //System.out.println("Trying: " + move + " Next: " + next);

                if (next != null && !gridWorld.isBlocked(next) && previousMoveData[next.row][next.col] == null) {
                    //System.out.println("Recording next: " + next);
                    // Record the move and enqueue the next index
                    previousMoveData[next.row][next.col] = move;
                    queue.enqueue(next);
                }
            }
        }

        // If we exhaust the BFS without finding a path, return null
        return null;
    }

    // Helper method to reconstruct the path from the end to the start
    private SinglyLinkedList<Move> reconstructPath(Move[][] previousMoveData, GridIndex end) {
        //System.out.println("RECONSTRUCTIN=-------------------");
        SinglyLinkedList<Move> path = new SinglyLinkedList<>();
        GridIndex current = end;

        // Trace back the moves from the end to the start
        while (!gridWorld.isStartingPoint(current)) {
            for (Move move : Move.values()) {
                GridIndex prev = gridWorld.tryUndoMove(current, move);
                if (prev != null && previousMoveData[current.row][current.col] == move) {
                    //System.out.println("Adding");
                    path.addFirst(move);
                    current = prev;
                    break;
                }
            }
        }
        return path;
    }
}

