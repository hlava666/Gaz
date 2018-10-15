package fittings;

import javafx.scene.image.Image;
import game.Direction;

public class Tee extends Fitting {
    public Tee() {
        super(3);
        this.directions[0] = new Direction(Direction.Side.west);
        this.directions[1] = new Direction(Direction.Side.north);
        this.directions[2] = new Direction(Direction.Side.east);
        this.image = new Image(Tee.class.getResourceAsStream("Tee.png"));
    }
}
