package fittings;

import javafx.scene.image.Image;
import game.Direction;

public class Pipe extends Fitting {
    public Pipe() {
        super(2);
        this.directions[0] = new Direction(Direction.Side.east);
        this.directions[1] = new Direction(Direction.Side.west);
        this.image = new Image(Pipe.class.getResourceAsStream("Pipe.png"));
    }
}
