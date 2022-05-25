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
import java.util.concurrent.CopyOnWriteArrayList;

import static javafx.geometry.Pos.CENTER;

public class tasks {
    //FXML Components
    public Label lblDate, lblTime;
    public ImageView memberIcon, reqIcon;
    public ScrollPane taskPane;
    public VBox taskVertical;
    public JFXButton btnMembers, btnRequests, btnLogout, btnKanban, btnTasks, btnChat, btnProfile, exitBtn, btnDeleteTask, btnEditTask, btnCreateTask;
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
        } //User validation

        getTasks();

    } //Initialise controller

    public void getTasks() {
        int x = 0;
        ArrayList<HBox> hBoxArrayList = new ArrayList<>();
        CopyOnWriteArrayList<ToggleButton> buttonArray = new CopyOnWriteArrayList<>();
        ArrayList<taskObject> taskArray = new ArrayList();
        taskVertical.getChildren().clear();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
            Statement statement = connection.createStatement();
            String queryString = "SELECT taskid, tasktype, taskname, taskdesc, taskhex, taskprogress, section, tasksubject FROM tasks"; //gets task data from database
            ResultSet resultSet = statement.executeQuery(queryString);

            while (resultSet.next()) {
                x++;

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

                ToggleButton toggleButton = new ToggleButton();
                toggleButton.setMinHeight(225);
                toggleButton.setMinWidth(248);
                toggleButton.setMaxHeight(225);
                toggleButton.setMaxWidth(248);
                toggleButton.setWrapText(true);
                toggleButton.setTextOverrun(OverrunStyle.CLIP);
                toggleButton.setStyle("-fx-base: " + hex + ";" + "-fx-background-radius: 0;" + "-fx-font-size: 11.0;" + "-fx-alignment: TOP-LEFT;" + "-fx-focus-color: white;" + "-fx-font-family: Segoe UI; " + "fx-focus-color: white;");
                toggleButton.setText("Task ID: " + id + "\n" + "Task: " + name + "\n" + "\n" + "Description:" + "\n" + desc + "\n" + "PROGRESS: " + prog + "\n" + "SUBJECT: " + subject);
                toggleButton.setId(String.valueOf(id));
                toggleButton.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                        if (mouseEvent.getClickCount() == 1)
                            System.out.println(toggleButton.getId());
                        //SET UP TASK VIEW / EDITOR.
                    }
                });

                buttonArray.add(toggleButton);

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
                if (buttonArray.size() < 4 && x == 10) {
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