package btth2.conferencemanagement;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "account", schema = "coferencemanagement", catalog = "")
public class AccountEntity {
    private int idAccount;
    private String ten;
    private String username;
    private String password;
    private String email;
    private Integer role;
    private Integer state;
    private Integer soHoiNghiThamDu;
    private Collection<ThamduhoinghiEntity> thamduhoinghisByIdAccount;

    @Id
    @Column(name = "id_account", nullable = false)
    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    @Basic
    @Column(name = "ten", nullable = true, length = 45)
    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    @Basic
    @Column(name = "username", nullable = true, length = 45)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = true, length = 100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 45)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "role", nullable = true)
    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    @Basic
    @Column(name = "state", nullable = true)
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Basic
    @Column(name = "so_hoi_nghi_tham_du", nullable = true)
    public Integer getSoHoiNghiThamDu() {
        return soHoiNghiThamDu;
    }

    public void setSoHoiNghiThamDu(Integer soHoiNghiThamDu) {
        this.soHoiNghiThamDu = soHoiNghiThamDu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return idAccount == that.idAccount &&
                Objects.equals(ten, that.ten) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(email, that.email) &&
                Objects.equals(role, that.role) &&
                Objects.equals(state, that.state) &&
                Objects.equals(soHoiNghiThamDu, that.soHoiNghiThamDu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAccount, ten, username, password, email, role, state, soHoiNghiThamDu);
    }

    @OneToMany(mappedBy = "accountByIdAccount")
    public Collection<ThamduhoinghiEntity> getThamduhoinghisByIdAccount() {
        return thamduhoinghisByIdAccount;
    }

    public void setThamduhoinghisByIdAccount(Collection<ThamduhoinghiEntity> thamduhoinghisByIdAccount) {
        this.thamduhoinghisByIdAccount = thamduhoinghisByIdAccount;
    }
}
