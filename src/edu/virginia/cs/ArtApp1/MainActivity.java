package edu.virginia.cs.ArtApp1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.Intent;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import edu.virginia.cs.ArtApp.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

  private Spinner spinnerFilter;
  private Button buttonFilter;
  private EditText editValue;
  private String filterString = "cost";
  private String filterValue = "all";
  public artItemAdapter adapter;

  ListView courseList;
  String webserviceURL = "http://plato.cs.virginia.edu/~cs4720s14onion/final/module11/";
  ArrayList<artItem> values;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d("onCreate", "start up");
    setContentView(R.layout.activity_art_list);
    addListenerOnButton();
    initView();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_course_list, menu);
    return true;
  }

  public void initView() {
	  Globals g = (Globals)getApplication();
	  String username = g.getUser();
	  TextView welcomeText = (TextView)findViewById(R.id.welcomeView);
	  welcomeText.setText("Hello, " + username + "!");
	  
    courseList = (ListView) findViewById(R.id.courseList);
    values = new ArrayList<artItem>();

    String url;
    // Adjust the URL with the appropriate parameters
    if (filterString.equals("All")) {
    	url = webserviceURL + "cost/all";
    } else {
    	url = webserviceURL + filterString + "/" + filterValue;
    }

    // First paramenter - Context
    // Second parameter - Layout for the row
    // Third parameter - ID of the TextView to which the data is written
    // Forth - the Array of data
    //Log.d("HTTP", url);
    adapter = new artItemAdapter(this,
        R.layout.listview_item_row, values);

    // Assign adapter to ListView
    courseList.setAdapter(adapter);

    new GetCoursesTask().execute(url);

  }

  public static String getJSONfromURL(String url) {

    // initialize
    InputStream is = null;
    String result = "";

    // http post
    try {
      HttpClient httpclient = new DefaultHttpClient();
      HttpPost httppost = new HttpPost(url);
      HttpResponse response = httpclient.execute(httppost);
      HttpEntity entity = response.getEntity();
      is = entity.getContent();

    } catch (Exception e) {
      Log.e("ArtApp1", "Error in http connection " + e.toString());
    }

    // convert response to string
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(
          is, "iso-8859-1"), 8);
      StringBuilder sb = new StringBuilder();
      String line = null;
      while ((line = reader.readLine()) != null) {
        sb.append(line + "\n");
      }
      is.close();
      result = sb.toString();
    } catch (Exception e) {
      Log.e("ArtApp2", "Error converting result " + e.toString());
    }

    return result;
  }

  // The definition of our task class
  private class GetCoursesTask extends AsyncTask<String, Integer, String> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {
      String url = params[0];
      ArrayList<artItem> lcs = new ArrayList<artItem>();

      try {

        String webJSON = getJSONfromURL(url);
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray Jarray = parser.parse(webJSON).getAsJsonArray();

        for (JsonElement obj : Jarray) {
          artItem cse = gson.fromJson(obj, artItem.class);
          lcs.add(cse);
        }

      } catch (Exception e) {
        Log.e("ArtApp3", "JSONPARSE:" + e.toString());
      }

      values.clear();
      values.addAll(lcs);

      return "Done!";
    }

    @Override
    protected void onProgressUpdate(Integer... ints) {

    }

    @Override
    protected void onPostExecute(String result) {
      // tells the adapter that the underlying data has changed and it
      // needs to update the view
      adapter.notifyDataSetChanged();
    }
  }

  public void addListenerOnButton() {

    spinnerFilter = (Spinner) findViewById(R.id.spinnerFilter);
    editValue = (EditText)findViewById(R.id.editValue);
    buttonFilter = (Button) findViewById(R.id.buttonFilter);
    buttonFilter.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        filterString = String.valueOf(spinnerFilter.getSelectedItem());
        filterValue = editValue.getText().toString();
        initView();
      }
    });
  }

  /** Called when the user clicks the Filter button */
  public void filterArts(View view) {
      filterString = String.valueOf(spinnerFilter.getSelectedItem());
      filterValue = editValue.getText().toString();
      initView();
  }

  /** Called when the user clicks the Add Art button */
  public void addNewArt(View view) {
      Intent intent = new Intent(this, AddNewArt.class);
      startActivity(intent);
  }
}
