package GUI_Classes;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class adminMenu {


    public Label lblDate;
    public Label lblTime;
    public Label lblTips;

    private int minute;
    private int hour;

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

    public adminMenu(){

        ArrayList<String> tips = new ArrayList<String>();

        tips.add("Tip: Click on Kanban to begin managing your project");
        tips.add("Tip: Click on Tasks to begin manage/edit tasks");
        tips.add("Tip: Click on Team Chat to communicate with team");
        tips.add("Tip: Click on My Profile to edit your profile");
        tips.add("Tip: Click on Members to view/edit members");
        tips.add("Tip: Click on Client to see project requests");



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
            int x = rand.nextInt((5 - 1) + 1) + 1;
            lblTips.setText(tips.get(x));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        tip.setCycleCount(Animation.INDEFINITE);
        tip.play();
    }

    public void exit(ActionEvent actionEvent) { //Exit functionality
        System.exit(0);
    }

    public void logOut(ActionEvent actionEvent) {
    }

    public void kanban(ActionEvent actionEvent) {
    }

    public void tasks(ActionEvent actionEvent) {
    }

    public void teamChat(ActionEvent actionEvent) {
    }

    public void userProfile(ActionEvent actionEvent) {
    }

    public void members(ActionEvent actionEvent) {
    }

    public void requests(ActionEvent actionEvent) {
    }


}
