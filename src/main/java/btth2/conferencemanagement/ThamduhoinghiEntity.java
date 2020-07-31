package btth2.conferencemanagement;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "thamduhoinghi", schema = "coferencemanagement", catalog = "")
@IdClass(ThamduhoinghiEntityPK.class)
public class ThamduhoinghiEntity {
    private int idAccount;
    private int idHoinghi;
    private Integer isAccepted;
    private Integer isDeleted;
    private AccountEntity accountByIdAccount;
    private HoinghiEntity hoinghiByIdHoinghi;

    @Id
    @Column(name = "id_account", nullable = false)
    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    @Id
    @Column(name = "id_hoinghi", nullable = false)
    public int getIdHoinghi() {
        return idHoinghi;
    }

    public void setIdHoinghi(int idHoinghi) {
        this.idHoinghi = idHoinghi;
    }

    @Basic
    @Column(name = "isAccepted", nullable = true)
    public Integer getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Integer isAccepted) {
        this.isAccepted = isAccepted;
    }

    @Basic
    @Column(name = "isDeleted", nullable = true)
    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThamduhoinghiEntity that = (ThamduhoinghiEntity) o;
        return idAccount == that.idAccount &&
                idHoinghi == that.idHoinghi &&
                Objects.equals(isAccepted, that.isAccepted) &&
                Objects.equals(isDeleted, that.isDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAccount, idHoinghi, isAccepted, isDeleted);
    }

    @ManyToOne
    @JoinColumn(name = "id_account", referencedColumnName = "id_account", nullable = false)
    public AccountEntity getAccountByIdAccount() {
        return accountByIdAccount;
    }

    public void setAccountByIdAccount(AccountEntity accountByIdAccount) {
        this.accountByIdAccount = accountByIdAccount;
    }

    @ManyToOne
    @JoinColumn(name = "id_hoinghi", referencedColumnName = "id_hoinghi", nullable = false)
    public HoinghiEntity getHoinghiByIdHoinghi() {
        return hoinghiByIdHoinghi;
    }

    public void setHoinghiByIdHoinghi(HoinghiEntity hoinghiByIdHoinghi) {
        this.hoinghiByIdHoinghi = hoinghiByIdHoinghi;
    }
}
