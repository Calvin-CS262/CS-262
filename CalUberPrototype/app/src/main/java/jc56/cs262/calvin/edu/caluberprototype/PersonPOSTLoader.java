package jc56.cs262.calvin.edu.caluberprototype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class PersonPOSTLoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = PersonPOSTLoader.class.getSimpleName();
    private String personId;
    private String email;
    private String password;
    private String lastName;
    private String firstName;

    /**
     * Method called when starting the loader
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }



    public PersonPOSTLoader(@NonNull Context context, String personId, String email,
                            String password, String lastName, String firstName) {
        super(context);
        this.personId = personId;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    /**
     * Method performs the task in the background
     *
     * @return
     */
    @Nullable
    @Override
    public String loadInBackground() {
        Log.e(LOG_TAG, "postPersonInfo called");
        return PersonNetworkUtils.postPersonInfo(personId, email, password, lastName, firstName);
    }
}
