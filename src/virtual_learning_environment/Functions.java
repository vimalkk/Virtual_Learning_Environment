package virtual_learning_environment;

import Cloudme.CloudmeException;
import Cloudme.CloudmeUser;
import com.jfoenix.controls.JFXComboBox;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.controlsfx.control.Notifications;
import static virtual_learning_environment.Controller.*;
import static virtual_learning_environment.Data_Upload_FRMController.*;
import static virtual_learning_environment.Queries_Feedback_FRMController.*;
import static virtual_learning_environment.Task_Student_FRMController.id1;
import static virtual_learning_environment.Task_Upload_FRMController.des;

/**
 *
 * @author Virtual_Learning_Environment
 */
class Functions {

    static String strOtp, temp;
    static File selectedFile;
    static String[] split = null;

    /**
     *
     * @param fxmlFile
     * @param anchorPaneHome
     */
    //***************************************************************************************************
    //DBConnection Code
    public static void DBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://172.16.100.11:3306/vle", "root", " ");
            //conn = DriverManager.getConnection("jdbc:mysql://192.168.0.3:3306/vle", "root", " ");
        } catch (ClassNotFoundException | SQLException ex) {
            Notification("images/warning.png", ex.getMessage());
        }
    }
    //***************************************************************************************************
    //Create Stage Code 

    public void createStage(String fxmlFile, AnchorPane anchorPaneHome) {
        try {
            pane = FXMLLoader.load(getClass().getResource(fxmlFile));
            anchorPaneHome.getChildren().setAll(pane);
        } catch (IOException e) {
            Notification("images/alert.png", e.getMessage());
        }
    }
    
     //***************************************************************************************************
    //Fetch Subject Id Code
    public void subject_ID()
    {
        try {
            DBConnection();
            stmt = conn.prepareStatement("SELECT Subject_ID FROM subject WHERE Subject_Code = ? AND User_ID = ? ORDER BY subject.Subject_ID");
            stmt.setString(1, subCode);
            stmt.setString(2, id);
            rshow = stmt.executeQuery();
            while (rshow.next()) {
                subID = rshow.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //***************************************************************************************************
    //Table Code
    static String table = null, qry = null;
    static TableColumn col;
    public static ObservableList<ObservableList> data;

    public void Tableview(String qry, TableView tableName, ObservableList<ObservableList> data, String str) {
        try {
            Functions.DBConnection();
            stmt = conn.prepareStatement(qry);
            
                stmt.setString(1, id);

            rshow = stmt.executeQuery();
            rmd = rshow.getMetaData();
            cc = rmd.getColumnCount();
            data = FXCollections.observableArrayList();
            ObservableList<String> row;

            for (int i = 0; i < cc; i++) {
                final int j = i;
                col = new TableColumn(rshow.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                if (str == null) {
                    tableName.getColumns().addAll(col);
                }
            }
            while (rshow.next()) {
                row = FXCollections.observableArrayList();
                for (int i = 1; i <= cc; i++) {
                    row.add(rshow.getString(i));
                }
                data.add(row);
            }
            tableName.setItems(data);

        } catch (SQLException e) {
            Notification("images/alert.png", e.getMessage());
        }

    }

    public void fetchDataTable(TableView tableView) {
        int row = tableView.getSelectionModel().getSelectedIndex();
        String selected = tableView.getItems().get(row).toString();
        split = selected.split(",");
    }

    //***************************************************************************************************
    //Combobox Add Item Code
    public static void cmb_Add_Item(JFXComboBox cmb) {
        cmb.getItems().clear();
        cmb.getItems().addAll("Student", "Teacher");
    }

    public static void cmb_Subject_Add_Item(JFXComboBox cmb) {
        try {
            cmb.getItems().clear();
            Functions.DBConnection();
            stmt = conn.prepareStatement("SELECT Subject_Name,Subject_Code FROM subject where User_ID = ?");
            stmt.setString(1, id);
            rshow = stmt.executeQuery();
            while (rshow.next()) {
                cmb.getItems().addAll(rshow.getString(1) + "-" + rshow.getString(2));
            }
        } catch (SQLException e) {
            Notification("images/alert.png", e.getMessage());
        }
    }

    //***************************************************************************************************
    //Fetch Cmb Data
    public static void fetch_Cmb_Data(JFXComboBox cmb) {
        split = cmb.getValue().toString().split("-");
        subNm = split[0];
        subCode = split[1];

    }

    //***************************************************************************************************
    //OTP Pane Code
    public void otpPane(String str) {
        try {
            stage = new Stage();
            AnchorPane anchorPane = (AnchorPane) FXMLLoader.load(getClass().getResource(str));
            Scene scene = new Scene(anchorPane);
            stage.getIcons().add(new Image("images/logo.jpg"));
            stage.setTitle("OTP Authentication");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Notification("images/alert.png", e.getMessage());
        }
    }

    //***************************************************************************************************
    //Generate OTP For Number Code
    public static char[] generateOTP(int length) {
        String numbers = "0123456789";
        Random random = new Random();
        char[] otp = new char[length];
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return otp;
    }

    //***************************************************************************************************
    //Generate OTP For Subject Code
    public static char[] generateSubCode() {
        String numbers = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        char[] otp = new char[6];
        for (int i = 0; i < 6; i++) {
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return otp;
    }

    //***************************************************************************************************
    //History Query Code
    public static void historyQry(String str) {
        String id = null;
        try {
            LocalTime nowTime = LocalTime.now();
            Time time = Time.valueOf(nowTime);
            LocalDate nowDate = LocalDate.now();
            Date date = Date.valueOf(nowDate);
            stmt = conn.prepareStatement("SELECT User_ID FROM user WHERE Mail_ID=? AND Category=?");
            stmt.setString(1, mail);
            stmt.setString(2, cat);
            rshow = stmt.executeQuery();
            while (rshow.next()) {
                id = rshow.getString(1);
            }

            stmt = conn.prepareStatement("INSERT INTO history (Activity ,Date ,Time ,User_ID) VALUES (?,?,?,?)");
            stmt.setString(1, str);
            stmt.setDate(2, date);
            stmt.setTime(3, time);
            stmt.setString(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Notification("images/alert.png", e.getMessage());
        }

    }

    //***************************************************************************************************
    //Upload File Code
    @FXML
    TableView tableView;

    public void upload_File(String temp, String qry, String path, String fileName) {
        try {

            CloudmeUser user = new CloudmeUser("vle", "vle@12345");
            subject_ID();
            stmt = conn.prepareStatement(qry);
            if (temp.equals("upload")) {
                stmt.setString(1, fileName);
                stmt.setString(2, subID);
                stmt.setString(3, id);
                stmt.executeUpdate();
                user.getFileManager().uploadFile(path, "/Virtual_Learning_Environment/Data/");
                user.killUser();
                historyQry("Data uploaded and file name is " + fileName + "");
            } else if (temp.equals("task")) {
                stmt.setString(1, fileName);
                stmt.setString(2, "upload");
                stmt.setString(3, des);
                stmt.setDate(4, Date.valueOf(dob));
                stmt.setString(5, subID);
                stmt.setString(6, id);
                stmt.executeUpdate();
                user.getFileManager().uploadFile(path, "/Virtual_Learning_Environment/Task/");
                user.killUser();
                historyQry("Task uploaded and file name is " + fileName + "");
            } else {
                Notification("images/alert.png", "Error");
            }
        } catch (CloudmeException | SQLException e) {
            Notification("images/alert.png", e.getLocalizedMessage());
        }
    }

    //***************************************************************************************************
    //Download File Code
    public void download_File(String fileName) {
        try {
            File file = new File(fileName);
            CloudmeUser user = new CloudmeUser("vle", "vle@12345");
            user.getFileManager().downloadFile("/Virtual_Learning_Environment/" + temp + "/" + fileName + "", file.getAbsolutePath());
            user.killUser();
            Notification("images/confirm.png", "Document successfuly download to " + file.getAbsolutePath() + ".");
        } catch (CloudmeException e) {
            Notification("images/alert.png", e.getMessage());
        }
    }

    //***************************************************************************************************
    //Download File Code
    public void delete_File(String fileName, String qry) {
        try {
            DBConnection();
            stmt = conn.prepareStatement(qry);
            stmt.setString(1, fileName);
            stmt.setString(2, subID);
            stmt.setString(3, id);
            stmt.executeUpdate();

            CloudmeUser user = new CloudmeUser("vle", "vle@12345");
            user.getFileManager().deleteFile("/Virtual_Learning_Environment/" + temp + "/" + fileName + "");
            user.killUser();
            Notification("images/confirm.png", "Document deleted.");
        } catch (CloudmeException | SQLException e) {
            Notification("images/alert.png", e.getMessage());
        }
    }

    //***************************************************************************************************
    //Notification Code
    public static void Notification(String path, String msg) {
        Image img = new Image(path);
        Notifications.create()
                .text(msg)
                .graphic(new ImageView(img))
                .position(Pos.BOTTOM_RIGHT)
                .show();
    }

    //***************************************************************************************************
    //MD5 Code
    /*
	 * String to MD5 converter
	 * Source : http://www.asjava.com/core-java/java-md5-example/
	 * @author Virtual_Learning_Environment
     */
    public static String getMD5(String input) {
        String hashtext = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            Notification("images/alert.png", e.getMessage());
        }
        return hashtext;
    }

    //***************************************************************************************************
    //Valid Mail Code
    public static boolean validateMail(String email) {
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email);
        return matcher.find();
    }

    //***************************************************************************************************
    //Send Mail Code
    public static void sendMail(String to, String status) {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("virtuallearningenvironment123@gmail.com", "vle@12345");
            }
        });

        try {
            strOtp = Arrays.toString(Functions.generateOTP(6)).replace(",", "").replace("[", "").replace("]", "").replace(" ", "");
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress("virtuallearningenvironment123@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            if (status.equals("user")) {
                message.setSubject("Authentication");
                message.setText("Your verification code is : " + strOtp);
            } else if (status.equals("query")) {
                message.setSubject(commentSubject);
                message.setText("Mail ID : " + mail + "\n" + commentDesc);
            }
            Transport.send(message);
        } catch (MessagingException e) {
            Notification("images/alert.png", e.getMessage());
        }
    }
}
