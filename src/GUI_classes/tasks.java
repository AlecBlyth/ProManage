package GUI_classes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import object_classes.taskObject;
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

public class tasks {
    //FXML Components
    public Label lblDate;
    public Label lblTime;
    public ImageView memberIcon;
    public ImageView reqIcon;
    public JFXButton btnMembers;
    public JFXButton btnRequests;
    public JFXButton btnCreateTask;
    public JFXButton btnEditTask;
    public JFXButton btnDeleteTask;
    public JFXListView<taskObject> lsvTasks;

    //Variables
    private double xOffset = 0;
    private double yOffset = 0;

    //Passed Variables
    private String currentUser;
    private String currentUsername;

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

    public void initialize(String userType, String userName) {
        initTime();
        currentUser = userType; //Sets currentUser to userType
        currentUsername = userName;

        if ("ADMIN".equals(userType)) {
            btnMembers.setVisible(true);
            btnRequests.setVisible(true);
            memberIcon.setVisible(true);
            reqIcon.setVisible(true);
            btnCreateTask.setVisible(true);
            btnEditTask.setVisible(true);
            btnDeleteTask.setVisible(true);
            btnCreateTask.setDisable(false);
            btnEditTask.setDisable(false);
            btnDeleteTask.setDisable(false);
            btnMembers.setDisable(false);
            btnRequests.setDisable(false);
        } else {
            btnMembers.setVisible(false);
            btnRequests.setVisible(false);
            memberIcon.setVisible(false);
            reqIcon.setVisible(false);
            btnCreateTask.setVisible(false);
            btnEditTask.setVisible(false);
            btnDeleteTask.setVisible(false);
            btnMembers.setDisable(true);
            btnRequests.setDisable(true);
            btnCreateTask.setDisable(true);
            btnEditTask.setDisable(true);
            btnDeleteTask.setDisable(true);
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
            Statement statement = connection.createStatement();
            String queryString = "SELECT taskid, tasktype, taskname, taskdesc, taskhex, taskprogress, section, tasksubject FROM tasks"; //gets task data from database
            ResultSet resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                int id = resultSet.getInt("taskid");
                String type = resultSet.getString("tasktype");
                String name = resultSet.getString("taskname");
                String desc = resultSet.getString("taskdesc");
                String hex = resultSet.getString("taskhex");
                int prog = resultSet.getInt("taskprogress");
                int section = resultSet.getInt("section");
                String subject = resultSet.getString("tasksubject");

                taskObject taskObject = new taskObject(id, type, name, desc, hex, prog, section, subject);
                lsvTasks.getItems().addAll(taskObject);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        JSONParser parser = new JSONParser(); //Used to read from JSON file
        try {
            Object obj = parser.parse(new FileReader("src/Datafiles/logs/ProjectFile.json"));
            JSONObject jsonObject = (JSONObject) obj;
            boolean subCheck = (Boolean) jsonObject.get("projectSubjects");
            lsvTasks.setCellFactory(param -> new ListCell<taskObject>() {
                @Override
                public void updateItem(taskObject task, boolean empty) {
                    super.updateItem(task, empty);
                    if (!subCheck) {
                        if (empty || task == null) {
                            setText(null);
                        } else {
                            setText("Task ID: " + task.getTaskID() + " | Task: " + task.getTaskName() + " | Description: " + task.getTaskDesc() + " | Progress: " + task.getTaskProgress());
                        }
                    } else {
                        if (empty || task == null) {
                            setText(null);
                        } else {
                            setText("Task ID: " + task.getTaskID() + " | Task: " + task.getTaskName() + " | Description: " + task.getTaskDesc() + " | Progress: " + task.getTaskProgress() + " | Subject:  " + task.getTaskSubject());
                        }
                    }
                }
            });
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

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
        kanbanScene.initialize(currentUser, currentUsername);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene menuViewScene = new Scene(root);
        Stage window = (Stage) ((Node) kanban.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(menuViewScene);
        window.show();
    }

    public void tasks(ActionEvent tasks) {
        //DO NOTHING
    }

    public void chat(ActionEvent chatRoom) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/chat.fxml"));
        AnchorPane root = loader.load();
        chat chat = loader.getController();
        chat.initialize(currentUser, currentUsername);
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

    public void viewTask(ActionEvent view) {
    }

    //ADMIN FEATURES
    public void createTask(ActionEvent create) {
    }

    public void editTask(ActionEvent edit) {
    }

    public void deleteTask(ActionEvent delete) {
        taskObject selectedItem = lsvTasks.getSelectionModel().getSelectedItem();
        System.out.println(selectedItem.getTaskID());
    }

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
        menu.initialize(currentUser, currentUsername);
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