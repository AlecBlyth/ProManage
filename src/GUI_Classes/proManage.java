package GUI_Classes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class proManage extends Application {

    //Variables
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        splashScreen splash = new splashScreen(3500); //Sets duration of splash screen
        splash.showIntro(); //Show custom intro splash screen
        Parent root = FXMLLoader.load(getClass().getResource("../FXMLs/login.fxml")); //Loads Login FXML

        primaryStage.initStyle(StageStyle.UNDECORATED); //Removes default app controls
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> { //Allows user to drag screen
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        primaryStage.setScene(new Scene(root, 1280, 680)); //This should be adjusted to user resolution (Was tested on a 3440x1440p screen)
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
