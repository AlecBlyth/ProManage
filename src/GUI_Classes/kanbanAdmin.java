package GUI_Classes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class kanbanAdmin {

    public Label lblBacklog, lblTodo, lblProgress, lblComplete, lblBlocked;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML private FlowPane paneOne, paneTwo, paneThree, paneFour, paneFive;

    public void initialize(){

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

    public void teamChat(ActionEvent actionEvent) {
    }

    public void userProfile(ActionEvent actionEvent) {
    }

    public void members(ActionEvent actionEvent) {
    }

    public void requests(ActionEvent actionEvent) {
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
