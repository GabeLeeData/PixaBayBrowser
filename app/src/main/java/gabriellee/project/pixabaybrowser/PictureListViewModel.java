package gabriellee.project.pixabaybrowser;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import gabriellee.project.pixabaybrowser.model.Hit;
import gabriellee.project.pixabaybrowser.repositories.HitRepository;

public class PictureListViewModel extends ViewModel {

    private HitRepository mHitRepository;
    private boolean mIsPerformingQuery;

    public PictureListViewModel() {
        mHitRepository = HitRepository.getInstance();

        mIsPerformingQuery = false;
    }



    public void searchNextPage(){
        if( !isQueryExhausted().getValue()) {
            mHitRepository.searchNextPage();
        }

    }

    public void setIsPerformingQuery(Boolean isPerformingQuery) {
        mIsPerformingQuery = isPerformingQuery;
    }


    public LiveData<Boolean> isQueryExhausted(){
        return mHitRepository.isQueryExhausted();
    }

    public LiveData<List<Hit>> getHits() {
        return mHitRepository.getHits();
    }

    public void searchHitsApi(String query, int pageNumber) {
        mIsPerformingQuery = true;
        mHitRepository.searchHitsApi(query, pageNumber);
    }

}
