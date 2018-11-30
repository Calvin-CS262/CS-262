package jc56.cs262.calvin.edu.caluberprototype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/** MainActivity class
 * This class sets up the Login Activity
 * Allows access to the SignUp Activity freely, restricts access to the HomePage Activity
 * Should be linked to the database to check the username and password when logging in
 */
public class MainActivity extends AppCompatActivity {

    //For presentation of logIn.
    //ToDo: Use getter and compare instead of forcing
    private String userName = "user1";
    private String password = "pass1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText userNameText = findViewById(R.id.username);
        EditText passwordText = findViewById(R.id.password);

        //call hideKeyboard method when clicking outside of userNameText
        userNameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        //call hideKeyboard method when clicking outside of passwordText
        passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

    }

    //method to hide android keyboard when not in an editText
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Starts the signUp Activity when button is pressed
    public void sign_up_page(View view) {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }

    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    //Login check
    public void go_to_home(View view) {
        final EditText userNameText = findViewById(R.id.username);
        EditText passwordText = findViewById(R.id.password);

        if (userName.equals(userNameText.getText().toString()) &&
                password.equals(passwordText.getText().toString()) ) {
            Intent intent = new Intent(this, HomePage.class);
            toastMsg("Logged In");
            startActivity(intent);
        }
        else
            toastMsg("Incorrect Username and Password");
    }


}
