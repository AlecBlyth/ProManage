package GUI_Classes;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class kanbanUser {

    public Label lblBacklog, lblTodo, lblProgress, lblComplete, lblBlocked;
    public static DataFormat buttonFormat = new DataFormat(" ");
    public Label lblDate;
    public Label lblTime;

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML private FlowPane paneOne, paneTwo, paneThree, paneFour, paneFive;
    @FXML private JFXButton draggingButton;

    public void initialize() {

        paneDrag(paneOne);
        paneDrag(paneTwo);
        paneDrag(paneThree);
        paneDrag(paneFour);
        paneDrag(paneFive);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/companyusers", "root", "admin"); //Connect to mySQL dummy database |NOTE This is prone to SQL Injection
            Statement statement = connection.createStatement();
            String queryString = "SELECT section, taskhex, taskname, taskdesc FROM tasks"; //get tasks from database
            ResultSet resultSet = statement.executeQuery(queryString);
            while(resultSet.next()) {
                String colour = resultSet.getString("taskhex");
                String name = resultSet.getString("taskname");
                String desc = resultSet.getString("taskdesc");
                int paneID = resultSet.getInt("section");

                switch(paneID){
                    case 1:
                        paneOne.getChildren().add(initButton(colour, name, desc));
                        break;
                    case 2:
                        paneTwo.getChildren().add(initButton(colour, name, desc));
                        break;
                    case 3:
                        paneThree.getChildren().add(initButton(colour, name, desc));
                        break;
                    case 4:
                        paneFour.getChildren().add(initButton(colour, name, desc));
                        break;
                    case 5:
                        paneFive.getChildren().add(initButton(colour, name, desc));
                        break;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(new FileReader("src/Datafiles/logs/ProjectFile.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray testList = (JSONArray) jsonObject.get("projectLabels");

            lblBacklog.setText(testList.get(0).toString());
            lblTodo.setText(testList.get(1).toString());
            lblProgress.setText(testList.get(2).toString());
            lblComplete.setText(testList.get(3).toString());
            lblBlocked.setText(testList.get(4).toString());


        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void kanban(ActionEvent actionEvent) {
    }

    public void tasks(ActionEvent actionEvent) {
    }

    public void chat(ActionEvent actionEvent) {
    }

    public void userProfile(ActionEvent actionEvent) {
    }

    private JFXButton initButton(String colour,String taskname, String taskdesc){
        String fontcol = " ; -fx-text-fill: white;";
        JFXButton button = new JFXButton(taskname + "\n" +taskdesc);
        button.setStyle("-fx-background-color: " + colour + fontcol + "-fx-font-weight: bold;" + "-fx-background-radius: 0;" + "-fx-font-size:9.0;");
        button.setFont(Font.font("Segoe UI"));

        button.setPrefWidth(202);
        button.setPrefHeight(82);
        button.setWrapText(true);

        button.setOnDragDetected(e-> {
            Dragboard db = button.startDragAndDrop(TransferMode.MOVE);
            db.setDragView(button.snapshot(null, null));
            ClipboardContent cc = new ClipboardContent();
            cc.put(buttonFormat, "button");
            db.setContent(cc);
            draggingButton = button;
        });
        button.setOnDragDone(e -> draggingButton = null);
        return button;
    }

    private void paneDrag(Pane pane){
        pane.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            if(db.hasContent(buttonFormat) && draggingButton != null && draggingButton.getParent() != pane){
                e.acceptTransferModes(TransferMode.MOVE);
            }
        });

        pane.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            if(db.hasContent(buttonFormat)){
                ((Pane)draggingButton.getParent()).getChildren().remove(draggingButton);
                pane.getChildren().add(draggingButton);
                e.setDropCompleted(true);
            }
        });
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void logOut(ActionEvent logout) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/login.fxml")); //Display admin menu
        AnchorPane root = loader.load();
        root.setOnMousePressed(event -> { //Allow to move app around
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        Scene menuViewScene = new Scene(root);
        Stage window = (Stage) ((Node) logout.getSource()).getScene().getWindow();
        root.setOnMouseDragged(event -> {
            window.setX((event.getScreenX() - xOffset));
            window.setY((event.getScreenY() - yOffset));
        });
        window.setScene(menuViewScene); //Show new scene
        window.show();
    }
}
