package btth2.conferencemanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConferenceListViewCell extends ListCell<HoinghiEntity> {
    @FXML
    private ImageView imageConference;

    @FXML
    private Label nameConference;

    @FXML
    private Label briefDescription;

    @FXML
    private Label timeStart;

    @FXML
    private Label location;

    @FXML
    private Label registered;

    @FXML
    private Label maxParticipant;

    @FXML
    private AnchorPane anchorPaneListCell;

    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(HoinghiEntity hoinghi, boolean empty) {
        super.updateItem(hoinghi, empty);

        if (empty || hoinghi == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/list_cell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Image image = null;
            try {
                image = new Image(new FileInputStream(System.getProperty("user.dir") + hoinghi.getHinhAnh()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageConference.setImage(image);
            nameConference.setText(String.valueOf(hoinghi.getTen()));
            briefDescription.setText(String.valueOf(hoinghi.getMoTaNganGon()));
            timeStart.setText(String.valueOf(hoinghi.getThoiGianBatDau()));
            registered.setText(registered.getText().substring(0, 12) + hoinghi.getNguoiThamDu());
            maxParticipant.setText(maxParticipant.getText().substring(0, 17) + hoinghi.getSoLuongToiDa());
            Session session = HibernateSession.getSession();
            String addressLocation;
            try {
                String hql = "select diaChi FROM DiadiemtochucEntity WHERE idDiadiemtochuc = " + hoinghi.getIdDiadiemtochuc();
                Query query = session.createQuery(hql);
                addressLocation = query.list().get(0).toString();
            } finally {
                session.close();
            }
            location.setText(addressLocation);
            anchorPaneListCell.setPadding(new Insets(5));

            setText(null);
            setGraphic(anchorPaneListCell);
        }
    }
}
