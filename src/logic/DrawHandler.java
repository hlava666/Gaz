package logic;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import windows.Controller;

public class DrawHandler {
    private Controller controller;

    public DrawHandler(Controller controller) {
        this.controller = controller;
    }

    public void popUpWindow() {
        controller.getMainCanvas().getGraphicsContext2D().clearRect(50, 50, 320, 320);
        controller.getMainCanvas().getGraphicsContext2D().setFill(Color.RED);
        controller.getMainCanvas().getGraphicsContext2D().setStroke(Color.RED);
        controller.getMainCanvas().getGraphicsContext2D().setFont(new Font(14));
        controller.getMainCanvas().getGraphicsContext2D().strokePolygon(new double[]{50, 50, 370, 370}, new double[]{50, 370, 370, 50}, 4);
    }

    void showMenu() {
        popUpWindow();
        if (controller.getGame().nextLevel()) {
            controller.getMainCanvas().getGraphicsContext2D().fillText("Kliknij aby rozpocząć.", 65, 300);
        } else {
            controller.setGameOver(true);
            controller.getMainCanvas().getGraphicsContext2D().fillText("Stałeś się zasłużonym dla gazownictwa.", 65, 300);
        }
        controller.getMainCanvas().getGraphicsContext2D().setFont(new Font(20));
        controller.getMainCanvas().getGraphicsContext2D().fillText(controller.getGame().getMessage(), 65, 200);
    }

    public void drawLines() {
        controller.getMainCanvas().getGraphicsContext2D().setStroke(Color.LIGHTGRAY);
        for (int i = 0; i <= 7; i++) {
            controller.getMainCanvas().getGraphicsContext2D().strokeLine(0, 60 * i, 420, 60 * i);
            controller.getMainCanvas().getGraphicsContext2D().strokeLine(60 * i, 0, 60 * i, 420);
        }
    }

    public void drawFittingsOnSide() {
        controller.getSideCanvas().getGraphicsContext2D().clearRect(0, 0, controller.getSideCanvas().getWidth(), controller.getSideCanvas().getHeight());
        for (int i = 0; i < controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getFittings().size(); i++) {
            controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getFittings().get(i).draw(controller.getSideCanvas(), (i * 60 / 420) * 60, (i * 60) % 420);
        }
    }

}

