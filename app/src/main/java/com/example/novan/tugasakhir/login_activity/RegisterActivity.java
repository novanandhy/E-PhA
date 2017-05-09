package com.example.novan.tugasakhir.login_activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.util.database.AppConfig;
import com.example.novan.tugasakhir.util.database.AppController;
import com.example.novan.tugasakhir.util.database.DataHelper;
import com.example.novan.tugasakhir.util.database.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    Button signup;
    ImageButton fb, google;
    EditText  name, username, password, repassword;
    Spinner spinner;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    DataHelper db;
    String name_string, username_string, password_string, repassword_string,
            previllage_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //get all input form
        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.username_signup);
        password = (EditText) findViewById(R.id.password_signup);
        repassword = (EditText) findViewById(R.id.repassword_signup);

        //progress dialog show
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        //Session manager
        sessionManager = new SessionManager(getApplicationContext());

        //SQLite database handler
        db = new DataHelper(getApplicationContext());

        //check if user already logged in
        if(sessionManager.isLoggedIn()){;
            finish();
        }

        //set spinner object
        spinner = (Spinner) findViewById(R.id.spinnerPrevillage);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.previllage, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //signup button action
        signup = (Button) findViewById(R.id.btn_signup2);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set String form
                name_string = name.getText().toString().trim();
                username_string = username.getText().toString().trim();
                password_string = password.getText().toString().trim();
                repassword_string = repassword.getText().toString().trim();
                if(!password_string.equals(repassword_string)){
                    Toast.makeText(RegisterActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }else if(!name_string.isEmpty() && !username_string.isEmpty()
                        && !password_string.isEmpty() && !repassword_string.isEmpty()){
                    registerUser(name_string,previllage_string,username_string,password_string);
                }else{
                    Toast.makeText(RegisterActivity.this, "Please fill all form", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fb = (ImageButton) findViewById(R.id.btn_facebook_signup);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, "FB clicked", Toast.LENGTH_SHORT).show();
            }
        });

        google = (ImageButton) findViewById(R.id.btn_google_signup);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, "Google clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(final String name_string, final String previllage_string,
                              final String username_string, final String password_string) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        progressDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String previllage = user.getString("previllage");
                        String username = user.getString("username");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(uid, name, previllage, username, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name_string);
                params.put("previllage", previllage_string);
                params.put("username", username_string);
                params.put("password", password_string);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        previllage_string = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, LoginregisterActivity.class);
        startActivity(intent);
        finish();
    }
}