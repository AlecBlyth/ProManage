package GUI_Classes;

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


public class Login {

    public JFXTextField txtUsername; //GUI elements
    public JFXPasswordField txtPassword;
    public Label lblWarning;

    private double xOffset = 0;
    private double yOffset = 0;

    public void exit(){
        System.exit(0); //Exits on button press
    }

    public void login(ActionEvent login) {
        boolean check = false; //Successful login checker
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connect to mySQL dummy database |NOTE This is prone to SQL Injection
            Statement statement = connection.createStatement();
            String queryString = "SELECT username, password, usertype FROM userdata"; //get username and password from database
            ResultSet resultSet = statement.executeQuery(queryString);

            while (resultSet.next()){ //Checks for username and password
                String compUser = resultSet.getString("username");
                String password = resultSet.getString("password");
                String userType = resultSet.getString("usertype");
                if(txtUsername.getText().equals(compUser) && txtPassword.getText().equals(password)){
                    check=true;
                    if(userType.equals("ADMIN")){ //Checks for usertype for GUI display and functionality
                        System.out.println("Logging in as ADMIN");
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/Admin/adminMenu.fxml")); //Display admin menu
                        AnchorPane root = loader.load();
                        root.setOnMousePressed(event -> { //Allow to move app around
                            xOffset = event.getSceneX();
                            yOffset = event.getSceneY();
                        });
                        Scene menuViewScene = new Scene(root);
                        Stage window = (Stage) ((Node) login.getSource()).getScene().getWindow();
                        root.setOnMouseDragged(event -> {
                            window.setX((event.getScreenX() - xOffset));
                            window.setY((event.getScreenY() - yOffset));
                        });
                        window.setScene(menuViewScene); //Show new scene
                        window.show();
                    }
                    if(userType.equals("USER")){
                        System.out.println("USER Login");
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/User/userMenu.fxml")); //Display admin menu
                        AnchorPane root = loader.load();
                        root.setOnMousePressed(event -> { //Allow to move app around
                            xOffset = event.getSceneX();
                            yOffset = event.getSceneY();
                        });
                        Scene menuViewScene = new Scene(root);
                        Stage window = (Stage) ((Node) login.getSource()).getScene().getWindow();
                        root.setOnMouseDragged(event -> {
                            window.setX((event.getScreenX() - xOffset));
                            window.setY((event.getScreenY() - yOffset));
                        });
                        window.setScene(menuViewScene); //Show new scene
                        window.show();

                    }
                    if(userType.equals("CLIENT")){
                        System.out.println("CLIENT Login");
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/Client/clientMenu.fxml")); //Display admin menu
                        AnchorPane root = loader.load();
                        root.setOnMousePressed(event -> { //Allow to move app around
                            xOffset = event.getSceneX();
                            yOffset = event.getSceneY();
                        });
                        Scene menuViewScene = new Scene(root);
                        Stage window = (Stage) ((Node) login.getSource()).getScene().getWindow();
                        root.setOnMouseDragged(event -> {
                            window.setX((event.getScreenX() - xOffset));
                            window.setY((event.getScreenY() - yOffset));
                        });
                        window.setScene(menuViewScene); //Show new scene
                        window.show();
                    }
                }
                if (!check) {
                    lblWarning.setVisible(true);
                    if (lblWarning.isVisible()) {
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(1550), lblWarning); //Shows invalid login animation on failed attempt
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);
                        fadeOut.play();
                    }
                }
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }
}
