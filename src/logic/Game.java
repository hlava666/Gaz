package logic;

import java.io.FileNotFoundException;

public class Game {
    private Level[] levels;
    private int currentLevel;
    private String message;

    public Game() throws FileNotFoundException {
        this.levels = new Level[3];
        levels[0] = new Level(30, new Beginning(Direction.Side.east, 60, 0), new End[] {new End(Direction.Side.north, 180, 180)});
        levels[0].getFittings().add(new Pipe());
        levels[0].getFittings().add(new Pipe());
        levels[0].getFittings().add(new Pipe());
        levels[0].getFittings().add(new Elbow());
        levels[1] = new Level(100, new Beginning(Direction.Side.west, 240, 0), new End[] {new End(Direction.Side.east, 60, 180)});
        levels[1].getFittings().add(new Pipe());
        levels[1].getFittings().add(new Elbow());
        levels[1].getFittings().add(new Elbow());
        levels[1].getFittings().add(new Elbow());
        levels[1].getFittings().add(new Elbow());
        levels[1].getFittings().add(new Tee());
        levels[1].getFittings().add(new Tee());
        levels[2] = new Level(40, new Beginning(Direction.Side.south, 120, 60), new End[] {new End(Direction.Side.east, 60, 300),
        new End(Direction.Side.west, 300,180), new End(Direction.Side.west, 300,240)});
        levels[2].getFittings().add(new Pipe());
        levels[2].getFittings().add(new Pipe());
        levels[2].getFittings().add(new Pipe());
        levels[2].getFittings().add(new Elbow());
        levels[2].getFittings().add(new Elbow());
        levels[2].getFittings().add(new Tee());
        levels[2].getFittings().add(new Tee());
        this.currentLevel = 0;
    }

    public Level[] getLevels() {
        return levels;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public boolean nextLevel() {
        if (this.currentLevel < this.levels.length - 1) {
            this.message = "Brawo!!! Kolejny poziom";
            this.currentLevel++;
            return true;
        } else {
            this.message = "Wszystkie poziomy ukończone!!!";
            return false;
        }
    }

    public String getMessage() {
        return message;
    }
}
