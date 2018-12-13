package jc56.cs262.calvin.edu.caluberprototype;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

//import com.google.api.server.spi.config.*;
//import static com.google.api.server.spi.config.ApiMethod.HttpMethod.POST;

/** CreateRide Class
 * Sets up the Create Ride activity
 * Lets users create a ride
 * Should update the database when finished
 */
public class CreateRide extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    //TODO: implement google maps api to choose locations.
    //this will help streamline the search parameters for
    //finding a ride
    private List<Ride> rideList = new ArrayList<>();


    String AmPm;
    private static String TAG = "CreateRide";
    private TextView DateText;
    private EditText TimeText;
    private EditText StartText;
    private EditText EndText;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);

        //for whatever reason the page crashes if you define view names outside of
        //methods so we have to do it here and in submitRideMsg
        spinner = findViewById(R.id.number);

        DateText = findViewById(R.id.date_text);
        TimeText = findViewById(R.id.time);
        StartText = findViewById(R.id.startPoint);
        EndText = findViewById(R.id.destination);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        //call hideKeyboard method when clicking outside of EditText boxes
        TimeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        StartText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        EndText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    //method to hide android keyboard when not in an editText
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //spinner to select date of departure
    public void datePicker(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), getString(R.string.datePicker));
    }

    //toast message
    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    //calling toast message after driver clicks "create a ride" button
    //if all fields are filled out, create ride and return to home page
    //otherwise, display toast asking for complete info.
    public void submitRideMsg(View view) {

        Spinner spinner = findViewById(R.id.number);

        TextView dateText = findViewById(R.id.date_text);
        EditText TimeText = findViewById(R.id.time);
        EditText StartText = findViewById(R.id.startPoint);
        EditText EndText = findViewById(R.id.destination);
        //create write only if all fields are field
        if (dateText.getText().length() > 0 && TimeText.getText().length() > 0 && StartText.getText().length() > 0 &&
                EndText.getText().length()> 0) {
            new CreateRide.PostRideTask().execute(createURL(""));
            toastMsg(getString(R.string.CreateRideMessage));
            //return home
            //go_to_home();

        } else { // else toast a message to fill out the form
            toastMsg(getString(R.string.CreateARideFailedMsg));
        }
    }

    //To get the list of players from the database
    //on post execute calls updateDisplay
    private class PostRideTask extends AsyncTask<URL, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(URL... params) {
            HttpURLConnection connection = null;
            StringBuilder jsonText = new StringBuilder();
            JSONArray result = null;
            try {
                // Hard-code a new player using JSON.
                JSONObject jsonData = new JSONObject();
                jsonData.put("driverId", Globals.getInstance().getValue());
                jsonData.put("departure", StartText.getText());
                jsonData.put("destination", EndText.getText());
                jsonData.put("passengerLimit", spinner.getSelectedItem().toString());
                //get the date and time formatted correctly
                String[] date = DateText.getText().toString().split(" ");
                String dateTime;
                //formatting date
                dateTime = date[2] + "-";   //year-
                switch (date[0]) {          //month-
                    case "Jan" :
                        dateTime += "01-";
                        break;
                    case "Feb" :
                        dateTime += "02-";
                        break;
                    case "Mar" :
                        dateTime += "03-";
                        break;
                    case "Apr" :
                        dateTime += "04-";
                        break;
                    case "May" :
                        dateTime += "05-";
                        break;
                    case "Jun" :
                        dateTime += "06-";
                        break;
                    case "Jul" :
                        dateTime += "07-";
                        break;
                    case "Aug" :
                        dateTime += "08-";
                        break;
                    case "Sep" :
                        dateTime += "09-";
                        break;
                    case "Oct" :
                        dateTime += "10-";
                        break;
                    case "Nov" :
                        dateTime += "11-";
                        break;
                    case "Dec" :
                        dateTime += "12-";
                        break;
                }
                if (date[1].length() == 2) {
                    dateTime += "0";
                }
                dateTime += date[1].substring(0, date[1].length() - 1); //day
                dateTime += "T";
                //formatting time
//                Log.d(TAG, "'" + TimeText.getText().toString() + "'");
                dateTime += TimeText.getText().toString().substring(0, 5) + ":00Z";

                jsonData.put("departureDateTime", dateTime);
                // Open the connection as usual.
                connection = (HttpURLConnection) params[0].openConnection();
                // Configure the connection for a POST, including outputting streamed JSON data.
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type","application/json");
                connection.setFixedLengthStreamingMode(jsonData.toString().length());
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(jsonData.toString());
                out.flush();
                out.close();
                // Handle the response from the (Lab09) server as usual.
//                Log.d(TAG, "dateTime string is: " + dateTime);
//                Log.d(TAG, "Connection response code: " + getString(connection.getResponseCode()));
//                Log.d(TAG, "HttpUrlConnection ok: " + getString(HttpURLConnection.HTTP_OK));
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonText.append(line);
                    }
                    Log.d(TAG, jsonText.toString());
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
        protected void onPostExecute(JSONArray rides) {
            rideList.clear();
            if (rides == null) {
                Toast.makeText(CreateRide.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            } else if (rides.length() == 0) {
                Toast.makeText(CreateRide.this, getString(R.string.no_results_error), Toast.LENGTH_SHORT).show();
            } else {
                convertJSONtoArrayList(rides);
                CreateRide.this.go_to_home();
            }
        }

    }

    //redirect view to HomePage activity
    public void go_to_home() {
        Intent intent = new Intent(this, HomePage.class);
        toastMsg("Logged In");
        startActivity(intent);
    }

    //spinner for selecting date of departure
    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

        ((TextView) findViewById(R.id.date_text))
                .setText(dateFormat.format(calendar.getTime()));

    }

    //set date to value that was selected via calendar widget
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //data selection widget
    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);
        }

    }
    //Pick time. Code is found at https://www.codingdemos.com/android-timepicker-edittext/
    public void timePicker(View view ){
        Calendar calendar;
        int currentHour;
        int currentMinute;
        TimePickerDialog timePickerDialog;
        final EditText chooseTime = findViewById(R.id.time);
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(CreateRide.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet (TimePicker timePicker,int hourOfDay, int minutes) {
                if (hourOfDay >= 12 ){
                    AmPm =getString(R.string.PM);
                } else {
                    AmPm =getString(R.string.AM);
                }
                chooseTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + AmPm );
            }
        }, currentHour, currentMinute, false);
        timePickerDialog.show();
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
                urlString += "/ride";
            } else {
                throw new Exception();
            }
            return new URL(urlString);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    /**
     * Converts the JSON player data to an arraylist suitable for a listview adapter
     *
     */
