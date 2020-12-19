package GUI_Classes.Client;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class clientProgressOverall {

    public Gauge gaugeProgress;
    private double xOffset = 0;
    private double yOffset = 0;

    public Label lblDate;
    public Label lblTime;

    private int minute;
    private int hour;

    int sum = 0;
    int compeleteTotal;

    public static String getFormattedDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day=cal.get(Calendar.DATE);

        if(!((day>10) && (day<19))) //Allows for th st nd suffixes
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
    } //Date formatter for date label Label lblTips;

    public void initialize(){

        ArrayList<Integer> TaskProgress = new ArrayList<>();

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> { //Updates clock every second and changes label according to time
            Calendar cal = Calendar.getInstance();
            minute = cal.get(Calendar.MINUTE);
            hour = cal.get(Calendar.HOUR);
            String curTime = String.format("%02d:%02d", hour, minute);
            lblTime.setText(curTime);
            lblDate.setText(getFormattedDate(cal.getTime())); //Gets date and changes label to date
        }),
                new KeyFrame(Duration.seconds(1))
        );

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        gaugeProgress.barColorProperty().setValue(Color.rgb(45,121,255)); //Changes colour of value bar
        gaugeProgress.valueColorProperty().setValue(Color.WHITE); //Changes font colour

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connect to mySQL dummy database |NOTE This is prone to SQL Injection
            Statement statement = connection.createStatement();
            String queryString = "SELECT taskprogress FROM tasks"; //get tasks from database
            ResultSet resultSet = statement.executeQuery(queryString);

            while (resultSet.next()) { //Checks for username and password
                int taskprog = resultSet.getInt("taskprogress");
                TaskProgress.add(taskprog);
                compeleteTotal = compeleteTotal+1;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        for(Integer tempSum : TaskProgress){ //Calculates average percentage
            sum+= tempSum;
        }
        compeleteTotal = compeleteTotal * 100;
        double currentProgress = sum;
        double totalPercentage = currentProgress / compeleteTotal * 100;
        gaugeProgress.setValue(totalPercentage);

    }

    public void progress(ActionEvent actionEvent) {
        //Do nothing since already on stage.
    }

    public void chat(ActionEvent actionEvent) {
    }

    public void profile(ActionEvent actionEvent) {
    }

    public void request(ActionEvent actionEvent) {
    }

    public void btnViewmore(ActionEvent actionEvent) {
    }

    public void logOut(ActionEvent logout) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/login.fxml")); //Display admin menu
        AnchorPane root = loader.load();
        root.setOnMousePressed(event -> { //Allow to move app around
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene menuViewScene = new Scene(root);
        Stage window = (Stage) ((Node) logout.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(menuViewScene); //Show new scene
        window.show();
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
