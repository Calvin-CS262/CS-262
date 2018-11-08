package jc56.cs262.calvin.edu.caluberprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //For presentation of logIn.
    //ToDo: Use getter and compare instead of forcing
    private String userName = "user1";
    private String password = "pass1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void sign_up_page(View view) {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }

    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    public void go_to_home(View view) {
        EditText userNameText = findViewById(R.id.username);
        EditText passwordText = findViewById(R.id.password);

        if (userName.equals(userNameText.getText().toString()) &&
                password.equals(passwordText.getText().toString()) ) {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        }
        else
            toastMsg("Incorrect Username and Password");
    }


}
