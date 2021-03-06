package controller;

import bo.BoFactory;
import bo.custom.LoginBo;
import dto.LoginDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.regex.Pattern;

public class LoginFormController {
    public TextField txtUserName;
    public PasswordField txtPassword;
    public AnchorPane root;

    LoginBo bo;

    public void initialize(){
        bo = BoFactory.getInstance().getBo(BoFactory.BOType.LOGIN);
    }

    public void txtUserNameOnAction(ActionEvent actionEvent) {
        if (Pattern.compile("^[A-z|0-9]{1,50}$").matcher(txtUserName.getText().trim()).matches()) {
            txtUserName.setStyle("-fx-border-color:  #45aaf2;");
            txtPassword.requestFocus();
        } else {
            txtUserName.setStyle("-fx-border-color:  #eb3b5a; -fx-border-width: 3");
            txtUserName.requestFocus();
        }
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) throws Exception {
        if (Pattern.compile("^[A-z|0-9]{1,50}$").matcher(txtPassword.getText().trim()).matches()) {
            txtPassword.setStyle("-fx-border-color:  #45aaf2;");
            LoginDTO dto = bo.checkLogin(new LoginDTO(txtUserName.getText().trim(),txtPassword.getText().trim()));
            if(dto != null){
                    Notifications notificationBuilder = Notifications.create()
                            .title("Login Successfully.!")
                            .text("You have Successfully login to the System.")
                            .graphic(new ImageView(new Image("/assert/done.png")))
                            .hideAfter(Duration.seconds(4))
                            .position(Pos.BOTTOM_RIGHT);
                            notificationBuilder.darkStyle();
                            notificationBuilder.show();

                    this.root.getChildren().clear();
                    this.root.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/MainForm.fxml")));
                }else {
                    Notifications notificationBuilder = Notifications.create()
                            .title("Login Error.!")
                            .text("Password does not match, Try Again..!")
                            .graphic(new ImageView(new Image("/assert/errorpng.png")))
                            .hideAfter(Duration.seconds(4))
                            .position(Pos.BOTTOM_RIGHT);
                    notificationBuilder.darkStyle();
                    notificationBuilder.show();
                    txtPassword.setStyle("-fx-border-color:  #eb3b5a; -fx-border-width: 3;");
                    txtPassword.requestFocus();
                }
        } else {
            txtPassword.setStyle("-fx-border-color:  #eb3b5a; -fx-border-width: 3;");
            txtPassword.requestFocus();
        }

    }

    public void btnLoginOnAction(ActionEvent actionEvent) throws Exception {
        if(txtUserName.getText().trim().equalsIgnoreCase(null)){
            txtUserName.setStyle("-fx-border-color:  #eb3b5a; -fx-border-width: 3;");
            txtUserName.requestFocus();
        }else{
            txtUserNameOnAction(actionEvent);
        }
        if(txtPassword.getText().trim().equalsIgnoreCase(null)){
            txtPassword.setStyle("-fx-border-color:  #eb3b5a; -fx-border-width: 3;");
            txtPassword.requestFocus();
        }else {
            txtPasswordOnAction(actionEvent);
        }
    }

    public void hyperNewAccountOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/view/RegistrationForm.fxml"))));
    }

    public void imgExitOnAction(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void imgMinimizeOnAction(MouseEvent mouseEvent) {
        Stage s = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        s.setIconified(true);
    }
}


