package btth2.conferencemanagement;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ThamduhoinghiEntityPK implements Serializable {
    private int idAccount;
    private int idHoinghi;

    @Column(name = "id_account", nullable = false)
    @Id
    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    @Column(name = "id_hoinghi", nullable = false)
    @Id
    public int getIdHoinghi() {
        return idHoinghi;
    }

    public void setIdHoinghi(int idHoinghi) {
        this.idHoinghi = idHoinghi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThamduhoinghiEntityPK that = (ThamduhoinghiEntityPK) o;
        return idAccount == that.idAccount &&
                idHoinghi == that.idHoinghi;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAccount, idHoinghi);
    }
}
