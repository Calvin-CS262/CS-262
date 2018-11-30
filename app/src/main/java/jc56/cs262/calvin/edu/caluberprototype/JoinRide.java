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

/** JoinRide class
 * Displays upcoming rides in a list that users can choose to join
 */
public class JoinRide extends AppCompatActivity {

    //TODO: list all information of ride (date, time, start, destination)
    //TODO: add functionality and connect to database
    //TODO: allow drivers to delete rides that were created under their profile


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_ride);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //currently using hardcoded array, need to replace with database retrievals
        String[] availableArray = getArray();
        ArrayAdapter uRidesAdapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, availableArray);

        ListView uRidesView = (ListView) findViewById(R.id.rides_available);
        uRidesView.setAdapter(uRidesAdapter);
        setListViewHeightBasedOnChildren(uRidesView);
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

    // Will get the array of upcoming rides from the database when set up, currently returns a hard coded array
    public String[] getArray() {
        String[] hardcodedArray = {"newRide1","newRide2","newRide3","newRide4",
                "newRide5"};
        return hardcodedArray;
    }

}
