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

public class PassengerNetworkUtils {
    private static final String LOG_TAG = PassengerNetworkUtils.class.getSimpleName();
    private static final String PASSENGER_LIST_URL = "https://caluber-221319.appspot.com/caluber/v1/passengers";
    private static final String PASSENGER_ID_URL = "https://caluber-221319.appspot.com/caluber/v1/passenger/";
    private static final String PASSENGER_POST_URL = "https://caluber-221319.appspot.com/caluber/v1/passengers";
    private static final String PASSENGER_DELETE_URL = "https://caluber-221319.appspot.com/caluber/v1/passenger/";
//    private static final String PASSENGER_PUT_URL = "https://caluber-221319.appspot.com/caluber/v1/ride/";


    /**
     * Method POSTs to specified URI
     *
     * @param id
     * @param rideId
     * @param personId
     * @return String indicating status of POST
     */
    static String postPassengerInfo(String id, String rideId, String personId) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            Uri buildURI = Uri.parse(PASSENGER_POST_URL).buildUpon().build();
            //Convert URI to URL
            URL requestURL = new URL(buildURI.toString());
            //Define connection and request
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            //Define the data to send
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("id", id);
            jsonObject.accumulate("rideId", rideId);
            jsonObject.accumulate("personId", personId);
            //Make data output stream
            OutputStream outputStream = urlConnection.getOutputStream();
            //Create writer and make write
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(jsonObject.toString());
            Log.e(PassengerNetworkUtils.class.toString(), jsonObject.toString());
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

//    /**
//     * Method PUTs to specified URI
//     * Put not needed for Passenger Class
//     *
//     * @param id
//     * @param rideId
//     * @param personId
//     * @return String indicating status of PUT
//     */
//    static String putPassengerInfo(String id, String rideId, String personId) {
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//
//        try {
//            Uri buildURI = Uri.parse(PASSENGER_PUT_URL).buildUpon().build();
//            //Convert URI to URL
//            URL requestURL = new URL(buildURI.toString());
//            //Define connection and request
//            urlConnection = (HttpURLConnection) requestURL.openConnection();
//            urlConnection.setRequestMethod("PUT");
//            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
//            //Define the data to send
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.accumulate("rideId", rideId);
//            jsonObject.accumulate("driverId", driverId);
//            jsonObject.accumulate("departure", departure);
//            jsonObject.accumulate("destination", destination);
//            jsonObject.accumulate("passengerLimit", passengerLimit);
//            jsonObject.accumulate("departureDateTime", departureDateTime);
//            //Make data output stream
//            OutputStream outputStream = urlConnection.getOutputStream();
//            //Create writer and make write
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//            writer.write(jsonObject.toString());
//            Log.e(PassengerNetworkUtils.class.toString(), jsonObject.toString());
//            //Close
//            writer.flush();
//            writer.close();
//            outputStream.close();
//            urlConnection.connect();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "PUT failed";
//        } finally {
//            try {
//                return Objects.requireNonNull(urlConnection).getResponseMessage() + "";
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "PUT failed";
//            }
//        }
//    }

    /**
     * Method deletes data entry from Passenger table by id
     *
     * @param id
     * @return deletes the passenger from Passenger table
     */
    public static String deletePassengerInfo(String id) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            Uri builtURI = Uri.parse(PASSENGER_DELETE_URL).buildUpon().appendPath(id).build();
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
     * @return queries Passenger table for all its data
     */
    static String getPassengerListInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String passengerListJSONString = null;
        try{
            Uri builtURI = Uri.parse(PASSENGER_LIST_URL).buildUpon().build();
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
            passengerListJSONString = buffer.toString();
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
            if(passengerListJSONString != null) {
                Log.e(LOG_TAG, passengerListJSONString);
                return passengerListJSONString;
            } else {
                return "";
            }
        }
    }

    /**
     * Method queries specified URI
     *
     * @param queryString
     * @return queries Passenger table according to the argument passed by queryString
     */
    static String getPassengerIdInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String passengerListJSONString = null;
        try {
            Uri builtURI = Uri.parse(PASSENGER_ID_URL).buildUpon().build();
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
            passengerListJSONString = buffer.toString();
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
            if (passengerListJSONString != null) {
                Log.e(LOG_TAG, passengerListJSONString);
                return passengerListJSONString;
            } else {
                return "";
            }
        }
    }
}
