package com.tanghe.garben.capitalbooze;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gebruiker on 17/08/2016.
 */
class Create_Part extends AsyncTask {

    protected static Context context;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected Object doInBackground(Object[] objects) {
        List params = new ArrayList<>();
        params.add(new BasicNameValuePair("Name", "Stella")); //Add the parameters to an array
        params.add(new BasicNameValuePair("part_nr", "12"));

        // Do the HTTP POST Request with the JSON parameters
        // Change "RaspberryPi_IP to your home IP address or Noip service
        JSONObject json = jsonParser.makeHttpRequest(context.getString(R.string.url), "POST", params);
        try {
            int success = json.getInt("success");
            if (success == 1) {
                Log.d("debug","everything worked!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Sending part to the database..."); //Set the message for the loading window
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show(); //Place the loading message on the screen
    }

    protected void onPostExecute(String file_url) {
        pDialog.dismiss(); // Close the loading window when ready
    }

    public static void setArgument(Context context) {
        Create_Part.context = context;
    }
}
