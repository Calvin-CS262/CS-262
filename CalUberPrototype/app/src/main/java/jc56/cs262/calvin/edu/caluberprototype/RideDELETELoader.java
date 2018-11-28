package jc56.cs262.calvin.edu.caluberprototype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class RideDELETELoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = RideDELETELoader.class.getSimpleName();
    private String rideId;

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public RideDELETELoader(@NonNull Context context, String rideId) {
        super(context);
        this.rideId = rideId;
    }

    /**
     * Method performs the task in the background
     *
     * @return
     */
    @Nullable
    @Override
    public String loadInBackground() {
        Log.e(LOG_TAG, "deleteRideInfo called");
        return RideNetworkUtils.deleteRideInfo(rideId);
    }
}
