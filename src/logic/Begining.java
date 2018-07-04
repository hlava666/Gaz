package logic;

import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Begining extends Fitting{
    Begining(Direction.Side side, double x, double y) throws FileNotFoundException {
        super(1);
        this.directions[0] = new Direction(side);
        this.x = x;
        this.y = y;
        if (side == Direction.Side.south) {
            this.image = new Image(new FileInputStream("Picture\\StartS.png"));
        }
        else if (side == Direction.Side.west) {
            this.image = new Image(new FileInputStream("Picture\\StartW.png"));
        }
        else if (side == Direction.Side.north) {
            this.image = new Image(new FileInputStream("Picture\\StartN.png"));
        }
        else if (side == Direction.Side.east) {
            this.image = new Image(new FileInputStream("Picture\\StartE.png"));
        }
    }
}

