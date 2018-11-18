/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_learning_environment;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author Mr.XxX
 */
public class Queries_Feedback_FRMController implements Initializable {

    @FXML
    private JFXTextField txtCommentSub;

    @FXML
    private TextArea txtCommentDesc;

    public static String commentSubject = null, commentDesc = null;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void btnQueries_Send(Event event) {
        commentSubject = txtCommentSub.getText();
        commentDesc = txtCommentDesc.getText();
        Functions.sendMail("virtuallearningenvironment123@gmail.com", "query");
        txtCommentSub.clear();
        txtCommentDesc.clear();
    }

    @FXML
    private void btnQueries_Clear(Event event) {
        txtCommentSub.clear();
        txtCommentDesc.clear();
    }

}
