package logic;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Tee extends Fitting {
    public Tee() throws FileNotFoundException {
        super(3);
        this.directions[0] = new Direction(Direction.Side.north);
        this.directions[1] = new Direction(Direction.Side.east);
        this.directions[2] = new Direction(Direction.Side.south);
        this.image = new Image(new FileInputStream("/trojnik.jpg"));
    }
}
