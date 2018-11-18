/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_learning_environment;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import static virtual_learning_environment.Controller.conn;
import static virtual_learning_environment.Controller.gen;
import static virtual_learning_environment.Controller.id;
import static virtual_learning_environment.Controller.pane;
import static virtual_learning_environment.Controller.rshow;
import static virtual_learning_environment.Controller.stmt;
import static virtual_learning_environment.Functions.Notification;

/**
 * FXML Controller class
 *
 * @author Mr.XxX
 */
public class Profile_Edit_FRMController implements Initializable {

    static LocalDate dob;
    String fn, mn, ln, c;
    @FXML
    private JFXTextField txtProfileFirstNm, txtProfileMiddleNm, txtProfileLastNm, txtProfileContactNo, txtProfileMailId;
    @FXML
    private JFXRadioButton radioProfileFemale;
    @FXML
    private JFXRadioButton radioProfileMale;
    @FXML
    private JFXDatePicker dateProfileBirthDate;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @FXML
    private void btnProfile_Edit_Update(Event event) {
        if (radioProfileMale.isSelected()) {
            gen = "male";
        } else if (radioProfileFemale.isSelected()) {
            gen = "female";
        }
        try {
            Functions.DBConnection();
            stmt = conn.prepareStatement("UPDATE user SET First_Name=?, Middle_Name=?, Last_Name=?, Gender=?, Contact=?,Date_Of_Birth=? WHERE User_ID=?");
            stmt.setString(1, txtProfileFirstNm.getText());
            stmt.setString(2, txtProfileMiddleNm.getText());
            stmt.setString(3, txtProfileLastNm.getText());
            stmt.setString(4, gen);
            stmt.setString(5, txtProfileContactNo.getText());
            stmt.setDate(6, Date.valueOf(dateProfileBirthDate.getValue()));
            stmt.setString(7, id);
            stmt.executeUpdate();
            conn.close();
            Notification("images/confirm.png", "Profile successfuly updated.");
            new Functions().createStage("Profile_Show_FRM.fxml", pane);
        } catch (SQLException e) {
            Functions.Notification("images/alert.png", e.getMessage());
        }
    }

    @FXML
    private void btnProfile_Cancle(Event event) {
        new Functions().createStage("Profile_Show_FRM.fxml", pane);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Functions.DBConnection();
            stmt = conn.prepareStatement("SELECT First_Name, Middle_Name, Last_Name, Gender, Contact, Mail_ID, Date_Of_Birth from user WHERE User_ID=?");
            stmt.setString(1, id);
            rshow = stmt.executeQuery();
            while (rshow.next()) {
                txtProfileFirstNm.setText(rshow.getString(1));
                txtProfileMiddleNm.setText(rshow.getString(2));
                txtProfileLastNm.setText(rshow.getString(3));
                gen = rshow.getString(4);
                if (gen.equals("male")) {
                    radioProfileMale.setSelected(true);
                } else if (gen.equals("female")) {
                    radioProfileFemale.setSelected(true);
                }
                txtProfileContactNo.setText(rshow.getString(5));
                txtProfileMailId.setText(rshow.getString(6));
                dateProfileBirthDate.setValue(LocalDate.parse(rshow.getString(7)));
            }
            conn.close();

        } catch (SQLException ex) {
            Functions.Notification("images/warning.png", ex.getMessage());
        }
    }
}
