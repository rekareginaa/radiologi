package com.example.radiologi.data.dataSource.remote.response;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LoginResponse{

	@SerializedName("data")
	private List<DataItemLogin> data;

	@SerializedName("status")
	private String status;

	public List<DataItemLogin> getData(){
		return data;
	}

	public String getStatus(){
		return status;
	}
}