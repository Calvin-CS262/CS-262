package jc56.cs262.calvin.edu.caluberprototype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class PersonDELETELoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = PersonDELETELoader.class.getSimpleName();
    private String personId;

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public PersonDELETELoader(@NonNull Context context, String personId) {
        super(context);
        this.personId = personId;
    }

    /**
     * Method performs the task in the background
     *
     * @return
     */
    @Nullable
    @Override
    public String loadInBackground() {
        Log.e(LOG_TAG, "deletePersonInfo called");
        return PersonNetworkUtils.deletePersonInfo(personId);
    }
}
