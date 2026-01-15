import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.fxml.FXML;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.image.Image; 
import javafx.scene.layout.HBox;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;

    @FXML
    public GridPane mapGrid;

    public ImageView[] mapImageViews;

    @FXML
    private Label timerLabel;

    private javafx.animation.Timeline timer;
    private int remainingSeconds = 30;

    @FXML
    private HBox lifeBox; // FXMLで定義したIDと紐付け

    private int life = 3;
    private final String HEART_IMAGE_PATH = "png/catLeft2.png"; // ハートの画像パス
    private int goalX;
    private int goalY;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapData = new MapData(21, 15);
        decideGoalRightBottom();
        chara = new MoveChara(1, 1, mapData);
        mapImageViews = new ImageView[mapData.getHeight() * mapData.getWidth()];
        for (int y = 0; y < mapData.getHeight(); y ++) {
            for (int x = 0; x < mapData.getWidth(); x ++) {
                int index = y * mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x, y);
            }
        }
        drawMap(chara, mapData);
        drawLifeUI();
        // timer starts.
        startTimer();
    }

    // Draw the map
    public void drawMap(MoveChara c, MapData m) {
        int cx = c.getPosX();
        int cy = c.getPosY();
        mapGrid.getChildren().clear();
        m.setImageViews();
        for (int y = 0; y < mapData.getHeight(); y ++) {
            for (int x = 0; x < mapData.getWidth(); x ++) {
                int index = y * mapData.getWidth() + x;
                if (x == cx && y == cy) {
                    mapGrid.add(c.getCharaImageView(), x, y);
                } else {
                    mapGrid.add(m.getImageView(x, y), x, y);
                }
            }
        }
    }

    private void resetMap() {
        // reset the map
        
        mapData = new MapData(21, 15);
        chara = new MoveChara(1, 1, mapData);
        mapImageViews = new ImageView[mapData.getHeight() * mapData.getWidth()];
        for (int y = 0; y < mapData.getHeight(); y++) {
            for (int x = 0; x < mapData.getWidth(); x++) {
                int index = y * mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x, y);
            }
        }

        drawMap(chara, mapData);

        startTimer();
    }

    private void decideGoalRightBottom() {
        for (int y = mapData.getHeight() - 1; y >= 0; y--) {
            for (int x = mapData.getWidth() - 1; x >= 0; x--) {
                if (mapData.getMap(x,y) == MapData.TYPE_SPACE) {
                    goalX = x;
                    goalY = y;
                    System.out.println("ゴール座標:(" + goalX + "," + goalY + ")");
                    return;
                }
            }
        }
    }

    private void checkGoal() {
        if (chara.getPosX() == goalX && chara.getPosY() == goalY) {
            System.out.println("ゲームクリア");

            if (timer != null) {
                timer.stop();
            }

            StageDB.getMainStage().hide();
            StageDB.getMainSound().stop();
            StageDB.getGameOverStage().show();
        }
    }

    // Get users' key actions
    public void keyAction(KeyEvent event) {
        KeyCode key = event.getCode();
        System.out.println("keycode:" + key);
        if (key == KeyCode.A) {
            leftButtonAction();
        } else if (key == KeyCode.S) {
            downButtonAction();
        } else if (key == KeyCode.W) {
            upButtonAction();
        } else if (key == KeyCode.D) {
            rightButtonAction();
        }
    }

    // Operations for going the cat up
    public void upButtonAction() {
        printAction("UP");
        chara.setCharaDirection(MoveChara.TYPE_UP);
        boolean success = chara.move(0, -1);
        checkGoal();
        if(success == true) {
            StageDB.playWalkSound();
        } else {
            StageDB.playBumpSound();
        }
        checkItemGet();
        drawMap(chara, mapData);
    }

    // Operations for going the cat down
    public void downButtonAction() {
        printAction("DOWN");
        chara.setCharaDirection(MoveChara.TYPE_DOWN);
        boolean success = chara.move(0, 1);
        checkGoal();
        if(success == true) {
            StageDB.playWalkSound();
        } else {
            StageDB.playBumpSound();
        }
        checkItemGet();
        drawMap(chara, mapData);
    }

    // Operations for going the cat right
    public void leftButtonAction() {
        printAction("LEFT");
        chara.setCharaDirection(MoveChara.TYPE_LEFT);
        boolean success = chara.move(-1, 0);
        checkGoal();
        if(success == true) {
            StageDB.playWalkSound();
        } else {
            StageDB.playBumpSound();
        }
        checkItemGet();
        drawMap(chara, mapData);
    }

    // Operations for going the cat right
    public void rightButtonAction() {
        printAction("RIGHT");
        chara.setCharaDirection(MoveChara.TYPE_RIGHT);
        boolean success = chara.move(1, 0);
        checkGoal();
        if(success == true) {
            StageDB.playWalkSound();
        } else {
            StageDB.playBumpSound();
        }
        checkItemGet();
        drawMap(chara, mapData);
    }

    @FXML
    public void func1ButtonAction(ActionEvent event) {
        try {
            System.out.println("func1");

            if (timer != null) {
                timer.stop();
            }

            StageDB.getMainStage().hide();
            StageDB.getMainSound().stop();
            StageDB.getGameOverStage().show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    public void func2ButtonAction(ActionEvent event) {
       resetMap();
    }

    @FXML
    public void func3ButtonAction(ActionEvent event) {
        System.out.println("func3: Nothing to do");
    }

    @FXML
    public void func4ButtonAction(ActionEvent event) {
        System.out.println("func4: Nothing to do");
    }

    // ライフの表示を更新するメソッド
    private void drawLifeUI() {
        lifeBox.getChildren().clear();
        for (int i = 0; i < life; i++) {
            ImageView heart = new ImageView(new Image(HEART_IMAGE_PATH));
            heart.setFitWidth(30);  // サイズ調整
            heart.setFitHeight(30); // サイズ調整
            lifeBox.getChildren().add(heart);
        }
    }

// ライフを減らすメソッド
    public void reduceLife() {
    life--;
        drawLifeUI();
        if (life <= 0) {
         System.out.println("No Lives Left!");
         onTimeUp();
        }
    }

    // Print actions of user inputs
    public void printAction(String actionString) {
        System.out.println("Action: " + actionString);
    }
    private void checkItemGet() {
        int curX = chara.getPosX();
        int curY = chara.getPosY();
        int tileType = mapData.getMap(curX, curY);

        if (tileType == MapData.TYPE_HEAL) {
            System.out.println("Life Up!");
            life = Math.min(3, life + 1); // 最大3まで回復
            removeItem(curX, curY);
        } else if (tileType == MapData.TYPE_DAMAGE) {
            System.out.println("Life Down!");
            reduceLife(); // 前の回答で作ったライフ減少メソッド
            removeItem(curX, curY);
        }
    }

// アイテムを拾ったらその場所を床(SPACE)に戻す
    private void removeItem(int x, int y) {
        mapData.setMap(x, y, MapData.TYPE_SPACE);
        // マップ表示用のImageViewも床に更新
        int index = y * mapData.getWidth() + x;
        //mapImageViews[index] = mapData.getImageView(x, y); 
        drawMap(chara, mapData);
        drawLifeUI(); // ライフ表示を更新
    }
    
    private void startTimer(){
        if (timer != null){
            timer.stop();
        }

        remainingSeconds = 30;
        if (timerLabel != null){
            timerLabel.setText(String.valueOf(remainingSeconds));
        } else {
            System.out.println("timerLabel is null");
        }

        timer = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                remainingSeconds--;

                if (timerLabel != null){
                    timerLabel.setText(String.valueOf(remainingSeconds));
                }

                if (remainingSeconds <= 0){
                    timer.stop();
                    onTimeUp();
                }
            })
        );
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void onTimeUp(){
        try {
            System.out.println("Time Over");
            StageDB.getMainStage().hide();
            StageDB.stopWalkSound();
            StageDB.getMainSound().stop();
            StageDB.getGameOverSound().play();
            StageDB.getGameOverStage().show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
