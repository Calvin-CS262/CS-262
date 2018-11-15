package jc56.cs262.calvin.edu.caluberprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/** SignUp Class
 * Sets up the sign up Activity
 * Lets new users sign up for an account with CalUber
 * Should end up updating the Database with new login info
 */
public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void return_to_login(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
