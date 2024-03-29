package Controllers;

import Controllers.Client.clientMenu;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;


public class loginController {

    //FXML Components
    public JFXButton btnMembers, btnRequests, btnLogout, exitBtn;
    public JFXTextField txtEmail;
    public JFXPasswordField txtPassword;
    public Label lblWarning; //Label used to alert user of incorrect login

    //Scene Variables
    private double xOffset = 0; //Used to allow user to move GUI around screen
    private double yOffset = 0;

    //CONTROLLER METHOD
    public void initialize() {
        exitBtn.setOnMouseEntered(e -> exitBtn.setStyle("-fx-background-color: RED; -fx-background-radius: 0;"));
        exitBtn.setOnMouseExited(e -> exitBtn.setStyle("-fx-background-color: ; -fx-background-radius: 0;"));
    }

    public void login(ActionEvent login) {
        boolean check = false; //Boolean that checks for successful login
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companydatabase", "root", "admin"); //Connects to local mySQL server
            Statement statement = connection.createStatement(); //Creates a statement
            String queryString = "SELECT email, password, usertype, userID FROM userdata"; //gets user details from database
            ResultSet resultSet = statement.executeQuery(queryString);

            while (resultSet.next()) {
                String compUser = resultSet.getString("email"); //gets email from database
                String password = resultSet.getString("password"); //gets password from database
                String userType = resultSet.getString("usertype"); //gets usertype from database
                int id = resultSet.getInt("userID");

                if (txtEmail.getText().equals(compUser) && txtPassword.getText().equals(password)) {
                    check = true;
                    if (userType.equals("ADMIN") || userType.equals("USER")) { //Checks for usertype
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/menu.fxml")); //Loads FXML file
                        AnchorPane root = loader.load();
                        menu MenuScene = loader.getController(); //Gets controller from menu
                        MenuScene.initialize(userType, id); //Sends userType to menu
                        root.setOnMousePressed(event -> { //On mouse press, gets current cords
                            xOffset = event.getSceneX();
                            yOffset = event.getSceneY();
                        });
                        Scene menuViewScene = new Scene(root);
                        Stage window = (Stage) ((Node) login.getSource()).getScene().getWindow(); //gets scene and window
                        root.setOnMouseDragged(event -> { //On mouse drag change cords
                            window.setX((event.getScreenX() - xOffset));
                            window.setY((event.getScreenY() - yOffset));
                        });
                        window.setScene(menuViewScene); //Sets new window as menu
                        window.show(); //shows menu
                    }
                    if (userType.equals("CLIENT")) { //Checks for client usertype
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/Client/clientMenu.fxml"));
                        AnchorPane root = loader.load();
                        clientMenu MenuScene = loader.getController(); //Gets controller from menu
                        MenuScene.initialize(userType, id);
                        root.setOnMousePressed(event -> {
                            xOffset = event.getSceneX();
                            yOffset = event.getSceneY();
                        });
                        Scene menuViewScene = new Scene(root);
                        Stage window = (Stage) ((Node) login.getSource()).getScene().getWindow();
                        root.setOnMouseDragged(event -> {
                            window.setX((event.getScreenX() - xOffset));
                            window.setY((event.getScreenY() - yOffset));
                        });
                        window.setScene(menuViewScene);
                        window.show();
                    }
                }
                if (!check) {
                    lblWarning.setVisible(true); //Displays label
                    if (lblWarning.isVisible()) { //Plays fade out animation
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblWarning);
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);
                        fadeOut.play();
                    }
                }
            }
            connection.close();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    //NAVIGATION METHODS

    public void exit() {
        System.exit(0);
    }
}
