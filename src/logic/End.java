package logic;

import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class End extends Fitting {
    End(Direction.Side side, double x, double y) throws FileNotFoundException {
        super(1);
        this.directions[0] = new Direction(side);
        this.x = x;
        this.y = y;
        if (side == Direction.Side.south) {
            this.image = new Image(new FileInputStream("Picture\\EndS.png"));
        }
        else if (side == Direction.Side.west) {
            this.image = new Image(new FileInputStream("Picture\\EndW.png"));
        }
        else if (side == Direction.Side.north) {
            this.image = new Image(new FileInputStream("Picture\\EndN.png"));
        }
        else if (side == Direction.Side.east) {
            this.image = new Image(new FileInputStream("Picture\\EndE.png"));
        }
    }
}

