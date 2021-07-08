package com.example.radiologi.data.dataSource.remote.response;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class NoticeItem {

	@SerializedName("text")
	private String text;

	public String getText(){
		return text;
	}

	@NonNull
	@Override
 	public String toString(){
		return 
			"Notice{" + 
			"text = '" + text + '\'' + 
			"}";
		}
}