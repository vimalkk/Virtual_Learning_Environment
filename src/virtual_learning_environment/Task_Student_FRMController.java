/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_learning_environment;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import static virtual_learning_environment.Functions.*;
import static virtual_learning_environment.Controller.*;

/**
 * FXML Controller class
 *
 * @author Mr.XxX
 */
public class Task_Student_FRMController implements Initializable {
    public static String id1;
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
    @FXML
    private void cmb_Subject_Add(Event event) {
        Functions.cmb_Subject_Add_Item(cmbSubject);
    }

    @FXML
    private void tableClick(Event event) {
        new Functions().fetchDataTable(tableView);
    }

    @FXML
    public void task_student_search(Event event){
        try {
            System.out.println("Hello");
            Functions.fetch_Cmb_Data(cmbSubject);
            System.out.println(1);
            new Functions().subject_ID();
            System.out.println(2);
            System.out.println(subCode);
            
            DBConnection();
            stmt=conn.prepareStatement("select User.User_ID from user,subject where subject.User_ID=user.User_ID and subject.Subject_Code=? ORDER by user.Category DESC LIMIT 1");
            System.out.println(3);
            stmt.setString(1,subCode);
            System.out.println(3.1);
            rshow=stmt.executeQuery();
            System.out.println(4);
            while(rshow.next())
            {
                System.out.println(5);
                id1=rshow.getString(1);
                System.out.println(id1);
                System.out.println(subCode);
            }
            System.out.println(6);
            temp="teacher";
            System.out.println(7);
            table = null;
            System.out.println(8);
            qry = "SELECT File_Name,Last_Date,Description,Subject_Name from task,subject,user WHERE task.Subject_id=subject.Subject_ID AND task.User_ID = ? AND task.Subject_ID = ?";
            System.out.println(9);
            new Functions().Tableview(qry, tableView, data, table);
            System.out.println(10);
        } catch (SQLException ex) {
            System.out.println(11);
            Logger.getLogger(Task_Student_FRMController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        table = null;
//        qry = "SELECT File_Name,Last_Date,Description,Subject_Name from task,subject,user WHERE task.Subject_id=subject.Subject_ID AND task.User_ID=user.User_ID AND user.User_ID=?";
//        new Functions().Tableview(qry, tableView, data, table);
    }
}
