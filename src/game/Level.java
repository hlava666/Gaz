package game;

import fittings.Beginning;
import fittings.End;
import fittings.Fitting;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private List<Fitting> fittings;
    private int time;
    private Beginning beginning;
    private End[] end;

    Level(int time, Beginning beginning, End[] end) {
        this.fittings = new ArrayList<>();
        this.time = time;
        this.beginning = beginning;
        this.end = end;
    }

    public List<Fitting> getFittings() {
        return fittings;
    }

    public int getTimer() {
        return time;
    }

    public Beginning getBeginning() {
        return beginning;
    }

    public End[] getEnd() {
        return end;
    }
}
