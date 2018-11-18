/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_learning_environment;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
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
 * @author MAKWANA
 */
public class Sub_Add_FRMController implements Initializable {

    @FXML
    private JFXTextField txtSubCode, txtDeleteSub;
    @FXML
    private TableView tableView;
    private ObservableList<ObservableList> data;

    /**
     * Initializes the controller class.
     */
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
                stmt = conn.prepareStatement("DELETE FROM subject WHERE Subject_Code = ? and User_ID = ?");
                stmt.setString(1, txtDeleteSub.getText());
                stmt.setString(2, id);
                stmt.executeUpdate();
                historyQry("Subject deleted " + txtDeleteSub.getText() + "");
                table = "load";
                qry = "SELECT Subject_Name,Subject_Code FROM subject WHERE User_ID=? ORDER BY Subject_Name";
                new Functions().Tableview(qry, tableView, data, table);
                conn.close();
                Notification("images/confirm.png", " Subject successfuly deleted.");
                txtDeleteSub.clear();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void btnSub_Add_AddSubject(Event event) {
        try {
            if (!txtSubCode.getText().equals("")) {
                table = "load";
                Functions.DBConnection();
                stmt = conn.prepareStatement("SELECT Subject_Name FROM subject WHERE Subject_Code=? ORDER BY Subject_Name");
                stmt.setString(1, txtSubCode.getText());
                rshow = stmt.executeQuery();
                while (rshow.next()) {
                    subNm = rshow.getString(1);
                }
                stmt = conn.prepareStatement("INSERT INTO subject (Subject_Name,Subject_Code,User_ID) VALUES (?,?,?)");
                stmt.setString(1, subNm);
                stmt.setString(2, txtSubCode.getText());
                stmt.setString(3, id);
                stmt.executeUpdate();
                conn.close();
                txtSubCode.setText("");
                Notification("images/confirm.png", " Subject successfuly added.");
                qry = "SELECT Subject_Name,Subject_Code FROM subject WHERE User_ID = ?  ORDER BY Subject_Name";
                new Functions().Tableview(qry, tableView, data, table);
            } else {
                Notification("images/alert.png", "Enter subject name properly...!!!");
                txtSubCode.clear();
            }
        } catch (SQLException e) {
            Functions.Notification("images/alert.png", "Subject already exist...!!!");
            txtSubCode.clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table = null;
        qry = "SELECT Subject_Name,Subject_Code FROM subject WHERE User_ID = ?  ORDER BY Subject_Name";
        new Functions().Tableview(qry, tableView, data, table);
    }
}
