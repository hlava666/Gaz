package fittings;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import game.Direction;
import control.Controller;

public abstract class Fitting {
    Direction[] directions;
    Image image;
    double x;
    double y;
    private double imageSize = Controller.STEP_SIZE;

    Fitting(int numberOfOutlets) {
        this.directions = new Direction[numberOfOutlets];
    }


    public void rotate(Canvas canvas) {
        for (Direction k : this.directions) {
            if (k.getSide() == Direction.Side.north)
                k.setSide(Direction.Side.east);
            else if (k.getSide() == Direction.Side.east)
                k.setSide(Direction.Side.south);
            else if (k.getSide() == Direction.Side.south)
                k.setSide(Direction.Side.west);
            else if (k.getSide() == Direction.Side.west)
                k.setSide(Direction.Side.north);
        }
        ImageView imageView = new ImageView(image);
        imageView.setRotate(90);
        image = imageView.snapshot(null, null);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, x, y, imageSize, imageSize);
    }

    public void draw(Canvas canvas, double x, double y) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        this.x = x - x % imageSize;
        this.y = y - y % imageSize;
        gc.drawImage(image, this.x, this.y, imageSize, imageSize);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Direction[] getDirections() {
        return directions;
    }

    public int isConectedFromNorth(){
        for (Direction direction : directions){
            if (direction.getSide() == Direction.Side.south){
                return -1;
            }
        }
        return 0;
    }

    public int isConectedFromSouth(){
        for (Direction direction : directions){
            if (direction.getSide() == Direction.Side.north){
                return -1;
            }
        }
        return 0;
    }

    public int isConectedFromWest(){
        for (Direction direction : directions){
            if (direction.getSide() == Direction.Side.east){
                return -1;
            }
        }
        return 0;
    }

    public int isConectedFromEast(){
        for (Direction direction : directions){
            if (direction.getSide() == Direction.Side.west){
                return -1;
            }
        }
        return 0;
    }

}

