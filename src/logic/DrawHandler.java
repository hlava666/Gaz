package logic;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import windows.Controller;

public class DrawHandler {
    private Controller controller;
    private final double CANVAS_SIZE = 420;

    public DrawHandler(Controller controller) {
        this.controller = controller;
    }

    public void popUpWindow() {
        double POP_UP_WINDOW_SIZE = 320;
        double POP_UP_WINDOW_START_POINT = 50;
        controller.getMainCanvas().getGraphicsContext2D().clearRect(POP_UP_WINDOW_START_POINT, POP_UP_WINDOW_START_POINT, POP_UP_WINDOW_SIZE, POP_UP_WINDOW_SIZE);
        controller.getMainCanvas().getGraphicsContext2D().setFill(Color.RED);
        controller.getMainCanvas().getGraphicsContext2D().setStroke(Color.RED);
        controller.getMainCanvas().getGraphicsContext2D().setFont(new Font(14));
        controller.getMainCanvas().getGraphicsContext2D().strokePolygon(new double[]{POP_UP_WINDOW_START_POINT, POP_UP_WINDOW_START_POINT,
                POP_UP_WINDOW_START_POINT + POP_UP_WINDOW_SIZE, POP_UP_WINDOW_START_POINT + POP_UP_WINDOW_SIZE},
                new double[]{POP_UP_WINDOW_START_POINT, POP_UP_WINDOW_START_POINT + POP_UP_WINDOW_SIZE, POP_UP_WINDOW_START_POINT + POP_UP_WINDOW_SIZE,
                        POP_UP_WINDOW_START_POINT}, 4);
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
        for (int i = 0; i <= Controller.BOARD_SIZE; i++) {
            controller.getMainCanvas().getGraphicsContext2D().strokeLine(0, Controller.STEP_SIZE * i, CANVAS_SIZE, Controller.STEP_SIZE * i);
            controller.getMainCanvas().getGraphicsContext2D().strokeLine(Controller.STEP_SIZE * i, 0, Controller.STEP_SIZE * i, CANVAS_SIZE);
        }
    }

    public void drawFittingsOnSide() {
        controller.getSideCanvas().getGraphicsContext2D().clearRect(0, 0, controller.getSideCanvas().getWidth(), controller.getSideCanvas().getHeight());
        for (int i = 0; i < controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getFittings().size(); i++) {
            controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getFittings().get(i).draw(controller.getSideCanvas(),
                    (i * Controller.STEP_SIZE / CANVAS_SIZE) * Controller.STEP_SIZE, (i * Controller.STEP_SIZE) % CANVAS_SIZE);
        }
    }

    public void drawStartAndEndOnMainBoard(){
        controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getBeginning().draw(controller.getMainCanvas(),
                controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getBeginning().getX(),
                controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getBeginning().getY());
        for (End e : controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getEnd()) {
            e.draw(controller.getMainCanvas(), e.getX(), e.getY());
        }
    }

}

