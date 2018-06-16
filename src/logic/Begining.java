package logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Begining extends Fitting{
    public Begining(Direction.Side side, double x, double y) throws FileNotFoundException {
        super(1);
        this.directions[0] = new Direction(side);
        this.image = new Image(new FileInputStream("/start.jpg"));
        this.x = x;
        this.y = y;
        ImageView imageView = new ImageView(image);
        if (side == Direction.Side.south) {
            imageView.setRotate(90);
        }
        else if (side == Direction.Side.west) {
            imageView.setRotate(180);
        }
        else if (side == Direction.Side.north) {
            imageView.setRotate(270);
        }
        image = imageView.snapshot(null, null);
    }
    public Direction.Side getSide(){
        return this.directions[0].getSide();
    }
}

