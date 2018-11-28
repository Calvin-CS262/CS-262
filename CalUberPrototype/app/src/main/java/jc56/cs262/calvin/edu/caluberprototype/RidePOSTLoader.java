package jc56.cs262.calvin.edu.caluberprototype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.time.Instant;

public class RidePOSTLoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = RidePOSTLoader.class.getSimpleName();
    private String rideId;
    private String driverId;
    private String departure;
    private String destination;
    private String passengerLimit;
    private String departureDateTime;

    /**
     * Method called when starting the loader
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }



    public RidePOSTLoader(@NonNull Context context, String rideId, String driverId, String departure,
                          String destination, String passengerLimit, String departureDateTime) {
        super(context);
        this.rideId = rideId;
        this.driverId = driverId;
        this.departure = departure;
        this.destination = destination;
        this.passengerLimit = passengerLimit;
        this.departureDateTime = departureDateTime;
    }

    /**
     * Method performs the task in the background
     *
     * @return
     */
    @Nullable
    @Override
    public String loadInBackground() {
        Log.e(LOG_TAG, "postRideInfo called");
        return RideNetworkUtils.postRideInfo(rideId, driverId, departure, destination, passengerLimit, departureDateTime);
    }
}
