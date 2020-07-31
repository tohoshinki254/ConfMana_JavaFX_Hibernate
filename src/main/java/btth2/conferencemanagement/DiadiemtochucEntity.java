package btth2.conferencemanagement;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "diadiemtochuc", schema = "coferencemanagement", catalog = "")
public class DiadiemtochucEntity {
    private int idDiadiemtochuc;
    private String ten;
    private String diaChi;
    private Integer sucChua;
    private Collection<HoinghiEntity> hoinghisByIdDiadiemtochuc;

    @Id
    @Column(name = "id_diadiemtochuc", nullable = false)
    public int getIdDiadiemtochuc() {
        return idDiadiemtochuc;
    }

    public void setIdDiadiemtochuc(int idDiadiemtochuc) {
        this.idDiadiemtochuc = idDiadiemtochuc;
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
    @Column(name = "dia_chi", nullable = true, length = 500)
    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    @Basic
    @Column(name = "suc_chua", nullable = true)
    public Integer getSucChua() {
        return sucChua;
    }

    public void setSucChua(Integer sucChua) {
        this.sucChua = sucChua;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiadiemtochucEntity that = (DiadiemtochucEntity) o;
        return idDiadiemtochuc == that.idDiadiemtochuc &&
                Objects.equals(ten, that.ten) &&
                Objects.equals(diaChi, that.diaChi) &&
                Objects.equals(sucChua, that.sucChua);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDiadiemtochuc, ten, diaChi, sucChua);
    }

    @OneToMany(mappedBy = "diadiemtochucByIdDiadiemtochuc")
    public Collection<HoinghiEntity> getHoinghisByIdDiadiemtochuc() {
        return hoinghisByIdDiadiemtochuc;
    }

    public void setHoinghisByIdDiadiemtochuc(Collection<HoinghiEntity> hoinghisByIdDiadiemtochuc) {
        this.hoinghisByIdDiadiemtochuc = hoinghisByIdDiadiemtochuc;
    }
}
