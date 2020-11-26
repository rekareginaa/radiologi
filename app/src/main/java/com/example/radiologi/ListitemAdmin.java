package com.example.radiologi;

public class ListitemAdmin {

    private String noRekam;
    private String namaLengkap;
    private String tangLahir;
    private String gender;
    private String gambar;
    private String status;
    private String diagnosa;
    private String tdt;

    public String getTangLahir() {
        return tangLahir;
    }

    public void setTangLahir(String tangLahir) {
        this.tangLahir = tangLahir;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public void setNoRekam(String noRekam) {
        this.noRekam = noRekam;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getNoRekam() {
        return noRekam;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiagnosa() {
        return diagnosa;
    }

    public void setDiagnosa(String diagnosa) {
        this.diagnosa = diagnosa;
    }

    public String getTdt() {
        return tdt;
    }

    public void setTdt(String tdt) {
        this.tdt = tdt;
    }
}
