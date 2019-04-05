package gabriellee.project.pixabaybrowser.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import gabriellee.project.pixabaybrowser.model.Hit;
import gabriellee.project.pixabaybrowser.util.AppExecutors;
import gabriellee.project.pixabaybrowser.util.Constants;
import retrofit2.Call;
import retrofit2.Response;

import static gabriellee.project.pixabaybrowser.util.Constants.NETWORK_TIMEOUT;

public class HitApiClient {
    private static final String TAG = "HitApiClient";
    private static HitApiClient instance;
    private MutableLiveData<List<Hit>> mHits;
    private RetrieveHitsRunnable mRetrieveHitsRunnable;

    public static HitApiClient getInstance() {
        if(instance==null){
            instance = new HitApiClient();
        }
        return instance;
    }

    private HitApiClient(){
        mHits = new MutableLiveData<>();
    }

    public LiveData<List<Hit>> getHits(){
        return mHits;
    }

    public void searchHitsApi(String query, int pageNumber) {

        if(mRetrieveHitsRunnable !=null) {
            mRetrieveHitsRunnable = null;
        }

        mRetrieveHitsRunnable = new RetrieveHitsRunnable(query, pageNumber);
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveHitsRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Let the user know its timed out
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }


    private class RetrieveHitsRunnable implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveHitsRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getPictures(query, pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200) {
                    List<Hit> list = new ArrayList<>(((PictureResponse)response.body()).getHit());
                    if(pageNumber == 1) {
                        mHits.postValue(list);
                    }
                    else {
                        List<Hit> currentHits = mHits.getValue();
                        currentHits.addAll(list);
                    }
                }
                else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mHits.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mHits.postValue(null);
            }

        }

        private Call<PictureResponse> getPictures(String query, int pageNumber) {
            return ServiceGenerator.getPixabayApi().searchPicture(Constants.API_KEY, query, String.valueOf(pageNumber));
        }

        private void cancelRequest(){
            Log.d(TAG, "cancelRequest: canceling the search request.");
            cancelRequest = true;
        }

    }


}
