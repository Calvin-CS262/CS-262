package jc56.cs262.calvin.edu.caluberprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {
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