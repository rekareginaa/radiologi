package com.example.radiologi.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.radiologi.data.dataSource.remote.vo.ApiResponse;
import com.example.radiologi.utils.vo.Resource;

public abstract class NetworkFetchResource<ResultType, RequestType> {
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    public NetworkFetchResource(){
        result.setValue(Resource.loading(null));

        fetchFromNetwork();

    }

    protected abstract LiveData<ApiResponse<RequestType>> createCall();
    protected abstract ResultType mapper(RequestType data);

    private void fetchFromNetwork(){
        LiveData<ApiResponse<RequestType>> apiResponse = createCall();

        result.addSource(apiResponse, response ->{
            result.removeSource(apiResponse);

            switch (response.status){
                case SUCCESS:
                    result.addSource(apiResponse, data->{
                        result.setValue(Resource.success(mapper(data.body)));
                    });
                    break;
                case EMPTY:
                    result.addSource(apiResponse, data->{
                        result.setValue(Resource.error("Data Kosong", null));
                    });
                    break;
                case ERROR:
                    result.addSource(apiResponse, data->{
                        result.setValue(Resource.error(data.message, null));
                    });
            }
        });
    }

    public LiveData<Resource<ResultType>> asLiveData(){return result;}
}
