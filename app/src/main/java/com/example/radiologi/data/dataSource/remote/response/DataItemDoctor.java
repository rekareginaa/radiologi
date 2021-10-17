package com.example.radiologi.data.dataSource.remote.response;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class DataItemDoctor {

	@SerializedName("password")
	private String password;

	@SerializedName("role")
	private String role;

	@SerializedName("nama")
	private String nama;

	@SerializedName("nip")
	private String nip;

	@SerializedName("id")
	private String id;

	@SerializedName("email")
	private String email;

	@SerializedName("token")
	private String token;

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public void setNip(String nip) {
		this.nip = nip;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPassword(){
		return password;
	}

	public String getRole(){
		return role;
	}

	public String getNama(){
		return nama;
	}

	public String getNip(){
		return nip;
	}

	public String getId(){
		return id;
	}

	public String getEmail(){
		return email;
	}

	public String getToken(){
		return token;
	}

	@NonNull
	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"password = '" + password + '\'' + 
			",role = '" + role + '\'' + 
			",nama = '" + nama + '\'' + 
			",nip = '" + nip + '\'' + 
			",id = '" + id + '\'' + 
			",email = '" + email + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}
}