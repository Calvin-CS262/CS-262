package jc56.cs262.calvin.edu.caluberprototype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;    //Added for connectivity
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;    //Added for connectivity
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;    //Added for connectivity
import org.json.JSONException;    //Added for connectivity
import org.json.JSONObject;    //Added for connectivity

import java.io.BufferedReader;    //Added for connectivity
import java.io.DataOutputStream;    //Added for connectivity
import java.io.InputStreamReader;    //Added for connectivity
import java.net.HttpURLConnection;    //Added for connectivity
import java.net.URL;    //Added for connectivity
import java.text.NumberFormat;    //Added for connectivity
import java.util.ArrayList;    //Added for connectivity
import java.util.HashMap;    //Added for connectivity
import java.util.List;    //Added for connectivity
import android.widget.SimpleAdapter;    //Added for connectivity
import android.widget.ListView;    //Added for connectivity

/** MainActivity class
 * This class sets up the Login Activity
 * Allows access to the SignUp Activity freely, restricts access to the HomePage Activity
 * Should be linked to the database to check the username and password when logging in
 */
public class MainActivity extends AppCompatActivity {

    //For presentation of logIn.
    //ToDo: Use getter and compare instead of forcing
//    private String userName = "user1";
//    private String password = "pass1";
    private EditText userNameText;
    private EditText passwordText;
    private Button loginButton;
    private List<Person> personList = new ArrayList<>();
    private ListView itemsListView;
    private NumberFormat numberFormat = NumberFormat.getInstance();

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        itemsListView = (ListView) findViewById(R.id.personListView);


        //call hideKeyboard method when clicking outside of userNameText
        userNameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        //call hideKeyboard method when clicking outside of passwordText
        passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetPlayerTask().execute(createURL(""));
            }
        });



    }

    //method to hide android keyboard when not in an editText
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Starts the signUp Activity when button is pressed
    public void sign_up_page(View view) {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }

    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    //Login check
    public void go_to_home(View view) {
        final EditText userNameText = findViewById(R.id.username);
        EditText passwordText = findViewById(R.id.password);

        if (userName.equals(userNameText.getText().toString()) &&
                password.equals(passwordText.getText().toString()) ) {
            Intent intent = new Intent(this, HomePage.class);
            toastMsg("Logged In");
            startActivity(intent);
        }
        else
            toastMsg("Incorrect Username and Password");
    }


    //To get the list of players from the database
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
        protected void onPostExecute(JSONArray persons) {
            personList.clear();
            if (persons == null) {
                Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            } else if (persons.length() == 0) {
                Toast.makeText(MainActivity.this, getString(R.string.no_results_error), Toast.LENGTH_SHORT).show();
            } else {
                convertJSONtoArrayList(persons);
            }
            MainActivity.this.updateDisplay();
        }

    }



    /**
     * Formats a URL for the webservice specified in the string resources.
     *
     * @param id string version of the desired ID (or BLANK for all players)
     * @return URL formatted for the course monopoly server
     */
    private URL createURL(String id) {
        try {
            String urlString = getString(R.string.web_service_url);
            if (id.equals("")) {
                urlString += "/persons";
            } else {
                urlString += "/person/" + id;
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
     * @param players JSON array of player objects
     */
    private void convertJSONtoArrayList(JSONArray players) {
        Log.d(TAG, players.toString());
        try {
            for (int i = 0; i < players.length(); i++) {
                JSONObject player = players.getJSONObject(i);
                personList.add(new Person(
                        player.getInt("personId"),
                        player.optString("email", "no email"),
                        player.optString("password", "no password"),
                        player.optString("lastName", "no last name"),
                        player.optString("firstName", "no first name")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, personList.toString());
    }


    /**
     * Refresh the weather data on the forecast ListView through a simple adapter
     */
    private void updateDisplay() {
        if (personList == null) {
            Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (Person item : personList) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("personId", numberFormat.format(item.getPersonId()));
            map.put("email", item.getEmail());
            map.put("password", item.getPassword());
            map.put("lastName", item.getLastName());
            map.put("firstName", item.getFirstName());
            data.add(map);
        }
        int resource = R.layout.player_item;    //TODO: Not sure what R.Layout.player_item should look like
        String[] from = {"personId", "email", "password", "lastName", "firstName"};
        int[] to = {R.id.personIdTextView, R.id.emailTextView, R.id.passwordTextView,
                R.id.lastNameTextView, R.id.firstNameTextView};

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);
    }


}
