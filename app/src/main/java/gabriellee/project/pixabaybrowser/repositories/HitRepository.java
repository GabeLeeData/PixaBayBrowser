package gabriellee.project.pixabaybrowser.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.List;

import gabriellee.project.pixabaybrowser.model.Hit;
import gabriellee.project.pixabaybrowser.requests.HitApiClient;

public class HitRepository {

    private static HitRepository instance;
    private HitApiClient mHitApiClient;
    private String mQuery;
    private int mPageNumber;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Hit>> mHits = new MediatorLiveData<>();

    public static HitRepository getInstance() {
        if(instance == null){
            instance = new HitRepository();
        }
        return instance;
    }

    private HitRepository() {
        mHitApiClient = HitApiClient.getInstance();
        initMediators();

    }

    private void initMediators() {
        LiveData<List<Hit>> hitListApiSource = mHitApiClient.getHits();
        mHits.addSource(hitListApiSource, new Observer<List<Hit>>() {
            @Override
            public void onChanged(@Nullable List<Hit> hits) {
                if(hits != null) {
                    mHits.setValue(hits);
                    doneQuery(hits);
                }

                else {
                    //search database cache
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery(List<Hit> list) {
        if (list != null) {
            if (list.size() %20 != 0) {
                mIsQueryExhausted.setValue(true);
            }
        }
        else {
            mIsQueryExhausted.setValue(true);
        }
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mIsQueryExhausted;
    }

    public LiveData<List<Hit>> getHits(){
        return mHits;
    }

    public void searchHitsApi(String query, int pageNumber) {
        if(pageNumber ==0) {
            pageNumber = 1;
        }
        mQuery = query;
        mPageNumber = pageNumber;
        mIsQueryExhausted.setValue(false);
        mHitApiClient.searchHitsApi(query, pageNumber);
    }

    public  void searchNextPage() {
        searchHitsApi(mQuery, mPageNumber+1);
    }

}
