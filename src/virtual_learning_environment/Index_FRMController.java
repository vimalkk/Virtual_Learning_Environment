package virtual_learning_environment;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Mr.XxX
 */
public class Index_FRMController implements Initializable {

    @FXML
    public Label lblCurrentSession;
    @FXML
    public AnchorPane anchorPaneHome;

    @FXML
    private void btnIndex_LogIn(Event event) {
        new Functions().createStage("Login_FRM.fxml", anchorPaneHome);
    }

    @FXML
    private void btnIndex_AboutUs(Event event) {
        new Functions().createStage("AboutUs_FRM.fxml", anchorPaneHome);
    }

    @FXML
    private void btnIndex_SignUp(Event event) {
        new Functions().createStage("SignUp_FRM.fxml", anchorPaneHome);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Functions().createStage("AboutUs_FRM.fxml", anchorPaneHome);
    }

}
