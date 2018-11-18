/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_learning_environment;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import static virtual_learning_environment.Functions.*;

/**
 * FXML Controller class
 *
 * @author Mr.XxX
 */
public class Data_Download_FRMController implements Initializable {

    @FXML
    TableView tableView;

    @FXML
    JFXTextField txtDataDownload;

    @FXML
    private JFXComboBox cmbSubject;

    @FXML
    private void tableClick(Event event) {
        new Functions().fetchDataTable(tableView);
        txtDataDownload.setText(split[0].replace("[", ""));
    }

    @FXML
    private void cmb_Subject_Add(Event event) {
        Functions.cmb_Subject_Add_Item(cmbSubject);
    }

    @FXML
    private void btnData_Download_DownloadData(Event event) {
        if (txtDataDownload.getText().equals("")) {
            Notification("images/alert.png", " Please enter file name.");
        } else {
            temp = "Data";
            new Functions().download_File(txtDataDownload.getText());
            historyQry("File downloaded and file name is " + txtDataDownload.getText() + "");
            table = "load";
            qry = "SELECT File_Name,Subject_Name FROM data,subject WHERE data.Subject_ID = subject.Subject_ID and data.User_ID = ? ORDER BY subject.Subject_ID";
            new Functions().Tableview(qry, tableView, data, table);
            txtDataDownload.clear();
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        table = null;
        qry = "SELECT File_Name,Subject_Name FROM data,subject WHERE data.Subject_ID = subject.Subject_ID AND data.User_ID = ? ORDER BY subject.Subject_ID";
        new Functions().Tableview(qry, tableView, data, table);
    }

}
