import java.io.IOException;
import java.io.File;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

class StageDB {

    static private Stage mainStage = null;
    static private Stage gameOverStage = null;
    static private MediaPlayer mainSound = null;
    static private MediaPlayer gameOverSound = null;
    static private MediaPlayer walkSound = null;
    static private MediaPlayer bumpSound = null;
    @SuppressWarnings("rawtypes")
    static private Class mainClass;
    static private final String mainSoundFileName = "sound/MainSound.mp3"; // BGM by OtoLogic
    static private final String gameOverSoundFileName = "sound/GameOverSound.mp3";
    static private final String walkSoundFileName = "sound/Walk.mp3";
    static private final String bumpSoundFileName = "sound/Bump.mp3";

    @SuppressWarnings("rawtypes")
    public static void setMainClass(Class mainClass) {
        StageDB.mainClass = mainClass;
    }

    public static MediaPlayer getMainSound() {
        if (mainSound == null) {
            try {
                Media m = new Media(new File(mainSoundFileName).toURI().toString());
                MediaPlayer mp = new MediaPlayer(m);
                mp.setCycleCount(MediaPlayer.INDEFINITE); // loop play
                mp.setRate(1.0); // 1.0 = normal speed
                mp.setVolume(0.2); // volume from 0.0 to 1.0
                mainSound = mp;
                mainSound.setOnReady(() -> {
                    mainSound.setVolume(0.2);
                    mainSound.play();
                });
            } catch (Exception io) {
                System.err.print(io.getMessage());
            }
        }
        if(mainSound != null) {
            mainSound.setVolume(0.2);
        }
        return mainSound;
    }

    public static MediaPlayer getGameOverSound() {
        if (gameOverSound == null) {
            try {
                Media m = new Media(new File(gameOverSoundFileName).toURI().toString());
                MediaPlayer mp = new MediaPlayer(m);
                mp.setCycleCount(MediaPlayer.INDEFINITE);
                mp.setRate(1.0);
                mp.setVolume(0.2);
                gameOverSound = mp;
            } catch (Exception io) {
                System.err.print(io.getMessage());
            }
        }
        return gameOverSound;
    }

    public static MediaPlayer playWalkSound() {
        if(walkSound == null) {
            try {
                Media m = new Media(new File(walkSoundFileName).toURI().toString());
                MediaPlayer mp = new MediaPlayer(m);
                mp.setVolume(0.5);
                walkSound = mp;
            } catch (Exception e) {
                System.err.print(e.getMessage());
            }
        }
        if(bumpSound != null) bumpSound.stop();

        walkSound.stop();
        walkSound.seek(javafx.util.Duration.ZERO);
        walkSound.play();
        return walkSound;
    }

    public static MediaPlayer playBumpSound() {
        try {
            Media m = new Media(new File(bumpSoundFileName).toURI().toString());
            MediaPlayer mp = new MediaPlayer(m);
            mp.setVolume(0.5);
            if(walkSound != null) {
            walkSound.stop();
            }
            mp.play();
            bumpSound = mp;

            } catch (Exception e) {
                System.err.print(e.getMessage());
        }
        return bumpSound;
    }

    public static Stage getMainStage() {
        if (mainStage == null) {
            try {
                FXMLLoader loader = new FXMLLoader(mainClass.getResource("MapGame.fxml"));
                VBox root = loader.load();
                Scene scene = new Scene(root);
                mainStage = new Stage();
                mainStage.setScene(scene);
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
        }
        return mainStage;
    }

    public static Stage getGameOverStage() {
        if (gameOverStage == null) {
            try {
                System.out.println("StageDB:getGameOverStage()");
                FXMLLoader loader = new FXMLLoader(mainClass.getResource("MapGameOver.fxml"));
                VBox root = loader.load();
                Scene scene = new Scene(root);
                gameOverStage = new Stage();
                gameOverStage.setScene(scene);
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
        }
        return gameOverStage;
    }

    public static void resetMainStage(){
        if (mainStage != null){
            mainStage.close();
            mainStage=null;
        }
    }
}
