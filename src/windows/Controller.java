package windows;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.*;

import java.io.FileNotFoundException;

public class Controller {
    private Game game;
    private boolean moveFit = false;
    private boolean gameOn = false;
    private boolean gameOver = true;
    private Fitting[][] movingFittings;
    private Fitting temp = null;

    @FXML
    private Canvas mainCanvas;

    @FXML
    private Canvas sideCanvas;

    @FXML
    private Pane sidePane;

    @FXML
    private ProgressBar bar;

    @FXML
    public void newGame() throws FileNotFoundException {
        game = new Game();
        gameOver = false;
        nextLevel();
        time();
    }

    @FXML
    public void mouseClick(MouseEvent event) {
        if (!gameOn && !gameOver) {
            if (event.getX() > 50 && event.getX() < 320 && event.getY() > 50 && event.getY() < 320){
                gameOn = true;
                nextLevel();
            }
        } else {
            if (event.getClickCount() == 2 && event.getSceneX() > sidePane.getWidth()
                    && event.getSceneX() < sidePane.getWidth() + mainCanvas.getWidth() && gameOn) {
                int x = (int) ((event.getX() - event.getX() % game.getLevels()[game.getCurrentLevel()].getEnd()[0].getImageSize())
                        / game.getLevels()[game.getCurrentLevel()].getEnd()[0].getImageSize());
                int y = (int) ((event.getY() - event.getY() % game.getLevels()[game.getCurrentLevel()].getEnd()[0].getImageSize())
                        / game.getLevels()[game.getCurrentLevel()].getEnd()[0].getImageSize());
                if (movingFittings[x][y] != null && !(movingFittings[x][y] instanceof Begining)
                        && !(movingFittings[x][y] instanceof End)) {
                    movingFittings[x][y].rotate(mainCanvas);
                    movingFittings[x][y].draw(mainCanvas, movingFittings[x][y].getX(), movingFittings[x][y].getY());
                    if (isHermetic()) {
                        gameOn = false;
                        showMenu();
                    }
                }
            } else if (event.getSceneX() > sidePane.getWidth() + mainCanvas.getWidth() && event.getX() < 120 && event.getY() > 0
                    && event.getY() < mainCanvas.getHeight() && gameOn) {
                if (event.getX() < 60 && (int) event.getY() / 60 <= game.getLevels()[game.getCurrentLevel()].getFittings().size() - 1) {
                    temp = game.getLevels()[game.getCurrentLevel()].getFittings().get((int) event.getY() / 60);
                    drawFittingsOnSide();
                    sideCanvas.getGraphicsContext2D().strokePolygon(new double[]{0, 0, 60, 60}, new double[]{60 * ((int) event.getY() / 60),
                            60 + 60 * ((int) event.getY() / 60), 60 + 60 * ((int) event.getY() / 60), 60 * ((int) event.getY() / 60)}, 4);
                } else if (event.getX() > 60 && (int) event.getY() / 60 <= game.getLevels()[game.getCurrentLevel()].getFittings().size() - 8) {
                    temp = game.getLevels()[game.getCurrentLevel()].getFittings().get(((int) event.getY() / 60) + 7);
                    drawFittingsOnSide();
                    sideCanvas.getGraphicsContext2D().strokePolygon(new double[]{60, 60, 120, 120}, new double[]{60 * ((int)
                            event.getY() / 60),
                            60 + 60 * ((int) event.getY() / 60), 60 + 60 * ((int) event.getY() / 60), 60 * ((int) event.getY() / 60)}, 4);
                }
                moveFit = false;
            } else if (temp == null && event.getSceneX() > sidePane.getWidth() && event.getSceneX() < sidePane.getWidth() + mainCanvas.getWidth()
                    && event.getY() > 0 && event.getY() < mainCanvas.getHeight() && gameOn) {
                Fitting fitting = movingFittings[(int) event.getX() / 60][(int) event.getY() / 60];
                if (fitting != null && !(fitting instanceof Begining) && !(fitting instanceof End)) {
                    temp = fitting;
                    moveFit = true;
                }
            } else if (temp != null && event.getSceneX() > sidePane.getWidth() && event.getSceneX() < sidePane.getWidth() + mainCanvas.getWidth()
                    && gameOn && !moveFit) {
                if (movingFittings[(int) event.getX() / 60][(int) event.getY() / 60] == null) {
                    temp.draw(mainCanvas, event.getX(), event.getY());
                    movingFittings[(int) temp.getX() / 60][(int) temp.getY() / 60] = temp;
                    game.getLevels()[game.getCurrentLevel()].getFittings().remove(temp);
                    sideCanvas.getGraphicsContext2D().clearRect(0, 0, sideCanvas.getWidth(), sideCanvas.getHeight());
                    drawFittingsOnSide();
                    temp = null;
                    if (isHermetic()) {
                        gameOn = false;
                        showMenu();
                    }
                }
            } else if (temp != null && event.getSceneX() > sidePane.getWidth() && event.getSceneX() < sidePane.getWidth() + mainCanvas.getWidth()
                    && gameOn && moveFit) {
                if (movingFittings[(int) event.getX() / 60][(int) event.getY() / 60] == null) {
                    movingFittings[(int) temp.getX() / 60][(int) temp.getY() / 60] = null;
                    mainCanvas.getGraphicsContext2D().clearRect(temp.getX() + 1, temp.getY() + 1, 59, 59);
                    temp.draw(mainCanvas, event.getX(), event.getY());
                    movingFittings[(int) temp.getX() / 60][(int) temp.getY() / 60] = temp;
                    temp = null;
                    if (isHermetic()) {
                        gameOn = false;
                        showMenu();
                    }
                }
            }
        }
    }

