package com.example.radiologi.data.dataSource.remote.response;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AdminItemResponse{

	@SerializedName("data")
	private List<DataItemAdmin> data;

	@SerializedName("status")
	private String status;

	public List<DataItemAdmin> getData(){
		return data;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"AdminItemResponse{" + 
			"data = '" + data + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}