package Controllers.Admin;

import Controllers.menu;
import Controllers.tasks;
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
import java.util.concurrent.CopyOnWriteArrayList;

import static javafx.geometry.Pos.CENTER;

public class users {

    //FXML Components
    public ScrollPane userPane;
    public VBox userVertical;
    public JFXButton btnMembers, btnRequests, btnLogout, btnKanban, btnTasks, btnChat, btnProfile, exitBtn, btnCreateUser, btnDeleteUser, btnEditUser;
    public ToggleGroup toggleGroup;
    public ImageView reqIcon, memberIcon;
    public Label lblDate, lblTime, lblInfo;

    //Scene Variables
    private double xOffset = 0;
    private double yOffset = 0;

    //Passed Variables
    private String currentUser;
    private int currentID;

    //Local variables
    private int userID;

    //CONTROLLER METHODS
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

        currentUser = userType; //Sets currentUser to userType
        currentID = userID;
        btnEditUser.setDisable(true);
        btnDeleteUser.setDisable(true);

        initTime();
        getUsers();

    } //Initialise controller

    public void getUsers() {
        int x = 0;
        ArrayList<HBox> hBoxArrayList = new ArrayList<>();
        CopyOnWriteArrayList<ToggleButton> buttonArray = new CopyOnWriteArrayList<>();
        userVertical.getChildren().clear();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companydatabase", "root", "admin"); //Connects to MySQL server
            Statement statement = connection.createStatement();
            String queryString = "SELECT userID, usertype, email, firstname, surname, role, department FROM userdata"; //gets user data from database
            ResultSet resultSet = statement.executeQuery(queryString);

            while (resultSet.next()) {
                x++;
                int id = resultSet.getInt("userID");
                String type = resultSet.getString("usertype");

                String firstname = resultSet.getString("firstname");
                String surname = resultSet.getString("surname");

                String dept = resultSet.getString("department");
                String role = resultSet.getString("role");

                String email = resultSet.getString("email");

                ToggleButton toggleButton = new ToggleButton();
                toggleButton.setMinHeight(150);
                toggleButton.setMinWidth(220);
                toggleButton.setMaxHeight(150);
                toggleButton.setMaxWidth(220);
                toggleButton.setWrapText(true);
                toggleButton.setTextOverrun(OverrunStyle.CLIP);

                switch (type) {
                    case "ADMIN":
                        toggleButton.setStyle("-fx-base: " + "#9a0000" + ";" + "-fx-background-radius: 0;" + "-fx-font-size: 10.5;" + "-fx-alignment: TOP-LEFT;" + "-fx-focus-color: white;" + "-fx-font-family: Segoe UI; " + "fx-focus-color: white;");
                        break;
                    case "USER":
                        toggleButton.setStyle("-fx-base: " + "#2d79ff" + ";" + "-fx-background-radius: 0;" + "-fx-font-size: 10.5;" + "-fx-alignment: TOP-LEFT;" + "-fx-focus-color: white;" + "-fx-font-family: Segoe UI; " + "fx-focus-color: white;");
                        break;
                    case "CLIENT":
                        toggleButton.setStyle("-fx-base: " + "#009a00" + ";" + "-fx-background-radius: 0;" + "-fx-font-size: 10.5;" + "-fx-alignment: TOP-LEFT;" + "-fx-focus-color: white;" + "-fx-font-family: Segoe UI; " + "fx-focus-color: white;");
                        break;
                }
                toggleButton.setText("User ID: " + id + "\n" + type + "\n" + "Firstname: " + firstname + "\n" + "Surname: " + surname + "\n" + "Department: " + dept + "\n" + "Role: " + role + "\n" + "\n" + "Email: " + email);

                toggleButton.setId(String.valueOf(id));
                toggleButton.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                        if (mouseEvent.getClickCount() == 1)
                            userID = id; //Sets ID to currently clicked user's ID
                        System.out.println(userID);
                        btnEditUser.setDisable(false); //Added better validation
                        btnDeleteUser.setDisable(false);
                        if (currentID == userID) {
                            btnDeleteUser.setDisable(true);
                        }
                    }
                });

                toggleButton.setToggleGroup(toggleGroup); //Puts toggle button into toggle group to unselect/select correct users.
                buttonArray.add(toggleButton); //Add toggle button to array

                if (buttonArray.size() == 4) {
                    HBox userHorizontal = new HBox();
                    userHorizontal.setSpacing(5);
                    userHorizontal.setAlignment(CENTER);
                    userHorizontal.minWidth(Region.USE_COMPUTED_SIZE);
                    userHorizontal.minHeight(Region.USE_PREF_SIZE);
                    userHorizontal.prefWidth(1020);
                    userHorizontal.prefHeight(235);
                    userHorizontal.getChildren().addAll(buttonArray);
                    buttonArray.clear();
                    hBoxArrayList.add(userHorizontal);
                }
                if (buttonArray.size() < 4 && x > buttonArray.size()) {
                    HBox userHorizontal = new HBox();
                    userHorizontal.setSpacing(5);
                    userHorizontal.setAlignment(CENTER);
                    userHorizontal.minWidth(Region.USE_COMPUTED_SIZE);
                    userHorizontal.minHeight(Region.USE_PREF_SIZE);
                    userHorizontal.prefWidth(1020);
                    userHorizontal.prefHeight(235);
                    userHorizontal.getChildren().addAll(buttonArray);
                    hBoxArrayList.add(userHorizontal);
                }
                if (buttonArray.size() < 4 && x < 4) {
                    HBox userHorizontal = new HBox();
                    userHorizontal.setSpacing(5);
                    userHorizontal.setAlignment(CENTER);
                    userHorizontal.minWidth(Region.USE_COMPUTED_SIZE);
                    userHorizontal.minHeight(Region.USE_PREF_SIZE);
                    userHorizontal.prefWidth(1020);
                    userHorizontal.prefHeight(235);
                    userHorizontal.getChildren().addAll(buttonArray);
                    hBoxArrayList.add(userHorizontal);
                }
            }
            userVertical.getChildren().addAll(hBoxArrayList);
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createUser(ActionEvent createUser) throws IOException, ParseException {
        int selectedID = 0;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/profile.fxml"));
        AnchorPane root = loader.load();
        Controllers.profile controller = loader.getController();
        controller.initialize(currentUser, currentID, true, selectedID);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene menuViewScene = new Scene(root);
        Stage window = (Stage) ((Node) createUser.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(menuViewScene);
        window.show();
    }

    public void editUser(ActionEvent editUser) throws IOException, ParseException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/profile.fxml"));
        AnchorPane root = loader.load();
        Controllers.profile controller = loader.getController();
        controller.initialize(currentUser, currentID, false, userID);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene menuViewScene = new Scene(root);
        Stage window = (Stage) ((Node) editUser.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(menuViewScene);
        window.show();
    }

    public void deleteUser() {
        if (userID == 0) {
            lblInfo.setText("Select a User to Delete!");
            lblInfo.setVisible(true); //Displays label
            if (lblInfo.isVisible()) { //Plays fade out animation
                FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblInfo);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.play();
            }
        }
        if (userID == currentID) {
            lblInfo.setText("Cannot delete own account while using!");
            lblInfo.setVisible(true); //Displays label
            if (lblInfo.isVisible()) { //Plays fade out animation
                FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblInfo);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.play();
            }
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companydatabase", "root", "admin"); //Connects to MySQL server
            Statement statement = connection.createStatement();
            String queryString = "SELECT userID FROM userdata"; //gets task data from database
            String updateQuery = "DELETE from userdata WHERE userID=?";
            PreparedStatement ps = connection.prepareStatement(updateQuery);
            ResultSet resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                int id = resultSet.getInt("userID");
                if (userID == id) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    getUsers();
                    lblInfo.setText("User Deleted!");
                    lblInfo.setVisible(true); //Displays label
                    if (lblInfo.isVisible()) { //Plays fade out animation
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblInfo);
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);
                        fadeOut.play();
                    }
                }
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //ADMIN NAVIGATION METHODS
    public void kanban(ActionEvent kanban) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/kanban.fxml"));
        AnchorPane root = loader.load();
        Controllers.kanban kanbanScene = loader.getController();
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
        Controllers.chat chat = loader.getController();
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
        int selectedID = currentID;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/profile.fxml"));
        AnchorPane root = loader.load();
        Controllers.profile controller = loader.getController();
        controller.initialize(currentUser, currentID, false, selectedID);
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

    public void requests(ActionEvent requests) throws IOException, ParseException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/Admin/requests.fxml"));
        AnchorPane root = loader.load();
        Controllers.Admin.requests controller = loader.getController();
        controller.initialize(currentUser, currentID);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene chatViewScene = new Scene(root);
        Stage window = (Stage) ((Node) requests.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(chatViewScene);
        window.show();
    }

    //NAVIGATION METHODS
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

    public void exit() { //Exit functionality
        System.exit(0);
    }

}
