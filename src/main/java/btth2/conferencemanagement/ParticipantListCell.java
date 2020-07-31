package btth2.conferencemanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ParticipantListCell extends ListCell<AccountEntity> {
    @FXML
    private Label lbNameParticipant;

    @FXML
    private Label lbEmailParticipant;

    @FXML
    private AnchorPane anchorPaneListParCell;

    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(AccountEntity accountEntity, boolean empty) {
        super.updateItem(accountEntity, empty);

        if (empty || accountEntity == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/participant_list_cell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            lbNameParticipant.setText(lbNameParticipant.getText().substring(0, 11) + accountEntity.getTen());
            lbEmailParticipant.setText(lbEmailParticipant.getText().substring(0, 7) + accountEntity.getEmail());

            setText(null);
            setGraphic(anchorPaneListParCell);
        }
    }
}
