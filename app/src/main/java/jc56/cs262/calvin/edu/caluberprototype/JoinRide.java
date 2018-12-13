package jc56.cs262.calvin.edu.caluberprototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** JoinRide class
 * Displays upcoming rides in a list that users can choose to join
 */
public class JoinRide extends AppCompatActivity {

    //TODO: list all information of ride (date, time, start, destination)
    //TODO: add functionality and connect to database
    //TODO: allow drivers to delete rides that were created under their profile

    private EditText searchBar;
    private String searchBarText;

    private ArrayList<Ride> rideList = new ArrayList<>();
    private ListView listView;
    private RideAdapter mAdapter;
    private List<Passenger> passengerList = new ArrayList<>();
    private int rideId;
    private static String TAG = "JoinRide";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_ride);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.rides_available);
        new JoinRide.GetPlayerTask().execute(createURL(""));
        mAdapter = new RideAdapter(this,rideList);
        listView.setAdapter(mAdapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        searchBar = findViewById(R.id.search_bar);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,View view,final int position,long id) {
            final int pos = position;
            rideId = rideList.get(pos).getRideId();
            int available = rideList.get(pos).getPassengerLimit();
            int numPassenger = 0;
            new JoinRide.GetPassengerTask().execute(createURL("passengers"));
            for (int i =0; i < passengerList.size(); i ++ ) {
                if (passengerList.get(i).getRideId() == rideId ) {
                    numPassenger =+ 1;
                }
            }
            available = available - numPassenger;
                if (available > 0 ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinRide.this);
                    builder.setMessage("Do you want to join this ride?")
                            .setPositiveButton("Yes!",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    new JoinRide.PostPassengerTask().execute(createURL("passenger"));
//                                    listView.getDisplay();    //Automatically refreshing listView (doesn't work on JoinRide)
                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    //nothing happens
//                                    listView.getDisplay();
                                }
                            });
                    builder.create().show();
                }
                else {
                    Toast.makeText(JoinRide.this, "This Ride is Full :(", Toast.LENGTH_SHORT).show();
                }
            }

        });

        //call hideKeyboard method
        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v,boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBarText = searchBar.getText().toString();
                //PutUserTask().execute(createURL(searchBarText);
            }
        });


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
            rideList.clear();
            if (passengers == null) {
                // Toast.makeText(JoinRide, "It's not connecting", Toast.LENGTH_SHORT).show();
            } else if (passengers.length() == 0) {
                //   Toast.makeText(JoinRide, "there is no result", Toast.LENGTH_SHORT).show();
            } else {
                convertJSONtoPassenger(passengers);
            }
        }

    }

    /**
     * Converts the JSON ride data to an arraylist
     *
     * @param passengers JSON array of player objects
     */
    private void convertJSONtoPassenger(JSONArray passengers) {
        Log.d(TAG,passengers.toString());
        try {
            for (int i = 0; i < passengers.length(); i++) {
                JSONObject passenger = passengers.getJSONObject(i);
                passengerList.add(new Passenger(
                        passenger.getInt("id"),
                        passenger.optInt("rideId"),
                        passenger.optInt("personId")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG,passengerList.toString());
    }


    private class GetPlayerTask extends AsyncTask<URL, Void, JSONArray> {

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
            rideList.clear();
            if (rides == null) {
                // Toast.makeText(JoinRide, "It's not connecting", Toast.LENGTH_SHORT).show();
            } else if (rides.length() == 0) {
                //   Toast.makeText(JoinRide, "there is no result", Toast.LENGTH_SHORT).show();
            } else {
                convertJSONtoArrayList(rides);
            }
        }

    }

    /**
     * Converts the JSON ride data to an arraylist
     *
     * @param rides JSON array of player objects
     */
    private void convertJSONtoArrayList(JSONArray rides) {
        Log.d(TAG,rides.toString());
        try {
            for (int i = 0; i < rides.length(); i++) {
                JSONObject ride = rides.getJSONObject(i);
                //Check if ride is in the future
                //If it is, show it (by adding it to rideList
                Ride tempRide = new Ride(
                        ride.getInt("rideId"),
                        ride.optInt("driverId"),
                        ride.optString("departure"),
                        ride.optString("destination"),
                        ride.optInt("passengerLimit"),
                        ride.optString("departureDateTime")
                );
                Instant checkDateTime = Instant.parse(tempRide.getDepartureDateTime());
                if (checkDateTime.isAfter(checkDateTime.now())) {
                    rideList.add(tempRide);
                }
//                rideList.add(new Ride(
//                        ride.getInt("rideId"),
//                        ride.optInt("driverId"),
//                        ride.optString("departure"),
//                        ride.optString("destination"),
//                        ride.optInt("passengerLimit"),
//                        ride.optString("departureDateTime")
//                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG,rideList.toString());
    }

    /**
     * Formats a URL for the webservice specified in the string resources.
     *
     * @param id string version of the desired ID (or BLANK for all rides)
     * @return URL formatted for the ride server
     */
    private URL createURL(String id) {
        try {
            String urlString = getString(R.string.web_service_url);
            if (id.equals("")) {
                urlString += "/rides";
            } else if (id.equals("passenger")) {
                urlString += "/passenger";
            }
            else if (id.equals("passengers")) {
                urlString += "/passengers";
            }
            else {
                throw new Exception();
            }
            return new URL(urlString);
        } catch (Exception e) {
            Toast.makeText(this,getString(R.string.connection_error),Toast.LENGTH_SHORT).show();
        }

        return null;
    }


    private class PostPassengerTask extends AsyncTask<URL, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(URL... params) {
            HttpURLConnection connection = null;
            StringBuilder jsonText = new StringBuilder();
            JSONArray result = null;
            try {
                JSONObject jsonData = new JSONObject();
                jsonData.put("rideId", rideId);
                jsonData.put("personId",Globals.getInstance().getValue());
                connection = (HttpURLConnection) params[0].openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type","application/json");
                connection.setFixedLengthStreamingMode(jsonData.toString().length());
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(jsonData.toString());
                out.flush();
                out.close();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonText.append(line);
                    }
                    //Log.d(TAG, jsonText.toString());
                    if (jsonText.charAt(0) == '[') {
                        result = new JSONArray(jsonText.toString());
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
            passengerList.clear();
            if (passengers == null) {
                Toast.makeText(JoinRide.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            } else if (passengers.length() == 0) {
                Toast.makeText(JoinRide.this, getString(R.string.no_results_error), Toast.LENGTH_SHORT).show();
            } else {
                convertJSONtoPassenger(passengers);
//                JoinRide.this.go_to_home();
            }
        }
    }

    //redirect view to HomePage activity
    public void go_to_home() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

}
