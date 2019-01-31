package jc56.cs262.calvin.edu.caluberprototype;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    private EditText changeName;
    private EditText changePassword;
    private TextView userEmail;
    private TextView userName;
    private Button changeNameButton;
    private Button changePassButton;

    private static String TAG = "ProfileFragment";

    private List<Person> personList = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);*/
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changeName = getView().findViewById(R.id.change_name);
        changePassword = getView().findViewById(R.id.change_pass);
        userEmail = getView().findViewById(R.id.editText10);
        userName = getView().findViewById(R.id.editText11);
        changeNameButton = getView().findViewById(R.id.name_button);
        changePassButton = getView().findViewById(R.id.pass_button);


        try {
            new ProfileFragment.GetPlayerTask().execute(createURL(String.valueOf(Globals.getInstance().getValue())));
        } catch (Exception e) {
            Log.d(TAG, e.toString() + " Database offline");
        }


        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProfileFragment.PutPlayerNameTask().execute(createURL(String.valueOf(Globals.getInstance().getValue())));
                // displayToastMsgN(v);

            }
        });

        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProfileFragment.PutPlayerPassTask().execute(createURL(String.valueOf(Globals.getInstance().getValue())));
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
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
        protected void onPostExecute(JSONArray persons) {
            personList.clear();
            if (persons == null) {
                Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            } else if (persons.length() == 0) {
                Toast.makeText(getActivity(), getString(R.string.no_results_error), Toast.LENGTH_SHORT).show();
            } else {
                convertJSONtoArrayList(persons);
            }
            ProfileFragment.this.updateDisplay();
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
            Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            } else if (players.length() == 0) {
                Toast.makeText(getActivity(), getString(R.string.no_results_error), Toast.LENGTH_SHORT).show();
            } else {
                convertJSONtoArrayList(players);
            }
            ProfileFragment.this.updateDisplay();
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
                Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            } else if (players.length() == 0) {
                Toast.makeText(getActivity(), getString(R.string.no_results_error), Toast.LENGTH_SHORT).show();
            } else {
                convertJSONtoArrayList(players);
            }
            ProfileFragment.this.updateDisplay();
            toastMsg("Password changed"); //toast for successfully changing the password

        }
    }
    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
        toast.show();

    }

}
