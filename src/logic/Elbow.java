package logic;

import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class Elbow extends Fitting {
    Elbow() throws FileNotFoundException {
        super(2);
        this.directions[0] = new Direction(Direction.Side.east);
        this.directions[1] = new Direction(Direction.Side.south);
        this.image = new Image(new FileInputStream("Picture\\Elbow.png"));
    }
}
