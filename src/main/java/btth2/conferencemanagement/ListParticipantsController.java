package btth2.conferencemanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class ListParticipantsController {
    @FXML
    private ListView<AccountEntity> lvParticipants;

    @FXML
    private Button btnBackListPar;

    private HoinghiEntity hoinghiEntity;

    @FXML
    private void setBtnBackListPar(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) btnBackListPar.getScene().getWindow();
        stage.close();
    }

    public void showListParticipants(HoinghiEntity hoinghiEntity) {
        Session session = HibernateSession.getSession();
        List<AccountEntity> listPar;
        try {
            String hql = "select b FROM ThamduhoinghiEntity a , AccountEntity b where a.idHoinghi = :id and a.isAccepted = :accept and a.idAccount = b.idAccount";
            Query query = session.createQuery(hql)
                    .setParameter("id", hoinghiEntity.getIdHoinghi())
                    .setParameter("accept", 1);
            listPar = query.list();
        } finally {
            session.close();
        }
        ObservableList<AccountEntity> participantObservableList = FXCollections.observableArrayList();
        participantObservableList.setAll(listPar);
        lvParticipants.setItems(participantObservableList);
        lvParticipants.setCellFactory(listView -> new ParticipantListCell());
    }
}
