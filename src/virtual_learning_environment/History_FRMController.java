/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_learning_environment;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import static virtual_learning_environment.Functions.data;
import static virtual_learning_environment.Functions.qry;
import static virtual_learning_environment.Functions.table;

/**
 * FXML Controller class
 *
 * @author Mr.XxX
 */
public class History_FRMController implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @FXML
    TableView tableView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table = null;
        qry = "SELECT Activity,Date,Time from history WHERE User_ID = ? ORDER BY Date DESC,Time DESC";
        new Functions().Tableview(qry, tableView, data, table);
    }
}
