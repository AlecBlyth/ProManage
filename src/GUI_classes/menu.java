package GUI_classes;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class menu {

    //FXML Components
    public Label lblDate;
    public Label lblTime;
    public Label lblTips;
    public ImageView memberIcon;
    public ImageView reqIcon;
    public JFXButton btnMembers;
    public JFXButton btnRequests;

    //Variables
    private double xOffset = 0;
    private double yOffset = 0;

    //Passed Variables
    private String currentUser;
    private int currentID;

    //SYSTEM METHODS
    public static String getFormattedDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DATE);

        if (!((day > 10) && (day < 19))) //Allows for th st nd suffixes
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
        return new SimpleDateFormat("d'th' MMMM yyyy").format(date);
    } //Date formatter for date label

    public void initialize(String userType, int id) {
        initTime();
        currentUser = userType; //Sets currentUser to userType
        currentID = id;

        ArrayList<String> tips = new ArrayList<>(); //Arraylist for tips

        tips.add("Tip: Click on kanban to begin managing your project"); //Adds tips to array
        tips.add("Tip: Click on tasks to begin manage/edit tasks");
        tips.add("Tip: Click on chat to communicate with team");
        tips.add("Tip: Click on my profile to edit your profile");

        switch (userType) { //If UserType is user, hide admin functions and disable them
            case "USER":
                btnMembers.setVisible(false);
                btnRequests.setVisible(false);
                memberIcon.setVisible(false);
                reqIcon.setVisible(false);
                btnMembers.setDisable(true);
                btnRequests.setDisable(true);
                break;

            case "ADMIN":
                btnMembers.setVisible(true);
                btnRequests.setVisible(true);
                memberIcon.setVisible(true);
                reqIcon.setVisible(true);
                btnMembers.setDisable(false);
                btnRequests.setDisable(false);

                tips.add("Tip: Click on members to view/edit members"); //Adds admin related tips
                tips.add("Tip: Click on client to see project requests");

                break;
            default:
                btnMembers.setVisible(false);
                btnRequests.setVisible(false);
                memberIcon.setVisible(false);
                reqIcon.setVisible(false);
                btnMembers.setDisable(true);
                btnRequests.setDisable(true);
                tips.clear(); //Clears all tips
                tips.add("ALERT! You currently do not have a usertype."); //Warns user of no known usertype
                break;
        }

        int y = tips.size() - 1; //Sets y to array size and subtracts to fix out of bounds issue

        Timeline tip = new Timeline(new KeyFrame(Duration.seconds(10), e -> { //Every 10 seconds change label text to tip in array
            int x = ThreadLocalRandom.current().nextInt(0, y + 1);
            lblTips.setText(tips.get(x));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        tip.setCycleCount(Animation.INDEFINITE);
        tip.play();

    } //Initialise controller

    public void initTime() {
        Calendar cal = Calendar.getInstance();
        lblDate.setText(getFormattedDate(cal.getTime()) + "  |  "); //Gets date and changes label to date
        DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
                event -> lblTime.setText(LocalTime.now().format(SHORT_TIME_FORMATTER))),
                new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play(); //Updates the clock
    } //Initialise time

    //ADMIN AND USER FEATURES
    public void kanban(ActionEvent kanban) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/kanban.fxml"));
        AnchorPane root = loader.load();
        GUI_classes.kanban kanbanScene = loader.getController();
        kanbanScene.initialize(currentUser, currentID);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene kanbanViewScene = new Scene(root);
        Stage window = (Stage) ((Node) kanban.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(kanbanViewScene);
        window.show();
    }

    public void tasks(ActionEvent task) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/tasks.fxml"));
        AnchorPane root = loader.load();
        tasks tasks = loader.getController();
        tasks.initialize(currentUser, currentID);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene taskViewScene = new Scene(root);
        Stage window = (Stage) ((Node) task.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(taskViewScene);
        window.show();
    }

    public void chat(ActionEvent chatRoom) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/chat.fxml"));
        AnchorPane root = loader.load();
        chat chat = loader.getController();
        chat.initialize(currentUser, currentID);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene chatViewScene = new Scene(root);
        Stage window = (Stage) ((Node) chatRoom.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(chatViewScene);
        window.show();
    }

    public void profile(ActionEvent profile) {
    }

    //ADMIN FEATURES
    public void members(ActionEvent members) {
    }

    public void requests(ActionEvent requests) {
    }

    //NAVIGATION
    public void exit(ActionEvent exit) { //Exit functionality
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
}
