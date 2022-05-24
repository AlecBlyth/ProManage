package GUI_classes;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import object_classes.taskObject;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import static javafx.geometry.Pos.CENTER;

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

    public ScrollPane taskPane;
    public VBox taskVertical;


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

    public void initialize(String userType, int userID) {
        initTime();
        currentUser = userType; //Sets currentUser to userType
        currentID = userID;

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

        getTasks();

    } //Initialise controller

    public void getTasks() {

        int i = 0;
        ArrayList<taskObject> taskArray = new ArrayList();
        taskVertical.getChildren().clear();
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
                taskObject task = new taskObject(id, type, name, desc, hex, prog, section, subject);
                taskArray.add(task);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //BAD CODE BELOW, TRIED TO MAKE THIS DYNAMIC BUT ENDED UP SETTING UP A HARD LIMIT OF 24 TASKS PER PROJECT//

        HBox taskHorizontal = new HBox();
        taskHorizontal.setSpacing(5);
        taskHorizontal.setAlignment(CENTER);
        taskHorizontal.minWidth(Region.USE_COMPUTED_SIZE);
        taskHorizontal.minHeight(Region.USE_PREF_SIZE);
        taskHorizontal.prefWidth(1020);
        taskHorizontal.prefHeight(235);

        HBox taskHorizontal2 = new HBox();
        taskHorizontal2.setSpacing(5);
        taskHorizontal2.setAlignment(CENTER);
        taskHorizontal2.minWidth(Region.USE_COMPUTED_SIZE);
        taskHorizontal2.minHeight(Region.USE_PREF_SIZE);
        taskHorizontal2.prefWidth(1020);
        taskHorizontal2.prefHeight(235);

        HBox taskHorizontal3 = new HBox();
        taskHorizontal3.setSpacing(5);
        taskHorizontal3.setAlignment(CENTER);
        taskHorizontal3.minWidth(Region.USE_COMPUTED_SIZE);
        taskHorizontal3.minHeight(Region.USE_PREF_SIZE);
        taskHorizontal3.prefWidth(1020);
        taskHorizontal3.prefHeight(235);

        HBox taskHorizontal4 = new HBox();
        taskHorizontal4.setSpacing(5);
        taskHorizontal4.setAlignment(CENTER);
        taskHorizontal4.minWidth(Region.USE_COMPUTED_SIZE);
        taskHorizontal4.minHeight(Region.USE_PREF_SIZE);
        taskHorizontal4.prefWidth(1020);
        taskHorizontal4.prefHeight(235);

        HBox taskHorizontal5 = new HBox();
        taskHorizontal5.setSpacing(5);
        taskHorizontal5.setAlignment(CENTER);
        taskHorizontal5.minWidth(Region.USE_COMPUTED_SIZE);
        taskHorizontal5.minHeight(Region.USE_PREF_SIZE);
        taskHorizontal5.prefWidth(1020);
        taskHorizontal5.prefHeight(235);

        HBox taskHorizontal6 = new HBox();
        taskHorizontal6.setSpacing(5);
        taskHorizontal6.setAlignment(CENTER);
        taskHorizontal6.minWidth(Region.USE_COMPUTED_SIZE);
        taskHorizontal6.minHeight(Region.USE_PREF_SIZE);
        taskHorizontal6.prefWidth(1020);
        taskHorizontal6.prefHeight(235);


        for (taskObject taskObject : taskArray){
            i++;
            ToggleButton toggleButton = new ToggleButton();
            toggleButton.setMinHeight(225);
            toggleButton.setMinWidth(248);

            toggleButton.setMaxHeight(225);
            toggleButton.setMaxWidth(248);

            toggleButton.setWrapText(true);
            toggleButton.setTextOverrun(OverrunStyle.CLIP);
            toggleButton.setStyle("-fx-base: " + taskObject.getTaskHex() + ";" + "-fx-background-radius: 0;" + "-fx-font-size: 11.0;" + "-fx-alignment: TOP-LEFT;" + "-fx-focus-color: white;" + "-fx-font-family: Segoe UI; " + "fx-focus-color: white;");
            toggleButton.setText("Task ID: " + taskObject.getTaskID() + "\n" + "Task: " + taskObject.getTaskName() + "\n" + "\n" + "Description:" + "\n" + taskObject.getTaskDesc() + "\n" + "PROGRESS: " + taskObject.getTaskProgress() + "\n" + "SUBJECT: " + taskObject.getTaskSubject());
            toggleButton.setId(String.valueOf(taskObject.getTaskID()));

            toggleButton.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    if (mouseEvent.getClickCount() == 1)
                        System.out.println(toggleButton.getId());
                    //SET UP TASK VIEW / EDITOR.
                }
            });


            if(i <= 4) {
                taskHorizontal.getChildren().addAll(toggleButton);
            } //Was going to use case statements, but you can't have 0-4 ranges with case statements, have to use if statements
            if(i <= 8 && i > 4) {
                taskHorizontal2.getChildren().addAll(toggleButton);
            }
            if(i <= 12  && i > 8) {
                taskHorizontal3.getChildren().addAll(toggleButton);
            }
            if(i <= 16 && i > 12) {
                taskHorizontal4.getChildren().addAll(toggleButton);
            }
            if(i <= 20 && i > 16) {
                taskHorizontal5.getChildren().addAll(toggleButton);
            }
            if(i <= 24 && i > 20) {
                taskHorizontal6.getChildren().addAll(toggleButton);
            } // God this is so messy.
        }
        taskVertical.getChildren().addAll(taskHorizontal,taskHorizontal2,taskHorizontal3,taskHorizontal4,taskHorizontal5,taskHorizontal6); //At least this is clean, sort of.




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

    public void viewTask(ActionEvent view) {
    }

    //ADMIN FEATURES
    public void createTask(ActionEvent create) {
    }

    public void editTask(ActionEvent edit) {
    }

    public void deleteTask(ActionEvent delete) {
        //taskObject selectedItem = lsvTasks.getSelectionModel().getSelectedItem();
        //System.out.println(selectedItem.getTaskID());
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