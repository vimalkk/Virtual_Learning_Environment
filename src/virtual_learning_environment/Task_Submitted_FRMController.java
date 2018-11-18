/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_learning_environment;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import static virtual_learning_environment.Functions.*;

/**
 * FXML Controller class
 *
 * @author Mr.XxX
 */
public class Task_Submitted_FRMController implements Initializable {

    @FXML
    private JFXComboBox cmbSubject;
    @FXML
    private TableView tableView;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table = null;
        qry = "SELECT File_Name,Last_Date,Subject_Name FROM task,subject WHERE task.Subject_ID = subject.Subject_ID AND task.User_ID = ? AND Status='submit' ORDER BY subject.Subject_ID";
        new Functions().Tableview(qry, tableView, data, table);
    }

    @FXML
    private void cmb_Subject_Add(MouseEvent event) {
        cmb_Subject_Add_Item(cmbSubject);
        fetch_Cmb_Data(cmbSubject);
        
    }
}