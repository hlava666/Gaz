package logic;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import windows.Controller;

public class MouseHandler {
    private MouseEvent event;

    public MouseHandler(MouseEvent event) {
        this.event = event;
    }

    public void startGame(Controller controller) {
        controller.nextLevel();
        controller.setGameOn(true);
    }

    public void rotateFitting(Controller controller) {
        int x = (int) ((event.getX() - event.getX() % controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getEnd()[0].getImageSize())
                / controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getEnd()[0].getImageSize());
        int y = (int) ((event.getY() - event.getY() % controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getEnd()[0].getImageSize())
                / controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getEnd()[0].getImageSize());
        if (controller.getMovingFittings()[x][y] != null && !(controller.getMovingFittings()[x][y] instanceof Begining)
                && !(controller.getMovingFittings()[x][y] instanceof End)) {
            controller.getMovingFittings()[x][y].rotate(controller.getMainCanvas());
            controller.getMovingFittings()[x][y].draw(controller.getMainCanvas(), controller.getMovingFittings()[x][y].getX(), controller.getMovingFittings()[x][y].getY());
            checkForWin(controller);
        }
    }

    public void chooseFittingFromSide(Controller controller) {
        controller.getDrawHandler().drawLines();
        if (event.getX() < 60 && (int) event.getY() / 60 <= controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getFittings().size() - 1) {
            controller.setTemp(controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getFittings().get((int) event.getY() / 60));
            controller.getDrawHandler().drawFittingsOnSide();
            controller.getSideCanvas().getGraphicsContext2D().strokePolygon(new double[]{0, 0, 60, 60}, new double[]{60 * ((int) event.getY() / 60),
                    60 + 60 * ((int) event.getY() / 60), 60 + 60 * ((int) event.getY() / 60), 60 * ((int) event.getY() / 60)}, 4);
        } else if (event.getX() > 60 && (int) event.getY() / 60 <= controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getFittings().size() - 8) {
            controller.setTemp(controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getFittings().get(((int) event.getY() / 60) + 7));
            controller.getDrawHandler().drawFittingsOnSide();
            controller.getSideCanvas().getGraphicsContext2D().strokePolygon(new double[]{60, 60, 120, 120}, new double[]{60 * ((int)
                    event.getY() / 60),
                    60 + 60 * ((int) event.getY() / 60), 60 + 60 * ((int) event.getY() / 60), 60 * ((int) event.getY() / 60)}, 4);
        }
        controller.setMovingFittingFromMainBoard(false);
    }

    public void chooseFittingFromMainBoard(Controller controller){
        Fitting fitting = controller.getMovingFittings()[(int) event.getX() / 60][(int) event.getY() / 60];
        if (fitting != null && !(fitting instanceof Begining) && !(fitting instanceof End)) {
            controller.setTemp(fitting);
            controller.setMovingFittingFromMainBoard(true);
            int x = (int) event.getX() / 60;
            int y = (int) event.getY() / 60;
            controller.getMainCanvas().getGraphicsContext2D().setStroke(Color.BLACK);
            controller.getMainCanvas().getGraphicsContext2D().strokePolygon(new double[]{60 * x, 60 * x, 60 + 60 * x, 60 +60 * x},
                    new double[]{60 * y, 60 + 60 * y, 60 + 60 * y, 60 * y}, 4); // do poprawy!!!!!!!!!!!!!!!
        }
    }

    public void placeFittingFromSide(Controller controller) {
        if (controller.getMovingFittings()[(int) event.getX() / 60][(int) event.getY() / 60] == null) {
            controller.getTemp().draw(controller.getMainCanvas(), event.getX(), event.getY());
            controller.setMovingFittingsElement((int) controller.getTemp().getX() / 60,(int) controller.getTemp().getY() / 60, controller.getTemp());
            controller.getGame().getLevels()[controller.getGame().getCurrentLevel()].getFittings().remove(controller.getTemp());
            controller.getSideCanvas().getGraphicsContext2D().clearRect(0, 0, controller.getSideCanvas().getWidth(), controller.getSideCanvas().getHeight());
            controller.getDrawHandler().drawFittingsOnSide();
            controller.setTemp(null);
            checkForWin(controller);
        }
    }

    public void placeFittingFromMainBoard(Controller controller){
        if (controller.getMovingFittings()[(int) event.getX() / 60][(int) event.getY() / 60] == null) {
            controller.setMovingFittingsElement((int) controller.getTemp().getX() / 60, (int) controller.getTemp().getY() / 60, null);
            controller.getMainCanvas().getGraphicsContext2D().clearRect(controller.getTemp().getX() + 1, controller.getTemp().getY() + 1, 59, 59);
            controller.getTemp().draw(controller.getMainCanvas(), event.getX(), event.getY());
            controller.setMovingFittingsElement((int) controller.getTemp().getX() / 60, (int) controller.getTemp().getY() / 60, controller.getTemp());
            controller.setTemp(null);
            controller.getDrawHandler().drawLines();
            checkForWin(controller);
        }
    }

    private void checkForWin(Controller controller){
        if (controller.isHermetic()) {
            controller.setGameOn(false);
            controller.getDrawHandler().showMenu();
        }
    }
}