    private boolean isHermetic() {
        if (!game.getLevels()[game.getCurrentLevel()].getFittings().isEmpty()) {
            return false;
        } else {
            for (int j = 0; j < 7; j++) {
                for (int i = 0; i < 7; i++) {
                    if (movingFittings[i][j] != null) {
                        int box = movingFittings[i][j].getDirections().length;
                        for (Direction d : movingFittings[i][j].getDirections()) {
                            if (d.getSide() == Direction.Side.north && j - 1 >= 0 && movingFittings[i][j - 1] != null) {
                                for (Direction d2 : movingFittings[i][j - 1].getDirections()) {
                                    if (d2.getSide() == Direction.Side.south) {
                                        box--;
                                    }
                                }
                            } else if (d.getSide() == Direction.Side.south && j + 1 < 7 && movingFittings[i][j + 1] != null) {
                                for (Direction d2 : movingFittings[i][j + 1].getDirections()) {
                                    if (d2.getSide() == Direction.Side.north) {
                                        box--;
                                    }
                                }
                            } else if (d.getSide() == Direction.Side.west && i - 1 >= 0 && movingFittings[i - 1][j] != null) {
                                for (Direction d2 : movingFittings[i - 1][j].getDirections()) {
                                    if (d2.getSide() == Direction.Side.east) {
                                        box--;
                                    }
                                }
                            } else if (d.getSide() == Direction.Side.east && i + 1 < 7 && movingFittings[i + 1][j] != null) {
                                for (Direction d2 : movingFittings[i + 1][j].getDirections()) {
                                    if (d2.getSide() == Direction.Side.west) {
                                        box--;
                                    }
                                }
                            }
                        }
                        if (box != 0) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void popUpWindow(){
        mainCanvas.getGraphicsContext2D().clearRect(50, 50, 320, 320);
        mainCanvas.getGraphicsContext2D().setFill(Color.RED);
        mainCanvas.getGraphicsContext2D().setStroke(Color.RED);
        mainCanvas.getGraphicsContext2D().setFont(new Font(14));
        mainCanvas.getGraphicsContext2D().strokePolygon(new double[]{50, 50, 370, 370}, new double[]{50, 370, 370, 50}, 4);
    }

    private void showMenu() {
        popUpWindow();
        if (game.nextLevel()) {
            mainCanvas.getGraphicsContext2D().fillText("Kliknij aby rozpocząć.", 65, 300);
        }
        else {
            gameOver = true;
            mainCanvas.getGraphicsContext2D().fillText("Stałeś się zasłużonym dla gazownictwa.", 65, 300);
        }
        mainCanvas.getGraphicsContext2D().setFont(new Font(20));
        mainCanvas.getGraphicsContext2D().fillText(game.getMessage(), 65, 200);
    }

    private void drawLines() {
        mainCanvas.getGraphicsContext2D().setStroke(Color.LIGHTGRAY);
//        for (int i = 0; i < 41; i++) {
//            for (int j = 1; j < 7; j++) {
//                mainCanvas.getGraphicsContext2D().strokeLine(10 * i, 60 * j, 10 * i + 5, 60 * j);
//            }
//        }
//        for (int i = 0; i < 41; i++) {
//            for (int j = 1; j < 7; j++) {
//                mainCanvas.getGraphicsContext2D().strokeLine(60 * j, 10 * i, 60 * j, 10 * i + 5);
//            }
//        }
        for (int i = 0; i <= 7; i++) {
            mainCanvas.getGraphicsContext2D().strokeLine(0, 60 * i, 420, 60 * i);
            mainCanvas.getGraphicsContext2D().strokeLine(60 * i, 0, 60 * i, 420);
        }
    }

    private void drawFittingsOnSide() {
        sideCanvas.getGraphicsContext2D().clearRect(0, 0, sideCanvas.getWidth(), sideCanvas.getHeight());
        for (int i = 0; i < game.getLevels()[game.getCurrentLevel()].getFittings().size(); i++) {
            game.getLevels()[game.getCurrentLevel()].getFittings().get(i).draw(sideCanvas, (i * 60 / 420) * 60, (i * 60) % 420);
        }
    }

    @FXML
    private void nextLevel() {
        mainCanvas.getGraphicsContext2D().clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
        drawLines();
        gameOn = true;
        movingFittings = new Fitting[7][7];
        movingFittings[(int) (game.getLevels()[game.getCurrentLevel()].getBegining().getX() / 60)]
                [(int) (game.getLevels()[game.getCurrentLevel()].getBegining().getY() / 60)]
                = game.getLevels()[game.getCurrentLevel()].getBegining();
        for (End e : game.getLevels()[game.getCurrentLevel()].getEnd()) {
            movingFittings[(int) (e.getX() / 60)][(int) (e.getY() / 60)] = e;
        }
        drawFittingsOnSide();
        game.getLevels()[game.getCurrentLevel()].getBegining().draw(mainCanvas, game.getLevels()[game.getCurrentLevel()].getBegining().getX(),
                game.getLevels()[game.getCurrentLevel()].getBegining().getY());
        for (End e : game.getLevels()[game.getCurrentLevel()].getEnd()) {
            e.draw(mainCanvas, e.getX(), e.getY());
        }
        time();
    }

    private void time() {
        Service<Void> t = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {

                    @Override
                    protected Void call() {
                        int t = game.getLevels()[game.getCurrentLevel()].getTimer() * 10;
                        int x = t;
                        while (t > 0 && gameOn) {
                            try {
                                bar.setProgress((double) t / x);

                                Thread.sleep(100);
                                t--;

                            } catch (InterruptedException e) {
                                System.out.println("Error");
                            }
                        }
                        if (t <= 0) {
                            gameOver = true;
                            popUpWindow();
                            mainCanvas.getGraphicsContext2D().setFont(new Font(20));
                            mainCanvas.getGraphicsContext2D().fillText("Niestety koniec czasu.", 105, 200);
                            gameOn = false;
                        }
                        return null;
                    }
                };
            }
        };
        t.start();
    }

    @FXML
    private void exit() {
        System.exit(0);
    }

    @FXML
    private void text() {
        gameOver = true;
        popUpWindow();
        mainCanvas.getGraphicsContext2D().fillText("Gra \"Gazownik\" - wersja 1.0", 65, 75);
        mainCanvas.getGraphicsContext2D().fillText("Gra wykonana w ramach pracy dyplomowej", 65, 95);
        mainCanvas.getGraphicsContext2D().fillText("na studiów podyplomowych na WWSIS", 65, 115);
        mainCanvas.getGraphicsContext2D().fillText("na kieruneku Inżynieria Oprogramowania", 65, 135);
        mainCanvas.getGraphicsContext2D().fillText("- Programowanie", 65, 155);
        mainCanvas.getGraphicsContext2D().fillText("Autor programu - Paweł Jarco", 65, 175);
        mainCanvas.getGraphicsContext2D().fillText("Promotor pracy - Krzysztof Węzowski", 65, 195);
        mainCanvas.getGraphicsContext2D().fillText("Grafika - Katarzyna Jarco", 65, 215);
        mainCanvas.getGraphicsContext2D().fillText("Zasady gry:", 65, 240);
        mainCanvas.getGraphicsContext2D().fillText("Klikając wybieramy kształtkę, którą chcemy", 65, 260);
        mainCanvas.getGraphicsContext2D().fillText("przesunąć. Kolejnym klinięciem wybieramy", 65, 280);
        mainCanvas.getGraphicsContext2D().fillText("miejsce, w którym chcemy umieścić kształtkę.", 65, 300);
        mainCanvas.getGraphicsContext2D().fillText("Podwójnym kliknięciem obracamy kształtkę.", 65, 320);
        mainCanvas.getGraphicsContext2D().fillText("Należy wykorzystać wszystkie kształtki.", 65, 340);
        mainCanvas.getGraphicsContext2D().fillText("Układ musi być szczelny.", 65, 360);
    }

    public void initialize() {
        drawLines();
    }
}
