package btth2.conferencemanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class AddNewConferenceController implements Initializable {
    @FXML
    private TextArea nameOfNewConf;

    @FXML
    private TextArea briefOfNewConf;

    @FXML
    private TextArea detailOfNewConf;

    @FXML
    private TextArea amountOfNewConf;

    @FXML
    private DatePicker beginOfNewConf;

    @FXML
    private ComboBox<Integer> timeOfNewConf;

    @FXML
    private ComboBox<String> locationOfNewConf;

    @FXML
    private Button btnConfirmNewConf;

    @FXML
    private Button btnBackNewConf;

    @FXML
    private TextArea linkOfAddImage;

    @FXML
    private Button fileChooserNew;

    @FXML
    private TextArea hourNewConf;

    @FXML
    private TextArea minuteNewConf;

    @FXML
    private TextArea secondNewConf;

    private List<DiadiemtochucEntity> listLocation;

    private File file;

    private void showAlertNotify(String notify) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(notify);
        alert.showAndWait();
    }

    @FXML
    private void setFileChooserNew(ActionEvent event) {
        event.consume();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"));
        file = fileChooser.showOpenDialog(fileChooserNew.getScene().getWindow());

        if (file != null) {
            linkOfAddImage.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void setBtnBackNewConf(ActionEvent event) {
        event.consume();
        nameOfNewConf.setText("");
        briefOfNewConf.setText("");
        detailOfNewConf.setText("");
        hourNewConf.setText("");
        minuteNewConf.setText("");
        secondNewConf.setText("");
        amountOfNewConf.setText("");
        linkOfAddImage.setText("");
        Stage stage = (Stage) btnBackNewConf.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void setBtnConfirmNewConf(ActionEvent event) throws ParseException {
        event.consume();
        Session session = HibernateSession.getSession();
        HoinghiEntity newConf = new HoinghiEntity();

        if (beginOfNewConf.getValue() == null || hourNewConf.getText().equals("") || minuteNewConf.getText().equals("") || secondNewConf.getText().equals("")) {
            showAlertNotify("Vui lòng nhập thời gian bắt đầu!");
            return;
        }

        Date timeStart = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(beginOfNewConf.getValue().toString() + " " +
                hourNewConf.getText() + ":" + minuteNewConf.getText() + ":" + secondNewConf.getText());
        Calendar temp = Calendar.getInstance();
        temp.setTime(timeStart);
        temp.add(Calendar.HOUR_OF_DAY, timeOfNewConf.getValue());
        Date timeEnd = temp.getTime();

        Integer idLocation = 0, newId = 0; Boolean locationIsUsed = false, exceedAmount = false;
        for (DiadiemtochucEntity a : listLocation) {
            if (a.getDiaChi().equals(locationOfNewConf.getValue())) idLocation = a.getIdDiadiemtochuc();
            if (a.getDiaChi().equals(locationOfNewConf.getValue()) && a.getSucChua() < Integer.parseInt(amountOfNewConf.getText())) exceedAmount = true;
        }

        try {
            String getLocation = "select idDiadiemtochuc from HoinghiEntity where thoiGianBatDau between :start and :end";
            Query query = session.createQuery(getLocation)
                    .setParameter("start", timeStart)
                    .setParameter("end", timeEnd);
            List<Integer> listUsed = query.list();
            for (Integer num : listUsed) {
                if (num.equals(idLocation)) locationIsUsed = true;
            }

            String getNewId = "from HoinghiEntity";
            Query queryId = session.createQuery(getNewId);
            List<HoinghiEntity> idConf = queryId.list();
            for (HoinghiEntity num : idConf) {
                newId = num.getIdHoinghi();
            }

            if (locationIsUsed) {
                showAlertNotify("Địa điểm này đã được đăng ký cho hội nghị khác!");
            }
            else if (exceedAmount) {
                showAlertNotify("Số lượng tối đa vượt qua sức chứa của địa điểm!");
            }
            else {
                try {
                    Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(System.getProperty("user.dir") + "/images/" + file.getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                newConf.setIdHoinghi(newId + 1);
                newConf.setTen(nameOfNewConf.getText());
                newConf.setMoTaNganGon(briefOfNewConf.getText());
                newConf.setMoTaChiTiet(detailOfNewConf.getText());
                newConf.setThoiGianBatDau(new Timestamp(timeStart.getTime()));
                newConf.setThoiGianKetThuc(new Timestamp(timeEnd.getTime()));
                newConf.setSoLuongToiDa(Integer.parseInt(amountOfNewConf.getText()));

                if (idLocation != 0)
                    newConf.setIdDiadiemtochuc(idLocation);

                newConf.setHinhAnh("/images/" + file.getName());
                newConf.setNguoiThamDu(0);

                session.beginTransaction();
                session.save(newConf);
                session.getTransaction().commit();
                showAlertNotify("Thêm hội nghị thành công!");
                Stage stage = (Stage) btnConfirmNewConf.getScene().getWindow();
                stage.close();
            }
        }
        finally {
            session.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Session session = HibernateSession.getSession();
        try {
            String hqlGetListLocation = "from DiadiemtochucEntity";
            Query query = session.createQuery(hqlGetListLocation);
            listLocation = query.list();
            ArrayList<String> address = new ArrayList<>();
            for (DiadiemtochucEntity a : listLocation) {
                address.add(a.getDiaChi());
            }
            assert false;
            locationOfNewConf.getItems().addAll(address);
            locationOfNewConf.setValue(address.get(0));
            timeOfNewConf.setValue(1);
        }
        finally {
            session.close();
        }

    }
}
