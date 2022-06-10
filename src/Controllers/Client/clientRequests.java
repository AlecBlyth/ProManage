package Controllers.Client;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@SuppressWarnings({"unchecked"})

public class clientRequests {

    //FXML components
    public JFXButton exitBtn, btnLogout, btnProgress, btnChat, btnProfile, btnRequest, btnSendRequest;
    public JFXTextArea txtFieldDesc, txtFieldSubject, txtFieldName;
    public JFXComboBox<String> choBoxTypes;
    public Label lblDate, lblTime, lblSubject, lblValidation;

    //Scene Variables
    private double xOffset = 0;
    private double yOffset = 0;

    //Passed Variables
    private String currentUser;
    private int currentID;

    //Local Variables
    boolean checked = true;
    boolean subCheck = false;

    Random rnd = new Random();
    int uniqueID = rnd.nextInt(9999);

    //CONTROLLER METHODS
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

    public void initialize(String userType, int id) throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("src/Datafiles/logs/ProjectFile.json"));
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray types = (JSONArray) jsonObject.get("projectTasks");
        choBoxTypes.getItems().addAll(types);
        subCheck = (Boolean) jsonObject.get("projectSubjects");

        if (!subCheck) {
            txtFieldSubject.setVisible(false);
            lblSubject.setVisible(false);
        }

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

        currentUser = userType;
        currentID = id;

        initTime();
    }

    public void sendRequest() {

        if (txtFieldDesc.getText().length() > 260) {
            lblValidation.setText("Description is too long, please limit to 270 characters");
            lblValidation.setTextFill(Color.RED);
            lblValidation.setVisible(true); //Displays label
            checked = false;
            if (lblValidation.isVisible()) { //Plays fade out animation
                FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblValidation);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.play();
            }
        }

        if (subCheck) {
            if (txtFieldSubject.getText().length() > 26) {
                lblValidation.setText("Subject Name is too long, please limit to 26 characters, if possible shorten name to 'Mr. Example Sr'");
                lblValidation.setTextFill(Color.RED);
                lblValidation.setVisible(true); //Displays label
                checked = false;
                if (lblValidation.isVisible()) { //Plays fade out animation
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblValidation);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.play();
                }
            }
        }
        if (txtFieldName.getText().length() > 40) {
            lblValidation.setText("Task name is too long, please limit to 40 characters");
            lblValidation.setTextFill(Color.RED);
            lblValidation.setVisible(true); //Displays label
            checked = false;
            if (lblValidation.isVisible()) { //Plays fade out animation
                FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblValidation);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.play();
            }
        }

        if (!subCheck) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
                String insertQuery = "INSERT INTO requests (taskid, tasktype, taskname, taskdesc, taskhex, taskprogress, section)" + "values(?,?,?,?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(insertQuery);
                ps.setInt(1, uniqueID);
                ps.setString(2, choBoxTypes.getValue());
                ps.setString(3, txtFieldName.getText());
                ps.setString(4, txtFieldDesc.getText());
                ps.setString(5, "#123d82");
                ps.setInt(6, 0);
                ps.setInt(7, 1);
                ps.execute();
                lblValidation.setText("Request Sent!");
                uniqueID = rnd.nextInt(9999);
                lblValidation.setTextFill(Color.GREEN);
                lblValidation.setVisible(true); //Displays label
                if (lblValidation.isVisible()) { //Plays fade out animation
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblValidation);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.play();
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                if (e instanceof SQLIntegrityConstraintViolationException) {
                    lblValidation.setText("Error, please try submitting again!");
                    lblValidation.setTextFill(Color.RED);
                    lblValidation.setVisible(true); //Displays label
                    if (lblValidation.isVisible()) { //Plays fade out animation
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblValidation);
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);
                        fadeOut.play();
                    }
                }
            }
        }
        if (subCheck) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
                String insertQuery = "INSERT INTO requests (taskid, tasktype, taskname, taskdesc, taskhex, taskprogress, section, tasksubject)" + "values(?,?,?,?,?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(insertQuery);
                ps.setInt(1, uniqueID);
                ps.setString(2, choBoxTypes.getValue());
                ps.setString(3, txtFieldName.getText());
                ps.setString(4, txtFieldDesc.getText());
                ps.setString(5, "#123d82");
                ps.setInt(6, 0);
                ps.setInt(7, 1);
                ps.setString(8, txtFieldSubject.getText());
                ps.execute();
                lblValidation.setText("Request Sent!");
                uniqueID = rnd.nextInt(9999);
                lblValidation.setTextFill(Color.GREEN);
                lblValidation.setVisible(true); //Displays label
                if (lblValidation.isVisible()) { //Plays fade out animation
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblValidation);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.play();
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                if (e instanceof SQLIntegrityConstraintViolationException) {
                    lblValidation.setText("Error, please try submitting again!");
                    lblValidation.setTextFill(Color.RED);
                    lblValidation.setVisible(true); //Displays label
                    if (lblValidation.isVisible()) { //Plays fade out animation
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblValidation);
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);
                        fadeOut.play();
                    }
                }
            }
        }
    }

    //CLIENT NAVIGATION METHODS
    public void progress(ActionEvent progress) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/Client/clientProgress.fxml"));
        AnchorPane root = loader.load();
        clientProgressOverall clientProgress = loader.getController();
        clientProgress.initialize(currentUser, currentID);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene progressViewScene = new Scene(root);
        Stage window = (Stage) ((Node) progress.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(progressViewScene);
        window.show();
    }

    public void chat(ActionEvent chatRoom) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/Client/clientChat.fxml"));
        AnchorPane root = loader.load();
        clientChat clientChat = loader.getController();
        clientChat.initialize(currentUser, currentID);
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

    public void profile(ActionEvent profile) throws IOException, ParseException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/profile.fxml"));
        AnchorPane root = loader.load();
        Controllers.profile controller = loader.getController();
        controller.initialize(currentUser, currentID, false, currentID);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene menuViewScene = new Scene(root);
        Stage window = (Stage) ((Node) profile.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(menuViewScene);
        window.show();
    }

    //NAVIGATION
    public void menu(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/Client/clientMenu.fxml"));
        AnchorPane root = loader.load();
        Controllers.Client.clientMenu temp = loader.getController();
        temp.initialize(currentUser, currentID);
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

    public void exit() {
        System.exit(0);
    }

}
