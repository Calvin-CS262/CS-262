package jc56.cs262.calvin.edu.caluberprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void sign_up_page(View view) {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }

    public void go_to_home(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}