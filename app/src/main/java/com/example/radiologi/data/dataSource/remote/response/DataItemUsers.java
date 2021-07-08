package com.example.radiologi.data.dataSource.remote.response;

import androidx.annotation.NonNull;

public class DataItemUsers {
	private String password;
	private String role;
	private String nama;
	private String nip;
	private String id;
	private String email;
	private String token;

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
