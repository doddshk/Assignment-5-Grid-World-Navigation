import java.io.*;
import java.util.*;

public class GridWorldTest {
  public static void main(String[] args) {
    System.out.println("Running GridWorldTest");
    System.out.println("=====================");

    int testsPassing = 0;
    int totalTests = 0;

    // Test all puzzles
    if (testPuzzle("all_open.txt", 8)) {
      testsPassing++;
    }
    totalTests++;

    if (testPuzzle("maze_like.txt", 42)) {
      testsPassing++;
    }
    totalTests++;

    if (testPuzzle("navigable_obstacles.txt", 12)) {
      testsPassing++;
    }
    totalTests++;

    if (testPuzzle("no_path.txt", null)) {
      testsPassing++;
    }
    totalTests++;

    if (testPuzzle("dense_navigable.txt", 60)) {
      testsPassing++;
    }
    totalTests++;


    System.out.println("\n========== FINAL VERDICT ==========");
    System.out.println("Tests Passing: " + testsPassing + " / " + totalTests);
  }

  private static boolean testPuzzle(String filename, Integer expectedLength) {
    System.out.println("\nTesting puzzle: " + filename);
    System.out.println("==================" + "=".repeat(filename.length()));
    
    SinglyLinkedList<String> lines = readPuzzleFile(filename);
    if (lines == null) {
      System.out.println("Failed to load puzzle file");
      return false;
    }

    // Create the grid and display it
    GridWorld world = new GridWorld(lines);
    System.out.println("Initial grid:");
    world.display();

    // Find the path
    PathFinder finder = new PathFinder(world);
    SinglyLinkedList<Move> solution = finder.findShortestPath();

    // Check results
    if (expectedLength == null) {
      if (solution == null) {
        System.out.println("✅ PASS: Correctly found no path");
        return true;
      }

      System.out.println("❌ FAIL: Expected no path, but found path of length " + solution.size());
      System.out.println("Found path: " + solution);
      return false;
    }

    if (solution == null) {
      System.out.println("❌ FAIL: Expected path of length " + expectedLength + ", but found no path");
      return false;
    }

    int size = solution.size();

    System.out.println("Solved grid:");
    boolean validPath = world.validateSolution(solution, true);
    if (!validPath) {
      System.out.println("❌ FAIL: Invalid path found");
      return false;
    }

    if (size == expectedLength) {
      System.out.println("✅ PASS: Found correct path length of " + expectedLength);
      System.out.println("Path: " + solution);
      return true;
    }

    System.out.println("❌ FAIL: Expected path length " + expectedLength + 
                     ", but found path of length " + size);
    System.out.println("Found path: " + solution);
    return false;
  }

  private static SinglyLinkedList<String> readPuzzleFile(String filename) {
    SinglyLinkedList<String> lines = new SinglyLinkedList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader("puzzles/" + filename))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (!line.trim().isEmpty()) {
          lines.addLast(line);
        }
      }
    } catch (IOException e) {
      System.err.println("Error reading file " + filename + ": " + e.getMessage());
      return null;
    }
    return lines;
  }
}