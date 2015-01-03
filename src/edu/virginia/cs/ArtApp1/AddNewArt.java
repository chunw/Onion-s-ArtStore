package edu.virginia.cs.ArtApp1;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.virginia.cs.ArtApp.R;

public class AddNewArt extends Activity {

	private Button buttonAddArt;

	private Button buttonUploadImage;

	private EditText editAddTitle;

	private EditText editAddPrice;

	private EditText editAddDesc;

	private EditText editAddName;

	private EditText editAddCategory;

	private static final int SELECT_PICTURE = 1; 

	private String selectedImagePath;

	private TextView imagePathView;
	
	private String addWebserviceURL = "http://onionartapp.azurewebsites.net/add?";
	private String imageHandlerURL = "http://plato.cs.virginia.edu/~cs4720s14onion/final/image.php";
	private String username;
	private String title_str;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_art);
		addListenerOnButton();
		Globals g = (Globals) getApplication();
		username = g.getUser();
		editAddName.setText(username);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_course_list, menu);
		return true;
	}

	public void initView() {
		// Adjust the URL with the appropriate parameters
		title_str = editAddTitle.getText().toString();
		new AddArtTask().execute();
		if (selectedImagePath!="") {
			new UploadImageTask().execute();
		}
		
		Context context = getApplicationContext();
		//TODO if both server responses are ok
		CharSequence text = "Art Successfully Added!";
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}


	// The definition of our add new art task class
	private class AddArtTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			try {
				String addUrl = addWebserviceURL + "title="+ title_str +
						"&seller=" + editAddName.getText().toString() +
						"&description=" + editAddDesc.getText().toString() +
						"&category=" + editAddCategory.getText().toString() +
						"&price=" + editAddPrice.getText().toString();
				
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(addUrl);
				HttpResponse response = httpclient.execute(httppost);
				//HttpEntity entity = response.getEntity();
			} catch (Exception e) {
				Log.e("Add New Art", ":" + e.toString());
			}
			return "Done in add new art task!";
		}
	}

	// This task uploads image to server
	private class UploadImageTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {

			try {
				//store the image as a key value pair
				System.out.println("Executing image uploading task.");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
	            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to PNG format
	            byte [] byte_arr = stream.toByteArray();
	            String image_str = Base64.encodeToString(byte_arr, 0);
	            ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
	            nameValuePairs.add(new BasicNameValuePair("image",image_str));
	            nameValuePairs.add(new BasicNameValuePair("title", title_str));
	            nameValuePairs.add(new BasicNameValuePair("seller", username));
	            
	            //make POST request to php server
	            HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(imageHandlerURL);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                //String the_string_response = convertResponseToString(response);
                System.out.println("Uploading response: " + response);
				
			} catch (Exception e) {
				Log.e("Upload image error", ":" + e.toString());
			}
			return "Done in image uploading task!";
		}

	}

	public void addListenerOnButton() {

		buttonAddArt = (Button) findViewById(R.id.addArtBtn);
		buttonUploadImage = (Button) findViewById(R.id.addArtUploadImage);
		editAddTitle = (EditText) findViewById(R.id.editAddTitle);

		editAddPrice = (EditText) findViewById(R.id.editAddPrice);

		editAddDesc = (EditText) findViewById(R.id.editAddDesc);

		editAddName = (EditText) findViewById(R.id.editAddName);
		editAddCategory = (EditText) findViewById(R.id.editAddCategory);
		buttonUploadImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openGallery(SELECT_PICTURE);
			}
		});
		buttonAddArt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initView();
			}

		});

	}

	public void openGallery(int req_code) {

		Intent intent = new Intent();

		intent.setType("image/*");

		intent.setAction(Intent.ACTION_GET_CONTENT);

		startActivityForResult(
				Intent.createChooser(intent, "Select file to upload "),
				req_code);

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {

			Uri selectedImageUri = data.getData();

			if (requestCode == SELECT_PICTURE) {

				selectedImageUri = data.getData();

				selectedImagePath = getPath(selectedImageUri);

				System.out.println("Image Path : " + selectedImagePath);

				imagePathView = (TextView) findViewById(R.id.imagePathView);

				imagePathView.setText(selectedImagePath);

			}

		}

	}

	public String getPath(Uri uri) {

		String[] projection = { MediaStore.Images.Media.DATA };

		Cursor cursor = managedQuery(uri, projection, null, null, null);

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		return cursor.getString(column_index);

	}

	@Override
	public void onBackPressed()

	{

		finish();

	}

}