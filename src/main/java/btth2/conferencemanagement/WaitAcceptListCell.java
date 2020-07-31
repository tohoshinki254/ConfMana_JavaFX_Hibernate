package btth2.conferencemanagement;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;

import static btth2.conferencemanagement.MainController.accountLogin;

public class WaitAcceptListCell extends ListCell<ThamduhoinghiEntity> {
    @FXML
    private Label idWaitLabel;

    @FXML
    private Button btnAcceptWait;

    @FXML
    private Button btnDeleteWait;

    @FXML
    private Label stateAfterAction;

    @FXML
    private AnchorPane anchorPaneListWaitCell;

    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(ThamduhoinghiEntity attend, boolean empty) {
        super.updateItem(attend, empty);

        if (empty || attend == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/list_wait_accept_cell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            idWaitLabel.setText(idWaitLabel.getText().substring(0, 4) + attend.getIdAccount());
            setText(null);
            setGraphic(anchorPaneListWaitCell);

            btnAcceptWait.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    attend.setIsAccepted(1);
                    stateAfterAction.setText("Đã chấp nhận");
                    btnAcceptWait.setVisible(false);
                    btnDeleteWait.setVisible(false);

                    Session session = HibernateSession.getSession();
                    try {
                        String hql = "update ThamduhoinghiEntity set isAccepted = :state where idAccount = :idAcc and idHoinghi = :idConf";
                        Query query = session.createQuery(hql)
                                .setParameter("state", 1)
                                .setParameter("idAcc", attend.getIdAccount())
                                .setParameter("idConf", attend.getIdHoinghi());
                        session.beginTransaction();
                        query.executeUpdate();
                        session.getTransaction().commit();

                        String hql_acc = "update AccountEntity set soHoiNghiThamDu = :number where idAccount = :id";
                        query = session.createQuery(hql_acc)
                                .setParameter("number", accountLogin.getSoHoiNghiThamDu() + 1)
                                .setParameter("id", accountLogin.getIdAccount());
                        session.beginTransaction();
                        query.executeUpdate();
                        session.getTransaction().commit();
                    } finally {
                        session.close();
                    }
                }
            });

            btnDeleteWait.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    attend.setIsDeleted(1);
                    stateAfterAction.setText("Đã xóa yêu cầu");
                    btnDeleteWait.setVisible(false);
                    btnAcceptWait.setVisible(false);

                    Session session = HibernateSession.getSession();
                    try {
                        String hql = "update ThamduhoinghiEntity set isDeleted = :state where idAccount = :idAcc and idHoinghi = :idConf";
                        Query query = session.createQuery(hql)
                                .setParameter("state", 1)
                                .setParameter("idAcc", attend.getIdAccount())
                                .setParameter("idConf", attend.getIdHoinghi());
                        session.beginTransaction();
                        query.executeUpdate();
                        session.getTransaction().commit();
                    } finally {
                        session.close();
                    }
                }
            });
        }
    }
}
