package jc56.cs262.calvin.edu.caluberprototype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class PassengerDELETELoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = PassengerDELETELoader.class.getSimpleName();
    private String id;

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public PassengerDELETELoader(@NonNull Context context, String id) {
        super(context);
        this.id = id;
    }

    /**
     * Method performs the task in the background
     *
     * @return
     */
    @Nullable
    @Override
    public String loadInBackground() {
        Log.e(LOG_TAG, "deletePassengerInfo called");
        return PassengerNetworkUtils.deletePassengerInfo(id);
    }
}
