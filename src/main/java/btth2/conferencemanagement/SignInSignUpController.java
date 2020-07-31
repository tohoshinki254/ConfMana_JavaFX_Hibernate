package btth2.conferencemanagement;

import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

import static btth2.conferencemanagement.MainController.*;

public class SignInSignUpController {
    @FXML
    private TextField inputUsernameLogin;

    @FXML
    private TextField inputPasswordLogin;

    @FXML
    private Button loginButton;

    @FXML
    private Label logupLabel;

    @FXML
    private Pane loginPane;

    @FXML
    private Pane logupPane;

    @FXML
    private TextField inputUsernameLogup;

    @FXML
    private TextField inputPasswordLogup;

    @FXML
    private TextField confirmPassword;

    @FXML
    private TextField inputNameLogup;

    @FXML
    private TextField inputEmailLogup;

    @FXML
    private Button signupButtonLogup;

    @FXML
    private Button backButtonLogup;

    @FXML
    private Button backButtonLogin;

    @FXML
    private void setLogupLabel(MouseEvent event) {
        event.consume();
        loginPane.setVisible(false);
        logupPane.setVisible(true);
    }

    private void showAlertNotify(String notify, boolean success, int scene) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(notify);

        if (success && scene == 1) {
            inputUsernameLogin.setText("");
            inputPasswordLogin.setText("");
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
        }

        if (success && scene == 2) {
            inputUsernameLogup.setText("");
            inputPasswordLogup.setText("");
            confirmPassword.setText("");
            inputNameLogup.setText("");
            inputEmailLogup.setText("");
        }
        alert.showAndWait();
    }

    @FXML
    private void setBackButtonLogup(ActionEvent event) {
        event.consume();
        loginPane.setVisible(true);
        logupPane.setVisible(false);
        inputUsernameLogup.setText("");
        inputPasswordLogup.setText("");
        confirmPassword.setText("");
        inputNameLogup.setText("");
        inputEmailLogup.setText("");
    }

    @FXML
    private void setSignupButtonLogup(ActionEvent event) {
        event.consume();
        Session session = HibernateSession.getSession();
        String hashPass = BCrypt.withDefaults().hashToString(12, inputPasswordLogup.getText().toCharArray());
        int newId = 0;
        boolean usernameUsed = false, emailUsed = false;
        
        AccountEntity newAccount = new AccountEntity();
        try {
            String hql = "FROM AccountEntity ";
            Query query = session.createQuery(hql);
            List<AccountEntity> temp = query.list();
            for (AccountEntity account : temp) {
                newId = account.getIdAccount();
                if (account.getUsername().equals(inputUsernameLogup.getText())) {
                    usernameUsed = true;
                    showAlertNotify("Tên đăng nhập đã được sử dụng!", false, 2);
                }
                if (account.getEmail().equals(inputEmailLogup.getText())) {
                    emailUsed = true;
                    showAlertNotify("Email đã được sử dụng!", false, 2);
                }
            }

            if (!inputPasswordLogup.getText().equals(confirmPassword.getText())) {
                showAlertNotify("Xác nhận tài khoản chưa đúng!", false, 2);
            }
            else if (!usernameUsed && !emailUsed) {
                newAccount.setIdAccount(newId + 1);
                newAccount.setTen(inputNameLogup.getText());
                newAccount.setUsername(inputUsernameLogup.getText());
                newAccount.setPassword(hashPass);
                newAccount.setEmail(inputEmailLogup.getText());
                newAccount.setState(1);
                newAccount.setRole(1);
                newAccount.setSoHoiNghiThamDu(0);

                // Còn chỉnh sửa
                newAccount.setRole(1);

                session.beginTransaction();
                session.save(newAccount);
                session.getTransaction().commit();
                showAlertNotify("Tạo tài khoản thành công!", true, 2);
                Stage stage = (Stage) signupButtonLogup.getScene().getWindow();
                stage.close();
            }
        } finally {
            session.close();
        }
    }

    @FXML
    private void setBackButtonLogin(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) backButtonLogin.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void setLoginButton(ActionEvent event) {
        event.consume();
        Session session = HibernateSession.getSession();
        if (inputUsernameLogin.getText().equals("") || inputPasswordLogin.getText().equals(""))
            showAlertNotify("Tên đăng nhập hoặc mật khẩu chưa đúng!", false, 1);
        else {
            try {
                String hql = "FROM AccountEntity WHERE username = '" + inputUsernameLogin.getText() + "'";
                Query query = session.createQuery(hql);
                List<AccountEntity> list = query.list();
                if (list.isEmpty() || !BCrypt.verifyer().verify(inputPasswordLogin.getText().toCharArray(), list.get(0).getPassword()).verified) {
                    showAlertNotify("Tên đăng nhập hoặc mật khẩu chưa đúng!", false, 1);
                }
                else if (!list.isEmpty() && list.get(0).getState() == 0) {
                    showAlertNotify("Tài khoản này hiện đang bị chặn truy cập!", false, 1);
                }
                else {
                    isLogin = true;
                    role = list.get(0).getRole();
                    accountLogin = list.get(0);
                    pass = inputPasswordLogin.getText();
                    showAlertNotify("Đăng nhập thành công!", true, 1);
                }
            } finally {
                session.close();
            }
        }
    }
}
