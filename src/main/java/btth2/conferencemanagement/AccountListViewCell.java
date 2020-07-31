package btth2.conferencemanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;

public class AccountListViewCell extends ListCell<AccountEntity> {
    @FXML
    private Label idUserLabel;

    @FXML
    private Label nameUserLabel;

    @FXML
    private Label emailUserLabel;

    @FXML
    private Label stateAccount;

    @FXML
    private Button btnEditState;

    @FXML
    private AnchorPane anchorPaneListUserCell;

    private FXMLLoader fxmlLoader;

    private AccountEntity account;

    @Override
    protected void updateItem(AccountEntity accountEntity, boolean empty) {
        super.updateItem(accountEntity, empty);

        if (empty || accountEntity == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            account = accountEntity;
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/list_user_cell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            idUserLabel.setText(idUserLabel.getText().substring(0, 4) + accountEntity.getIdAccount());
            nameUserLabel.setText(nameUserLabel.getText().substring(0, 11) + accountEntity.getTen());
            emailUserLabel.setText(emailUserLabel.getText().substring(0, 7) + accountEntity.getEmail());
            if (accountEntity.getState() == 1) {
                stateAccount.setText(stateAccount.getText().substring(0, 12) + "Bình thường");
                btnEditState.setText("CHẶN");
            }
            else {
                stateAccount.setText(stateAccount.getText().substring(0, 12) + "Bị chặn");
                btnEditState.setText("BỎ CHẶN");
            }

            setText(null);
            setGraphic(anchorPaneListUserCell);

            btnEditState.setOnAction(actionEvent -> {
                int state = account.getState();

                if (state == 1) {
                    state = 0;
                    account.setState(0);
                    stateAccount.setText(stateAccount.getText().substring(0, 12) + "Bị chặn");
                    btnEditState.setText("BỎ CHẶN");
                }
                else {
                    state = 1;
                    account.setState(1);
                    stateAccount.setText(stateAccount.getText().substring(0, 12) + "Bình thường");
                    btnEditState.setText("CHẶN");
                }

                Session session = HibernateSession.getSession();
                try {
                    String hql = "update AccountEntity set state = :state where idAccount = :id";
                    Query query = session.createQuery(hql)
                            .setParameter("state", state)
                            .setParameter("id", account.getIdAccount());
                    session.beginTransaction();
                    query.executeUpdate();
                    session.getTransaction().commit();
                } finally {
                    session.close();
                }
            });
        }
    }
}
