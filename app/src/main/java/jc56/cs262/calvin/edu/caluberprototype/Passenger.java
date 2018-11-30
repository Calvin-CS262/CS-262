package jc56.cs262.calvin.edu.caluberprototype;

public class Passenger {
	private int id;
	private int rideId;
	private int personId;


	public Passenger(){
		// The JSON marshaller used by Endpoints requires this default constructor.
	}

	public Passenger(int id, int rideId, int personId) {
		this.id = id;
		this.rideId = rideId;
		this.personId = personId;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPersonId() {
		return this.personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public int getRideId() {
		return this.rideId;
	}

	public void setRideId(int rideId) {
		this.rideId = rideId;
	}
}