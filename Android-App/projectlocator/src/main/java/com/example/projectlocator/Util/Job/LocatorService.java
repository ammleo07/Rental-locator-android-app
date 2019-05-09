package com.example.projectlocator.Util.Job;

/**
 * Created by alber on 29/11/2018.
 */
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectlocator.LoginActivity;
import com.example.projectlocator.R;
import com.example.projectlocator.UserActivity;
import com.example.projectlocator.Util.Realm.RealmController;
import com.example.projectlocator.Util.Retrofit.RetrofitService;

import com.example.projectlocator.Util.Retrofit.ApiUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import Model.UserLocation;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocatorService extends JobService {

    JobParameters params;
    DoItTask doIt;
    TextView txtLocation;
    private RetrofitService mService;
    @Override
    public boolean onStartJob(JobParameters params) {
        this.params = params;
        //Log.d("TestService", "Work to be called from here");
        Toast.makeText(getApplicationContext(), "Locator Service starts", Toast.LENGTH_LONG).show();
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

    UserLocation getCurrentUserLocation() throws ParserConfigurationException, SAXException, IOException
    {
        UserLocation userLocation= null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(getFilesDir().getAbsolutePath() + "/location.xml"));
        document.getDocumentElement().normalize();
        NodeList nList = document.getElementsByTagName("location");
        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                //Create new Employee Object
                userLocation = new UserLocation();
                userLocation.setUsername(eElement.getElementsByTagName("username").item(0).getTextContent());
                userLocation.setLatitude(eElement.getElementsByTagName("latitude").item(0).getTextContent());
                userLocation.setLongitude(eElement.getElementsByTagName("longitude").item(0).getTextContent());
                userLocation.setDateCaptured(eElement.getElementsByTagName("dateCaptured").item(0).getTextContent());
                //userLocation.setLatitude(eElement.getElementsByTagName("latitude").item(0).getTextContent());
            }
        }


        return userLocation;
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
            try {
                UserLocation userLocation = getCurrentUserLocation();
                mService = ApiUtils.getSOService();
                mService.forwardLocations(userLocation).enqueue(new Callback<String>() {

                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if (response.isSuccessful()) {
                            Log.d("Locator Service", "success");
                        } else {
                            int statusCode = response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("RenteePortalActivity", "error loading from API:" + t.getMessage());
                        Toast.makeText(getApplicationContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(), "error executing Locator Service ", Toast.LENGTH_LONG).show();
            }
        return null;
        }
    }

}
