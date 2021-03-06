package gabriellee.project.pixabaybrowser.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import gabriellee.project.pixabaybrowser.R;
import gabriellee.project.pixabaybrowser.model.Hit;

public class HitRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Hit> mHits;
    private static final int PICTURE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int EXHAUSTED_TYPE = 3;

    public HitRecyclerAdapter() {
        mHits = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = null;
        switch(i) {

            case PICTURE_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_hit_list_item, viewGroup, false);
                return new HitViewHolder(view);
            }

            case LOADING_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_item, viewGroup, false);
                return new LoadingViewHolder(view);
            }
            case EXHAUSTED_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_exhausted, viewGroup, false);
                return new ExhaustedViewHolder(view);
            }

            default:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_hit_list_item, viewGroup, false);
                return new HitViewHolder(view);
            }

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int itemViewType = getItemViewType(i);
        if(itemViewType == PICTURE_TYPE) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.ic_launcher_background);

            Glide.with(((HitViewHolder) viewHolder).itemView)
                    .setDefaultRequestOptions(options)
                    .load(mHits.get(i).getWebformatURL())
                    .into(((HitViewHolder) viewHolder).image);


            Glide.with(((HitViewHolder) viewHolder).itemView)
                    .setDefaultRequestOptions(options)
                    .load(mHits.get(i).getUserImageURL())
                    .into(((HitViewHolder) viewHolder).userImage);

            ((HitViewHolder)viewHolder).user.setText(mHits.get(i).getUser());
            ((HitViewHolder)viewHolder).views.setText(String.valueOf(mHits.get(i).getViews()));
            ((HitViewHolder)viewHolder).tags.setText(mHits.get(i).getTags());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mHits.get(position).getTags().equals("LOADING...")) {
            return LOADING_TYPE;
        }
        else if (mHits.get(position).getTags().equals("EXHAUSTED...")) {
            return EXHAUSTED_TYPE;
        }

        else if (position == mHits.size() - 1 && position != 0 && !mHits.get(position).getTags().equals("EXHAUSTED...")){
            return LOADING_TYPE;
        }
        else {
            return PICTURE_TYPE;
        }
    }

    public void setQueryExhausted(){
        hideLoading();
        Hit exhaustedHit = new Hit();
        exhaustedHit.setTags("EXHAUSTED...");
        mHits.add(exhaustedHit);
        notifyDataSetChanged();
    }

    private void hideLoading(){
        if(isLoading()) {
            for (Hit hit: mHits) {
                if(hit.getTags().equals("LOADING...")) {
                    mHits.remove(hit);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void displayLoading(){
        if(!isLoading()){
            Hit hit = new Hit();
            hit.setTags("LOADING...");
            List<Hit> loadingList = new ArrayList<>();
            loadingList.add(hit);
            mHits = loadingList;
            notifyDataSetChanged();
        }
    }

    private boolean isLoading(){
        if (mHits != null) {
            if(mHits.size() > 0) {
                if(mHits.get(mHits.size() - 1).getTags().equals("LOADING...")){
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int getItemCount() {
        if(mHits != null) {
            return mHits.size();
        }
        return 0;
    }

    public void setHits(List<Hit> hits) {
        mHits = hits;
        notifyDataSetChanged();
    }
}
