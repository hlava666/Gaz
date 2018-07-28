package windows;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import logic.*;
import java.io.FileNotFoundException;

public class Controller {
    private Game game;
    private boolean movingFittingFromMainBoard = false;
    private boolean gameOn = false;
    private boolean gameOver = true;
    private Fitting[][] movingFittings;
    private Fitting temp = null;
    private DrawHandler drawHandler = new DrawHandler(this);

    @FXML
    private Canvas mainCanvas;

    @FXML
    private Canvas sideCanvas;

    @FXML
    private Pane sidePane;

    @FXML
    private ProgressBar bar;

    // ==============SETTERS==============

    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setMovingFittingFromMainBoard(boolean moveFit) {
        this.movingFittingFromMainBoard = moveFit;
    }

    public void setTemp(Fitting temp) {
        this.temp = temp;
    }

    public void setMovingFittingsElement(int row, int column, Fitting fitting) {
        movingFittings[row][column] = fitting;
    }

    // ==============GETTERS==============

    public DrawHandler getDrawHandler() {
        return drawHandler;
    }

    public Game getGame() {
        return game;
    }

    public Fitting[][] getMovingFittings() {
        return movingFittings;
    }

    public Fitting getTemp() {
        return temp;
    }

    public Canvas getMainCanvas() {
        return mainCanvas;
    }

    public Canvas getSideCanvas() {
        return sideCanvas;
    }

    // ==============FXML==============

    @FXML
    public void newGame() throws FileNotFoundException {
        game = new Game();
        gameOver = false;
        nextLevel();
        time();
    }

    @FXML
    public void mouseClick(MouseEvent event) {
        MouseHandler mouseHandler = new MouseHandler(event);
        if (!gameOn && !gameOver) {
            mouseHandler.startGame(this);
        } else {
            if (event.getClickCount() == 2 && event.getSceneX() > sidePane.getWidth()
                    && event.getSceneX() < sidePane.getWidth() + mainCanvas.getWidth() && gameOn) {
                mouseHandler.rotateFitting(this);
            } else if (event.getSceneX() > sidePane.getWidth() + mainCanvas.getWidth() && event.getX() < 120 && event.getY() > 0
                    && event.getY() < mainCanvas.getHeight() && gameOn) {
                mouseHandler.chooseFittingFromSide(this);
            } else if (temp == null && event.getSceneX() > sidePane.getWidth() && event.getSceneX() < sidePane.getWidth() + mainCanvas.getWidth()
                    && event.getY() > 0 && event.getY() < mainCanvas.getHeight() && gameOn) {
                mouseHandler.chooseFittingFromMainBoard(this);
            } else if (temp != null && event.getSceneX() > sidePane.getWidth() && event.getSceneX() < sidePane.getWidth() + mainCanvas.getWidth()
                    && gameOn && !movingFittingFromMainBoard) {
                mouseHandler.placeFittingFromSide(this);
            } else if (temp != null && event.getSceneX() > sidePane.getWidth() && event.getSceneX() < sidePane.getWidth() + mainCanvas.getWidth()
                    && gameOn && movingFittingFromMainBoard) {
                mouseHandler.placeFittingFromMainBoard(this);
            }
        }
    }

     @FXML
    public void nextLevel() {
        mainCanvas.getGraphicsContext2D().clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
        drawHandler.drawLines();
        gameOn = true;
        movingFittings = new Fitting[7][7];
        movingFittings[(int) (game.getLevels()[game.getCurrentLevel()].getBegining().getX() / 60)]
                [(int) (game.getLevels()[game.getCurrentLevel()].getBegining().getY() / 60)]
                = game.getLevels()[game.getCurrentLevel()].getBegining();
        for (End e : game.getLevels()[game.getCurrentLevel()].getEnd()) {
            movingFittings[(int) (e.getX() / 60)][(int) (e.getY() / 60)] = e;
        }
        drawHandler.drawFittingsOnSide();
        game.getLevels()[game.getCurrentLevel()].getBegining().draw(mainCanvas, game.getLevels()[game.getCurrentLevel()].getBegining().getX(),
                game.getLevels()[game.getCurrentLevel()].getBegining().getY());
        for (End e : game.getLevels()[game.getCurrentLevel()].getEnd()) {
            e.draw(mainCanvas, e.getX(), e.getY());
        }
        time();
    }

    @FXML
    private void text() {
        gameOver = true;
        drawHandler.popUpWindow();
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

    @FXML
    private void exit() {
        System.exit(0);
    }

    // ==============OTHERS METHODS==============

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
                            drawHandler.popUpWindow();
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

    public boolean isHermetic() {
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

    public void initialize() {
        drawHandler.drawLines();
    }
}
