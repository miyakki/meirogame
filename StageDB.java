import java.io.IOException;
import java.io.File;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;

class StageDB {

    static private Stage mainStage = null;
    static private Stage gameOverStage = null;
    static private MediaPlayer mainSound = null;
    static private MediaPlayer gameOverSound = null;
    static private AudioClip walkSound;
    static private AudioClip bumpSound;
    static private AudioClip getItemSound;
    @SuppressWarnings("rawtypes")
    static private Class mainClass;
    static private final String mainSoundFileName = "sound/MainSound.mp3"; // BGM by OtoLogic
    static private final String gameOverSoundFileName = "sound/GameOverSound.mp3";
    static private final String walkSoundFileName = "sound/Walk.mp3";
    static private final String bumpSoundFileName = "sound/Bump.mp3";
    static private final String getItemSoundFileName = "sound/GetItem.mp3";

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
                mp.setVolume(0.5);
                mainSound = mp;
            } catch (Exception io) {
                System.err.print(io.getMessage());
            }
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
                mp.setVolume(0.5);
                gameOverSound = mp;
            } catch (Exception io) {
                System.err.print(io.getMessage());
            }
        }
        return gameOverSound;
    }

    public static void playWalkSound() {
        if(walkSound == null) {
            try {
                walkSound = new AudioClip(
                    StageDB.class
                        .getResource("sound/Walk.mp3")
                        .toExternalForm()
                );
                walkSound.setVolume(1.0);
            } catch (Exception e) {
                System.err.print(e.getMessage());
            }
        }
        if(!walkSound.isPlaying()) {
            walkSound.play();
        }
    }

    public static void stopWalkSound() {
        if(walkSound != null) {
            walkSound.stop();
        }
    }

    public static void playBumpSound() {
        try {
            bumpSound = new AudioClip(
                StageDB.class
                    .getResource("sound/Bump.mp3")
                    .toExternalForm()
            );
            } catch (Exception e) {
                System.err.print(e.getMessage());
            }
            if(walkSound != null) {
                walkSound.stop();
            }
        bumpSound.setVolume(1.0);
        bumpSound.play();
    }

    public static void playGetItemSound() {
            try {
                getItemSound = new AudioClip(
                    StageDB.class
                        .getResource("sound/GetItem.mp3")
                        .toExternalForm()
                );
            } catch (Exception e) {
                System.err.print(e.getMessage());
            }
        getItemSound.setVolume(1.0);
        getItemSound.play();
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
