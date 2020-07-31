package btth2.conferencemanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static btth2.conferencemanagement.MainController.*;

public class DetailConferenceController {
    private HoinghiEntity detailConference;
    @FXML
    private ImageView imageConferenceDetail;

    @FXML
    private Label nameDetail;

    @FXML
    private Label briefDescriptionDetail;

    @FXML
    private Label detailDescriptionDetail;

    @FXML
    private Label timeStart;

    @FXML
    private Label timeEnd;

    @FXML
    private Label maxAmountConf;

    @FXML
    private Label amountRegistered;

    @FXML
    private Label locationConferenceDetail;

    @FXML
    private Button backButtonDetailConf;

    @FXML
    private Button registerButtonConf;

    @FXML
    private Button editConfButton;

    @FXML
    private Button btnShowListParticipant;

    @FXML
    private AnchorPane anchorPaneConferenceDetail;

    @FXML
    private ListView<ThamduhoinghiEntity> listWaitAccept;

    @FXML
    private Label titleListWait;

    private void showAlertNotify(String notify) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(notify);
        alert.showAndWait();
    }

    private int showConfirmation(String question) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(question);

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            return 1;
        }
        else
            return 0;
    }

    @FXML
    private void setBtnShowListParticipant(ActionEvent event) {
        event.consume();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/list_participants.fxml"));
            Parent root = loader.load();

            ListParticipantsController listParticipantsController = loader.getController();
            listParticipantsController.showListParticipants(detailConference);

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

    @FXML
    private void setBackButtonDetailConf(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) backButtonDetailConf.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void setRegisterButtonConf(ActionEvent event) {
        event.consume();

        if (!isLogin) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/signin_signup.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Conference Management");
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Object numRegistered;
        Session session = HibernateSession.getSession();
        try {
            String hql = "select count(distinct idAccount) from ThamduhoinghiEntity where idHoinghi = :id and isDeleted = 0";
            Query query = session.createQuery(hql).setParameter("id", detailConference.getIdHoinghi());
            numRegistered = query.iterate().next();
        }
        finally {
            session.close();
        }

        if (isLogin && role == 1) {
            if (registerButtonConf.getText().equals("ĐĂNG KÝ")) {
                if (detailConference.getSoLuongToiDa() == numRegistered || detailConference.getSoLuongToiDa().equals(detailConference.getNguoiThamDu()))
                    showAlertNotify("Số lượng đăng ký đã đủ !");
                else {
                    int state = showConfirmation("Xác nhận đăng ký ?");
                    if (state == 1) {
                        showAlertNotify("Đăng ký thành công!");
                        registerButtonConf.setText("HỦY ĐĂNG KÝ");
                        session = HibernateSession.getSession();
                        try {
                            ThamduhoinghiEntity par = new ThamduhoinghiEntity();
                            par.setIdHoinghi(detailConference.getIdHoinghi());
                            par.setIdAccount(accountLogin.getIdAccount());
                            par.setIsDeleted(0);
                            par.setIsAccepted(0);
                            session.beginTransaction();
                            session.save(par);
                            session.getTransaction().commit();

                            String hql = "update HoinghiEntity set nguoiThamDu = :attend where idHoinghi = :id";
                            Query query = session.createQuery(hql)
                                    .setParameter("attend", detailConference.getNguoiThamDu() + 1)
                                    .setParameter("id", detailConference.getIdHoinghi());
                            session.beginTransaction();
                            query.executeUpdate();
                            session.getTransaction().commit();
                        } finally {
                            session.close();
                        }
                    }
                }
            }
            else {
                Date now = new Date();
                Date timeStart = null;
                try {
                    timeStart = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(detailConference.getThoiGianBatDau().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                session = HibernateSession.getSession();
                try {
                    assert timeStart != null;
                    if (timeStart.compareTo(now) < 0) {
                        showAlertNotify("Không thể hủy đăng ký - Hội nghị này đã diễn ra rồi!");
                    }
                    else {
                        String hql = "from ThamduhoinghiEntity where idAccount = :idacc and idHoinghi = :idconf";
                        Query query = session.createQuery(hql)
                                .setParameter("idacc", accountLogin.getIdAccount())
                                .setParameter("idconf", detailConference.getIdHoinghi());
                        List<ThamduhoinghiEntity> list = query.list();

                        if (list.get(0).getIsAccepted() == 1) {
                            String hql_acc = "update AccountEntity set soHoiNghiThamDu = :number where idAccount = :id";
                            query = session.createQuery(hql_acc)
                                    .setParameter("number", accountLogin.getSoHoiNghiThamDu() - 1)
                                    .setParameter("id", accountLogin.getIdAccount());
                            session.beginTransaction();
                            query.executeUpdate();
                            session.getTransaction().commit();
                        }

                        String hql_conf = "update HoinghiEntity set nguoiThamDu = :number where idHoinghi = :id";
                        query = session.createQuery(hql_conf)
                                .setParameter("number", detailConference.getNguoiThamDu() - 1)
                                .setParameter("id", detailConference.getIdHoinghi());
                        session.beginTransaction();
                        query.executeUpdate();
                        session.getTransaction().commit();

                        String hql_attend = "delete from ThamduhoinghiEntity where idAccount = :idacc and idHoinghi = :idconf";
                        query = session.createQuery(hql_attend)
                                .setParameter("idacc", accountLogin.getIdAccount())
                                .setParameter("idconf", detailConference.getIdHoinghi());
                        session.beginTransaction();
                        query.executeUpdate();
                        session.getTransaction().commit();
                        showAlertNotify("Hủy đăng ký thành công!");
                        registerButtonConf.setText("ĐĂNG KÝ");
                    }
                }
                finally {
                    session.close();
                }
            }
        }

        if (role == 2) {
            registerButtonConf.setVisible(false);
            editConfButton.setVisible(true);
        }
    }

    @FXML
    private void setEditConfButton(ActionEvent event) throws ParseException {
        event.consume();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit_conference.fxml"));
            Parent root = loader.load();

            EditConferenceController editConferenceController = loader.getController();
            editConferenceController.transferDataConference(detailConference);

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

    private List<ThamduhoinghiEntity> getListParticipant() {
        Session session = HibernateSession.getSession();
        List<ThamduhoinghiEntity> listParticipant;
        try {
            String hql = "FROM ThamduhoinghiEntity";
            Query query = session.createQuery(hql);
            listParticipant = query.list();
        }
        finally {
            session.close();
        }
        return listParticipant;
    }

    public void transferMessage(HoinghiEntity hoinghi) throws FileNotFoundException {
        if (isLogin && role == 2) {     // role = 2 -> admin
            registerButtonConf.setVisible(false);
            editConfButton.setVisible(true);
            listWaitAccept.setVisible(true);
            titleListWait.setVisible(true);
        }

        detailConference = hoinghi;

        Session session = HibernateSession.getSession();
        Image image = new Image(new FileInputStream(System.getProperty("user.dir") + hoinghi.getHinhAnh()));
        imageConferenceDetail.setImage(image);
        nameDetail.setText(String.valueOf(hoinghi.getTen()));
        briefDescriptionDetail.setText(String.valueOf(hoinghi.getMoTaNganGon()));
        detailDescriptionDetail.setText(String.valueOf(hoinghi.getMoTaChiTiet()));
        timeStart.setText("Thời gian bắt đầu: " + hoinghi.getThoiGianBatDau());
        timeEnd.setText("Thời gian kết thúc: " + hoinghi.getThoiGianKetThuc());
        maxAmountConf.setText("Số lượng: " + hoinghi.getSoLuongToiDa().toString());
        amountRegistered.setText("Đã đăng ký: " + hoinghi.getNguoiThamDu().toString());
        String address;

        try {
            String hql = "select diaChi FROM DiadiemtochucEntity WHERE idDiadiemtochuc = " + hoinghi.getIdDiadiemtochuc();
            Query query = session.createQuery(hql);
            address = query.list().get(0).toString();
        } finally {
            session.close();
        }

        locationConferenceDetail.setText("Địa điểm tổ chức: " + address);
        anchorPaneConferenceDetail.setPadding(new Insets(5));

        Session newSession = HibernateSession.getSession();
        List<ThamduhoinghiEntity> listWait;
        try {
            String hql = "FROM ThamduhoinghiEntity where idHoinghi = :id and isAccepted = :accept and isDeleted = :delete";
            Query query = newSession.createQuery(hql)
                    .setParameter("id", detailConference.getIdHoinghi())
                    .setParameter("accept", 0)
                    .setParameter("delete", 0);
            listWait = query.list();
        } finally {
            newSession.close();
        }
        ObservableList<ThamduhoinghiEntity> waitAcceptObservableList = FXCollections.observableArrayList();
        waitAcceptObservableList.setAll(listWait);
        listWaitAccept.setItems(waitAcceptObservableList);
        listWaitAccept.setCellFactory(listView -> new WaitAcceptListCell());

        List<ThamduhoinghiEntity> listParticipant = getListParticipant();
        Date date = new Date();
        try {
            Date timeConf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(detailConference.getThoiGianBatDau().toString().substring(0, 19));
            for (ThamduhoinghiEntity a : listParticipant) {
                assert timeConf != null;
                if (isLogin && a.getIdAccount() == accountLogin.getIdAccount() && a.getIdHoinghi() == hoinghi.getIdHoinghi() && timeConf.compareTo(date) > 0) {
                    registerButtonConf.setText("HỦY ĐĂNG KÝ");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}