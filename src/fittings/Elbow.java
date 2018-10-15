package fittings;

import javafx.scene.image.Image;
import game.Direction;

public class Elbow extends Fitting {
    public Elbow() {
        super(2);
        this.directions[0] = new Direction(Direction.Side.east);
        this.directions[1] = new Direction(Direction.Side.south);
        this.image = new Image(Elbow.class.getResourceAsStream("Elbow.png"));
    }
}
