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
    static String putRideInfo(String rideId, String driverId, String departure,
                              String destination, String passengerLimit, String departureDateTime) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            Uri buildURI = Uri.parse(RIDE_PUT_URL).buildUpon().build();
            //Convert URI to URL
            URL requestURL = new URL(buildURI.toString());
            //Define connection and request
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("PUT");
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
     * Method deletes data entry from Ride table by rideId
     *
     * @param rideId
     * @return deletes the ride from Ride table
     */
    public static String deleteRideInfo(String rideId) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            Uri builtURI = Uri.parse(RIDE_DELETE_URL).buildUpon().appendPath(rideId).build();
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
     * @return queries Ride table for all its data
     */
    static String getRideListInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String rideListJSONString = null;
        try{
            Uri builtURI = Uri.parse(RIDE_LIST_URL).buildUpon().build();
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
            rideListJSONString = buffer.toString();
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
            if(rideListJSONString != null) {
                Log.e(LOG_TAG, rideListJSONString);
                return rideListJSONString;
            } else {
                return "";
            }
        }
    }

    /**
     * Method queries specified URI
     *
     * @param queryString
     * @return queries Ride table according to the argument passed by queryString
     */
    static String getRideIdInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String rideListJSONString = null;
        try {
            Uri builtURI = Uri.parse(RIDE_ID_URL).buildUpon().build();
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
            rideListJSONString = buffer.toString();
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
            if (rideListJSONString != null) {
                Log.e(LOG_TAG, rideListJSONString);
                return rideListJSONString;
            } else {
                return "";
            }
        }
    }
}
