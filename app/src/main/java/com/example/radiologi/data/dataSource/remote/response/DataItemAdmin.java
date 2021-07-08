package com.example.radiologi.data.dataSource.remote.response;

import com.google.gson.annotations.SerializedName;

public class DataItemAdmin {

	@SerializedName("noregis")
	private String noregis;

	@SerializedName("pengirim")
	private String pengirim;

	@SerializedName("tanglahir")
	private String tanglahir;

	@SerializedName("namapasien")
	private String namapasien;

	@SerializedName("diagnosa")
	private String diagnosa;

	@SerializedName("gender")
	private String gender;

	@SerializedName("ttd")
	private String ttd;

	@SerializedName("penerima")
	private String penerima;

	@SerializedName("id")
	private String id;

	@SerializedName("gambar")
	private String gambar;

	@SerializedName("norekam")
	private String norekam;

	@SerializedName("status")
	private String status;

	public String getNoregis(){
		return noregis;
	}

	public String getPengirim(){
		return pengirim;
	}

	public String getTanglahir(){
		return tanglahir;
	}

	public String getNamapasien(){
		return namapasien;
	}

	public String getDiagnosa(){
		return diagnosa;
	}

	public String getGender(){
		return gender;
	}

	public String getTtd(){
		return ttd;
	}

	public String getPenerima(){
		return penerima;
	}

	public String getId(){
		return id;
	}

	public String getGambar(){
		return gambar;
	}

	public String getNorekam(){
		return norekam;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"noregis = '" + noregis + '\'' + 
			",pengirim = '" + pengirim + '\'' + 
			",tanglahir = '" + tanglahir + '\'' + 
			",namapasien = '" + namapasien + '\'' + 
			",diagnosa = '" + diagnosa + '\'' + 
			",gender = '" + gender + '\'' + 
			",ttd = '" + ttd + '\'' + 
			",penerima = '" + penerima + '\'' + 
			",id = '" + id + '\'' + 
			",gambar = '" + gambar + '\'' + 
			",norekam = '" + norekam + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}