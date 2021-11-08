package com.example.radiologi.utils.mapper;

import com.example.radiologi.data.dataSource.remote.response.AdminItemResponse;
import com.example.radiologi.data.dataSource.remote.response.DataItemAdmin;
import com.example.radiologi.data.dataSource.remote.response.DataItemDoctor;
import com.example.radiologi.data.dataSource.remote.response.DoctorListResponse;
import com.example.radiologi.data.entitiy.ItemAdminEntity;
import com.example.radiologi.data.entitiy.ItemDoctorEntity;

import java.util.ArrayList;
import java.util.List;

public class MapperHelper {
    public static List<ItemAdminEntity> mapAdminNewResponseToEntity(AdminItemResponse response, int page){
        final ArrayList<ItemAdminEntity> adminEntities = new ArrayList<>();
        for(DataItemAdmin item : response.getData()){
            if (item.getStatus().equals("0")){
                ItemAdminEntity adminEntity = new ItemAdminEntity(
                        item.getNoregis(),
                        item.getPengirim(),
                        item.getTanglahir(),
                        item.getNamapasien(),
                        item.getDiagnosa(),
                        item.getGender(),
                        item.getTtd(),
                        item.getPenerima(),
                        item.getId(),
                        item.getGambar(),
                        item.getNorekam(),
                        item.getStatus(),
                        page
                );
                adminEntities.add(adminEntity);
            }
        }
        return adminEntities;
    }

    public static List<ItemAdminEntity> mapAdminResponseToEntity(AdminItemResponse response, int page){
        final ArrayList<ItemAdminEntity> adminEntities = new ArrayList<>();
        for(DataItemAdmin item : response.getData()){
            ItemAdminEntity adminEntity = new ItemAdminEntity(
                    item.getNoregis(),
                    item.getPengirim(),
                    item.getTanglahir(),
                    item.getNamapasien(),
                    item.getDiagnosa(),
                    item.getGender(),
                    item.getTtd(),
                    item.getPenerima(),
                    item.getId(),
                    item.getGambar(),
                    item.getNorekam(),
                    item.getStatus(),
                    page
            );
            adminEntities.add(adminEntity);
        }
        return adminEntities;
    }

    public static List<ItemDoctorEntity> mapDoctorResponseToEntity(AdminItemResponse response, int page){
        ArrayList<ItemDoctorEntity> listDoctor = new ArrayList<>();
        for (DataItemAdmin items : response.getData()){
            ItemDoctorEntity doctorEntity = new ItemDoctorEntity(
                    items.getId(),
                    items.getNoregis(),
                    items.getPengirim(),
                    items.getTanglahir(),
                    items.getNamapasien(),
                    items.getDiagnosa(),
                    items.getGender(),
                    items.getTtd(),
                    items.getPenerima(),
                    items.getGambar(),
                    items.getNorekam(),
                    items.getStatus(),
                    page
            );
            listDoctor.add(doctorEntity);
        }
        return listDoctor;
    }

    public static List<ItemDoctorEntity> mapNewDoctorResponseToEntity(AdminItemResponse response, int page){
        ArrayList<ItemDoctorEntity> listDoctor = new ArrayList<>();
        for (DataItemAdmin items : response.getData()){
            if (items.getStatus().equals("0")){
                ItemDoctorEntity doctorEntity = new ItemDoctorEntity(
                        items.getId(),
                        items.getNoregis(),
                        items.getPengirim(),
                        items.getTanglahir(),
                        items.getNamapasien(),
                        items.getDiagnosa(),
                        items.getGender(),
                        items.getTtd(),
                        items.getPenerima(),
                        items.getGambar(),
                        items.getNorekam(),
                        items.getStatus(),
                        page
                );
                listDoctor.add(doctorEntity);
            }
        }
        return listDoctor;
    }
}
