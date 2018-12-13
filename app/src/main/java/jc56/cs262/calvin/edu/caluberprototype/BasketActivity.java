package jc56.cs262.calvin.edu.caluberprototype;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/** BasketActivity Class
 * BasketActivity sets up the basket page, where users can see their upcoming rides and previous rides they've been on
 */

public class BasketActivity extends AppCompatActivity {

    ArrayList<String> upcomingRideArray = new ArrayList<>();
    ArrayList<String> upcomingDriveArray = new ArrayList<>();
    ArrayList<String> pastArray = new ArrayList<>();

    private ArrayList<Ride> RideList = new ArrayList<>();
    private ArrayList<Passenger> PassengerList = new ArrayList<>();
    private Globals userInfo = Globals.getInstance();
    private static String TAG = "BasketActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GetRideTask().execute(createURL(""));
        new GetPassengerTask().execute(createURL("passengers"));

        Log.d(TAG, RideList.toString() + "List of Ride objects");
        Log.d(TAG, PassengerList.toString() + "List of Passenger objects");



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

    private String helpMessage =
            "Past Rides:\n" +
                    "You can use this section of the Rides page to look at past rides that you have been on.\n" +
                    "\n" +
                    "Imagine that you want to remember when you carpooled to a certain destination. To do this you can:\n" +
                    "1. Click on the Rides page button at the top right of the home page.\n" +
                    "2. Scroll down to the Past Rides section.\n" +
                    "3. Look for the ride you are searching for.";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BasketActivity.this);
            builder.setMessage(helpMessage)
                    .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
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

    public void refresh(View view) {
        Log.d(TAG, RideList.toString());
        Log.d(TAG, PassengerList.toString());
        upcomingDriveArray.clear();
        upcomingRideArray.clear();
        pastArray.clear();
        Date today = Calendar.getInstance().getTime();
        Log.d(TAG, today.toString());
        for (int i = 0; i < RideList.size(); i++) {
            if (RideList.get(i).getDriverId() == Globals.getInstance().getValue()) {
                String rideDay = RideList.get(i).getDepartureDateTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try {
                    Date date = format.parse(rideDay);
                    if (today.after(date)) {
                        pastArray.add("Departure: " + RideList.get(i).getDeparture() + "  Destination: " + RideList.get(i).getDestination() + " Date: " + RideList.get(i).getDepartureDateTime());
                    } else {
                        upcomingDriveArray.add("Departure: " + RideList.get(i).getDeparture() + "  Destination: " + RideList.get(i).getDestination() + " Date: " + RideList.get(i).getDepartureDateTime());
                    }
                } catch (ParseException e) {
                    Log.d(TAG, e.getMessage());
                }
            }
            for (int j = 0; j < PassengerList.size(); j++) {
                if (PassengerList.get(j).getPersonId() == Globals.getInstance().getValue() && PassengerList.get(j).getRideId() == RideList.get(i).getRideId()) {
                    String rideDay = RideList.get(i).getDepartureDateTime();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    try {
                        Date date = format.parse(rideDay);
                        if (today.after(date)) {
                            pastArray.add("Departure: " + RideList.get(i).getDeparture() + " Destination: " + RideList.get(i).getDestination() + " Date: " + RideList.get(i).getDepartureDateTime());
                        } else {
                            upcomingRideArray.add("Departure: " + RideList.get(i).getDeparture() + " Destination: " + RideList.get(i).getDestination() + " Date: " + RideList.get(i).getDepartureDateTime());
                        }
                    } catch (ParseException e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }
        }

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

    //To get the list of users from the database
    private class GetRideTask extends AsyncTask<URL, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(URL... params) {
            HttpURLConnection connection = null;
            StringBuilder jsonText = new StringBuilder();
            JSONArray result = null;
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonText.append(line);
                    }
                    //Log.d(TAG, jsonText.toString());
                    if (jsonText.toString().startsWith("{ \"items\":")) {
                        // Google Cloud can't return a raw JSON list, so we had to add this "items" field;
                        // remove it now.
                        String jsonItemsText = new JSONObject(jsonText.toString()).get("items").toString();
                        result = new JSONArray(jsonItemsText);
                    } else if (jsonText.toString().equals("null")) {
                        result = new JSONArray();
                    } else {
                        result = new JSONArray().put(new JSONObject(jsonText.toString()));
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }


        @Override
        protected void onPostExecute(JSONArray rides) {
            RideList.clear();
            convertRideJSONtoArrayList(rides);
//            if (rides == null) {
//                //Toast.makeText(BasketActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
//            } else if (rides.length() == 0) {
//                //Toast.makeText(BasketActivity.this, getString(R.string.no_results_error), Toast.LENGTH_SHORT).show();
//            } else {
//                convertRideJSONtoArrayList(rides);
//            }
        }

    }

    private class GetPassengerTask extends AsyncTask<URL, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(URL... params) {
            HttpURLConnection connection = null;
            StringBuilder jsonText = new StringBuilder();
            JSONArray result = null;
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonText.append(line);
                    }
                    //Log.d(TAG, jsonText.toString());
                    if (jsonText.toString().startsWith("{ \"items\":")) {
                        // Google Cloud can't return a raw JSON list, so we had to add this "items" field;
                        // remove it now.
                        String jsonItemsText = new JSONObject(jsonText.toString()).get("items").toString();
                        result = new JSONArray(jsonItemsText);
                    } else if (jsonText.toString().equals("null")) {
                        result = new JSONArray();
                    } else {
                        result = new JSONArray().put(new JSONObject(jsonText.toString()));
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }


        @Override
        protected void onPostExecute(JSONArray passengers) {
            PassengerList.clear();
            convertPassengerJSONtoArrayList(passengers);
//            if (passengers == null) {
////                Toast.makeText(BasketActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
//            } else if (passengers.length() == 0) {
////                Toast.makeText(BasketActivity.this, getString(R.string.no_results_error), Toast.LENGTH_SHORT).show();
//            } else {
//                convertPassengerJSONtoArrayList(passengers);
//            }
        }

    }

    private void convertRideJSONtoArrayList(JSONArray rides) {
        Log.d(TAG, rides.toString() + " Before conversion");
        try {
            for (int i = 0; i < rides.length(); i++) {
                JSONObject ride = rides.getJSONObject(i);
                RideList.add(new Ride(
                        ride.getInt("rideId"),
                        ride.getInt("driverId"),
                        ride.optString("departure", "no departure"),
                        ride.optString("destination", "no destination"),
                        ride.optInt("passengerLimit"),
                        ride.optString("departureDateTime", "no departure date and time")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, RideList.toString() + "After conversion");
    }

    private void convertPassengerJSONtoArrayList(JSONArray passengers) {
        Log.d(TAG, passengers.toString() + "Before conversion");
        try {
            for (int i = 0; i < passengers.length(); i++) {
                JSONObject passenger = passengers.getJSONObject(i);
                PassengerList.add(new Passenger(
                        passenger.getInt("id"),
                        passenger.getInt("rideId"),
                        passenger.getInt("personId")

                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, PassengerList.toString() + "After conversion");
    }

    private URL createURL(String id) {
        try {
            String urlString = getString(R.string.web_service_url);
            if (id.equals("")) {
                urlString += "/rides";
            } else if (id.equals("passenger")) {
                urlString += "/passenger";
            } else if (id.equals("passengers")) {
                urlString += "/passengers";
            } else {
                throw new Exception();
            }
            return new URL(urlString);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

}