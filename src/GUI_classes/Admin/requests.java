package GUI_classes.Admin;

import GUI_classes.Client.clientChat;
import GUI_classes.menu;
import GUI_classes.tasks;
import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static javafx.geometry.Pos.CENTER;

public class requests {
    //FXML Components
    public Label lblDate, lblTime, lblInfo;
    public ImageView reqIcon, memberIcon;
    public JFXButton btnMembers, btnRequests, btnLogout, btnKanban, btnTasks, btnChat, btnProfile, exitBtn, btnDecline, btnApprove, btnClientChat;
    public ToggleGroup toggleGroup;
    public VBox taskVertical;
    public ScrollPane taskPane;

    //Variables
    private double xOffset = 0;
    private double yOffset = 0;

    //Passed Variables
    private String currentUser;
    private int currentID;

    //Local Variables
    Random rnd = new Random();
    int uniqueID = rnd.nextInt(9999);
    int requestID;
    int id;
    String tasktype;
    String requestName;
    String taskdesc;
    String tasksubject;


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

    public void initialize(String userType, int userID) {

        exitBtn.setOnMouseEntered(e -> exitBtn.setStyle("-fx-background-color: RED; -fx-background-radius: 0;"));
        exitBtn.setOnMouseExited(e -> exitBtn.setStyle("-fx-background-color: ; -fx-background-radius: 0;"));
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle("-fx-background-color: RED; -fx-background-radius: 0;"));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle("-fx-background-color: #262626; -fx-background-radius: 0;"));
        btnProfile.setOnMouseEntered(e -> btnProfile.setStyle("-fx-background-color: #4287ff; -fx-background-radius: 0;"));
        btnProfile.setOnMouseExited(e -> btnProfile.setStyle("-fx-background-color: #2d7aff; -fx-background-radius: 0;"));
        btnMembers.setOnMouseEntered(e -> btnMembers.setStyle("-fx-background-color: #4287ff; -fx-background-radius: 0;"));
        btnMembers.setOnMouseExited(e -> btnMembers.setStyle("-fx-background-color: #2d7aff; -fx-background-radius: 0;"));
        btnRequests.setOnMouseEntered(e -> btnRequests.setStyle("-fx-background-color: #4287ff; -fx-background-radius: 0;"));
        btnRequests.setOnMouseExited(e -> btnRequests.setStyle("-fx-background-color: #2d7aff; -fx-background-radius: 0;"));
        btnKanban.setOnMouseEntered(e -> btnKanban.setStyle("-fx-background-color: #4287ff; -fx-background-radius: 0;"));
        btnKanban.setOnMouseExited(e -> btnKanban.setStyle("-fx-background-color: #2d7aff; -fx-background-radius: 0;"));
        btnTasks.setOnMouseEntered(e -> btnTasks.setStyle("-fx-background-color: #4287ff; -fx-background-radius: 0;"));
        btnTasks.setOnMouseExited(e -> btnTasks.setStyle("-fx-background-color: #2d7aff; -fx-background-radius: 0;"));
        btnChat.setOnMouseEntered(e -> btnChat.setStyle("-fx-background-color: #4287ff; -fx-background-radius: 0;"));
        btnChat.setOnMouseExited(e -> btnChat.setStyle("-fx-background-color: #2d7aff; -fx-background-radius: 0;"));

        initTime();
        currentUser = userType; //Sets currentUser to userType
        currentID = userID;

        btnApprove.setDisable(true);
        btnDecline.setDisable(true);

        getRequests();

    } //Initialise controller

    public void getRequests() {
        int x = 0;
        ArrayList<HBox> hBoxArrayList = new ArrayList<>();
        CopyOnWriteArrayList<ToggleButton> buttonArray = new CopyOnWriteArrayList<>();
        taskVertical.getChildren().clear();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
            Statement statement = connection.createStatement();
            String queryString = "SELECT taskid, tasktype, taskname, taskdesc, taskhex, taskprogress, tasksubject FROM requests"; //gets task data from database
            ResultSet resultSet = statement.executeQuery(queryString);

            while (resultSet.next()) {
                x++;
                int id = resultSet.getInt("taskid");
                String name = resultSet.getString("taskname");
                String type = resultSet.getString("tasktype");
                String desc = resultSet.getString("taskdesc");
                String hex = resultSet.getString("taskhex");
                int prog = resultSet.getInt("taskprogress");
                String subject = resultSet.getString("tasksubject");
                ToggleButton toggleButton = new ToggleButton();
                toggleButton.setMinHeight(225);
                toggleButton.setMinWidth(248);
                toggleButton.setMaxHeight(225);
                toggleButton.setMaxWidth(248);
                toggleButton.setWrapText(true);
                toggleButton.setTextOverrun(OverrunStyle.CLIP);

                toggleButton.setStyle("-fx-base: " + hex + ";" + "-fx-background-radius: 0;" + "-fx-font-size: 10.5;" + "-fx-alignment: TOP-LEFT;" + "-fx-focus-color: white;" + "-fx-font-family: Segoe UI; " + "fx-focus-color: white;");
                if (subject != null) {
                    toggleButton.setText("Task: " + name + "\n" + type + "\n" + "\n" + "Description:" + "\n" + desc + "\n" + "PROGRESS: " + prog + "\n" + "SUBJECT: " + subject);
                } else {
                    toggleButton.setText("Task: " + name + "\n" + type + "\n" + "\n" + "Description:" + "\n" + desc + "\n" + "PROGRESS: " + prog);
                }

                toggleButton.setId(String.valueOf(id));
                toggleButton.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                        if (mouseEvent.getClickCount() == 1)
                            requestID = id; //Sets ID to currently clicked task's ID
                        btnApprove.setDisable(false); //Added better validation
                        btnDecline.setDisable(false);
                    }
                });

                toggleButton.setToggleGroup(toggleGroup); //Puts toggle button into toggle group to unselect/select correct tasks.
                buttonArray.add(toggleButton); //Add toggle button to array

                if (buttonArray.size() == 4) {
                    HBox taskHorizontal = new HBox();
                    taskHorizontal.setSpacing(5);
                    taskHorizontal.setAlignment(CENTER);
                    taskHorizontal.minWidth(Region.USE_COMPUTED_SIZE);
                    taskHorizontal.minHeight(Region.USE_PREF_SIZE);
                    taskHorizontal.prefWidth(1020);
                    taskHorizontal.prefHeight(235);
                    taskHorizontal.getChildren().addAll(buttonArray);
                    buttonArray.clear();
                    hBoxArrayList.add(taskHorizontal);
                }
                if (buttonArray.size() < 4 && x > buttonArray.size()) {
                    HBox taskHorizontal = new HBox();
                    taskHorizontal.setSpacing(5);
                    taskHorizontal.setAlignment(CENTER);
                    taskHorizontal.minWidth(Region.USE_COMPUTED_SIZE);
                    taskHorizontal.minHeight(Region.USE_PREF_SIZE);
                    taskHorizontal.prefWidth(1020);
                    taskHorizontal.prefHeight(235);
                    taskHorizontal.getChildren().addAll(buttonArray);
                    hBoxArrayList.add(taskHorizontal);
                }
                if (buttonArray.size() < 4 && x < 4) {
                    HBox taskHorizontal = new HBox();
                    taskHorizontal.setSpacing(5);
                    taskHorizontal.setAlignment(CENTER);
                    taskHorizontal.minWidth(Region.USE_COMPUTED_SIZE);
                    taskHorizontal.minHeight(Region.USE_PREF_SIZE);
                    taskHorizontal.prefWidth(1020);
                    taskHorizontal.prefHeight(235);
                    taskHorizontal.getChildren().addAll(buttonArray);
                    hBoxArrayList.add(taskHorizontal);
                }
            }
            taskVertical.getChildren().addAll(hBoxArrayList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void approveRequest() {

        if (requestID == 0) {
            lblInfo.setText("Select a Request to Approve!");
            lblInfo.setVisible(true); //Displays label
            if (lblInfo.isVisible()) { //Plays fade out animation
                FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblInfo);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.play();
            }
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
            Statement statement = connection.createStatement();
            String queryString = "SELECT taskid, taskname, tasktype, taskdesc, tasksubject FROM requests"; //gets task data from database
            String updateQuery = "DELETE from requests WHERE taskid=?";
            PreparedStatement ps = connection.prepareStatement(updateQuery);
            ResultSet resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                System.out.println(id);
                if (requestID == resultSet.getInt("taskid")) {
                    id = resultSet.getInt("taskid");
                    requestName = resultSet.getString("taskname");
                    tasktype = resultSet.getString("tasktype");
                    tasksubject = resultSet.getString("tasksubject");
                    taskdesc = resultSet.getString("taskdesc");
                    ps.setInt(1, id);
                    sendMessage(requestName, true);
                    ps.executeUpdate();
                    getRequests();
                    lblInfo.setText("Request Approved!");
                    lblInfo.setVisible(true); //Displays label
                    if (lblInfo.isVisible()) { //Plays fade out animation
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblInfo);
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);
                        fadeOut.play();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            System.out.println(tasktype);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
            String insertQuery = "INSERT INTO tasks (taskid, tasktype, taskname, taskdesc, taskhex, taskprogress, section)" + "values(?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(insertQuery);
            ps.setInt(1, uniqueID);
            ps.setString(2, tasktype);
            ps.setString(3, requestName);
            ps.setString(4, taskdesc);
            ps.setString(5, "#123d82");
            ps.setInt(6, 0);
            ps.setInt(7, 1);
            ps.execute();
            uniqueID = rnd.nextInt(9999);
        } catch (SQLException e) {
            e.printStackTrace();
            if (e instanceof SQLIntegrityConstraintViolationException) {
                lblInfo.setText("Duplicate ID, try again!");
                lblInfo.setTextFill(Color.RED);
                lblInfo.setVisible(true); //Displays label
                if (lblInfo.isVisible()) { //Plays fade out animation
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblInfo);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.play();
                }
            }
        }
    }

    public void declineRequest() {
        if (requestID == 0) {
            lblInfo.setText("Select a Request to Decline!");
            lblInfo.setVisible(true); //Displays label
            if (lblInfo.isVisible()) { //Plays fade out animation
                FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblInfo);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.play();
            }
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
            Statement statement = connection.createStatement();
            String queryString = "SELECT taskid, taskname FROM requests"; //gets task data from database
            String updateQuery = "DELETE from requests WHERE taskid=?";
            PreparedStatement ps = connection.prepareStatement(updateQuery);
            ResultSet resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                int id = resultSet.getInt("taskid");
                String requestName = resultSet.getString("taskname");
                if (requestID == id) {
                    ps.setInt(1, id);
                    sendMessage(requestName, false);
                    ps.executeUpdate();
                    getRequests();
                    lblInfo.setText("Request Denied!");
                    lblInfo.setVisible(true); //Displays label
                    if (lblInfo.isVisible()) { //Plays fade out animation
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblInfo);
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);
                        fadeOut.play();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void sendMessage(String taskname, boolean approved) {

        String message;
        String bool;

        if (approved) {
            message = "HAS BEEN APPROVED";
            bool = "TRUE";
        } else {
            message = "HAS BEEN DECLINED";
            bool = "FALSE";
        }

        DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm");
        String time = LocalTime.now().format(SHORT_TIME_FORMATTER);
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
            String queryString = "insert into clientchatlog (username, email, message, create_time)" + " VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, "Automated Message");
            preparedStatement.setString(2, bool);
            preparedStatement.setString(3, taskname + " " + message);
            preparedStatement.setString(4, time);
            preparedStatement.executeUpdate();
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void chatClient(ActionEvent chatRoom) throws IOException {
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
        GUI_classes.chat chat = loader.getController();
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

    public void profile(ActionEvent profile) throws IOException, ParseException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/profile.fxml"));
        AnchorPane root = loader.load();
        GUI_classes.profile controller = loader.getController();
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

    //ADMIN FEATURES
    public void members(ActionEvent members) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/Admin/users.fxml"));
        AnchorPane root = loader.load();
        users users = loader.getController();
        users.initialize(currentUser, currentID);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene chatViewScene = new Scene(root);
        Stage window = (Stage) ((Node) members.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(chatViewScene);
        window.show();
    }

    //NAVIGATION
    public void exit() { //Exit functionality
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
