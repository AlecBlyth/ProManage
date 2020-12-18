package GUI_Classes;

import eu.hansolo.medusa.Gauge;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
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

        gaugeProgress.setValue(100);


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

    public void logOut(ActionEvent actionEvent) {
    }

    public void exit(ActionEvent actionEvent) {
    }
}
