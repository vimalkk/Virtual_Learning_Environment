package virtual_learning_environment;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static virtual_learning_environment.Functions.*;

/**
 *
 * @author Virtual_Learning_Environment
 */
public class Controller {

    public static String id = null, subNm = null, subCode = null, cmbData = null;
    static Stage stage;
    public static Connection conn;
    public static Statement si;
    public static Statement show;
    public static PreparedStatement stmt;
    public static ResultSet rshow;
    public static ResultSetMetaData rmd;
    public static int cc;

    @FXML
    private JFXComboBox cmbCategory;

    @FXML
    public static AnchorPane pane, pane2;
    @FXML
    private AnchorPane anchorPaneHome, anchorPane, anchorPane2;

    //**************************************************************************************************
    // SignUp Form Code
    @FXML
    private JFXTextField txtSignupFirstNm, txtSignupMiddleNm, txtSignupLastNm, txtSignupMailId, txtSignupContactNo;
    @FXML
    private JFXPasswordField txtSignupPwd, txtSignupConPwd;
    @FXML
    private JFXRadioButton radioSignUpMale, radioSignUpFemale;
    @FXML
    private JFXDatePicker dateSignupBirthDate;

    static String gen, cat;

    @FXML
    private void btnSignUp_SignUp(Event event) {
        String otp;
        if (!txtSignupFirstNm.getText().equals("") && !txtSignupMiddleNm.getText().equals("") && !txtSignupLastNm.getText().equals("") && !txtSignupMailId.getText().equals("") && !txtSignupPwd.getText().equals("") && !txtSignupConPwd.getText().equals("")) {
            if (txtSignupPwd.getText().equals(txtSignupConPwd.getText())) {

                if (radioSignUpMale.isSelected()) {
                    gen = "male";
                } else if (radioSignUpFemale.isSelected()) {
                    gen = "female";
                }
                if (cmbCategory.getValue() == null) {
                    Notification("images/warning.png", "Please SELECT category.");
                } else {
                    cat = cmbCategory.getValue().toString().toLowerCase();
                    firstNm = txtSignupFirstNm.getText();
                    MidNm = txtSignupMiddleNm.getText();
                    Lastnm = txtSignupLastNm.getText();
                    contact = txtSignupContactNo.getText();
                    mail = txtSignupMailId.getText();
                    pwd = Functions.getMD5(txtSignupPwd.getText());
                    dob = dateSignupBirthDate.getValue();

                    if (Functions.validateMail(mail) == false) {
                        Notification("images/warning.png", "Please enter valid mail id.");
                        txtSignupMailId.setText("");
                    } else if (Functions.validateMail(mail) == true) {
                        try {
                            DBConnection();
                            stmt = conn.prepareStatement("SELECT User_ID FROM user WHERE Mail_ID = ? AND Category = ?");
                            stmt.setString(1, mail);
                            stmt.setString(2, cat);
                            rshow = stmt.executeQuery();

                            while (rshow.next()) {
                                temp = rshow.getString(1);
                            }
                            stmt.close();
                            rshow.close();
                            conn.close();

                            if (temp == null) {
                                sendMail(mail, "user");
                                new Functions().otpPane("OTP_Mail_FRM.fxml");
                            }
                        } catch (SQLException ex) {
                            Notification("images/alert.png", ex.getLocalizedMessage());
                        }
                    }
                }
            } else {
                Notification("images/warning.png", "Password AND confirm password should be equal...!!!");
            }
        } else {
            Notification("images/alert.png", "Please fill all details...!!!");
        }
    }

    @FXML
    private void btnSignUp_Clear(Event event) {
        txtSignupFirstNm.setText("");
        txtSignupMiddleNm.setText("");
        txtSignupLastNm.setText("");
        txtSignupMailId.setText("");
        txtSignupPwd.setText("");
        txtSignupConPwd.setText("");
        txtSignupContactNo.setText("");
        cmbCategory.setValue("Category");
        radioSignUpMale.setSelected(true);
        dateSignupBirthDate.setValue(LocalDate.now());

    }

    //***************************************************************************************************
    // Forgote Password Form Code
    @FXML
    JFXTextField txtForgotMailId;

    @FXML
    private void btnForgot_Next(Event event) {
        mail = txtForgotMailId.getText();
        cat = cmbCategory.getValue().toString();
        if (Functions.validateMail(mail) == true) {
            sendMail(mail, "user");
            new Functions().otpPane("OTP_Forgot_FRM.fxml");
        } else {
            Notification("images/alert.png", "Please enter valid mail id...!!!");
            txtForgotMailId.setText("");
        }

    }

    @FXML
    JFXPasswordField txtForgotNewPwd, txtForgotConPwd;

    @FXML
    private void btnForgot_ChangePwd(Event event) {
        pwd = txtForgotNewPwd.getText();
        conPwd = txtForgotConPwd.getText();
        try {
            if (bool == false) {
                Notification("images/alert.png", "Please verify your mail first...!!!.");
                txtForgotNewPwd.setText("");
                txtForgotConPwd.setText("");
            } else if (bool == true) {
                if (pwd.equals(conPwd)) {
                    Functions.DBConnection();
                    stmt = conn.prepareStatement("UPDATE user SET Password=? WHERE Mail_ID=? AND Category=?");
                    stmt.setString(1, Functions.getMD5(pwd));
                    stmt.setString(2, mail);
                    stmt.setString(3, cat);
                    stmt.executeUpdate();
                    historyQry("Password reset.");
                    new Functions().createStage("Login_FRM.fxml", pane);
                    Notification("images/confirm.png", "Password successfuly changed");
                    conn.close();
                } else {
                    Notification("images/alert.png", "New password AND confirm password must be same...!!!");
                    txtForgotNewPwd.setText("");
                    txtForgotConPwd.setText("");
                }
            }
        } catch (SQLException e) {
            Notification("images/alert.png", e.getMessage());
        }
    }

