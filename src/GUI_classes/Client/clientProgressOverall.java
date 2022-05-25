package GUI_classes.Client;

import com.jfoenix.controls.JFXButton;
import eu.hansolo.medusa.Gauge;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class clientProgressOverall {

    //FXML Components
    public Gauge gaugeProgress;
    public Label lblDate, lblTime;
    public JFXButton exitBtn, btnLogout, btnProgress, btnChat, btnProfile, btnRequest;

    //Variables
    private double xOffset = 0;
    private double yOffset = 0;
    private int sum = 0;
    private int completeTotal;

    //SYSTEM METHODS
    public static String getFormattedDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DATE);

        if (!((day > 10) && (day < 19)))
            switch (day % 10) {
                case 1:
                    return new SimpleDateFormat("d'st' MMMM yyyy").format(date);
                case 2:
                    return new SimpleDateFormat("d'nd' MMMM yyyy").format(date);
                case 3:
                    return new SimpleDateFormat("d'rd' MMMM yyyy").format(date);
                default:
                    return new SimpleDateFormat("d'th' MMMM yyyy").format(date);
            }
        return new SimpleDateFormat("d'th' MMMM yyyy    ").format(date);
    }

    public void initialize() {

        exitBtn.setOnMouseEntered(e -> exitBtn.setStyle("-fx-background-color: RED; -fx-background-radius: 0;"));
        exitBtn.setOnMouseExited(e -> exitBtn.setStyle("-fx-background-color: ; -fx-background-radius: 0;"));
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle("-fx-background-color: RED; -fx-background-radius: 0;"));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle("-fx-background-color: #262626; -fx-background-radius: 0;"));
        btnProgress.setOnMouseEntered(e -> btnProgress.setStyle("-fx-background-color: #4287ff; -fx-background-radius: 0;"));
        btnProgress.setOnMouseExited(e -> btnProgress.setStyle("-fx-background-color: #2d7aff; -fx-background-radius: 0;"));
        btnChat.setOnMouseEntered(e -> btnChat.setStyle("-fx-background-color: #4287ff; -fx-background-radius: 0;"));
        btnChat.setOnMouseExited(e -> btnChat.setStyle("-fx-background-color: #2d7aff; -fx-background-radius: 0;"));
        btnProfile.setOnMouseEntered(e -> btnProfile.setStyle("-fx-background-color: #4287ff; -fx-background-radius: 0;"));
        btnProfile.setOnMouseExited(e -> btnProfile.setStyle("-fx-background-color: #2d7aff; -fx-background-radius: 0;"));
        btnRequest.setOnMouseEntered(e -> btnRequest.setStyle("-fx-background-color: #4287ff; -fx-background-radius: 0;"));
        btnRequest.setOnMouseExited(e -> btnRequest.setStyle("-fx-background-color: #2d7aff; -fx-background-radius: 0;"));

        initTime();

        ArrayList<Integer> TaskProgress = new ArrayList<>();

        gaugeProgress.barColorProperty().setValue(Color.rgb(45, 121, 255)); //Changes colour of gauge bar
        gaugeProgress.valueColorProperty().setValue(Color.WHITE); //Changes font colour of gauge

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connect to mySQL server
            Statement statement = connection.createStatement();
            String queryString = "SELECT taskprogress FROM tasks";
            ResultSet resultSet = statement.executeQuery(queryString);

            while (resultSet.next()) {
                int taskprog = resultSet.getInt("taskprogress");
                TaskProgress.add(taskprog);
                completeTotal = completeTotal + 1;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        for (Integer tempSum : TaskProgress) { //Calculates average percentage
            sum += tempSum;
        }
        completeTotal = completeTotal * 100;
        double currentProgress = sum;
        double totalPercentage = currentProgress / completeTotal * 100;
        gaugeProgress.setValue(totalPercentage); //Sets gauge value to total percentage

    }

    public void initTime() {
        Calendar cal = Calendar.getInstance();
        lblDate.setText(getFormattedDate(cal.getTime()) + "  |  ");
        DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
                event -> lblTime.setText(LocalTime.now().format(SHORT_TIME_FORMATTER))),
                new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    //CLIENT FEATURES
    public void progress(ActionEvent progress) {
        //Do nothing since already on stage.
    }

    public void chat(ActionEvent chat) {
    }

    public void request(ActionEvent request) {
    }

    public void btnViewmore(ActionEvent viewMore) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/Client/clientProgressDetailed.fxml"));
        AnchorPane root = loader.load();
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene detailedViewScene = new Scene(root);
        Stage window = (Stage) ((Node) viewMore.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(detailedViewScene);
        window.show();
    }

    //USER FEATURES
    public void profile(ActionEvent profile) {
    }

    //NAVIGATION
    public void exit(ActionEvent exit) {
        System.exit(0);
    }

    public void logOut(ActionEvent logout) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/login.fxml"));
        AnchorPane root = loader.load();
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene loginViewScene = new Scene(root);
        Stage window = (Stage) ((Node) logout.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(loginViewScene);
        window.show();
    }

    public void menu(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/Client/clientMenu.fxml"));
        AnchorPane root = loader.load();
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene menuViewScene = new Scene(root);
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(menuViewScene);
        window.show();
    }
}
