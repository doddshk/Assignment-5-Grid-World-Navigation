public class GridIndex {
  final public int row, col;

  public GridIndex(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public GridIndex doMove(Move move) {
    switch(move) {
      case UP: return new GridIndex(row - 1, col);
      case DOWN: return new GridIndex(row + 1, col);
      case LEFT: return new GridIndex(row, col - 1);
      case RIGHT: return new GridIndex(row, col + 1);
    }

    return null;
  }

  public GridIndex undoMove(Move move) {
    return doMove(move.getOpposite());
  }

  public String toString() {
    return "(" + row + ", " + col + ")";
  }
}