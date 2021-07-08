package com.example.radiologi.data.dataSource.remote.response;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class SimplesResponse{

	@SerializedName("data")
	private int data;

	@SerializedName("text")
	private String text;

	public int getData(){
		return data;
	}

	public String getText(){
		return text;
	}

	@NonNull
	@Override
 	public String toString(){
		return 
			"SimplesResponse{" + 
			"data = '" + data + '\'' + 
			",text = '" + text + '\'' + 
			"}";
		}
}