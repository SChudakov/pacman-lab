package pacman.game;

import javafx.application.Application;
import javafx.stage.Stage;

public class PacManGame extends Application {

    @Override
    public void start(Stage primaryStage) {
        GameConfiguration configuration =
                new GameConfiguration.GameConfigurationBuilder()
                        .setDrawCollider(false)
                        .build();
        GameContainer gc = new GameContainer(configuration, primaryStage);
        gc.start();
        primaryStage.setOnCloseRequest(e -> exitPlatform(primaryStage));
    }

    private void exitPlatform(Stage primaryStage) {
        primaryStage.close();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
