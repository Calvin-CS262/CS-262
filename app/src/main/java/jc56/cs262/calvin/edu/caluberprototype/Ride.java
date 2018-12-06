package jc56.cs262.calvin.edu.caluberprototype;
import java.time.Instant;


/** Ride Class
 * Sets up class for the Ride Table
 * A Ride includes a rideID, a driverId (a Person), departure time, destination, passenger limit, date, time and status
 */
public class Ride {

    private int rideId; //Primary Key
    private int driverId; //Foreign Key
    private String departure;
    private String destination;
    private int passengerLimit;
    private Instant departureDateTime;
    // private boolean status;

    public Ride() {
        // The JSON marshaller used by Endpoints requires this default constructor.
    }
    // Constructor
    public Ride(int rideId, int driverId, String  departure, String destination,
                int passengerLimit, String dateTime) {  //String status
        this.rideId = rideId;   
        this.driverId = driverId;
        this.departure = departure;
        this.destination = destination;
        this.passengerLimit = passengerLimit;
        this.departureDateTime = Instant.parse(dateTime);
        // this.status = Boolean.parseBoolean(status);
        // this.status = this.departureDateTime.isAfter(Instant.now());  //true if ride has passed, false if ride is upcoming

    }

    public int getRideId() { return rideId; }

    public void setRideId(int rideId) { this.rideId = rideId; }

    public int getDriverId() { return driverId; }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() { return destination; }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getPassengerLimit() {
        return passengerLimit;
    }

    public void setPassengerLimit(int passengerLimit) {
        this.passengerLimit = passengerLimit;
    }

    public String getDepartureDateTime() { return departureDateTime.toString(); }

    public void setDepartureDateTime(String dateTime) { this.departureDateTime = Instant.parse(dateTime); }

    // public String getStatus() {
    //     status = departureDateTime.isAfter(Instant.now());  //true if ride has passed, false if ride is upcoming
    //     return Boolean.toString(status);
    // }

    // public void setStatus(String status) {
    //     this.status = departureDateTime.isAfter(Instant.now());  //true if ride has passed, false if ride is upcoming
    // }
    
}
