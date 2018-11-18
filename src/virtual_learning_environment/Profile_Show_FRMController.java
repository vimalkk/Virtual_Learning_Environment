/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_learning_environment;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import static virtual_learning_environment.Controller.conn;
import static virtual_learning_environment.Controller.id;
import static virtual_learning_environment.Controller.rshow;
import static virtual_learning_environment.Controller.stmt;

/**
 * FXML Controller class
 *
 * @author Mr.XxX
 */
public class Profile_Show_FRMController implements Initializable {

    @FXML
    private Label lblFullNm;
    @FXML
    private Label lblGen;
    @FXML
    private Label lblCat;
    @FXML
    private Label lblCon;
    @FXML
    private Label lblMail;
    @FXML
    private Label lblDob;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Functions.DBConnection();
            stmt = conn.prepareStatement("select First_Name, Middle_Name, Last_Name, Gender, Category, Contact, Mail_ID, Date_Of_Birth from user where User_ID=?");
            stmt.setString(1, id);
            rshow = stmt.executeQuery();
            while (rshow.next()) {
                lblFullNm.setText(rshow.getString(1) + " " + rshow.getString(2) + " " + rshow.getString(3));
                lblGen.setText(rshow.getString(4));
                lblCat.setText(rshow.getString(5));
                lblCon.setText(rshow.getString(6));
                lblMail.setText(rshow.getString(7));
                lblDob.setText(rshow.getString(8));
            }

        } catch (SQLException e) {
            Functions.Notification("images/alert.png", e.getMessage());
        }
    }
}
