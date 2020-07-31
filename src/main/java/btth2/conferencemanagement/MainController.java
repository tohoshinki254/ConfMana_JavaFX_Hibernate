package btth2.conferencemanagement;

import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    private List<AccountEntity> listAcc;
    private List<HoinghiEntity> listConf;
    @FXML
    private ListView<HoinghiEntity> listConference;

    private ObservableList<HoinghiEntity> conferenceObservableList;

    @FXML
    private ListView<AccountEntity> listUser;

    private ObservableList<AccountEntity> userObservableList;

    public static boolean isLogin = false;
    public static int role;
    public static AccountEntity accountLogin;
    public static String pass;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnIntroduceMenu;

    @FXML
    private Button btnListConfRegistered;

    @FXML
    private Button btnConferenceManagement;

    @FXML
    private Button btnUserManagement;

    @FXML
    private Pane paneProfile;

    @FXML
    private TextField nameProfile;

    @FXML
    private TextField emailProfile;

    @FXML
    private PasswordField passwordProfile;

    @FXML
    private PasswordField confirmPassProfile;

    @FXML
    private Button btnEditProfile;

    @FXML
    private CheckBox sortConfFollowName;

    @FXML
    private CheckBox sortConfFollowRegistered;

    @FXML
    private Pane paneIntroduce;

    @FXML
    private Pane paneListConference;

    @FXML
    private Group groupListConf;

    @FXML
    private Group groupRegistered;

    @FXML
    private Group groupConfManagement;

    @FXML
    private Button addNewConf;

    @FXML
    private CheckBox sortUserFollowName;

    @FXML
    private CheckBox sortUserFollowConf;

    @FXML
    private Pane paneUserManagement;

    private void setVisibleForPanes(boolean profile, boolean introduce, boolean conference, boolean user) {
        paneProfile.setVisible(profile);
        paneIntroduce.setVisible(introduce);
        paneListConference.setVisible(conference);
        paneUserManagement.setVisible(user);
    }

    private void setVisibleForGroup(boolean list, boolean registered, boolean management) {
        groupListConf.setVisible(list);
        groupRegistered.setVisible(registered);
        groupConfManagement.setVisible(management);
    }

    public void setVisibleForButtonsAfterLogin(boolean profile, boolean login, boolean logout, boolean listRegistered, boolean confMana, boolean userMana) {
        btnProfile.setVisible(profile);
        btnProfile.setText(accountLogin.getUsername());
        btnLogin.setVisible(login);
        btnLogout.setVisible(logout);
        btnListConfRegistered.setVisible(listRegistered);
        btnConferenceManagement.setVisible(confMana);
        btnUserManagement.setVisible(userMana);
    }

    private void showAlertNotify(String notify) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(notify);
        alert.showAndWait();
    }

    @FXML
    private void setSortConfFollowName(ActionEvent event) {
        event.consume();
        if (sortConfFollowName.isSelected()) {
            sortConfFollowRegistered.setSelected(false);
            List<HoinghiEntity> temp = new ArrayList<HoinghiEntity>(listConf);
            temp.sort(new Comparator<HoinghiEntity>() {
                @Override
                public int compare(HoinghiEntity o1, HoinghiEntity o2) {
                    return o1.getTen().compareToIgnoreCase(o2.getTen());
                }
            });
            conferenceObservableList = FXCollections.observableArrayList();
            conferenceObservableList.setAll(temp);
        }
        else {
            conferenceObservableList = FXCollections.observableArrayList();
            conferenceObservableList.setAll(listConf);
        }
        listConference.setItems(conferenceObservableList);
        listConference.setCellFactory(conference -> new ConferenceListViewCell());
    }

    @FXML
    private void setSortConfFollowRegistered(ActionEvent event) {
        event.consume();
        if (sortConfFollowRegistered.isSelected()) {
            sortConfFollowName.setSelected(false);
            List<HoinghiEntity> temp = new ArrayList<HoinghiEntity>(listConf);
            temp.sort(new Comparator<HoinghiEntity>() {
                @Override
                public int compare(HoinghiEntity o1, HoinghiEntity o2) {
                    return o1.getNguoiThamDu().compareTo(o2.getNguoiThamDu());
                }
            });
            conferenceObservableList = FXCollections.observableArrayList();
            conferenceObservableList.setAll(temp);
        }
        else {
            conferenceObservableList = FXCollections.observableArrayList();
            conferenceObservableList.setAll(listConf);
        }
        listConference.setItems(conferenceObservableList);
        listConference.setCellFactory(conference -> new ConferenceListViewCell());
    }

    @FXML
    private void setSortUserFollowName(ActionEvent event) {
        event.consume();
        if (sortUserFollowName.isSelected()) {
            sortUserFollowConf.setSelected(false);
            List<AccountEntity> temp = new ArrayList<AccountEntity>(listAcc);
            temp.sort(new Comparator<AccountEntity>() {
                @Override
                public int compare(AccountEntity o1, AccountEntity o2) {
                    return o1.getTen().compareToIgnoreCase(o2.getTen());
                }
            });
            userObservableList = FXCollections.observableArrayList();
            userObservableList.setAll(temp);
        }
        else {
            userObservableList = FXCollections.observableArrayList();
            userObservableList.setAll(listAcc);
        }
        listUser.setItems(userObservableList);
        listUser.setCellFactory(accountListView -> new AccountListViewCell());
    }

    @FXML
    private void setSortUserFollowConf(ActionEvent event) {
        event.consume();
        if (sortUserFollowConf.isSelected()) {
            sortUserFollowName.setSelected(false);
            List<AccountEntity> temp = new ArrayList<AccountEntity>(listAcc);
            temp.sort(new Comparator<AccountEntity>() {
                @Override
                public int compare(AccountEntity o1, AccountEntity o2) {
                    return o1.getSoHoiNghiThamDu().compareTo(o2.getSoHoiNghiThamDu());
                }
            });
            userObservableList = FXCollections.observableArrayList();
            userObservableList.setAll(temp);
        }
        else {
            userObservableList = FXCollections.observableArrayList();
            userObservableList.setAll(listAcc);
        }
        listUser.setItems(userObservableList);
        listUser.setCellFactory(accountListView -> new AccountListViewCell());
    }

    @FXML
    private void setBtnIntroduceMenu(ActionEvent event) {
        event.consume();
        setVisibleForPanes(false, true, false, false);
        if (isLogin) {
            if (role == 1) {
                setVisibleForButtonsAfterLogin(true, false, true, true, true, false);
                btnConferenceManagement.setText("2. Danh sách hội nghị");
            }
            if (role == 2) {
                setVisibleForButtonsAfterLogin(true, false, true, false, true, true);
                btnConferenceManagement.setText("2. Quản lý hội nghị");
            }
        }
    }

    @FXML
    private void setBtnListConfRegistered(ActionEvent event) {
        event.consume();
        setVisibleForPanes(false, false, true, false);
        setVisibleForGroup(false, true, false);
        sortConfFollowName.setSelected(false);
        sortConfFollowRegistered.setSelected(false);

        Session session = HibernateSession.getSession();
        try {
            String hql = "select a from HoinghiEntity a, ThamduhoinghiEntity b where b.idAccount = :idacc and b.idHoinghi = a.idHoinghi";
            Query query = session.createQuery(hql).setParameter("idacc", accountLogin.getIdAccount());
            listConf = query.list();
            conferenceObservableList = FXCollections.observableArrayList();
            conferenceObservableList.setAll(listConf);
            listConference.setItems(conferenceObservableList);
            listConference.setCellFactory(hoinghi -> new ConferenceListViewCell());
        }
        finally {
            session.close();
        }
    }

    @FXML
    private void setBtnConferenceManagement(ActionEvent event) {
        event.consume();
        setVisibleForPanes(false, false, true, false);
        sortConfFollowName.setSelected(false);
        sortConfFollowRegistered.setSelected(false);
        if (btnConferenceManagement.getText().equals("2. Quản lý hội nghị")) {
            setVisibleForGroup(false, false, true);
        }
        else {
            setVisibleForGroup(true, false, false);
        }

        getListConf();
        listConference.setItems(conferenceObservableList);
        listConference.setCellFactory(conferenceListView -> new ConferenceListViewCell());
    }

    @FXML
    private void setBtnUserManagement(ActionEvent event) {
        event.consume();
        setVisibleForPanes(false, false, false, true);
        setVisibleForGroup(false, false, false);

        getListUser();
        listUser.setItems(userObservableList);
        listUser.setCellFactory(accountListView -> new AccountListViewCell());
    }

    @FXML
    private void setBtnLogin(ActionEvent event) {
        event.consume();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signin_signup.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Conference Management");
            stage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (isLogin) {
            if (role == 1) {
                setVisibleForButtonsAfterLogin(true, false, true, true, true, false);
                btnConferenceManagement.setText("2. Danh sách hội nghị");
            }
            if (role == 2) {
                setVisibleForButtonsAfterLogin(true, false, true, false, true, true);
                btnConferenceManagement.setText("2. Quản lý hội nghị");
            }
        }
    }

    @FXML
    private void setBtnProfile(ActionEvent event) {
        event.consume();
        setVisibleForPanes(true, false, false, false);
        nameProfile.setText(accountLogin.getTen());
        emailProfile.setText(accountLogin.getEmail());
    }

    @FXML
    private void setBtnEditProfile(ActionEvent event) {
        event.consume();
        boolean emailUsed = false;
        Session session = HibernateSession.getSession();
        try {
            String getDatahql = "FROM AccountEntity";
            Query queryData = session.createQuery(getDatahql);
            List<AccountEntity> temp = queryData.list();
            temp.removeIf(item -> item.getIdAccount() == accountLogin.getIdAccount());
            for (AccountEntity account : temp) {
                if (emailProfile.getText().equals(account.getEmail()))
                    emailUsed = true;
            }
            if (emailUsed) {
                showAlertNotify("Email đã được sử dụng!");
            }
            else if (!passwordProfile.getText().equals(confirmPassProfile.getText())) {
                showAlertNotify("Xác nhận mật khẩu chưa đúng!");
            }
            else {
                String hql = "update AccountEntity set ten = :ten, email = :email, password = :pass where idAccount = :id";
                Query query = session.createQuery(hql);
                query.setParameter("ten", nameProfile.getText());
                query.setParameter("email", emailProfile.getText());
                query.setParameter("pass", BCrypt.withDefaults().hashToString(12, pass.toCharArray()));
                query.setParameter("id", accountLogin.getIdAccount());
                session.beginTransaction();
                query.executeUpdate();
                session.getTransaction().commit();
                showAlertNotify("Chỉnh sửa thông tin cá nhân thành công!");
            }
        }
        finally {
            session.close();
        }
    }

    @FXML
    private void setBtnLogout(ActionEvent event) {
        event.consume();
        showAlertNotify("Đăng xuất thành công!");
        setVisibleForButtonsAfterLogin(false, true, false, false, true, false);
        btnConferenceManagement.setText("2. Danh sách hội nghị");
        setVisibleForPanes(false, true, false, false);
        isLogin = false;
    }

    @FXML
    private void setAddNewConf(ActionEvent event) {
        event.consume();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_new_conference.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Conference Management");
            stage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getListConf() {
        Session session = HibernateSession.getSession();
        try {
            String hql = "FROM btth2.conferencemanagement.HoinghiEntity";
            Query query = session.createQuery(hql);
            listConf = query.list();
        } finally {
            session.close();
        }
        conferenceObservableList = FXCollections.observableArrayList();
        conferenceObservableList.setAll(listConf);
    }

    private void getListUser() {
        Session newSession = HibernateSession.getSession();
        try {
            String hql = "FROM btth2.conferencemanagement.AccountEntity where role = 1"; // role = 1 -> không lấy các tài khoản admin
            Query query = newSession.createQuery(hql);
            listAcc = query.list();
        } finally {
            newSession.close();
        }
        userObservableList = FXCollections.observableArrayList();
        userObservableList.setAll(listAcc);
    }

    public MainController() {
        getListConf();
        getListUser();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listConference.setItems(conferenceObservableList);
        listConference.setCellFactory(conferenceListView -> new ConferenceListViewCell());
        listConference.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/conference_details.fxml"));
                    Parent root = loader.load();

                    DetailConferenceController detailConferenceController = loader.getController();
                    detailConferenceController.transferMessage(listConference.getSelectionModel().getSelectedItem());

                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.setTitle("Conference Management");
                    stage.show();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        listUser.setItems(userObservableList);
        listUser.setCellFactory(accountListView -> new AccountListViewCell());
    }
}
