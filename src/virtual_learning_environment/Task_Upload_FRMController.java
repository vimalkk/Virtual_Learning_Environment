/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_learning_environment;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
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
import static virtual_learning_environment.Data_Upload_FRMController.fileName;
import static virtual_learning_environment.Data_Upload_FRMController.filePath;
import static virtual_learning_environment.Functions.Notification;
import static virtual_learning_environment.Functions.data;
import static virtual_learning_environment.Functions.historyQry;
import static virtual_learning_environment.Functions.qry;
import static virtual_learning_environment.Functions.selectedFile;
import static virtual_learning_environment.Functions.split;
import static virtual_learning_environment.Functions.table;
import static virtual_learning_environment.Functions.temp;

/**
 * FXML Controller class
 *
 * @author Mr.XxX
 */
public class Task_Upload_FRMController implements Initializable {

    @FXML
    private JFXComboBox cmbSubject;
    @FXML
    private JFXTextField txtTaskUploadBrowse, txtTaskDelete;
    @FXML
    private JFXTextArea txtTaskDescriptiopn;
    @FXML
    private TableView tableView;
    @FXML
    private JFXDatePicker dateLastDate;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table = null;
        qry = "SELECT File_Name,Last_Date,Status,Subject_Name FROM task,subject WHERE task.Subject_ID = subject.Subject_ID AND task.User_ID = ? ORDER BY subject.Subject_ID";
        new Functions().Tableview(qry, tableView, data, table);
    }

    @FXML
    private void cmb_Subject_Add(Event event) {
        Functions.cmb_Subject_Add_Item(cmbSubject);
    }

    @FXML
    private void btnTask_Upload_Browse(Event event) {
        FileChooser fileChooser = new FileChooser();
        selectedFile = fileChooser.showOpenDialog(null);
        fileName = selectedFile.getName();
        filePath = selectedFile.getAbsolutePath().replace("\\", "/");
        if (selectedFile != null) {
            txtTaskUploadBrowse.setText(filePath);
        }
    }
    static String des;

    @FXML
    private void btnTaskTeacher_Upload(Event event) {
        try {
            Functions.fetch_Cmb_Data(cmbSubject);
            temp = "task";
            dob = dateLastDate.getValue();
            des = txtTaskDescriptiopn.getText();

            qry = "INSERT INTO task (File_Name ,Status ,Description,Last_Date ,Subject_ID ,User_ID ) VALUES (?,?,?,?,?,?)";

            new Functions().upload_File(temp, qry, filePath, fileName);
            table = "load";
            qry = "SELECT File_Name,Last_Date,Status,Subject_Name FROM task,subject WHERE task.Subject_ID = subject.Subject_ID AND task.User_ID = ? ORDER BY subject.Subject_ID";
            new Functions().Tableview(qry, tableView, data, table);
            conn.close();
            txtTaskUploadBrowse.clear();
            txtTaskDescriptiopn.clear();
        } catch (SQLException e) {
            Functions.Notification("images/alert.png", e.getMessage());
        }

    }

    @FXML
    private void tableClick(Event event) {
        new Functions().fetchDataTable(tableView);
        txtTaskDelete.setText(split[0].replace("[", ""));
    }

    @FXML
    private void btnTask_Upload_deleteData(Event event) {
        try {
            if (txtTaskDelete.getText().equals("")) {

                Notification("images/alert.png", " Please enter subject code.");
            } else {
                new Functions().delete_File(txtTaskDelete.getText(), "DELETE FROM task WHERE File_Name = ? AND User_ID = ?");
                historyQry("File deleted and file name " + fileName + "");
                table = "load";
                qry = "SELECT File_Name,Last_Date,Status,Subject_Name FROM task,subject WHERE task.Subject_ID = subject.Subject_ID AND task.User_ID = ? ORDER BY subject.Subject_ID";
                new Functions().Tableview(qry, tableView, data, table);
                conn.close();
                txtTaskDelete.clear();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