//    private void convertArrayListtoJSON() {
//        JSONObject rides = new JSONObject();
//        Log.d(TAG, "convertArrayListtoJSON error");
//        try {
//            rides.put("driverId", Globals.getInstance().getValue());
//            rides.put("departure", StartText.getText());
//            rides.put("destination", EndText.getText());
//            rides.put("passengerLimit", spinner.getSelectedItem().toString());
//            String dateTime = DateText.getText().toString()+TimeText.getText().toString();
//            rides.put("departureDateTime", dateTime);
//
//            } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//
//        Log.d(TAG, "something");
//    }

    /**
     * Converts the JSON player data to an arraylist suitable for a listview adapter
     *
     * @param rides JSON array of player objects
     */
    private void convertJSONtoArrayList(JSONArray rides) {
        Log.d(TAG, rides.toString());
        try {
            for (int i = 0; i < rides.length(); i++) {
                JSONObject ride = rides.getJSONObject(i);
                rideList.add(new Ride(
                        ride.getInt("id"),
                        ride.optInt("driverId", Globals.getInstance().getValue()),
                        ride.optString("departure", "no departure"),
                        ride.optString("destination", "no destination"),
                        ride.optInt("passengerLimit", 0),
                        ride.optString("departureDateTime", "no departure date")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, rideList.toString());
    }

}
