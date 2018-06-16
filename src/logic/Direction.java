package logic;

public class Direction {
    public enum Side {
        north, south, east, west;
    }
    private Side side;

    public Direction(Side side) {
        this.side = side;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

}
