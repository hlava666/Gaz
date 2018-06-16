package logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class End extends Fitting {
    public End(Direction.Side side, double x, double y) throws FileNotFoundException {
        super(1);
        this.directions[0] = new Direction(side);
        this.image = new Image(new FileInputStream("/kuchenka.jpg"));
        this.x = x;
        this.y = y;
        ImageView imageView = new ImageView(image);
        if (side == Direction.Side.east) {
            imageView.setRotate(90);
        }
        else if (side == Direction.Side.south) {
            imageView.setRotate(180);
        }
        else if (side == Direction.Side.west) {
            imageView.setRotate(270);
        }
        image = imageView.snapshot(null, null);
    }
}

