public enum Move {
  UP,
  DOWN,
  RIGHT,
  LEFT;

  // Optional: Add additional methods if needed, e.g., for getting the opposite direction.
  public Move getOpposite() {
    switch (this) {
      case UP: return DOWN;
      case DOWN: return UP;
      case RIGHT: return LEFT;
      case LEFT: return RIGHT;
      default: throw new IllegalStateException("Unexpected value: " + this);
    }
  }
}