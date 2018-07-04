package logic;

import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class Tee extends Fitting {
    Tee() throws FileNotFoundException {
        super(3);
        this.directions[0] = new Direction(Direction.Side.west);
        this.directions[1] = new Direction(Direction.Side.north);
        this.directions[2] = new Direction(Direction.Side.east);
        this.image = new Image(new FileInputStream("Picture\\Tee.png"));
    }
}
