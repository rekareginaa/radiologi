package com.example.radiologi.data.dataSource.remote.response;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class SimpleResponse{

	@SerializedName("notice")
	private NoticeItem notice;

	public NoticeItem getNotice(){
		return notice;
	}

	@NonNull
	@Override
 	public String toString(){
		return 
			"SimpleResponse{" + 
			"notice = '" + notice + '\'' + 
			"}";
		}
}