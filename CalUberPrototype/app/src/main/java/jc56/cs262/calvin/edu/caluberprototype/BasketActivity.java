package jc56.cs262.calvin.edu.caluberprototype;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BasketActivity extends AppCompatActivity {

    String[] ridesArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, ridesArray);

        ListView listView = (ListView) findViewById(R.id.upcoming_rides);
        listView.setAdapter(adapter);
    }

}
