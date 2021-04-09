import objectdraw.WindowController;


public class MazeGenerator extends WindowController {
    Maze maze;

    @Override
    public void begin() {
        maze = new Maze(canvas);

        StdOut.println("\nStart knocking walls down!!!");
        maze.knockWallDown();
    }

    public static void main(String[] args) {
        new MazeGenerator().startController(Maze.CANVAS_WIDTH, Maze.CANVAS_HEIGHT, "Abravi's MazeGenerator");
    }
}
