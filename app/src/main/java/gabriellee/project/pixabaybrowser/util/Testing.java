package gabriellee.project.pixabaybrowser.util;

import android.util.Log;

import java.util.List;

import gabriellee.project.pixabaybrowser.model.Hit;


public class Testing {

    public static void printHits(List<Hit> list, String tag) {
        for(Hit hit:list) {
            Log.d(tag, "printHits: " +hit.getWebformatURL());
        }
    }
}
