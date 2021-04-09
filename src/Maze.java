import objectdraw.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Abravi E. Tekpa - Spring 2021
 * Maze generation using Arraylist and Sedgewick union-find algorithm
 * Objectdraw library used to draw the maze
 */

public class Maze {
    public static final int CANVAS_WIDTH = 800;
    public static final int CANVAS_HEIGHT = 800;
    private final int N = 10;
    private final int M = 10;
    private final double CELL_Y_OFFSET = CANVAS_HEIGHT / M;

    private int size = N * M;
    private double cellWidth = Math.floor((CANVAS_WIDTH - (M + 5.0)) / M);
    private double cellHeight = 50;
    private ArrayList result = new ArrayList();
    private ArrayList<Integer> maze;
    private DrawingCanvas canvas;
    private FramedRect gridCell;
    private Text text;


    public Maze(DrawingCanvas aCanvas) {
        canvas = aCanvas;

        maze = new ArrayList<>();
        System.out.println("Maze size: " + size);
        System.out.println("\tMaze at start");
        drawGridCells();
        printMazeGrid();
    }

    /**
     * Draws a N by M grid with each cells containing its number as text
     */
    public void drawGridCells() {
        int cell = 0;
        int j = 0;
        for (int i = 0; i < size; i++) {
            //get x coordinate:
            // to get first cell's position: start from canvas middle using half of cell's width
            // next cell will start at first cell's position + brick width and padding
            double x = canvas.getWidth()/2 - (M * cellWidth)/2 - (M - 1)/2 + (j * cellWidth) + j;

            //get y coordinate: start from brick y-offset using the height and the spacing between the bricks
            double y = CELL_Y_OFFSET + ((i / N) * cellHeight) + i / N;

            //draw a new brick
            result.add(new FramedRect(x, y, cellWidth, cellHeight, Color.LIGHT_GRAY, canvas));
            gridCell = (FramedRect)result.get(i);
            text = new Text(cell, gridCell.getX() + cellWidth/2, gridCell.getY() + cellHeight/2, canvas);
            text.setColor(Color.BLACK);
            cell++;
            j++;

            //end of a column, go to next line (row)
            if (j == M) j = 0;
        }
    }

    /**
     * Knock walls down to generate the maze after required cells are connected
     * The connection are represented wit a line between the cells
     */
    public void knockWallDown() {
        UF uf = new UF(size);

        // Retrieve all the adjacent cells in the grid
        ArrayList<ArrayList<Integer>> adjacentCell = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < size; i++) {
            if (i < (size - M)) {
                ArrayList<Integer> pair = new ArrayList<>();
                pair.add(i);
                pair.add(maze.get(i + N));
                adjacentCell.add(pair);
            }
            if (i >= N) {
                ArrayList<Integer> pair = new ArrayList<>();
                pair.add(i);
                pair.add(maze.get(i - N));
                adjacentCell.add(pair);
            }
            if ((i + 1) % M != 0) {
                ArrayList<Integer> pair = new ArrayList<>();
                pair.add(i);
                pair.add(maze.get(i + 1));
                adjacentCell.add(pair);
            }
            if (i % M != 0) {
                ArrayList<Integer> pair = new ArrayList<>();
                pair.add(i);
                pair.add(maze.get(i - 1));
                adjacentCell.add(pair);
            }
        }

        // shuffle the selected adjacent cells to have randomized pairs
        Collections.shuffle(adjacentCell);
        System.out.println("\nAdjacent cells after shuffle: " + adjacentCell);

        int i = 0;
        while (uf.find(0) != uf.find(size - 1)) {
            if (i < adjacentCell.size()) {
                ArrayList<Integer> pair = adjacentCell.get(i);

                //get pairs to connect
                int p = pair.get(0);
                int q = pair.get(1);
                System.out.print("\nChecking Pair: ");
                System.out.print(p);
                System.out.print(" " + q);
                i++;

                // Continue: check other pairs if p and q already connected
                if (uf.find(p) == uf.find(q)) continue;

                // Connect the selected pairs
                uf.union(p, q);
                System.out.println("\n" + p + " and " + q + " joined");
                //draw a red line to represent the connection
                new Line(((FramedRect)result.get(p)).getX() + cellWidth/3, ((FramedRect)result.get(p)).getY() + (cellHeight/3) * 2,
                        ((FramedRect) result.get(q)).getX() + cellWidth/3, ((FramedRect) result.get(q)).getY() + (cellHeight/3) * 2, Color.RED, canvas);
            }
        }

        // first cell and last cell of the grid are connected
        if (uf.find(0) == uf.find(size - 1)) {
            text = new Text("Maze generated! Start (" + maze.get(0) + ") and end (" + maze.get(size - 1) + ") connected.",
                    canvas.getWidth()/10, CELL_Y_OFFSET / 2, canvas);
            text.setBold();
            text.setFontSize(20);
        }
    }

    /**
     * Prints the grid in the console.
     */
    public void printMazeGrid() {
        int col = 0;
        System.out.print("\t\t|\t");
        for (int i = 0; i < size; i++) {
            maze.add(i);
            System.out.print(maze.get(i) + " ");
            col++;
            if (col == M) {
                col = 0;
                System.out.print("\t|\n");
                if (i != size - 1) System.out.print("\t\t|\t");
            }
        }
    }

    @Override
    public String toString() {
        return maze.toString();
    }
}
