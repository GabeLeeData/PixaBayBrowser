package gabriellee.project.pixabaybrowser.util;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpacingDec extends RecyclerView.ItemDecoration {

    private final int VerticalSpaceHeight;

    public VerticalSpacingDec(int verticalSpaceHeight) {
        VerticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        outRect.top = VerticalSpaceHeight;
    }
}
