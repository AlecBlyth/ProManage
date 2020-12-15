package GUI_Classes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProManage extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        splashScreen splash = new splashScreen(3500);
        splash.showIntro();
        Parent root = FXMLLoader.load(getClass().getResource("../FXMLs/login.fxml"));

        primaryStage.initStyle(StageStyle.UNDECORATED); //Removes default app controls
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        primaryStage.setScene(new Scene(root, 1280, 680));
        primaryStage.show();
        System.out.println("test");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
