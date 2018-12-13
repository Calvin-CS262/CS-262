package jc56.cs262.calvin.edu.caluberprototype;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.widget.EditText;
import android.widget.TextView;
import java.io.DataOutputStream;
import java.util.ArrayList;


/** User Profile Class
 * Creates the user profile page of the app
 */
public class UserProfile extends AppCompatActivity {
    private EditText changeName;
    private EditText changePassword;
    private TextView userEmail;
    private TextView userName;
    private Button changeNameButton;
    private Button changePassButton;

    private static String TAG = "UserProfile";

    private List<Person> personList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changeName = findViewById(R.id.change_name);
        changePassword = findViewById(R.id.change_pass);
        userEmail = findViewById(R.id.editText10);
        userName = findViewById(R.id.editText11);
        changeNameButton = findViewById(R.id.name_button);
        changePassButton = findViewById(R.id.pass_button);


        new GetPlayerTask().execute(createURL(String.valueOf(Globals.getInstance().getValue())));

        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PutPlayerNameTask().execute(createURL(String.valueOf(Globals.getInstance().getValue())));
                // displayToastMsgN(v);

            }
        });

        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PutPlayerPassTask().execute(createURL(String.valueOf(Globals.getInstance().getValue())));
            }
        });
    }


    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();

    }

    public void displayToastMsgP(View v) {
        toastMsg("Password changed");

    }

    public void displayToastMsgN(View w) {

        toastMsg("Name changed");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }



    private String helpMessage =
            "To change your name on CalUber:" +
                    "\n" +
                    "1. Click on the user profile icon located on the top left corner of the home page." +
                    "\n" +
                    "2. Click on the name field on the bottom of the screen." +
                    "\n" +
                    "3. Enter your new name and click on the change name button. \n" +
                    "\n"+

                    "To change your password in CalUber:" +
                    "\n" +
                    "1.  Click on the user profile icon located on the top left corner of the home page." +
                    "\n" +
                    "2.Click on the password field on the bottom of the screen.\n" +
                    "3. Enter your new password and click on the change password button.\n";



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
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


    //To get the list of players from the database
    //on post execute calls updateDisplay
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
                Toast.makeText(UserProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            } else if (persons.length() == 0) {
                Toast.makeText(UserProfile.this, getString(R.string.no_results_error), Toast.LENGTH_SHORT).show();
            } else {
                convertJSONtoArrayList(persons);
            }
            UserProfile.this.updateDisplay();
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

    //if a person's login information corresponds to what is saved in the database,
    //then the HomePage activity is initiated

    //otherwise, a toast stating that there is incorrect login info pops up.
    private void updateDisplay() {
        if (personList == null) {
            Toast.makeText(UserProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
//        Toast.makeText(MainActivity.this, getString(personList.size()), Toast.LENGTH_SHORT).show();
        for (Person item : personList) {
            userEmail.setText(item.getEmail());
            userName.setText(item.getFirstName() + " " + item.getLastName());
        }


    }

    private class PutPlayerNameTask extends AsyncTask<URL, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(URL... params) {
            HttpURLConnection connection = null;
            StringBuilder jsonText = new StringBuilder();
            JSONArray result = null;
            JSONObject jsonData = new JSONObject();
            try {

                //Getting internal data for person
                Person item = personList.get(0);
                jsonData.put("personId", item.getPersonId());
                //making sure not adding another "@students.calvin.edu" to end of email
                String[] emailStringList = item.getEmail().split("@");
                jsonData.put("email", emailStringList[0]);
                jsonData.put("password", item.getPassword());
                //Changing name
                String newName = changeName.getText().toString();
                String[] nameList = newName.split(" ");
                newName = "";
                Log.d(TAG, "before for loop: " + newName);
                for (int i = 1; i < nameList.length; i++) {
                    newName += nameList[i] + " ";
                    Log.d(TAG, "after " + i + "th Last name: " + newName);
                }
                Log.d(TAG, "before substring: " + newName);
                newName = newName.substring(0, newName.length() - 1); //takes off last space
//                Log.d(TAG, "after substring: " + newName);
                jsonData.put("lastName", newName);
                jsonData.put("firstName", nameList[0]);
                //Saving changes internally
                item.setFirstName(nameList[0]);
                item.setLastName(newName);
                personList.set(0, item);

                connection = (HttpURLConnection) params[0].openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
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
        protected void onPostExecute(JSONArray players) {
            personList.clear();
            if (players == null) {
                Toast.makeText(UserProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            } else if (players.length() == 0) {
                Toast.makeText(UserProfile.this, getString(R.string.no_results_error), Toast.LENGTH_SHORT).show();
            } else {
                convertJSONtoArrayList(players);
            }
            UserProfile.this.updateDisplay();
            toastMsg("Name changed"); //toast for successfully changing the name
        }
    }


    private class PutPlayerPassTask extends AsyncTask<URL, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(URL... params) {
            HttpURLConnection connection = null;
            StringBuilder jsonText = new StringBuilder();
            JSONArray result = null;
            JSONObject jsonData = new JSONObject();
            try {

                //Getting internal data for person
                Person item = personList.get(0);
                jsonData.put("personId", item.getPersonId());
                //making sure not adding another "@students.calvin.edu" to end of email
                String[] emailStringList = item.getEmail().split("@");
                jsonData.put("email", emailStringList[0]);
                //Changing password in database
                jsonData.put("password", changePassword.getText().toString());

                jsonData.put("lastName", item.getLastName());
                jsonData.put("firstName", item.getFirstName());
                //Saving changes internally
                item.setPassword(changePassword.getText().toString());
                personList.set(0, item);

                connection = (HttpURLConnection) params[0].openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
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
        protected void onPostExecute(JSONArray players) {
            personList.clear();
            if (players == null) {
                Toast.makeText(UserProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            } else if (players.length() == 0) {
                Toast.makeText(UserProfile.this, getString(R.string.no_results_error), Toast.LENGTH_SHORT).show();
            } else {
                convertJSONtoArrayList(players);
            }
            UserProfile.this.updateDisplay();
            toastMsg("Password changed"); //toast for successfully changing the password

        }
    }
}
