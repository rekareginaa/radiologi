package com.example.radiologi.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.radiologi.data.dataSource.remote.vo.ApiResponse;
import com.example.radiologi.utils.AppExecutors;
import com.example.radiologi.utils.vo.Resource;

public abstract class NetworkBoundResource<ResultType, RequestType> {
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    private final AppExecutors appExecutors;

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;

        result.setValue(Resource.loading(null));

        LiveData<ResultType> dbSource = loadFromDB();

        result.addSource(dbSource, data ->{
            result.removeSource(dbSource);
            if (shouldFetch(data)){
                fetchFromNetwork(dbSource);
            }else {
                result.addSource(dbSource, newData -> result.setValue(Resource.success(newData)));
            }
        });
    }

    protected void onFetchFailed(){}
    protected abstract LiveData<ResultType> loadFromDB();
    protected abstract Boolean shouldFetch(ResultType data);
    protected abstract LiveData<ApiResponse<RequestType>> createCall();
    protected abstract void saveCallResult(RequestType data);

    private void fetchFromNetwork(LiveData<ResultType> dbSource){
        LiveData<ApiResponse<RequestType>> apiResponse = createCall();

        result.addSource(dbSource, newData ->
                result.setValue(Resource.loading(newData))
        );
        result.addSource(apiResponse, response ->{
                result.removeSource(apiResponse);
                result.removeSource(dbSource);

                switch (response.status){
                    case SUCCESS:
                        appExecutors.diskIO().execute(() -> {
                            saveCallResult(response.body);
                            appExecutors.mainThread().execute(() -> {
                                result.addSource(loadFromDB(),
                                        newData -> result.setValue(Resource.success(newData)));
                            });
                        });
                        break;
                    case EMPTY:
                        appExecutors.mainThread().execute(() ->
                                result.addSource(loadFromDB(),
                                    newData -> result.setValue(Resource.success(newData))));
                        break;
                    case ERROR:
                        onFetchFailed();
                        result.addSource(dbSource, newData ->
                                result.setValue(Resource.error(response.message, newData)));
                }
            }
        );
    }

    public LiveData<Resource<ResultType>> asLiveData(){
        return result;
    }

}
