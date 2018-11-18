/*          
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_learning_environment;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Mr.XxX
 */
public class Common_Controller implements Initializable {

    @FXML
    AnchorPane anchorPane;

    @FXML
    private void profile(Event event) {
        new Functions().createStage("Profile_FRM.fxml", anchorPane);
    }

    @FXML
    private void data(Event event) {

        new Functions().createStage("Data_FRM.fxml", anchorPane);
    }

    @FXML
    private void taskStudent(Event event) {
        new Functions().createStage("Task_Student_FRM.fxml", anchorPane);
    }

    @FXML
    private void taskTeacher(Event event) {
        new Functions().createStage("Task_Teacher_FRM.fxml", anchorPane);
    }

    @FXML
    private void btnStudent_AddSub(Event event) {
        new Functions().createStage("Sub_Add_FRM.fxml", anchorPane);
    }

    @FXML
    private void btnTeacher_CreateSub(Event event) {
        new Functions().createStage("Sub_Create_FRM.fxml", anchorPane);
    }

    @FXML
    private void btnFeedback_Queries(Event event) {
        new Functions().createStage("Queries_Feedback_FRM.fxml", anchorPane);
    }

    @FXML
    private void history(Event event) {
        new Functions().createStage("History_FRM.fxml", anchorPane);
    }

    @FXML
    private void chat(Event event) {
        new Functions().createStage("Chat_FRM.fxml", anchorPane);
    }

    @FXML
    private void help(Event event) {
        new Functions().createStage("Help_FRM.fxml", anchorPane);
    }

    @FXML
    private void logout(Event event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
