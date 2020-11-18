package com.example.radiologi;

public class ListitemAdmin {

    private String noRekam;
    private String namaLengkap;
    private String tangLahir;
    private String gender;
    private String gambar;


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
}
