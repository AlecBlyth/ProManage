package GUI_classes.Admin;

import GUI_classes.menu;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
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

public class taskEditor {
    //FXML Components
    public Label lblDate, lblTime, lblSubject, lblID, lblValidation, lblProg, lblNum;
    public ImageView memberIcon, reqIcon;
    public JFXButton btnMembers, btnRequests, btnLogout, btnKanban, btnTasks, btnChat, btnProfile, exitBtn, btnSave;
    public JFXTextArea txtFieldSubject, txtFieldName, txtFieldDesc;
    public FlowPane flowPaneBtns;
    public JFXComboBox<String> choBoxTypes;
    //Variables
    private double xOffset = 0;
    private double yOffset = 0;

    //Passed Variables
    private String currentUser;
    private int currentID;

    //Local Variables
    public int localTaskID;
    boolean checked = true;
    boolean subCheck = false;
    boolean createEnabled = false;

    Random rnd = new Random();
    int uniqueID = rnd.nextInt(9999);

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

    public void initialize(String userType, int userID, int taskID, boolean createCheck) throws IOException, ParseException {

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

        if ("ADMIN".equals(userType)) {
            btnMembers.setVisible(true);
            btnRequests.setVisible(true);
            memberIcon.setVisible(true);
            reqIcon.setVisible(true);
            btnMembers.setDisable(false);
            btnRequests.setDisable(false);
            txtFieldDesc.setEditable(true);
            txtFieldSubject.setEditable(true);
            txtFieldName.setEditable(true);
            choBoxTypes.setDisable(false);

        } else {
            btnMembers.setVisible(false);
            btnRequests.setVisible(false);
            memberIcon.setVisible(false);
            reqIcon.setVisible(false);
            btnMembers.setDisable(true);
            btnRequests.setDisable(true);
            flowPaneBtns.getChildren().remove(btnSave);
            lblID.setVisible(false);
            lblNum.setVisible(false);
            choBoxTypes.setDisable(true);
        }


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
        getTask(taskID, createCheck);
        createEnabled = createCheck;

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

    public void getTask(int taskID, boolean createCheck) {
        localTaskID = taskID;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
            Statement statement = connection.createStatement();
            String queryString = "SELECT taskid, tasktype, taskname, taskdesc, taskprogress, tasksubject FROM tasks"; //gets task data from database
            ResultSet resultSet = statement.executeQuery(queryString);

            while (resultSet.next()) {

                int id = resultSet.getInt("taskid");
                String name = resultSet.getString("taskname");
                String type = resultSet.getString("tasktype");
                String desc = resultSet.getString("taskdesc");
                int prog = resultSet.getInt("taskprogress");
                String subject = resultSet.getString("tasksubject");
                if (taskID == id) {
                    lblNum.setText(Integer.toString(id));
                    txtFieldName.setText(name);
                    choBoxTypes.setValue(type);
                    txtFieldDesc.setText(desc);
                    lblProg.setText(prog + "%");
                    txtFieldSubject.setText(subject);
                }

                if (taskID == 0 && !createCheck) {
                    lblValidation.setVisible(true);
                    lblValidation.setText("ERROR, TASK NOT FOUND! Report to Admin if issue persists");
                    lblValidation.setTextFill(Color.RED);
                    txtFieldName.setText("ERROR CANNOT RECEIVE TASK NAME!");
                    txtFieldName.setStyle("-fx-text-inner-color: red;");
                    txtFieldDesc.setText("ERROR CANNOT RECEIVE TASK DESCRIPTION!");
                    txtFieldDesc.setStyle("-fx-text-inner-color: red;");
                    txtFieldSubject.setText("ERROR CANNOT RECEIVE TASK SUBJECT!");
                    txtFieldSubject.setStyle("-fx-text-inner-color: red;");
                }
                if (createCheck) {
                    txtFieldName.setText("");
                    txtFieldDesc.setText("");
                    txtFieldSubject.setText("");
                    lblProg.setText("0%");
                    lblNum.setText(String.valueOf(uniqueID));
                }
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

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
        GUI_classes.tasks tasks = loader.getController();
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

    public void profile(ActionEvent profile) {
    }

    //ADMIN FEATURES

    public void saveTask() {

        if (localTaskID == 0 && !createEnabled) {
            lblValidation.setText("No ID Generated!, Something is broken!");
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

        if (!subCheck && createEnabled && checked) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
                String insertQuery = "INSERT INTO tasks (taskid, tasktype, taskname, taskdesc, taskhex, taskprogress, section)" + "values(?,?,?,?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(insertQuery);
                ps.setInt(1, uniqueID);
                ps.setString(2, choBoxTypes.getValue());
                ps.setString(3, txtFieldName.getText());
                ps.setString(4, txtFieldDesc.getText());
                ps.setString(5, "#123d82");
                ps.setInt(6, 0);
                ps.setInt(7, 1);
                ps.execute();
                lblValidation.setText("Task created!");
                uniqueID = rnd.nextInt(9999);
                lblNum.setText(String.valueOf(uniqueID));
                lblValidation.setTextFill(Color.GREEN);
                lblValidation.setVisible(true); //Displays label
                if (lblValidation.isVisible()) { //Plays fade out animation
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblValidation);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.play();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                if (e instanceof SQLIntegrityConstraintViolationException) {
                    lblValidation.setText("Duplicate ID!");
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

        if (subCheck && createEnabled && checked) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
                String insertQuery = "INSERT INTO tasks (taskid, tasktype, taskname, taskdesc, taskhex, taskprogress, section, tasksubject)" + "values(?,?,?,?,?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(insertQuery);
                ps.setInt(1, uniqueID);
                ps.setString(2, choBoxTypes.getValue());
                ps.setString(3, txtFieldName.getText());
                ps.setString(4, txtFieldDesc.getText());
                ps.setString(5, "#123d82");
                ps.setInt(6, 0);
                ps.setInt(7, 1);
                ps.setString(8, "");
                ps.execute();
                lblValidation.setText("Task created!");
                uniqueID = rnd.nextInt(9999);
                lblNum.setText(String.valueOf(uniqueID));
                lblValidation.setTextFill(Color.GREEN);
                lblValidation.setVisible(true); //Displays label
                if (lblValidation.isVisible()) { //Plays fade out animation
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblValidation);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.play();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                if (e instanceof SQLIntegrityConstraintViolationException) {
                    lblValidation.setText("Duplicate ID!");
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


        if (subCheck && checked && !createEnabled) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
                Statement statement = connection.createStatement();
                String queryString = "SELECT taskid, tasktype, taskname, taskdesc, tasksubject FROM tasks"; //gets task data from database
                String updateQuery = "UPDATE tasks SET taskname=?, tasktype=?, taskdesc=?, tasksubject=? WHERE taskid=?";
                PreparedStatement ps = connection.prepareStatement(updateQuery);
                ResultSet resultSet = statement.executeQuery(queryString);
                while (resultSet.next()) {
                    int id = resultSet.getInt("taskid");
                    if (localTaskID == id) {
                        ps.setString(1, txtFieldName.getText());
                        ps.setString(2, choBoxTypes.getValue());
                        ps.setString(3, txtFieldDesc.getText());
                        ps.setString(4, txtFieldSubject.getText());
                        ps.setInt(5, id);
                        ps.executeUpdate();
                        connection.close();
                        lblValidation.setText("Task saved!");
                        lblValidation.setTextFill(Color.GREEN);
                        lblValidation.setVisible(true); //Displays label
                        if (lblValidation.isVisible()) { //Plays fade out animation
                            FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblValidation);
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
        if (!subCheck && checked && !createEnabled) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
                Statement statement = connection.createStatement();
                String queryString = "SELECT taskid, tasktype, taskname, taskdesc FROM tasks"; //gets task data from database
                String updateQuery = "UPDATE tasks SET taskname=?, tasktype=?, taskdesc=? WHERE taskid=?";
                PreparedStatement ps = connection.prepareStatement(updateQuery);
                ResultSet resultSet = statement.executeQuery(queryString);
                while (resultSet.next()) {
                    int id = resultSet.getInt("taskid");
                    if (localTaskID == id) {
                        ps.setString(1, txtFieldName.getText());
                        ps.setString(2, choBoxTypes.getValue());
                        ps.setString(3, txtFieldDesc.getText());
                        ps.setInt(4, id);
                        ps.executeUpdate();
                        lblValidation.setText("Task saved!");
                        lblValidation.setTextFill(Color.GREEN);
                        lblValidation.setVisible(true); //Displays label
                        if (lblValidation.isVisible()) { //Plays fade out animation
                            FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblValidation);
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
    }

    public void members(ActionEvent members) {
    }

    public void requests(ActionEvent requests) {
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

    public void goBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/tasks.fxml"));
        AnchorPane root = loader.load();
        GUI_classes.tasks tasks = loader.getController();
        tasks.initialize(currentUser, currentID);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene taskViewScene = new Scene(root);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(taskViewScene);
        window.show();
    }
}
