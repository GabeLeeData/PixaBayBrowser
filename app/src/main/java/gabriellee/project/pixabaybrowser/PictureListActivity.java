package gabriellee.project.pixabaybrowser;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gabriellee.project.pixabaybrowser.model.Hit;
import gabriellee.project.pixabaybrowser.requests.PictureResponse;
import gabriellee.project.pixabaybrowser.requests.PixabayApi;
import gabriellee.project.pixabaybrowser.requests.ServiceGenerator;
import gabriellee.project.pixabaybrowser.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PictureListActivity extends BaseActivity {

    private static final String TAG = "PictureListActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_list);

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRetrofitRequest();
            }
        });
    }

    private void testRetrofitRequest(){
        PixabayApi pixabayApi = ServiceGenerator.getPixabayApi();

        Call<PictureResponse> responseCall = pixabayApi.searchPicture(Constants.API_KEY, "cats", "1");

        responseCall.enqueue(new Callback<PictureResponse>() {
            @Override
            public void onResponse(Call<PictureResponse> call, Response<PictureResponse> response) {
                Log.d(TAG, "onResponse: server response: " +response.toString());

                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: " +response.body().toString());
                    List<Hit> hits = new ArrayList<>(response.body().getHit());

                    for (Hit hit: hits){
                        Log.d(TAG, "onResponse: " + hit.getTags());
                    }
                }
                else {
                    try{
                        Log.d(TAG, "onResponse: " + response.errorBody().string());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PictureResponse> call, Throwable t) {

            }
        });
    }
}
