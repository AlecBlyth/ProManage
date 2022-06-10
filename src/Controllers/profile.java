package Controllers;

import Controllers.Admin.users;
import Controllers.Client.clientChat;
import Controllers.Client.clientProgressOverall;
import Controllers.Client.clientRequests;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
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
import javafx.scene.layout.VBox;
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

public class profile {

    //FXML Components
    public VBox clientBox, clientImages; //CLIENT ONLY
    public JFXButton btnProgress, btnRequest, btnChat1, btnProfile1; //CLIENT ONLY
    public JFXButton btnMembers, btnRequests, btnLogout, btnKanban, btnTasks, btnChat, btnProfile, exitBtn, btnSave;
    public JFXTextArea txtFieldFirstname, txtFieldsurName, txtFieldEmail;
    public JFXPasswordField txtFieldPassword;
    public JFXComboBox<String> choBoxTypes, choBoxDept, choBoxRole;
    public ImageView reqIcon, memberIcon;
    public Label lblDate, lblTime, lblID, lblNum, lblValidation;

    //Scene Variables
    private double xOffset = 0;
    private double yOffset = 0;

    //Passed Variables
    private String currentUser;
    private int currentID;

    //Local Variables
    Random rnd = new Random();
    boolean checked = true;
    boolean createEnabled = false;
    public int localUserID;
    public int uniqueID = rnd.nextInt(9999);

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

