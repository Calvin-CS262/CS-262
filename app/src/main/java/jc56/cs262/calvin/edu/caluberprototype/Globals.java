package jc56.cs262.calvin.edu.caluberprototype;

public class Globals {


    private static Globals instance = new Globals();

    // Getter-Setters
    public static Globals getInstance() {
        return instance;
    }

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    private int personId = -1;


    private Globals() {}

    public int getValue() {
        return personId;
    }

    public void setValue(int personId) {
        this.personId = personId;
    }

}