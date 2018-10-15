package handlers;

import fittings.Beginning;
import fittings.End;
import fittings.Fitting;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import control.Controller;

public class MouseHandler {
    private MouseEvent event;
    private Controller controller;

    public MouseHandler(MouseEvent event, Controller controller) {
        this.event = event;
        this.controller = controller;
    }

    public void startGame() {
        controller.nextLevel();
        controller.setGameOn(true);
    }

    public void rotateFitting() {
        int x = (int) ((event.getX() - event.getX() % Controller.STEP_SIZE) / Controller.STEP_SIZE);
        int y = (int) ((event.getY() - event.getY() % Controller.STEP_SIZE) / Controller.STEP_SIZE);
        if (controller.getFittingBoard()[x][y] != null && !(controller.getFittingBoard()[x][y] instanceof Beginning)
                && !(controller.getFittingBoard()[x][y] instanceof End)) {
            controller.getFittingBoard()[x][y].rotate(controller.getMainCanvas());
            controller.getFittingBoard()[x][y].draw(controller.getMainCanvas(), controller.getFittingBoard()[x][y].getX(), controller.getFittingBoard()[x][y].getY());
            checkForWin();
        }
    }

    public void chooseFittingFromSide() {
        controller.getDrawHandler().drawLines();
        int column = whichColumn();
        if (column == 1 || column == 2) {
            chooseFittingByColumn(column - 1);
        }
        controller.setMovingFittingFromMainBoard(false);
    }

    public void chooseFittingFromMainBoard(){
        Fitting fitting = controller.getFittingBoard()[(int) (event.getX() / Controller.STEP_SIZE)][(int) (event.getY() / Controller.STEP_SIZE)];
        if (fitting != null && !(fitting instanceof Beginning) && !(fitting instanceof End)) {
            controller.setCatchedFitting(fitting);
            controller.setMovingFittingFromMainBoard(true);
            int x = (int) (event.getX() / Controller.STEP_SIZE);
            int y = (int) (event.getY() / Controller.STEP_SIZE);
            controller.getMainCanvas().getGraphicsContext2D().setStroke(Color.BLACK);
            controller.getMainCanvas().getGraphicsContext2D().strokePolygon(new double[]{Controller.STEP_SIZE * x, Controller.STEP_SIZE * x,
                            Controller.STEP_SIZE + Controller.STEP_SIZE * x, Controller.STEP_SIZE + Controller.STEP_SIZE * x},
                    new double[]{Controller.STEP_SIZE * y, Controller.STEP_SIZE + Controller.STEP_SIZE * y, Controller.STEP_SIZE +
                            Controller.STEP_SIZE * y, Controller.STEP_SIZE * y}, 4);
        }
    }

    public void placeFittingFromSide() {
        if (controller.getFittingBoard()[(int) (event.getX() / Controller.STEP_SIZE)][(int) (event.getY() / Controller.STEP_SIZE)] == null) {
            controller.getCatchedFitting().draw(controller.getMainCanvas(), event.getX(), event.getY());
            controller.setMovingFittingsElement((int) (controller.getCatchedFitting().getX() / Controller.STEP_SIZE),
                    (int) (controller.getCatchedFitting().getY() / Controller.STEP_SIZE), controller.getCatchedFitting());
            controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getFittings().remove(controller.getCatchedFitting());
            controller.getSideCanvas().getGraphicsContext2D().clearRect(0, 0, controller.getSideCanvas().getWidth(), controller.getSideCanvas().getHeight());
            controller.getDrawHandler().drawFittingsOnSide();
            controller.setCatchedFitting(null);
            checkForWin();
        }
    }

    public void placeFittingFromMainBoard(){
        if (controller.getFittingBoard()[(int) (event.getX() / Controller.STEP_SIZE)][(int) (event.getY() / Controller.STEP_SIZE)] == null) {
            controller.setMovingFittingsElement((int) (controller.getCatchedFitting().getX() / Controller.STEP_SIZE),
                    (int) (controller.getCatchedFitting().getY() / Controller.STEP_SIZE), null);
            controller.getMainCanvas().getGraphicsContext2D().clearRect(controller.getCatchedFitting().getX(),
                    controller.getCatchedFitting().getY(), Controller.STEP_SIZE - 1, Controller.STEP_SIZE - 1);
            controller.getCatchedFitting().draw(controller.getMainCanvas(), event.getX(), event.getY());
            controller.setMovingFittingsElement((int) (controller.getCatchedFitting().getX() / Controller.STEP_SIZE),
                    (int) (controller.getCatchedFitting().getY() / Controller.STEP_SIZE), controller.getCatchedFitting());
            controller.setCatchedFitting(null);
            controller.getDrawHandler().drawLines();
            checkForWin();
        }
    }

    private void checkForWin(){
        if (controller.isHermetic()) {
            controller.setGameOn(false);
            controller.getDrawHandler().showMenu();
        }
    }

    private int whichColumn(){
        if (event.getX() < Controller.STEP_SIZE &&  (int) (event.getY() / Controller.STEP_SIZE) <=
                controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getFittings().size() - 1) {
            return 1;
        } else if (event.getX() > Controller.STEP_SIZE && (int) (event.getY() / Controller.STEP_SIZE) <=
                controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getFittings().size() - 1 - Controller.BOARD_SIZE) {
            return 2;
        }
        return 0;
    }

    private void chooseFittingByColumn(int parameter) {
        controller.setCatchedFitting(controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getFittings().get(((int) (event.getY() /
                Controller.STEP_SIZE)) + Controller.BOARD_SIZE * parameter));
        controller.getDrawHandler().drawFittingsOnSide();
        controller.getSideCanvas().getGraphicsContext2D().strokePolygon(new double[]{Controller.STEP_SIZE * parameter,
                Controller.STEP_SIZE * parameter, Controller.STEP_SIZE * (parameter + 1), Controller.STEP_SIZE * (parameter + 1)},
                new double[]{Controller.STEP_SIZE * ((int) (event.getY() / Controller.STEP_SIZE)),
                Controller.STEP_SIZE + Controller.STEP_SIZE * ((int) (event.getY() / Controller.STEP_SIZE)),
                Controller.STEP_SIZE + Controller.STEP_SIZE * ((int) (event.getY() / Controller.STEP_SIZE)),
                Controller.STEP_SIZE * ((int) (event.getY() / Controller.STEP_SIZE))}, 4);
    }
}