    //***************************************************************************************************
    // OTP Mail Form Code
    @FXML
    JFXTextField txtOtpMail;
    static String firstNm, MidNm, Lastnm, mail, pwd, conPwd, contact;
    static LocalDate dob;

    @FXML
    private void mailOTP(Event event) {
        try {
            if (txtOtpMail.getText().equals(Functions.strOtp)) {
                Functions.DBConnection();
                stmt = conn.prepareStatement("INSERT INTO user (First_Name ,Middle_Name ,Last_Name ,Gender ,Category ,Contact ,Mail_ID ,Password ,Date_Of_Birth) VALUES (?,?,?,?,?,?,?,?,?)");

                stmt.setString(1, firstNm);
                stmt.setString(2, MidNm);
                stmt.setString(3, Lastnm);
                stmt.setString(4, gen);
                stmt.setString(5, cat);
                stmt.setString(6, contact);
                stmt.setString(7, mail);
                stmt.setString(8, pwd);
                stmt.setDate(9, Date.valueOf(dob));
                stmt.executeUpdate();
                historyQry("Create account.");
                conn.close();
                new Functions().createStage("Login_FRM.fxml", pane);
                stage.close();
                Notification("images/confirm.png", "Your account is created.");
            } else {
                Notification("images/alert.png", "Please enter correct OTP...!!!");
            }
        } catch (SQLException e) {
            Notification("images/alert.png", e.getMessage());
        }
    }
    //***************************************************************************************************
    //OTP Forgote Form Code
    @FXML
    JFXTextField txtOtpForgot;

    static String otpForgot;
    static boolean bool = false;

    @FXML
    private void forgotOTP(Event event) {

        otpForgot = txtOtpForgot.getText();
        if (otpForgot.equals(Functions.strOtp)) {
            bool = true;
            stage.close();
        } else {
            bool = false;
            Notification("images/alert.png", "Please enter correct OTP...!!!");
        }
    }

    //***************************************************************************************************
    // Login Form Code
    @FXML
    private JFXTextField txtLoginMailId;
    @FXML
    private JFXPasswordField txtLoginPwd;
    @FXML
    public static JFXButton btnIndexLogIn, btnIndexSignUp;
    static String tempPwd, tempCat;

    @FXML
    private void btnLogIn_LogIn(Event event) {
        id = null;
        mail = txtLoginMailId.getText();
        pwd = Functions.getMD5(txtLoginPwd.getText());
        cat = cmbCategory.getValue().toString().toLowerCase();
        if ((!txtLoginMailId.getText().equals("") || !txtLoginPwd.getText().equals("")) || !cat.equals("")) {
            if (Functions.validateMail(mail) == true) {
                try {
                    Functions.DBConnection();
                    stmt = conn.prepareStatement("SELECT User_ID FROM user WHERE Mail_ID=? AND Category=? AND Password=?");
                    stmt.setString(1, mail);
                    stmt.setString(2, cat);
                    stmt.setString(3, pwd);
                    rshow = stmt.executeQuery();

                    while (rshow.next()) {
                        id = rshow.getString(1);
                    }
                    if (id != null) {
                        historyQry("User login");
                        conn.close();
                        if (cat.equals("teacher")) {
                            new Functions().createStage("Teacher_FRM.fxml", pane);
                        } else if (cat.equals("student")) {
                            new Functions().createStage("Student_FRM.fxml", pane);
                        }
                    } else {
                        Notification("images/warning.png", "Email ID or Password is invelid...!!!.");
                        txtLoginMailId.setText("");
                    }
                } catch (SQLException ex) {
                    Notification("images/alert.png", ex.getLocalizedMessage());
                }
            } else {
                Notification("images/warning.png", "Please enter valid mail id.");
                txtLoginMailId.setText("");
            }
        } else {
            Notification("images/alert.png", "Mail ID ,Category or Password should not null.");
        }
    }

    @FXML
    private void btnLogIn_Forgot_Pwd(Event event) {
        new Functions().createStage("ForgotPassword_FRM.fxml", pane);
    }

    @FXML
    private void btnLogin_Clear(Event event) {
        txtLoginMailId.setText("");
        txtLoginPwd.setText("");
        cmbCategory.setValue("Category");
        txtLoginMailId.requestFocus();
    }

    //***************************************************************************************************
    // Profile Form Code
    @FXML
    private void editProfile(Event event) {
        new Functions().createStage("Profile_Edit_FRM.fxml", anchorPane2);
    }

    @FXML
    private void showProfile(Event event) {
        new Functions().createStage("Profile_Show_FRM.fxml", anchorPane2);
    }

    //***************************************************************************************************
    // Data Form Code
    @FXML
    private void btnData_DownloadFile(Event event) {
        new Functions().createStage("Data_Download_FRM.fxml", anchorPane2);
    }

    @FXML
    private void btnData_UploadFile(Event event) {
        new Functions().createStage("Data_Upload_FRM.fxml", anchorPane2);
    }

    //***************************************************************************************************
    // Teacher Task Form Code
    @FXML
    private void taskTeacher_UploadTask(Event event) {
        new Functions().createStage("Task_Upload_FRM.fxml", anchorPane2);
    }

    @FXML
    private void taskTeacher_SubmitTask(Event event) {
        new Functions().createStage("Task_Submitted_FRM.fxml", anchorPane2);
    }

    //***************************************************************************************************
    // Common Code
    @FXML
    private void cmb_Add(Event event) {
        cmb_Add_Item(cmbCategory);
    }
}
