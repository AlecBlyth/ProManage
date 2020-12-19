package GUI_Classes.User;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class userMenu {

    public Label lblDate;
    public Label lblTime;
    public Label lblTips;

    private int minute;
    private int hour;

    private double xOffset = 0;
    private double yOffset = 0;

    Random rand = new Random();

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
    } //Date formatter for date label

    public void initialize(){
        ArrayList<String> tips = new ArrayList<String>();

        tips.add("Tip: Click on Kanban to begin managing your project");
        tips.add("Tip: Click on Tasks to begin manage/edit tasks");
        tips.add("Tip: Click on Chat to communicate with team");
        tips.add("Tip: Click on My Profile to edit your profile");


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

        Timeline tip = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            int x = rand.nextInt((3 - 1) + 1) + 1;
            lblTips.setText(tips.get(x));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        tip.setCycleCount(Animation.INDEFINITE);
        tip.play();
    }

    public void kanban(ActionEvent kanban) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/Admin/kanbanUser.fxml")); //Display admin menu
        AnchorPane root = loader.load();
        root.setOnMousePressed(event -> { //Allow to move app around
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene menuViewScene = new Scene(root);
        Stage window = (Stage) ((Node) kanban.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(menuViewScene); //Show new scene
        window.show();
    }

    public void tasks(ActionEvent actionEvent) {
    }

    public void teamChat(ActionEvent actionEvent) {
    }

    public void profile(ActionEvent actionEvent) {
    }

    public void logOut(ActionEvent actionEvent) {
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

}
