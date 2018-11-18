/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_learning_environment;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import static virtual_learning_environment.Controller.*;
import static virtual_learning_environment.Functions.*;

/**
 * FXML Controller class
 *
 * @author Mr.XxX
 */
public class Sub_Create_FRMController implements Initializable {

    @FXML
    private JFXTextField txtCreateSubNm, txtCreateSubCode, txtDeleteSub;
    @FXML
    private TableView tableView;
    private ObservableList<ObservableList> data;

    @FXML
    private void btnTeacher_GenerateCode(Event event) {
        strOtp = Arrays.toString(Functions.generateSubCode()).replace(",", "").replace("[", "").replace("]", "").replace(" ", "");
        txtCreateSubCode.setText(strOtp);
    }

    @FXML
    private void btnSub_Create_AddSubject(Event event) {
        try {
            if (!txtCreateSubCode.getText().equals("") && !txtCreateSubNm.getText().equals("")) {
                table = "load";
                Functions.DBConnection();
                stmt = conn.prepareStatement("INSERT INTO subject (Subject_Name,Subject_Code,User_ID) VALUES (?,?,?)");
                stmt.setString(1, txtCreateSubNm.getText());
                stmt.setString(2, txtCreateSubCode.getText());
                stmt.setString(3, id);
                stmt.executeUpdate();
                historyQry("Subject created [" + txtCreateSubNm.getText() + "," + txtCreateSubCode.getText() + "]");
                qry = "SELECT Subject_Name,Subject_Code from subject WHERE User_ID = ? ORDER BY Subject_Name";
                new Functions().Tableview(qry, tableView, data, table);
                Notification("images/confirm.png", "Subject successfuly Created.");
                txtCreateSubCode.clear();
                txtCreateSubNm.clear();
            } else {
                Notification("images/alert.png", "Enter subject name properly...!!!");
                txtCreateSubCode.clear();
                txtCreateSubNm.clear();
            }
        } catch (SQLException e) {
            Notification("images/alert.png", "Subject already exist...!!!");
            txtCreateSubCode.clear();
            txtCreateSubNm.clear();
        }
    }

    @FXML
    private void tableClick() {
        new Functions().fetchDataTable(tableView);
        txtDeleteSub.setText(split[1].replace("]", "").replace(" ", ""));
    }

    @FXML
    private void deleteSub() {
        try {
            if (txtDeleteSub.getText().equals("")) {
                Notification("images/alert.png", " Please enter subject code.");
            } else {
                Functions.DBConnection();
                stmt = conn.prepareStatement("DELETE FROM subject WHERE Subject_Code = ? AND User_ID=?");
                stmt.setString(1, split[1].replace("]", "").replace(" ", ""));
                stmt.setString(2, id);
                stmt.executeUpdate();
                table = "load";
                qry = "SELECT Subject_Name,Subject_Code FROM subject WHERE User_ID = ? ORDER BY Subject_Name";
                new Functions().Tableview(qry, tableView, data, table);
                historyQry("Subject deleted [" + split[1].replace("]", "").replace(" ", "") + "].");
                conn.close();
                Notification("images/confirm.png", " Subject successfuly deleted.");
                txtDeleteSub.clear();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
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
        qry = "SELECT Subject_Name,Subject_Code from subject WHERE User_ID = ? ORDER BY Subject_Name";
        new Functions().Tableview(qry, tableView, data, table);
    }
}
