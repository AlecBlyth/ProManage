package GUI_classes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import bluebub.Bubble;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class chat {

    //FXML Components
    public Label lblDate;
    public Label lblTime;
    public ImageView memberIcon;
    public ImageView reqIcon;
    public JFXButton btnMembers;
    public JFXButton btnRequests;
    public JFXTextArea txtMessage;
    public JFXButton btnSend;
    public VBox chatLine; //Replaces text field, used for modernisation.
    public ScrollPane chatPane;

    //Variables
    private double xOffset = 0;
    private double yOffset = 0;

    //Threads
    Thread messageThread; //Used for updating UI
    boolean isRunning; //Used for threading

    //Passed Variables
    private String currentUser;
    private String currentUsername;
    private String currentEmail;
    private int currentID;

    //SYSTEM METHODS

    final KeyCombination KeyCodeCombination =new KeyCodeCombination(KeyCode.ENTER,KeyCombination.CONTROL_DOWN); //Used for key combo input

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

        chatPane.vvalueProperty().bind(chatLine.heightProperty()); //Auto scrolls to bottom of scroll pane by by binding to vertical box's height value.

        initTime();
        currentUser = userType; //Sets currentUser to userType
        currentID = id;
        isRunning = true;

        if ("ADMIN".equals(userType)) {
            btnMembers.setVisible(true);
            btnRequests.setVisible(true);
            memberIcon.setVisible(true);
            reqIcon.setVisible(true);
            btnMembers.setDisable(false);
            btnRequests.setDisable(false);
        } else {
            btnMembers.setVisible(false);
            btnRequests.setVisible(false);
            memberIcon.setVisible(false);
            reqIcon.setVisible(false);
            btnMembers.setDisable(true);
            btnRequests.setDisable(true);
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to local mySQL server
            Statement statement = connection.createStatement(); //Creates a statement
            String queryString = "SELECT email, firstname, surname, userID FROM userdata"; //gets user details from database
            ResultSet resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                int idCheck = resultSet.getInt("userID");
                if (currentID == idCheck) {
                    currentEmail = resultSet.getString("email");
                    currentUsername = resultSet.getString("firstname") + " " + resultSet.getString("surname"); //Gets name from database and generates a username.
                }
            }
        } catch (SQLException throwables) {
        throwables.printStackTrace();

    }
        messageThread = new Thread(this::messageThread);
        messageThread.start();
    } //Initialise controller

    private void messageThread(){

        while(isRunning) {
            Platform.runLater(this::getMessages);
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    } //Updates chat room for any changes, allows for instant messaging, which without you will need to refresh manually

    public void getMessages() {

        chatLine.getChildren().clear(); //Clears chatbox to avoid looping duplicate messages

            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
                Statement statement = connection.createStatement();
                String queryString = "SELECT username, email, message, time FROM teamchatlog"; //gets message data from database
                ResultSet resultSet = statement.executeQuery(queryString);
                while (resultSet.next()) {
                    String email = resultSet.getString("email");
                    String username = resultSet.getString("username");
                    String message = resultSet.getString("message");
                    String time = resultSet.getString("time");

                    StringBuilder sb = new StringBuilder(message); //In order to fit into message bubbles, message is spilt after 70 characters and given a space.
                    int x = 0;
                    while (x + 70 < sb.length() && (x = sb.indexOf(" ", x + 70)) != 1){
                        sb.replace(x, x + 1, "\n"); //Adds space to string when needed.
                    }
                    message = sb.toString(); //Converts sb string to message

                    HBox hBox = new HBox(12); //Needed for chat bubbles
                    HBox hBox2 = new HBox(12);

                    if (email.equals(currentEmail)) {
                        Bubble b1 = new Bubble(message, username + " : " + time); //Current user bubble
                        b1.setBubbleColor(Color.rgb(33, 150, 243));
                        hBox.setAlignment(Pos.CENTER_RIGHT); //Aligns bubble to the right if the message belongs to current user.
                        hBox.setPadding(new Insets(6.5));
                        hBox.setSpacing(90);
                        hBox.getChildren().addAll(b1);
                    } else {
                        Bubble b2 = new Bubble(message, username + " : " + time); //Other user's bubble
                        hBox2.setAlignment(Pos.CENTER_LEFT); //Aligns bubble to left if the message isn't from the current user.
                        b2.setBubbleColor(Color.rgb(4, 44, 76));
                        hBox2.setPadding(new Insets(6.5));
                        hBox2.setSpacing(130);
                        hBox2.getChildren().addAll(b2);
                    }
                    chatLine.getChildren().addAll(hBox, hBox2); //Adds generated bubbles to vbox
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
    } //Gets chat messages from server and populates GUI with chat bubbles.

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

    public void chat(ActionEvent chat) {
        //DO NOTHING
    }

    public void profile(ActionEvent profile) {
        //TODO
    }

    public void sendMessage(ActionEvent sendMessage) {

        chatPane.vvalueProperty().bind(chatLine.heightProperty()); //Auto scrolls to bottom of scroll pane by by binding to vertical box's height value.

        DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm");
        String time = LocalTime.now().format(SHORT_TIME_FORMATTER);

        while (!txtMessage.getText().isEmpty() ){

            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
                String queryString = "insert into teamchatlog (username, email, message, time)" + " VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(queryString);
                preparedStatement.setString(1, currentUsername);
                preparedStatement.setString(2, currentEmail);
                preparedStatement.setString(3, txtMessage.getText());
                preparedStatement.setString(4, time);

                preparedStatement.executeUpdate();
                connection.close();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            txtMessage.clear();
        }
    } //Sends message to server

    public void CTRLEnter(KeyEvent keyEvent) {
        if(KeyCodeCombination.match(keyEvent)){
            btnSend.fire();
        }
    } //Allows user to send message via CTRL + Enter

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

    public void menu(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/menu.fxml"));
        AnchorPane root = loader.load();
        menu menu = loader.getController();
        menu.initialize(currentUser, currentID);
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
