package jc56.cs262.calvin.edu.caluberprototype;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.view.inputmethod.InputMethodManager;
import android.widget.TimePicker;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/** CreateRide Class
 * Sets up the Create Ride activity
 * Lets users create a ride
 * Should update the database when finished
 */
public class CreateRide extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    //TODO: implement google maps api to choose locations.
    //this will help streamline the search parameters for
    //finding a ride
    //TODO: only allow users to click "create a ride" if all fields are filled in
    String AmPm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);

        //for whatever reason the page crashes if you define view names outside of
        //methods so we have to do it here and in submitRideMsg
        Spinner spinner = findViewById(R.id.number);

        TextView dateText = findViewById(R.id.date_text);
        EditText TimeText = findViewById(R.id.time);
        EditText StartText = findViewById(R.id.startPoint);
        EditText EndText = findViewById(R.id.destination);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        //call hideKeyboard method when clicking outside of EditText boxes
        TimeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        StartText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        EndText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //spinner to select date of departure
    public void datePicker(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), getString(R.string.datePicker));
    }

    //toast message
    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    //calling toast message after driver clicks "create a ride" button
    //set all entry fields back to empty
    public void submitRideMsg(View view) {
        toastMsg("Your ride is created! ");

        Spinner spinner = findViewById(R.id.number);

        TextView dateText = findViewById(R.id.date_text);
        EditText TimeText = findViewById(R.id.time);
        EditText StartText = findViewById(R.id.startPoint);
        EditText EndText = findViewById(R.id.destination);

        //set everything back to default
        dateText.setText("");
        TimeText.setText("");
        StartText.setText("");
        EndText.setText("");
        spinner.setSelection(0);

    }

    //spinner for selecting date of departure
    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

        ((TextView) findViewById(R.id.date_text))
                .setText(dateFormat.format(calendar.getTime()));

    }

    //set date to value that was selected via calendar widget
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);
        }

    }
    //Pick time. Code is found at https://www.codingdemos.com/android-timepicker-edittext/
    public void timePicker(View view ){
        Calendar calendar;
        int currentHour;
        int currentMinute;
        TimePickerDialog timePickerDialog;
        final EditText chooseTime = findViewById(R.id.time);
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(CreateRide.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet (TimePicker timePicker,int hourOfDay, int minutes) {
                if (hourOfDay >= 12 ){
                    AmPm ="PM";
                } else {
                    AmPm ="AM";
                }
                chooseTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + AmPm );
            }
        }, currentHour, currentMinute, false);
        timePickerDialog.show();
    }
}
