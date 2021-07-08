package com.example.radiologi.data.dataSource.remote.response;

import androidx.annotation.NonNull;

import java.util.List;

public class UsersResponse{
	private List<DataItemUsers> data;

	public List<DataItemUsers> getData(){
		return data;
	}

	@NonNull
	@Override
 	public String toString(){
		return 
			"UsersResponse{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}