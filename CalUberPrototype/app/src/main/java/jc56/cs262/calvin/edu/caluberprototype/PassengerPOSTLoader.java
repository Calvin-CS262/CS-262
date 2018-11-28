package jc56.cs262.calvin.edu.caluberprototype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.time.Instant;

public class PassengerPOSTLoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = PassengerPOSTLoader.class.getSimpleName();
    private  String id;
    private String rideId;
    private String personId;

    /**
     * Method called when starting the loader
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }



    public PassengerPOSTLoader(@NonNull Context context, String id, String rideId, String personId) {
        super(context);
        this.id = id;
        this.rideId = rideId;
        this.personId = personId;
    }

    /**
     * Method performs the task in the background
     *
     * @return
     */
    @Nullable
    @Override
    public String loadInBackground() {
        Log.e(LOG_TAG, "postPassengerInfo called");
        return PassengerNetworkUtils.postPassengerInfo(id, rideId, personId);
    }
}
