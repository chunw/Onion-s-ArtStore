package edu.virginia.cs.ArtApp1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import edu.virginia.cs.ArtApp.R;
import edu.virginia.cs.ArtApp.R.id;
import edu.virginia.cs.ArtApp.R.layout;
import edu.virginia.cs.ArtApp.R.menu;
import edu.virginia.cs.ArtApp.R.string;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LoginActivity extends Activity {

	// Values for email and password at the time of the login attempt.
	private String inputUserName;
	private String inputPassword;
	private String authStatus;
	private String inputName;

	// UI references.
	private EditText usernameView;
	private EditText passwordView;
	private EditText nameView;
	String webserviceURL = "http://plato.cs.virginia.edu/~cs4720s14zucchini/";
	String url;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private UserLoginTask mAuthTask = null;
	private ToggleButton toggleLogin;
	String[] resultArray;
	private boolean isLogin = true;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		usernameView = (EditText) findViewById(R.id.email);
		usernameView.setText(inputUserName);

		passwordView = (EditText) findViewById(R.id.password);
		passwordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		toggleLogin = (ToggleButton) findViewById(R.id.toggleLoginbtn);
		
	}	
		
		
		public void onToggleClicked(View view) {
		    // Is the toggle on?
		    boolean on = ((ToggleButton) view).isChecked();
	        Button loginSubmit = (Button)findViewById(R.id.sign_in_button);
	    	nameView = (EditText)findViewById(R.id.newname);
	    	isLogin = !isLogin;

		    if (on) {
		    	loginSubmit.setText("Register");
		        nameView.setVisibility(View.VISIBLE);
		    } else {
		        loginSubmit.setText("Login");
		        nameView.setVisibility(View.INVISIBLE);
		    }
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		usernameView.setError(null);
		passwordView.setError(null);

		// Store values at the time of the login attempt.
		inputUserName = usernameView.getText().toString();
		inputPassword = passwordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(inputPassword)) {
			passwordView.setError(getString(R.string.error_field_required));
			focusView = passwordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(inputUserName)) {
			usernameView.setError(getString(R.string.error_field_required));
			focusView = usernameView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null); 
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
		    InputStream is = null;
		    String result = "";
			try {
					HttpClient httpclient = new DefaultHttpClient();
					if(isLogin) {
						url = webserviceURL + "currentUsers/" + inputUserName + "?password=" + inputPassword;
					} else {
						inputName = nameView.getText().toString();
						url = webserviceURL + "newUsers/" + inputUserName + "?password=" + inputPassword + "&name=" + inputName;
					}
				    HttpPost httppost = new HttpPost(url);
				    HttpResponse response = httpclient.execute(httppost);
				    HttpEntity entity = response.getEntity();
				    is = entity.getContent();
				    } catch (Exception e) {
				      Log.e("Login1", "Error in http connection " + e.toString());
				    }
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
		        resultArray = result.split(":");
		        authStatus = resultArray[4].substring(0, resultArray[4].length() - 2);
		        
		      } catch (Exception e) {
		        Log.e("Login2", "Error converting result " + e.toString());
		      }
			Log.d("Boolean: ", authStatus + " Actual String " + (authStatus == "true"));
		    if(authStatus.equals("true")) {
		    	Log.d("Boolean: ", authStatus + " Actual String " + (authStatus == "true"));
		  	  	Globals g = (Globals)getApplication();
		  	  	g.setUser(result.split("\"")[7]);

		    	return true;
		    } else {
			return false;
		    }
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			showProgress(false);
			if (success) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
				finish();
			} else {
				passwordView
						.setError(getString(R.string.error_incorrect_password));
				passwordView.requestFocus();
			}
		}
	}
}
