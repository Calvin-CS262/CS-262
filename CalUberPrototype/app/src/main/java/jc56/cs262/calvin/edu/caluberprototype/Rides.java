package jc56.cs262.calvin.edu.caluberprototype;

import java.sql.Time;
import java.util.Date;

/** Rides Class
 * Sets up class for the Rides Table
 * A Ride includes a rideID, a driver (a User), departure time, destination, passenger limit, date, time and status
 */
public class Rides {

    int rideId; //Primary Key
    String driver; //Foreign Key
    String departure;
    String destination;
    int passengerLimit;
    Date date;
    Time time;
    boolean status;


    // Constructor
    public Rides(int rideId, String driver, String  departure,
                 String destination, int passengerLimit, Date date, Time time, boolean status) {
        this.rideId = rideId;
        this.driver = driver;
        this.departure = departure;
        this.destination = destination;
        this.passengerLimit = passengerLimit;
        this.date = date;
        this.time = time;
        this.status = status;   // Status determines if a ride is upcoming or has already passed based on the departure time
    }

    public int getRideId() { return rideId; }

    public void setRideId(int rideId) { this.rideId = rideId; }

    public String getDriver() { return driver; }

    public void setDriver(String driver) {
        this.driver = driver;
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

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public Time getTime() { return time; }

    public void setTime(Time time) { this.time = time; }

    public boolean isStatus() { return status; }

    public void setStatus(boolean status) { this.status = status; }
    
}
