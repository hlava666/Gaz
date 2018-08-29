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
    private Fitting[][] fittingBoard;
    private Fitting catchedFitting = null;
    private DrawHandler drawHandler = new DrawHandler(this);

    @FXML
    private Canvas mainCanvas;

    @FXML
    private Canvas sideCanvas;

    @FXML
    private Pane sidePane;

    @FXML
    private ProgressBar bar;

    public static final double STEP_SIZE = 60;

    public static final int BOARD_SIZE = 7;

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

    public void setCatchedFitting(Fitting catchedFitting) {
        this.catchedFitting = catchedFitting;
    }

    public void setMovingFittingsElement(int row, int column, Fitting fitting) {
        fittingBoard[row][column] = fitting;
    }

    // ==============GETTERS==============

    public DrawHandler getDrawHandler() {
        return drawHandler;
    }

    public Game getGame() {
        return game;
    }

    public Fitting[][] getFittingBoard() {
        return fittingBoard;
    }

    public Fitting getCatchedFitting() {
        return catchedFitting;
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
        startTimer();
    }

    @FXML
    public void mouseClick(MouseEvent event) {
        MouseHandler mouseHandler = new MouseHandler(event, this);
        if (!gameOn && !gameOver) {
            mouseHandler.startGame();
        } else {
            if (doubleClickedOnMainBoard(event)) {
                mouseHandler.rotateFitting();
            } else if (clickedOnSideBoard(event)) {
                mouseHandler.chooseFittingFromSide();
            } else if (clickedOnMainBoardWithoutFitting(event)) {
                mouseHandler.chooseFittingFromMainBoard();
            } else if (clickedOnMainBoardWithFittingFromSide(event)) {
                mouseHandler.placeFittingFromSide();
            } else if (clickedOnMainBoardWithFittingFromMainBoard(event)) {
                mouseHandler.placeFittingFromMainBoard();
            }
        }
    }

     @FXML
    public void nextLevel() {
        mainCanvas.getGraphicsContext2D().clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
        drawHandler.drawLines();
        gameOn = true;
        createFittingBoard();
        drawHandler.drawFittingsOnSide();
        drawHandler.drawStartAndEndOnMainBoard();
        startTimer();
    }

    @FXML
    private void text() {
        gameOver = true;
        drawHandler.popUpWindow();
        mainCanvas.getGraphicsContext2D().fillText("Gra \"Gazownik\" - wersja 1.1", 65, 75);
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

    // ==============HELPERS==============

    private void startTimer() {
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
            return checkAllFields();
        }
    }

    private boolean isFittingCatched(){
        return (catchedFitting != null);
    }

    private boolean doubleClickedOnMainBoard(MouseEvent event) {
        return (event.getClickCount() == 2 && event.getSceneX() > sidePane.getWidth() && event.getSceneX() < sidePane.getWidth() + mainCanvas.getWidth() && gameOn);
    }

    private boolean clickedOnSideBoard(MouseEvent event) {
        return (event.getSceneX() > sidePane.getWidth() + mainCanvas.getWidth() && event.getX() < sideCanvas.getWidth() && event.getY() > 0
                && event.getY() < sideCanvas.getHeight() && gameOn);
    }

    private boolean clickedOnMainBoardWithoutFitting(MouseEvent event) {
        return (!isFittingCatched() && event.getSceneX() > sidePane.getWidth() && event.getSceneX() < sidePane.getWidth() + mainCanvas.getWidth()
                && event.getY() > 0 && event.getY() < mainCanvas.getHeight() && gameOn);
    }

    private boolean clickedOnMainBoardWithFittingFromSide(MouseEvent event) {
        return (isFittingCatched() && event.getSceneX() > sidePane.getWidth() && event.getSceneX() < sidePane.getWidth() + mainCanvas.getWidth()
                && gameOn && !movingFittingFromMainBoard);
    }

    private boolean clickedOnMainBoardWithFittingFromMainBoard(MouseEvent event){
        return(isFittingCatched() && event.getSceneX() > sidePane.getWidth() && event.getSceneX() < sidePane.getWidth() + mainCanvas.getWidth()
                && gameOn && movingFittingFromMainBoard);
    }

    private void createFittingBoard(){
        fittingBoard = new Fitting[BOARD_SIZE][BOARD_SIZE];
        fittingBoard[(int) (game.getLevels()[game.getCurrentLevel()].getBeginning().getX() / STEP_SIZE)]
                [(int) (game.getLevels()[game.getCurrentLevel()].getBeginning().getY() / STEP_SIZE)]
                = game.getLevels()[game.getCurrentLevel()].getBeginning();
        for (End e : game.getLevels()[game.getCurrentLevel()].getEnd()) {
            fittingBoard[(int) (e.getX() / STEP_SIZE)][(int) (e.getY() / STEP_SIZE)] = e;
        }
    }

    private boolean verifyAllDirection(int i, int j, int box) {
        for (Direction d : fittingBoard[i][j].getDirections()) {
            if (d.getSide() == Direction.Side.north && j - 1 >= 0 && fittingBoard[i][j - 1] != null) {
                box += fittingBoard[i][j - 1].isConectedFromNorth();
            } else if (d.getSide() == Direction.Side.south && j + 1 < 7 && fittingBoard[i][j + 1] != null) {
                box += fittingBoard[i][j + 1].isConectedFromSouth();
            } else if (d.getSide() == Direction.Side.west && i - 1 >= 0 && fittingBoard[i - 1][j] != null) {
                box += fittingBoard[i - 1][j].isConectedFromWest();
            } else if (d.getSide() == Direction.Side.east && i + 1 < 7 && fittingBoard[i + 1][j] != null) {
                box += fittingBoard[i + 1][j].isConectedFromEast();
            }
        }
        return box == 0;
    }

    private boolean isFieldHermetic(int i, int j) {
        if (fittingBoard[i][j] != null) {
            int box = fittingBoard[i][j].getDirections().length;
            return verifyAllDirection(i, j, box);
        } else return true;
    }

    private boolean checkAllFields(){
        for (int j = 0; j < BOARD_SIZE; j++) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (!isFieldHermetic(i, j)){
                    return false;
                }
            }
        }
        return true;
    }

    public void initialize() {
        drawHandler.drawLines();
    }
}
