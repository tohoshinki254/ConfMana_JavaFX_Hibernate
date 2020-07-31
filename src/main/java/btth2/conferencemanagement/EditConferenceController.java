package btth2.conferencemanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EditConferenceController {
    @FXML
    private TextArea nameOfEditConf;

    @FXML
    private TextArea briefOfEditConf;

    @FXML
    private TextArea detailOfEditConf;

    @FXML
    private TextArea amountOfEditConf;

    @FXML
    private DatePicker beginOfEditConf;

    @FXML
    private ComboBox<Integer> timeOfEditConf;

    @FXML
    private ComboBox<String> locationOfEditConf;

    @FXML
    private Button btnConfirmEditConf;

    @FXML
    private Button btnBackEditConf;

    @FXML
    private TextArea linkOfEditImage;

    @FXML
    private Button fileChooserEdit;

    @FXML
    private TextArea hourEditConf;

    @FXML
    private TextArea minuteEditConf;

    @FXML
    private TextArea secondEditConf;

    private List<DiadiemtochucEntity> listLocation;

    private File file;

    private HoinghiEntity hoiNghi;

    private void showAlertNotify(String notify) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(notify);
        alert.showAndWait();
    }

    @FXML
    private void setFileChooserEdit(ActionEvent event) {
        event.consume();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"));
        file = fileChooser.showOpenDialog(fileChooserEdit.getScene().getWindow());

        if (file != null) {
            linkOfEditImage.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void setBtnBackEditConf(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) btnBackEditConf.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void setBtnConfirmEditConf(ActionEvent event){
        event.consume();
        Session session = HibernateSession.getSession();
        if (beginOfEditConf.getValue() == null || hourEditConf.getText().equals("") || minuteEditConf.getText().equals("") || secondEditConf.getText().equals("")) {
            showAlertNotify("Vui lòng nhập thời gian bắt đầu!");
            return;
        }

        Date timeStart = null;
        try {
            timeStart = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(beginOfEditConf.getValue().toString() + " " +
                    hourEditConf.getText() + ":" + minuteEditConf.getText() + ":" + secondEditConf.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar temp = Calendar.getInstance();
        temp.setTime(timeStart);
        temp.add(Calendar.HOUR_OF_DAY, timeOfEditConf.getValue());
        Date timeEnd = temp.getTime();

        Integer idLocation = 0; Boolean locationIsUsed = false, exceedAmount = false;
        for (DiadiemtochucEntity a : listLocation) {
            if (a.getDiaChi().equals(locationOfEditConf.getValue())) idLocation = a.getIdDiadiemtochuc();
            if (a.getDiaChi().equals(locationOfEditConf.getValue()) && a.getSucChua() < Integer.parseInt(amountOfEditConf.getText())) exceedAmount = true;
        }

        try {
            String getLocation = "select b from HoinghiEntity b where b.thoiGianBatDau between :start and :end";
            Query query = session.createQuery(getLocation)
                    .setParameter("start", timeStart)
                    .setParameter("end", timeEnd);
            List<HoinghiEntity> listUsed = query.list();
            listUsed.removeIf(conf -> conf.getIdHoinghi() == hoiNghi.getIdHoinghi());
            for (HoinghiEntity num : listUsed) {
                if (num.getIdDiadiemtochuc() == idLocation) locationIsUsed = true;
            }

            if (locationIsUsed) {
                showAlertNotify("Địa điểm này đã được đăng ký cho hội nghị khác!");
            }
            else if (Integer.parseInt(amountOfEditConf.getText()) < hoiNghi.getNguoiThamDu()) {
                showAlertNotify("Số lượng tối đa nhỏ hơn số lượng đã đăng ký!");
            }
            else if (exceedAmount) {
                showAlertNotify("Số lượng tối đa vượt qua sức chứa của địa điểm!");
            }
            else {
                try {
                    if (file != null)
                        Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(System.getProperty("user.dir") + "/images/" + file.getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String hql = "update HoinghiEntity set ten = :ten, moTaNganGon = :brief, moTaChiTiet = :detail, hinhAnh = :image, thoiGianBatDau = :start, " +
                        "thoiGianKetThuc = :end, soLuongToiDa = :max, idDiadiemtochuc = :location where idHoinghi = :id";
                Query updateQuery = session.createQuery(hql);
                updateQuery.setParameter("ten", nameOfEditConf.getText());
                updateQuery.setParameter("brief", briefOfEditConf.getText());
                updateQuery.setParameter("detail", detailOfEditConf.getText());
                updateQuery.setParameter("image", linkOfEditImage.getText().equals("") ? hoiNghi.getHinhAnh() : linkOfEditImage.getText());
                updateQuery.setParameter("start", new Timestamp(timeStart.getTime()));
                updateQuery.setParameter("end", new Timestamp(timeEnd.getTime()));
                updateQuery.setParameter("max", Integer.parseInt(amountOfEditConf.getText()));
                updateQuery.setParameter("location", idLocation);
                updateQuery.setParameter("id", hoiNghi.getIdHoinghi());


                session.beginTransaction();
                updateQuery.executeUpdate();
                session.getTransaction().commit();
                showAlertNotify("Chỉnh sửa thông tin hội nghị thành công!");
                Stage stage = (Stage) btnConfirmEditConf.getScene().getWindow();
                stage.close();
            }
        }
        finally {
            session.close();
        }
    }

    public static final LocalDate LOCAL_DATE (String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }

    public void transferDataConference(HoinghiEntity hoinghiEntity) {
        hoiNghi = hoinghiEntity;
        Session session = HibernateSession.getSession();
        String location = null;
        try {
            String hqlGetListLocation = "from DiadiemtochucEntity";
            Query query = session.createQuery(hqlGetListLocation);
            listLocation = query.list();
            ArrayList<String> address = new ArrayList<>();
            for (DiadiemtochucEntity a : listLocation) {
                address.add(a.getDiaChi());
                if (a.getIdDiadiemtochuc() == hoinghiEntity.getIdDiadiemtochuc())
                    location = a.getDiaChi();
            }
            assert false;
            locationOfEditConf.getItems().addAll(address);
        }
        finally {
            session.close();
        }

        locationOfEditConf.setValue(location);
        nameOfEditConf.setText(hoinghiEntity.getTen());
        briefOfEditConf.setText(hoinghiEntity.getMoTaNganGon());
        detailOfEditConf.setText(hoinghiEntity.getMoTaChiTiet());
        beginOfEditConf.setValue(LOCAL_DATE(hoinghiEntity.getThoiGianBatDau().toString().substring(0, 10)));
        hourEditConf.setText(hoinghiEntity.getThoiGianBatDau().toString().substring(11, 13));
        minuteEditConf.setText(hoinghiEntity.getThoiGianBatDau().toString().substring(14, 16));
        secondEditConf.setText(hoinghiEntity.getThoiGianBatDau().toString().substring(17, 19));
        amountOfEditConf.setText(hoinghiEntity.getSoLuongToiDa().toString());

        Date start = null;
        Date end = null;
        try {
            start = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(hoinghiEntity.getThoiGianBatDau().toString());
            end = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(hoinghiEntity.getThoiGianKetThuc().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeOfEditConf.setValue((int) ((end.getTime() - start.getTime()) / (60 * 60 * 1000)));
    }
}
