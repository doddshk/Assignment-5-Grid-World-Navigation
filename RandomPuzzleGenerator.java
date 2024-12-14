import java.util.Random;

public class RandomPuzzleGenerator {
  private final int rows;
  private final int cols;
  private final double blockDensity;
  private final Random rand;

  public RandomPuzzleGenerator(int rows, int cols, double blockDensity, Long seed) {
    if (rows <= 0 || cols <= 0) {
      throw new IllegalArgumentException("Rows and cols must be positive");
    }

    if (blockDensity < 0.0 || blockDensity > 1.0) {
      throw new IllegalArgumentException("blockDensity must be between 0 and 1");
    }

    this.rows = rows;
    this.cols = cols;
    this.blockDensity = blockDensity;
    this.rand = (seed == null) ? new Random() : new Random(seed);
  }

  public void generateAndPrint() {
    char[][] grid = new char[rows][cols];

    // Fill the grid
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        grid[r][c] = (rand.nextDouble() < blockDensity) ? '#' : '.';
      }
    }

    // Make sure start and end are open if requested
    grid[0][0] = '.';
    grid[rows - 1][cols - 1] = '.';

    // Print the puzzle
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        System.out.print(grid[r][c]);
      }
      System.out.println();
    }
  }

  public static void main(String[] args) {
    if (args.length < 3) {
      System.err.println("Usage: java RandomPuzzleGenerator <rows> <cols> <blockDensity> [seed]");
      System.exit(1);
    }

    int rows = Integer.parseInt(args[0]);
    int cols = Integer.parseInt(args[1]);
    double blockDensity = Double.parseDouble(args[2]);
    Long seed = null;
    if (args.length > 3) {
      seed = Long.parseLong(args[3]);
    }

    RandomPuzzleGenerator gen = new RandomPuzzleGenerator(rows, cols, blockDensity, seed);
    gen.generateAndPrint();
  }
}