    public void initialize(String userType, int userID, boolean createCheck, int selectedID) throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("src/Datafiles/logs/ProjectFile.json"));
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray roles = (JSONArray) jsonObject.get("projectRoles");
        JSONArray depts = (JSONArray) jsonObject.get("projectDepts");
        choBoxTypes.getItems().addAll("ADMIN", "CLIENT", "USER");
        choBoxRole.getItems().addAll(roles);
        choBoxDept.getItems().addAll(depts);

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
        localUserID = selectedID;
        createEnabled = createCheck;

        if ("CLIENT".equals(userType)) {
            clientBox.setDisable(false);
            clientImages.setVisible(true);
            clientBox.setVisible(true);
        }

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
            choBoxDept.setDisable(true);
            choBoxTypes.setDisable(true);
            choBoxRole.setDisable(true);
        }

        initTime();
        getUsers(selectedID, createCheck);

    } //Initialise controller

    public void getUsers(int selectedID, boolean createCheck) {
        localUserID = selectedID;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
            Statement statement = connection.createStatement();
            String queryString = "SELECT userID, usertype, email, firstname, surname, role, password, department FROM userdata"; //gets user data from database
            ResultSet resultSet = statement.executeQuery(queryString);

            while (resultSet.next()) {
                int id = resultSet.getInt("userID");
                String type = resultSet.getString("usertype");

                String firstname = resultSet.getString("firstname");
                String surname = resultSet.getString("surname");

                String dept = resultSet.getString("department");
                String role = resultSet.getString("role");

                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                if (selectedID == id) {
                    lblNum.setText(Integer.toString(id));
                    choBoxTypes.setValue(type);
                    txtFieldFirstname.setText(firstname);
                    txtFieldsurName.setText(surname);
                    choBoxDept.setValue(dept);
                    choBoxRole.setValue(role);
                    txtFieldEmail.setText(email);
                    txtFieldPassword.setText(password);
                }

                if (id == 0 && !createCheck) {
                    lblValidation.setVisible(true);
                    lblValidation.setText("ERROR, USER NOT FOUND! Report to Admin if issue persists");
                    lblValidation.setTextFill(Color.RED);
                    txtFieldFirstname.setText("ERROR CANNOT RECEIVE TASK NAME!");
                    txtFieldFirstname.setStyle("-fx-text-inner-color: red;");
                    txtFieldsurName.setText("ERROR CANNOT RECEIVE TASK DESCRIPTION!");
                    txtFieldsurName.setStyle("-fx-text-inner-color: red;");
                    txtFieldEmail.setText("ERROR CANNOT RECEIVE TASK SUBJECT!");
                    txtFieldEmail.setStyle("-fx-text-inner-color: red;");
                    txtFieldPassword.setDisable(true);
                    choBoxRole.setDisable(true);
                    choBoxTypes.setDisable(true);
                    choBoxDept.setDisable(true);
                    btnSave.setDisable(true);
                }
                if (createCheck) {
                    txtFieldFirstname.setText("");
                    txtFieldsurName.setText("");
                    txtFieldEmail.setText("");
                    lblNum.setText(String.valueOf(uniqueID));
                }
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveUser() {

        if (localUserID == 0 && !createEnabled) {
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

        if (txtFieldFirstname.getText().length() > 45) {
            lblValidation.setText("Firstname is too long, please limit to 45 characters");
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

        if (txtFieldsurName.getText().length() > 45) {
            lblValidation.setText("Surname is too long, please limit to 40 characters");
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

        if (txtFieldEmail.getText().length() > 320) {
            lblValidation.setText("Email is too long, please limit to 320 characters");
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

        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(txtFieldEmail.getText());
        if (m.matches()) {
            checked = true;
        } else {
            lblValidation.setText("Email format incorrect!");
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

        if (createEnabled && checked) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
                String insertQuery = "INSERT INTO userdata (email, password, usertype, firstname, surname, role, department, userID)" + "values(?,?,?,?,?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(insertQuery);
                ps.setString(1, txtFieldEmail.getText());
                ps.setString(2, txtFieldPassword.getText());
                ps.setString(3, choBoxTypes.getValue());
                ps.setString(4, txtFieldFirstname.getText());
                ps.setString(5, txtFieldsurName.getText());
                ps.setString(6, choBoxRole.getValue());
                ps.setString(7, choBoxDept.getValue());
                ps.setInt(8, uniqueID);
                ps.execute();
                lblValidation.setText("User created!");
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
                connection.close();
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

        if (checked && !createEnabled) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connects to MySQL server
                Statement statement = connection.createStatement();
                String queryString = "SELECT email, password, usertype, firstname, surname, role, department, userID FROM userdata"; //gets task data from database
                String updateQuery = "UPDATE userdata SET email=?, password=?, usertype=?, firstname=?, surname=?, role=?, department=? WHERE userID=?";
                PreparedStatement ps = connection.prepareStatement(updateQuery);
                ResultSet resultSet = statement.executeQuery(queryString);
                while (resultSet.next()) {
                    int id = resultSet.getInt("userID");
                    if (localUserID == id) {
                        ps.setString(1, txtFieldEmail.getText());
                        ps.setString(2, txtFieldPassword.getText());
                        ps.setString(3, choBoxTypes.getValue());
                        ps.setString(4, txtFieldFirstname.getText());
                        ps.setString(5, txtFieldsurName.getText());
                        ps.setString(6, choBoxRole.getValue());
                        ps.setString(7, choBoxDept.getValue());
                        ps.setInt(8, id);
                        ps.execute();
                        connection.close();
                        lblValidation.setText("User saved!");
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
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    //ADMIN AND USER NAVIGATION METHODS
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

    //ADMIN NAVIGATION METHODS
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

    //CLIENT NAVIGATION METHODS
    public void request(ActionEvent request) throws IOException, ParseException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/Client/clientRequest.fxml"));
        AnchorPane root = loader.load();
        clientRequests requests = loader.getController();
        requests.initialize(currentUser, currentID);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene menuViewScene = new Scene(root);
        Stage window = (Stage) ((Node) request.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(menuViewScene);
        window.show();
    }

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

    public void clientchat(ActionEvent chatRoom) throws IOException {
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

    //NAVIGATION METHODS
    public void menu(MouseEvent mouseEvent) throws IOException {

        if (currentUser.equals("CLIENT")) {
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
        } else {
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
