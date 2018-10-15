package fittings;

import javafx.scene.image.Image;
import game.Direction;

public class End extends Fitting {
    public End(Direction.Side side, double x, double y) {
        super(1);
        this.directions[0] = new Direction(side);
        this.x = x;
        this.y = y;
        if (side == Direction.Side.south) {
            this.image = new Image(End.class.getResourceAsStream("EndS.png"));
        }
        else if (side == Direction.Side.west) {
            this.image = new Image(End.class.getResourceAsStream("EndW.png"));
        }
        else if (side == Direction.Side.north) {
            this.image = new Image(End.class.getResourceAsStream("EndN.png"));
        }
        else if (side == Direction.Side.east) {
            this.image = new Image(End.class.getResourceAsStream("EndE.png"));
        }
    }
}

