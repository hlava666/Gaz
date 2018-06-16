package logic;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private List<Fitting> fittings;
    private int time;
    private Begining begining;
    private End[] end;

    public Level(int time, Begining begining, End[] end) {
        this.fittings = new ArrayList<>();
        this.time = time;
        this.begining = begining;
        this.end = end;
    }

    public List<Fitting> getFittings() {
        return fittings;
    }

    public int getTimer() {
        return time;
    }

    public Begining getBegining() {
        return begining;
    }

    public End[] getEnd() {
        return end;
    }
}
