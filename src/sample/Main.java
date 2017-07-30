package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Group root = new Group();

    public static void main(String[] args) {
        Controller c = new Controller(root, 100);
        c.run();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(root, primaryStage.getMaxWidth(), primaryStage.getMaxHeight());
        primaryStage.setScene(scene);
        primaryStage.setTitle("ՀՄ Կուրսային");
        primaryStage.show();
    }
}
