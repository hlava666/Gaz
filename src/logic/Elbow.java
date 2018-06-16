package logic;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Elbow extends Fitting {
    public Elbow() throws FileNotFoundException {
        super(2);
        this.directions[0] = new Direction(Direction.Side.north);
        this.directions[1] = new Direction(Direction.Side.east);
        this.image = new Image(new FileInputStream("/kolano.png"));
    }
}
