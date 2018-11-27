package jc56.cs262.calvin.edu.caluberprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/** HomePage Class
 * Sets up the home page activity
 * The home page connects to all other activities except the sign up activity
 */
public class HomePage extends AppCompatActivity {
    private static final String LOG_TAG = HomePage.class.getSimpleName();
    private Person person;

    //TODO: connect to database

    //home page after login.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

    }

    public void launchJoinRide(View view) {
        Intent intent = new Intent(this, JoinRide.class);
        startActivity(intent);
    }


    public void launchUserProfile(View view) {
        Intent intent = new Intent(this,UserProfile.class);
        startActivity(intent);

    }

    public void launchCreateRide(View view) {
        Intent intent = new Intent(this,CreateRide.class);
        startActivity(intent);
    }

    public void launchBasket(View view) {
        Intent intent = new Intent(this,BasketActivity.class);
        startActivity(intent);
    }
    public void login_function(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}