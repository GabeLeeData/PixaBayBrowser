package gabriellee.project.pixabaybrowser;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;


import java.util.List;

import gabriellee.project.pixabaybrowser.adapters.HitRecyclerAdapter;
import gabriellee.project.pixabaybrowser.model.Hit;
import gabriellee.project.pixabaybrowser.util.BaseActivity;
import gabriellee.project.pixabaybrowser.util.Testing;
import gabriellee.project.pixabaybrowser.util.VerticalSpacingDec;


public class PictureListActivity extends BaseActivity {

    private static final String TAG = "PictureListActivity";
    private PictureListViewModel mPictureListViewModel;
    private RecyclerView mRecyclerView;
    private HitRecyclerAdapter mAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_list);
        mRecyclerView = findViewById(R.id.picture_list);
        mSearchView = findViewById(R.id.search_view);
        mPictureListViewModel = ViewModelProviders.of(this).get(PictureListViewModel.class);
        initRecyclerView();
        subscribeObservers();
        mPictureListViewModel.searchHitsApi("dogs", 1);

        initSearchView();

    }

    private void subscribeObservers(){
        mPictureListViewModel.getHits().observe(this, new Observer<List<Hit>>() {
            @Override
            public void onChanged(@Nullable List<Hit> hits) {

                if(hits != null) {
                    Testing.printHits(hits, "recipe test");
                    mPictureListViewModel.setIsPerformingQuery(false);
                    mAdapter.setHits(hits);

                }

            }
        });

        mPictureListViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean) {
                    mAdapter.setQueryExhausted();
                }
            }
        });
    }


    private void initSearchView() {

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mAdapter.displayLoading();
                mPictureListViewModel.searchHitsApi(s, 1);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void initRecyclerView(){
        mAdapter = new HitRecyclerAdapter();
        VerticalSpacingDec itemDecorator = new VerticalSpacingDec(40);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if(!mRecyclerView.canScrollVertically(1)){
                    //search the next page
                    mPictureListViewModel.searchNextPage();
                }
            }
        });

    }


    //    //Testing Api Connection.
//    private void testRetrofitRequest(){
//        PixabayApi pixabayApi = ServiceGenerator.getPixabayApi();
//
//        Call<PictureResponse> responseCall = pixabayApi.searchPicture(Constants.API_KEY, "cats", "1");
//
//        responseCall.enqueue(new Callback<PictureResponse>() {
//            @Override
//            public void onResponse(Call<PictureResponse> call, Response<PictureResponse> response) {
//                Log.d(TAG, "onResponse: server response: " +response.toString());
//
//                if (response.code() == 200) {
//                    Log.d(TAG, "onResponse: " +response.body().toString());
//                    List<Hit> hits = new ArrayList<>(response.body().getHit());
//
//                    for (Hit hit: hits){
//                        Log.d(TAG, "onResponse: " + hit.getTags());
//                    }
//                }
//                else {
//                    try{
//                        Log.d(TAG, "onResponse: " + response.errorBody().string());
//                    }catch (IOException e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PictureResponse> call, Throwable t) {
//
//            }
//        });
//    }
}
