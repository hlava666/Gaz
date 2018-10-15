package fittings;

import javafx.scene.image.Image;
import game.Direction;

public class Beginning extends Fitting{
    public Beginning(Direction.Side side, double x, double y)  {
        super(1);
        this.directions[0] = new Direction(side);
        this.x = x;
        this.y = y;
        if (side == Direction.Side.south) {
            this.image = new Image(Beginning.class.getResourceAsStream("StartS.png"));
        }
        else if (side == Direction.Side.west) {
            this.image = new Image(Beginning.class.getResourceAsStream("StartW.png"));
        }
        else if (side == Direction.Side.north) {
            this.image = new Image(Beginning.class.getResourceAsStream("StartN.png"));
        }
        else if (side == Direction.Side.east) {
            this.image = new Image(Beginning.class.getResourceAsStream("StartE.png"));
        }
    }
}

