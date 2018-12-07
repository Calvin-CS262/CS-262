package jc56.cs262.calvin.edu.caluberprototype;

import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;
import java.util.List;

/** BasketActivity Class
 * BasketActivity sets up the basket page, where users can see their upcoming rides and previous rides they've been on
 */
//NEED TO FIGURE OUT HOW TO MAKE LISTVIEW FILL ALL OF SCROLLVIEW

public class BasketActivity extends AppCompatActivity {

    String[] upcomingRideArray = {"newRide1", "newRide2", "newRide3", "newRide4",
            "newRide5"};
    String[] upcomingDriveArray = {"newDrive1", "newDrive2", "newDrive3"};

    String[] pastArray = {"PastRide1", "PastRide2", "PastRide3", "PastRide4",
            "PastRide5", "PastRide6", "PastRide7"};

    private List<Ride> RideList = new ArrayList<>();
    private List<Passenger> PassengerList = new ArrayList<>();
    private Globals userInfo = Globals.getInstance();
    private static String TAG = "BasketActivity";

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

        new GetRideTask().execute(createURL(""));
        new GetPassengerTask().execute(createPassengerURL(""));
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
            if (rides == null) {
                Toast.makeText(BasketActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            } else if (rides.length() == 0) {
                Toast.makeText(BasketActivity.this, getString(R.string.no_results_error), Toast.LENGTH_SHORT).show();
            } else {
                convertRideJSONtoArrayList(rides);
            }
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
            RideList.clear();
            if (passengers == null) {
                Toast.makeText(BasketActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            } else if (passengers.length() == 0) {
                Toast.makeText(BasketActivity.this, getString(R.string.no_results_error), Toast.LENGTH_SHORT).show();
            } else {
                convertPassengerJSONtoArrayList(passengers);
            }
        }

    }

    private void convertRideJSONtoArrayList(JSONArray rides) {
        Log.d(TAG, rides.toString());
        try {
            for (int i = 0; i < rides.length(); i++) {
                JSONObject player = rides.getJSONObject(i);
                RideList.add(new Ride(
                        player.getInt("rideId"),
                        player.getInt("driverId"),
                        player.optString("departure", "no departure"),
                        player.optString("destination", "no destination"),
                        player.optInt("passengerLimit"),
                        player.optString("departureDateTime", "no departure date and time")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, RideList.toString());
    }

    private void convertPassengerJSONtoArrayList(JSONArray passengers) {
        Log.d(TAG, passengers.toString());
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
        Log.d(TAG, RideList.toString());
    }

    private URL createURL(String id) {
        try {
            String urlString = getString(R.string.web_service_url);
            if (id.equals("")) {
                urlString += "/rides";
            } else {
                urlString += "/rides/" + id;
            }
            return new URL(urlString);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    private URL createPassengerURL(String id) {
        try {
            String urlString = getString(R.string.web_service_url);
            if (id.equals("")) {
                urlString += "/passengers";
            } else {
                urlString += "/passengers/" + id;
            }
            return new URL(urlString);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }

        return null;
    }
}