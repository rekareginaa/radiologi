package com.example.radiologi.data.dataSource.remote.response;

import androidx.annotation.NonNull;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DoctorListResponse{
	@SerializedName("data")
	private List<DataItemDoctor> data;

	public void setData(List<DataItemDoctor> data) {
		this.data = data;
	}

	public List<DataItemDoctor> getData(){
		return data;
	}

	@NonNull
	@Override
 	public String toString(){
		return 
			"DoctorListResponse{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}