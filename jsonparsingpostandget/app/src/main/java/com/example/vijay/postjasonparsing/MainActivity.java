package com.example.vijay.postjasonparsing;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView response;
    ListView ls;
    final ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        response = (TextView) findViewById(R.id.txt);
        ls = (ListView) findViewById(R.id.ls);
        String category = "health";
        new PostAsync().execute(category);
    }
    class PostAsync extends AsyncTask<String, String, JSONObject> {

        JASONparser jsonParser = new JASONparser();

        private ProgressDialog pDialog;

        private static final String LOGIN_URL = "http://newtechgadget.in/postjsonparsingroup.php";

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("category", args[0]);

                Log.d("request", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());

                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject result) {
            ArrayList<String> listItems = new ArrayList<String>();

            try {
                JSONArray jsonArray = result.getJSONArray("result");
                for (int i = 0; i< jsonArray.length(); i++) {
                    JSONObject ob = jsonArray.getJSONObject(i);
                    listItems.add(ob.getString("fname"));
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_list_item_1, listItems);
                    ls.setAdapter(adapter);
                    pDialog.hide();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        }
        }
