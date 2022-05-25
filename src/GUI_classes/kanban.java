package GUI_classes;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
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

public class kanban {

    //FXML Components
    public Label lblBacklog, lblTodo, lblProgress, lblComplete, lblBlocked, lblDate, lblTime;
    public JFXButton btnMembers, btnRequests, btnLogout, btnKanban, btnTasks, btnChat, btnProfile, exitBtn;
    public ImageView memberIcon, reqIcon;

    @FXML
    private FlowPane paneOne, paneTwo, paneThree, paneFour, paneFive;
    @FXML
    private JFXButton draggingButton;

    private static DataFormat btnFormat = new DataFormat(" "); //Allows buttons on clipboard

    //Variables
    private double xOffset = 0;
    private double yOffset = 0;

    //Passed variables
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
        switch (userType) {
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
                break;
            default:
                btnMembers.setVisible(false);
                btnRequests.setVisible(false);
                memberIcon.setVisible(false);
                reqIcon.setVisible(false);
                btnMembers.setDisable(true);
                btnRequests.setDisable(true);
                System.out.println("USER HAS NO USERTYPE");
                break;
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
            Statement statement = connection.createStatement();
            String queryString = "SELECT section, taskhex, taskname, taskdesc, taskid FROM tasks"; //gets task data from database
            ResultSet resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                String colour = resultSet.getString("taskhex");
                String name = resultSet.getString("taskname");
                String desc = resultSet.getString("taskdesc");
                int paneID = resultSet.getInt("section");
                int id = resultSet.getInt("taskid");
                paneDrag(paneOne); //Allows flowpanes for button dragging
                paneDrag(paneTwo);
                paneDrag(paneThree);
                paneDrag(paneFour);
                paneDrag(paneFive);

                switch (paneID) { //Depending on task section, add a button in flowpane
                    case 1:
                        paneOne.getChildren().add(initButton(colour, name, desc, id)); //Creates button with colour, name, desc and id  from database
                        break;
                    case 2:
                        paneTwo.getChildren().add(initButton(colour, name, desc, id));
                        break;
                    case 3:
                        paneThree.getChildren().add(initButton(colour, name, desc, id));
                        break;
                    case 4:
                        paneFour.getChildren().add(initButton(colour, name, desc, id));
                        break;
                    case 5:
                        paneFive.getChildren().add(initButton(colour, name, desc, id));
                        break;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        JSONParser parser = new JSONParser(); //Used to read from JSON file
        try {
            Object obj = parser.parse(new FileReader("src/Datafiles/logs/ProjectFile.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray testList = (JSONArray) jsonObject.get("projectLabels"); //Gets custom kanban labels from JSON file

            lblBacklog.setText(testList.get(0).toString()); //Sets labels to custom labels
            lblTodo.setText(testList.get(1).toString());
            lblProgress.setText(testList.get(2).toString());
            lblComplete.setText(testList.get(3).toString());
            lblBlocked.setText(testList.get(4).toString());

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    } //Initialises controller

    private JFXButton initButton(String colour, String taskname, String taskdesc, int id) { //Creates and initialises button
        JFXButton button = new JFXButton(taskname + "\n" + taskdesc); //Sets text in button
        button.setStyle("-fx-background-color: " + colour + " ; -fx-text-fill: white; " + "-fx-font-weight: bold;" + "-fx-background-radius: 0;" + "-fx-font-size:10.0;" + "-fx-alignment: TOP-LEFT;"); //Button formatting
        button.setFont(Font.font("Segoe UI")); //Button font
        button.setPrefWidth(195);
        button.setPrefHeight(80);
        button.setWrapText(true); //Allows text to break to allow for larger descriptions
        button.setOnDragDetected(e -> { //Allows button to be dragged from screen
            Dragboard db = button.startDragAndDrop(TransferMode.MOVE);
            db.setDragView(button.snapshot(null, null));
            ClipboardContent cc = new ClipboardContent();
            cc.put(btnFormat, "button");
            db.setContent(cc);
            draggingButton = button;
        });
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> button.setCursor(Cursor.OPEN_HAND));
        button.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> button.setCursor(Cursor.DEFAULT));
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> button.setCursor(Cursor.CLOSED_HAND));
        button.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> button.setCursor(Cursor.OPEN_HAND));
        button.setId(String.valueOf(id));

        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (mouseEvent.getClickCount() == 2)
                    System.out.println(button.getId());
            }
        });
        button.setOnDragDone(e -> draggingButton = null);
        return button;
    }

    private void paneDrag(Pane pane) {

        pane.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(btnFormat) && draggingButton != null && draggingButton.getParent() != pane) { //Gets current pane of button and allows it to be dragged from pane to pane
                e.acceptTransferModes(TransferMode.MOVE);
            }
        });
        pane.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            draggingButton.setCursor(Cursor.OPEN_HAND);
            if (db.hasContent(btnFormat)) {
                ((Pane) draggingButton.getParent()).getChildren().remove(draggingButton); //Removes button from previous pane
                pane.getChildren().add(draggingButton); //Adds button to new pane
                e.setDropCompleted(true); //finish dragging
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
                    Statement statement = connection.createStatement();
                    String queryString = "SELECT taskid FROM tasks"; //gets task data from database
                    String updateQuery = "UPDATE tasks SET section=?, taskhex=?, taskprogress=? WHERE taskid=?";
                    PreparedStatement ps = connection.prepareStatement(updateQuery);
                    ResultSet resultSet = statement.executeQuery(queryString);
                    while (resultSet.next()) {
                        int id = resultSet.getInt("taskid");
                        if (pane.getId().equals(paneOne.getId()) && draggingButton.getId().equals(String.valueOf(id))) {
                            ps.setInt(1, 1);
                            ps.setString(2, "#123d82");
                            ps.setInt(3, 0);
                            ps.setInt(4, id);
                            ps.executeUpdate();
                            draggingButton.setStyle("-fx-background-color: " + "#123d82" + " ; -fx-text-fill: white; " + "-fx-font-weight: bold;" + "-fx-background-radius: 0;" + "-fx-font-size:10.0;" + "-fx-alignment: TOP-LEFT;");
                        }
                        if (pane.getId().equals(paneTwo.getId()) && draggingButton.getId().equals(String.valueOf(id))) {
                            ps.setInt(1, 2);
                            ps.setString(2, "#333f50");
                            ps.setInt(3, 10);
                            ps.setInt(4, id);
                            ps.executeUpdate();
                            draggingButton.setStyle("-fx-background-color: " + "#333f50" + " ; -fx-text-fill: white; " + "-fx-font-weight: bold;" + "-fx-background-radius: 0;" + "-fx-font-size:10.0;" + "-fx-alignment: TOP-LEFT;");
                        }
                        if (pane.getId().equals(paneThree.getId()) && draggingButton.getId().equals(String.valueOf(id))) {
                            ps.setInt(1, 3);
                            ps.setString(2, "#2d79ff");
                            ps.setInt(3, 25);
                            ps.setInt(4, id);
                            ps.executeUpdate();
                            draggingButton.setStyle("-fx-background-color: " + "#2d79ff" + " ; -fx-text-fill: white; " + "-fx-font-weight: bold;" + "-fx-background-radius: 0;" + "-fx-font-size:10.0;" + "-fx-alignment: TOP-LEFT;");
                        }
                        if (pane.getId().equals(paneFour.getId()) && draggingButton.getId().equals(String.valueOf(id))) {
                            ps.setInt(1, 4);
                            ps.setString(2, "#00b050");
                            ps.setInt(3, 100);
                            ps.setInt(4, id);
                            ps.executeUpdate();
                            draggingButton.setStyle("-fx-background-color: " + "#00b050" + " ; -fx-text-fill: white; " + "-fx-font-weight: bold;" + "-fx-background-radius: 0;" + "-fx-font-size:10.0;" + "-fx-alignment: TOP-LEFT;");
                        }
                        if (pane.getId().equals(paneFive.getId()) && draggingButton.getId().equals(String.valueOf(id))) {
                            ps.setInt(1, 5);
                            ps.setString(2, "#c00000");
                            ps.setInt(3, 0);
                            ps.setInt(4, id);
                            ps.executeUpdate();
                            draggingButton.setStyle("-fx-background-color: " + "#c00000" + " ; -fx-text-fill: white; " + "-fx-font-weight: bold;" + "-fx-background-radius: 0;" + "-fx-font-size:10.0;" + "-fx-alignment: TOP-LEFT;");

                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
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

    //ADMIN AND USER FEATURES
    public void kanban(ActionEvent kanban) {
        //Do nothing already on stage
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
    public void members(ActionEvent actionEvent) {
    }

    public void requests(ActionEvent actionEvent) {
    }

    //NAVIGATION
    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void logOut(ActionEvent logout) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/login.fxml"));
        AnchorPane root = loader.load();
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene menuViewScene = new Scene(root);
        Stage window = (Stage) ((Node) logout.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(menuViewScene);
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
