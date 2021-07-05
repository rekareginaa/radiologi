package com.example.radiologi.data.entitiy;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "itemadminentity")
public class ItemAdminEntity implements Parcelable {
    protected ItemAdminEntity(Parcel in) {
        this.noregis = in.readString();
        this.pengirim = in.readString();
        this.tanggalLahir = in.readString();
        this.namaPasien = in.readString();
        this.diagnos = in.readString();
        this.gender = in.readString();
        this.ttd = in.readString();
        this.penerima = in.readString();
        this.id = in.readString();
        this.gambar = in.readString();
        this.norekam = in.readString();
        this.status = in.readString();
    }

    public static final Creator<ItemAdminEntity> CREATOR = new Creator<ItemAdminEntity>() {
        @Override
        public ItemAdminEntity createFromParcel(Parcel in) {
            return new ItemAdminEntity(in);
        }

        @Override
        public ItemAdminEntity[] newArray(int size) {
            return new ItemAdminEntity[size];
        }
    };

    public ItemAdminEntity(String noregis, String pengirim, String tanggalLahir, String namaPasien, String diagnos, String gender, String ttd, String penerima, String id, String gambar, String norekam, String status) {
        this.noregis = noregis;
        this.pengirim = pengirim;
        this.tanggalLahir = tanggalLahir;
        this.namaPasien = namaPasien;
        this.diagnos = diagnos;
        this.gender = gender;
        this.ttd = ttd;
        this.penerima = penerima;
        this.id = id;
        this.gambar = gambar;
        this.norekam = norekam;
        this.status = status;
    }

    public String getNoregis() {
        return noregis;
    }

    public void setNoregis(String noregis) {
        this.noregis = noregis;
    }

    public String getPengirim() {
        return pengirim;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getNamaPasien() {
        return namaPasien;
    }

    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }

    public String getDiagnos() {
        return diagnos;
    }

    public void setDiagnos(String diagnos) {
        this.diagnos = diagnos;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTtd() {
        return ttd;
    }

    public void setTtd(String ttd) {
        this.ttd = ttd;
    }

    public String getPenerima() {
        return penerima;
    }

    public void setPenerima(String penerima) {
        this.penerima = penerima;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getNorekam() {
        return norekam;
    }

    public void setNorekam(String norekam) {
        this.norekam = norekam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_data_admin")
    private String id;

    @ColumnInfo(name = "no_regis")
    private String noregis;

    @ColumnInfo(name = "pengirim")
    private String pengirim;

    @ColumnInfo(name = "tanggal_lahir")
    private String tanggalLahir;

    @ColumnInfo(name = "nama_pasien")
    private String namaPasien;

    @ColumnInfo(name = "diagnosa")
    private String diagnos;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "ttd")
    private String ttd;

    @ColumnInfo(name = "penerima")
    private String penerima;

    @ColumnInfo(name = "gambar")
    private String gambar;

    @ColumnInfo(name = "no_rekam")
    private String norekam;

    @ColumnInfo(name = "status")
    private String status;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.noregis);
        dest.writeString(this.pengirim);
        dest.writeString(this.tanggalLahir);
        dest.writeString(this.namaPasien);
        dest.writeString(this.diagnos);
        dest.writeString(this.gender);
        dest.writeString(this.ttd);
        dest.writeString(this.penerima);
        dest.writeString(this.id);
        dest.writeString(this.gambar);
        dest.writeString(this.norekam);
        dest.writeString(this.status);
    }

    @NonNull
    @Override
    public String toString() {
        return "ItemAdminEntity{" +
                "noregis='" + noregis + '\'' +
                ", pengirim='" + pengirim + '\'' +
                ", tanggalLahir='" + tanggalLahir + '\'' +
                ", namaPasien='" + namaPasien + '\'' +
                ", diagnos='" + diagnos + '\'' +
                ", gender='" + gender + '\'' +
                ", ttd='" + ttd + '\'' +
                ", penerima='" + penerima + '\'' +
                ", id='" + id + '\'' +
                ", gambar='" + gambar + '\'' +
                ", norekam='" + norekam + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
