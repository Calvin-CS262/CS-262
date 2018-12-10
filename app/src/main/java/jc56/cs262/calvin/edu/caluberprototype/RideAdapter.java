package jc56.cs262.calvin.edu.caluberprototype;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class RideAdapter extends ArrayAdapter<Ride> {
    private Context mContext;
    private List<Ride> rideList = new ArrayList<>();
    private static String TAG = "JoinRide";

    public RideAdapter (@NonNull Context context, @LayoutRes ArrayList<Ride> rides) {
        super(context, 0, rides);
        mContext = context;
        rideList = rides;
    }

    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView,@NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.activity_listview, parent, false);

        Ride currentRide = rideList.get(position);

        TextView list = (TextView) listItem.findViewById(R.id.label);
        list.setText("Destination: " + currentRide.getDestination() + " Departure: " + currentRide.getDeparture()
        + " Date and Time: " + currentRide.getDepartureDateTime() );

        return listItem;

    }

}
