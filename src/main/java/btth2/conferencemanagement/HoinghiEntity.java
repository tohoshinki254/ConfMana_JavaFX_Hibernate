package btth2.conferencemanagement;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "hoinghi", schema = "coferencemanagement", catalog = "")
public class HoinghiEntity {
    private int idHoinghi;
    private String ten;
    private String moTaNganGon;
    private String moTaChiTiet;
    private String hinhAnh;
    private Timestamp thoiGianBatDau;
    private Timestamp thoiGianKetThuc;
    private Integer nguoiThamDu;
    private Integer soLuongToiDa;
    private int idDiadiemtochuc;
    private DiadiemtochucEntity diadiemtochucByIdDiadiemtochuc;
    private Collection<ThamduhoinghiEntity> thamduhoinghisByIdHoinghi;

    @Id
    @Column(name = "id_hoinghi", nullable = false)
    public int getIdHoinghi() {
        return idHoinghi;
    }

    public void setIdHoinghi(int idHoinghi) {
        this.idHoinghi = idHoinghi;
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
    @Column(name = "mo_ta_ngan_gon", nullable = true, length = 100)
    public String getMoTaNganGon() {
        return moTaNganGon;
    }

    public void setMoTaNganGon(String moTaNganGon) {
        this.moTaNganGon = moTaNganGon;
    }

    @Basic
    @Column(name = "mo_ta_chi_tiet", nullable = true, length = 1000)
    public String getMoTaChiTiet() {
        return moTaChiTiet;
    }

    public void setMoTaChiTiet(String moTaChiTiet) {
        this.moTaChiTiet = moTaChiTiet;
    }

    @Basic
    @Column(name = "hinh_anh", nullable = true, length = 200)
    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Basic
    @Column(name = "thoi_gian_bat_dau", nullable = true)
    public Timestamp getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(Timestamp thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    @Basic
    @Column(name = "thoi_gian_ket_thuc", nullable = true)
    public Timestamp getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(Timestamp thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    @Basic
    @Column(name = "nguoi_tham_du", nullable = true)
    public Integer getNguoiThamDu() {
        return nguoiThamDu;
    }

    public void setNguoiThamDu(Integer nguoiThamDu) {
        this.nguoiThamDu = nguoiThamDu;
    }

    @Basic
    @Column(name = "so_luong_toi_da", nullable = true)
    public Integer getSoLuongToiDa() {
        return soLuongToiDa;
    }

    public void setSoLuongToiDa(Integer soLuongToiDa) {
        this.soLuongToiDa = soLuongToiDa;
    }

    @Basic
    @Column(name = "id_diadiemtochuc", nullable = false)
    public int getIdDiadiemtochuc() {
        return idDiadiemtochuc;
    }

    public void setIdDiadiemtochuc(int idDiadiemtochuc) {
        this.idDiadiemtochuc = idDiadiemtochuc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoinghiEntity that = (HoinghiEntity) o;
        return idHoinghi == that.idHoinghi &&
                idDiadiemtochuc == that.idDiadiemtochuc &&
                Objects.equals(ten, that.ten) &&
                Objects.equals(moTaNganGon, that.moTaNganGon) &&
                Objects.equals(moTaChiTiet, that.moTaChiTiet) &&
                Objects.equals(hinhAnh, that.hinhAnh) &&
                Objects.equals(thoiGianBatDau, that.thoiGianBatDau) &&
                Objects.equals(thoiGianKetThuc, that.thoiGianKetThuc) &&
                Objects.equals(nguoiThamDu, that.nguoiThamDu) &&
                Objects.equals(soLuongToiDa, that.soLuongToiDa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idHoinghi, ten, moTaNganGon, moTaChiTiet, hinhAnh, thoiGianBatDau, thoiGianKetThuc, nguoiThamDu, soLuongToiDa, idDiadiemtochuc);
    }

    @ManyToOne
    @JoinColumn(name = "id_diadiemtochuc", referencedColumnName = "id_diadiemtochuc", nullable = false)
    public DiadiemtochucEntity getDiadiemtochucByIdDiadiemtochuc() {
        return diadiemtochucByIdDiadiemtochuc;
    }

    public void setDiadiemtochucByIdDiadiemtochuc(DiadiemtochucEntity diadiemtochucByIdDiadiemtochuc) {
        this.diadiemtochucByIdDiadiemtochuc = diadiemtochucByIdDiadiemtochuc;
    }

    @OneToMany(mappedBy = "hoinghiByIdHoinghi")
    public Collection<ThamduhoinghiEntity> getThamduhoinghisByIdHoinghi() {
        return thamduhoinghisByIdHoinghi;
    }

    public void setThamduhoinghisByIdHoinghi(Collection<ThamduhoinghiEntity> thamduhoinghisByIdHoinghi) {
        this.thamduhoinghisByIdHoinghi = thamduhoinghisByIdHoinghi;
    }
}
