import java.io.*;

class GridWorld {
  final private int[][] originalGrid;
  final private int rowCount;
  final private int colCount;

  final private GridIndex endingPoint;

  // Make another grid with move information!
  final private Move[][] previousMoveData;


  final public static AnsiColor OpenColor = new AnsiColor("cyan");
  final public static AnsiColor BlockedColor = new AnsiColor("red");
  final public static AnsiColor NavigatedColor = new AnsiColor("yellow");

  // Test to see if an index is inside the grid (although it could still be blocked)
  public boolean isInsideWorld(GridIndex index) {
    if (index == null) return false;

    return (
      index.row >= 0 &&
        index.col >= 0 &&
        index.row < rowCount && 
        index.col < colCount
    );
  }

  // Attempt to move an index, but only return the next index if it is inside the grid
  public GridIndex tryMove(GridIndex index, Move move) {
    GridIndex newIndex = index.doMove(move);
    return isInsideWorld(newIndex)? newIndex: null;
  }

  // Attempt to undo an index move (move in the opposite direction), but return null unless the result is inside the grid
  public GridIndex tryUndoMove(GridIndex index, Move move) {
    GridIndex newIndex = index.undoMove(move);
    return isInsideWorld(newIndex)? newIndex: null;
  }

  // Check if
  public boolean isStartingPoint(GridIndex index) {
    return index.row == 0 && index.col == 0;
  }

  // Get the ending point for the puzzle
  public boolean isEndingPoint(GridIndex index) {
    return (index.row == (rowCount - 1)) && (index.col == (colCount - 1));
  }

  public int getRowCount() {
    return rowCount;
  }

  public int getColCount() {
    return colCount;
  }

  public GridIndex createIndex(int row, int col) {
    return new GridIndex(row, col);
  }

  public GridIndex getStartingPoint() {
    return new GridIndex(0, 0);
  }

  public GridIndex getEndingPoint() {
    return new GridIndex(rowCount - 1, colCount - 1);
  }

  // Check if a grid location is blocked
  public boolean isBlocked(GridIndex index) {
    return originalGrid[index.row][index.col] == 1;
  }

  /**
   * New constructor that takes lines representing the maze.
   * '.' = open (1)
   * '#' = blocked (0)
   */
  public GridWorld(SinglyLinkedList<String> lines) {
    // Determine dimensions
    rowCount = lines.size();
    if (rowCount == 0) {
      throw new IllegalArgumentException("No input lines provided");
    }

    // Assume all lines are of equal length
    String firstLine = lines.first();
    if (firstLine == null) {
      throw new IllegalArgumentException("First line is empty");
    }
    colCount = firstLine.length();

    // Initialize the grid
    originalGrid = new int[rowCount][colCount];
    int rowIndex = 0;
    while (!lines.isEmpty()) {
      String line = lines.removeFirst();
      if (line.length() != colCount) {
        throw new IllegalArgumentException("All lines must have the same length");
      }

      for(int colIndex = 0; colIndex < colCount; colIndex++) {
        char c = line.charAt(colIndex);
        originalGrid[rowIndex][colIndex] = (c == '.')? 0 : 1;
      }
      rowIndex++;
    }

    endingPoint = new GridIndex(rowCount - 1, colCount - 1);
    previousMoveData = new Move[rowCount][colCount];
  }

  public void display() {
    for(int row = 0; row < rowCount; row++) {
      for(int col = 0; col < colCount; col++) {
        AnsiColor color = (originalGrid[row][col] == 1)? BlockedColor: OpenColor;
        color.printBlock();
      }
      System.out.print("\n");
    }
  }

  public boolean validateSolution(SinglyLinkedList<Move> moves, boolean print) {
    GridIndex index = new GridIndex(0, 0);

    final int[][] validated = new int[rowCount][colCount];
    validated[0][0] = 1;

    int numMoves = moves.size();

    for(int i = 0; i < numMoves; i++) {
      Move move = moves.removeFirst();
      index = tryMove(index, move);

      if (index == null) {
        System.out.println("Error on move " + i + ", " + move + ": outside grid");
        if (print) printWalk(validated);
        return false;
      }

      if (originalGrid[index.row][index.col] == 1) {
        System.out.println("Error on move " + i + ", " + move + ": walked into blocked space");
        if (print) printWalk(validated);
        return false;
      }

      validated[index.row][index.col] = 1;
    }

    if (isEndingPoint(index)) {
      if (print) printWalk(validated);
      return true;
    }

    System.out.println("Failed to finish the puzzle");
    if (print) printWalk(validated);
    return false;
  }

  private void printWalk(int[][] validated) {
    for (int row = 0; row < rowCount; row++) {
      for (int col = 0; col < colCount; col++) {
        boolean inPath = validated[row][col] == 1;

        AnsiColor color = inPath? NavigatedColor: (
          (originalGrid[row][col] == 0) ? OpenColor : BlockedColor
        );

        color.printBlock();
      }
      System.out.println();
    }
  }

  public static void main(String[] args) throws IOException {
    // Read lines from standard input into a SinglyLinkedList<String>
    SinglyLinkedList<String> lines = new SinglyLinkedList<>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    String line;
    while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
      lines.addLast(line);
    }

    GridWorld gridWorld = new GridWorld(lines);

    System.out.println("=== Displaying Puzzle ===");
    gridWorld.display();

    // Solve mode
    System.out.println("=== Attempting to Solve Puzzle ===");

    PathFinder pathFinder = new PathFinder(gridWorld);
    SinglyLinkedList<Move> solution = pathFinder.findShortestPath();
    
    if (solution == null) {
      System.out.println("No solution found.");
    } else {
      System.out.println("Solution found in " + solution.size() + " moves!");
      System.out.println(solution);
      gridWorld.validateSolution(solution, true);
    }
  }
}
