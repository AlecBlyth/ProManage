package GUI_Classes;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.util.Duration;
import java.sql.*;

public class Login {

    public JFXTextField txtUsername; //GUI elements
    public JFXPasswordField txtPassword;
    public Label lblWarning;

    public void serverLogin(){
        boolean check = false; //Successful login checker
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connect to mySQL dummy database |NOTE This is prone to SQL Injection
            Statement statement = connection.createStatement();
            String queryString = "SELECT username, password FROM userdata"; //get username and password from database
            ResultSet resultSet = statement.executeQuery(queryString);

            while (resultSet.next()){ //Checks for username and password
                String compUser = resultSet.getString("username");
                String password = resultSet.getString("password");
                if(txtUsername.getText().equals(compUser) && txtPassword.getText().equals(password)){
                    System.out.println("Successful login");
                    check=true;
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    } //Login method

    public void exit(){
        System.exit(0); //Exits on button press
    }

    public void login(ActionEvent actionEvent) {
        serverLogin(); //This is just jumping through hoops really.
    }
}
