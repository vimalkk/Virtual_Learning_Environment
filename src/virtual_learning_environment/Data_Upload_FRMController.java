/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_learning_environment;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import static virtual_learning_environment.Controller.*;
import static virtual_learning_environment.Functions.*;

/**
 * FXML Controller class
 *
 * @author Mr.XxX
 */
public class Data_Upload_FRMController implements Initializable {

    @FXML
    private JFXTextField txtDataDelete;
    @FXML
    private JFXComboBox cmbSubject;
    @FXML
    private JFXTextField txtDataUploadBrowse;
    @FXML
    private TableView tableView;

    @FXML
    private void cmb_Subject_Add(Event event) {
        Functions.cmb_Subject_Add_Item(cmbSubject);
    }

    static String filePath, fileName, subID, subName;

    @FXML
    private void btnData_Upload_Browse(Event event) {
        FileChooser fileChooser = new FileChooser();
        selectedFile = fileChooser.showOpenDialog(null);
        fileName = selectedFile.getName();
        filePath = selectedFile.getAbsolutePath().replace("\\", "/");
        if (selectedFile != null) {
            txtDataUploadBrowse.setText(filePath);
        }
    }

    @FXML
    private void btnData_Upload_Upload(Event event) {
        try {
            Functions.fetch_Cmb_Data(cmbSubject);
            temp = "upload";
            if (cmbSubject.getValue() == null) {
                Functions.Notification("images/warning.png", "Please select category.");
            } else {
                if (txtDataUploadBrowse.getText().equals("")) {
                    Functions.Notification("images/warning.png", "Please fill all data.");
                } else {
                    qry = "INSERT INTO data (File_Name,Subject_ID,User_ID) VALUES (?,?,?)";
                    new Functions().upload_File(temp, qry, filePath, fileName);
                    table = "load";
                    qry = "SELECT File_Name,Subject_Name,Subject_Code FROM data,subject WHERE data.Subject_ID = subject.Subject_ID AND data.User_ID = ? ORDER BY subject.Subject_ID";
                    new Functions().Tableview(qry, tableView, data, table);
                    conn.close();
                    Notification("images/confirm.png", "File uploaded.");
                    txtDataUploadBrowse.clear();
                }
            }
        } catch (SQLException e) {
            Notification("images/alert.png", "File already exist...!!!");
        }
    }

    @FXML
    private void tableClick(Event event) {
        new Functions().fetchDataTable(tableView);
        txtDataDelete.setText(split[0].replace("[", ""));
        subCode = split[2].replace("]", "").replace(" ", "");
    }

    @FXML
    private void btnData_Upload_deleteData(Event event) {
        try {
            if (txtDataDelete.getText().equals("")) {
                Notification("images/alert.png", " Please enter subject code.");
            } else {
                conn.close();
                temp = "Data";
                new Functions().delete_File(txtDataDelete.getText(), "DELETE FROM data WHERE File_Name = ? AND Subject_ID = ? AND User_ID = ?");
                historyQry("File deleted and file name " + fileName + "");
                table = "load";
                qry = "SELECT File_Name,Subject_Name,Subject_Code FROM data,subject WHERE data.Subject_ID = subject.Subject_ID and data.User_ID = ? ORDER BY subject.Subject_ID";
                new Functions().Tableview(qry, tableView, data, table);
                conn.close();
                txtDataDelete.clear();
            }
        } catch (SQLException ex) {
            Notification("images/alert.png", ex.getLocalizedMessage());
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
        qry = "SELECT File_Name,Subject_Name,Subject_Code FROM data,subject WHERE data.Subject_ID = subject.Subject_ID AND data.User_ID = ? ORDER BY subject.Subject_ID";
        new Functions().Tableview(qry, tableView, data, table);
    }
}
