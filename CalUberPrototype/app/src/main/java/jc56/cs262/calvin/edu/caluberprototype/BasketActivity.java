package jc56.cs262.calvin.edu.caluberprototype;

import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/** BasketActivity Class
 * BasketActivity sets up the basket page, where users can see their upcoming rides and previous rides they've been on
 */
//NEED TO FIGURE OUT HOW TO MAKE LISTVIEW FILL ALL OF SCROLLVIEW

public class BasketActivity extends AppCompatActivity {

    String[] upcomingRideArray = {"newRide1","newRide2","newRide3","newRide4",
            "newRide5"};
    String[] upcomingDriveArray = {"newDrive1", "newDrive2", "newDrive3"};

    String[] pastArray = {"PastRide1", "PastRide2", "PastRide3", "PastRide4",
            "PastRide5", "PastRide6", "PastRide7"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayAdapter uRidesAdapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, upcomingRideArray);

        ArrayAdapter uDrivesAdapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, upcomingDriveArray);

        ArrayAdapter pastAdapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, pastArray);

        ListView uRidesView = (ListView) findViewById(R.id.upcoming_rides);
        uRidesView.setAdapter(uRidesAdapter);
        setListViewHeightBasedOnChildren(uRidesView);


        ListView uDriveView = (ListView) findViewById(R.id.upcoming_drives);
        uDriveView.setAdapter(uDrivesAdapter);
        setListViewHeightBasedOnChildren(uDriveView);

        ListView pastView = (ListView) findViewById(R.id.past_rides);
        pastView.setAdapter(pastAdapter);
        setListViewHeightBasedOnChildren(pastView);
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
