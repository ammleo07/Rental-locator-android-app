package com.example.projectlocator.Util.Job;

/**
 * Created by alber on 29/11/2018.
 */
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class TestService extends JobService {

    JobParameters params;
    DoItTask doIt;
    TextView txtLocation;
    @Override
    public boolean onStartJob(JobParameters params) {
        this.params = params;
        //Log.d("TestService", "Work to be called from here");
        Toast.makeText(getApplicationContext(), "Test Service starts", Toast.LENGTH_LONG).show();
        doIt = new DoItTask();
        doIt.execute();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("TestService", "System calling to stop the job here");
        if (doIt != null)
            doIt.cancel(true);
        return false;
    }

    private class DoItTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("DoItTask", "Clean up the task here and call jobFinished...");
            //Toast.makeText(getApplicationContext(), "Test Service finished task", Toast.LENGTH_LONG).show();
            jobFinished(params, false);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("DoItTask", "Working here...");
            //txtLocation = (TextView) findViewById(R.id.spinner);
            //Toast.makeText(getApplicationContext(), "executing Test Service ", Toast.LENGTH_LONG).show();
            return null;
        }
    }

}
