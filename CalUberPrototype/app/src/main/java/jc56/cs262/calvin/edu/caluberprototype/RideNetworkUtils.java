package jc56.cs262.calvin.edu.caluberprototype;

import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class RideNetworkUtils {
    private static final String LOG_TAG = RideNetworkUtils.class.getSimpleName();
    private static final String RIDE_LIST_URL = "https://caluber-221319.appspot.com/caluber/v1/rides";
    private static final String RIDE_ID_URL = "https://caluber-221319.appspot.com/caluber/v1/ride/";
    private static final String RIDE_POST_URL = "https://caluber-221319.appspot.com/caluber/v1/rides";
    private static final String RIDE_DELETE_URL = "https://caluber-221319.appspot.com/caluber/v1/ride/";
    private static final String RIDE_PUT_URL = "https://caluber-221319.appspot.com/caluber/v1/ride/";


    /**
     * Method POSTs to specified URI
     *
     * @param rideId
     * @param driverId
     * @param departure
     * @param destination
     * @param passengerLimit
     * @param departureDateTime
     * @return String indicating status of POST
     */
    static String postRideInfo(String rideId, String driverId, String departure,
                                 String destination, String passengerLimit, String departureDateTime) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            Uri buildURI = Uri.parse(RIDE_POST_URL).buildUpon().build();
            //Convert URI to URL
            URL requestURL = new URL(buildURI.toString());
            //Define connection and request
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            //Define the data to send
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("rideId", rideId);
            jsonObject.accumulate("driverId", driverId);
            jsonObject.accumulate("departure", departure);
            jsonObject.accumulate("destination", destination);
            jsonObject.accumulate("passengerLimit", passengerLimit);
            jsonObject.accumulate("departureDateTime", departureDateTime);
            //Make data output stream
            OutputStream outputStream = urlConnection.getOutputStream();
            //Create writer and make write
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(jsonObject.toString());
            Log.e(RideNetworkUtils.class.toString(), jsonObject.toString());
            //Close
            writer.flush();
            writer.close();
            outputStream.close();
            urlConnection.connect();
        } catch (Exception e) {
            e.printStackTrace();
            return "POST failed";
        } finally {
            try {
                return Objects.requireNonNull(urlConnection).getResponseMessage() + "";
            } catch (Exception e) {
                e.printStackTrace();
                return  "POST failed";
            }
        }
    }

    /**
     * Method PUTs to specified URI
     *
     * @param rideId
     * @param driverId
     * @param departure
     * @param destination
     * @param passengerLimit
     * @param departureDateTime
     * @return String indicating status of PUT
     */
    static String putPersonInfo(String personId, String email, String password,
                                String lastName, String firstName) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            Uri buildURI = Uri.parse(PERSON_PUT_URL).buildUpon().build();
            //Convert URI to URL
            URL requestURL = new URL(buildURI.toString());
            //Define connection and request
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            //Define the data to send
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("personId", personId);
            jsonObject.accumulate("email",email);
            jsonObject.accumulate("password", password);
            jsonObject.accumulate("lastName", lastName);
            jsonObject.accumulate("firstName", firstName);
            //Make data output stream
            OutputStream outputStream = urlConnection.getOutputStream();
            //Create writer and make write
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(jsonObject.toString());
            Log.e(PersonNetworkUtils.class.toString(), jsonObject.toString());
            //Close
            writer.flush();
            writer.close();
            outputStream.close();
            urlConnection.connect();
        } catch (Exception e) {
            e.printStackTrace();
            return "PUT failed";
        } finally {
            try {
                return Objects.requireNonNull(urlConnection).getResponseMessage() + "";
            } catch (Exception e) {
                e.printStackTrace();
                return "PUT failed";
            }
        }
    }

    /**
     * Method deletes data entry from Person table by personId
     *
     * @param personId
     * @return deletes the person from Person table
     */
    public static String deletePersonInfo(String personId) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            Uri builtURI = Uri.parse(PERSON_DELETE_URL).buildUpon().appendPath(personId).build();
            //Convert URI to URL
            URL requestURL = new URL(builtURI.toString());
            //Open connection and make request
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return "Connection failed";
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                return Objects.requireNonNull(urlConnection).getResponseMessage() + "";
            } catch (Exception e) {
                e.printStackTrace();
                return  "DELETE failed";
            }
        }
    }

    /**
     * Method queries specified URI
     *
     * @param queryString
     * @return queries Person table for all its data
     */
    static String getPersonListInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String personListJSONString = null;
        try{
            Uri builtURI = Uri.parse(PERSON_LIST_URL).buildUpon().build();
            //ConvertURI to URL
            URL requestURL = new URL(builtURI.toString());
            // Open connection and make request.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Read the response.
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if(buffer.length() == 0) {
                return null;
            }
            personListJSONString = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Connection failed";
        } finally {
            if(urlConnection != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(personListJSONString != null) {
                Log.e(LOG_TAG, personListJSONString);
                return personListJSONString;
            } else {
                return "";
            }
        }
    }

    /**
     * Method queries specified URI
     *
     * @param queryString
     * @return queries Person table according to the argument passed by queryString
     */
    static String getPersonIdInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String personListJSONString = null;
        try {
            Uri builtURI = Uri.parse(PERSON_ID_URL).buildUpon().build();
            //ConvertURI to URL
            URL requestURL = new URL(builtURI.toString());
            // Open connection and make request.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            //Read response
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            personListJSONString = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Connection failed";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (personListJSONString != null) {
                Log.e(LOG_TAG, personListJSONString);
                return personListJSONString;
            } else {
                return "";
            }
        }
    }
}
