package com.example.novan.tugasakhir.profile_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.novan.tugasakhir.MainActivity;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.User;
import com.example.novan.tugasakhir.util.database.AppConfig;
import com.example.novan.tugasakhir.util.database.AppController;
import com.example.novan.tugasakhir.util.database.DataHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    EditText name, username;
    CircleImageView photo;
    FloatingActionButton button_photo;
    Button button_submit;
    String TAG = "TAGapp";
    Bitmap image;
    private static final int SELECT_PICTURE = 100;
    int count = 0;

    ProgressDialog progressDialog;

    private DataHelper dataHelper;
    private ArrayList<User> users = new ArrayList<>();

    MainActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //initialize database
        dataHelper = new DataHelper(this);

        //initialize user
        users = dataHelper.getUserDetail();

        //progress dialog show
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (EditText) findViewById(R.id.edit_name_profile);
        username = (EditText) findViewById(R.id.edit_username_profile);
        photo = (CircleImageView) findViewById(R.id.image_edit_profile);
        button_photo = (FloatingActionButton) findViewById(R.id.image_edit_profile_button);
        button_submit = (Button) findViewById(R.id.submit_profile);

        name.setText(users.get(count).getName());
        username.setText(users.get(count).getUsername());
        photo.setImageBitmap(BitmapFactory.decodeByteArray(users.get(count).getImage(),0,
                users.get(count).getImage().length));

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openImageSource();
            }
        });
        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageSource();
            }
        });
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
    }

    private void updateData() {
        String name_string = name.getText().toString();
        String username_string = username.getText().toString();
        String unique_id = users.get(count).getUnique_id();
        String used_username = users.get(count).getUsername();
        Bitmap img = image;

        if(name_string == null || username_string == null){
            Toast.makeText(this, "Tolong isi semua form", Toast.LENGTH_SHORT).show();
        }else {
            if(img == null){
                //if image null, then image will not be replaced
                byte[] byteArray = users.get(count).getImage();

                updateUser(name_string,username_string,used_username,unique_id,byteArray);
            }else{
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.PNG,40,stream);
                byte[] byteArray = stream.toByteArray();

                //image replaced with new one
                updateUser(name_string,username_string,used_username,unique_id,byteArray);
            }
        }
    }

    private void openImageSource() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih gambar"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            //when image is picked
            if (resultCode == RESULT_OK) {
                if (requestCode == SELECT_PICTURE) {
                    // Get the url from data
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        image = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageUri);
                        photo.setImageBitmap(image);
                        //scale down image
                        image = scaleDownBitmap(image,70,EditProfileActivity.this);
                    }
                }
            }else {
                Toast.makeText(this, "Anda belum memilih gambar", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUser(final String name, final String username, final String used_username, final String uid, final byte[] image) {

        ma = new MainActivity();

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        progressDialog.setMessage("Updating ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //get ID of user
                users = dataHelper.getUserDetail();
                int id = users.get(count).getId();

                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        dataHelper.update_user(id,username,name,image);
                        Toast.makeText(getApplicationContext(), "Pengguna telah diperbarui.", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        ma.finish();
                        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                        intent.putExtra("TAG","profile");
                        startActivityForResult(intent,10);
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

                //convert bitmap to string
                String img = getStringImage(image);

                params.put("unique_id", uid);
                params.put("name", name);
                params.put("username", username);
                params.put("used_username", used_username);
                params.put("image",img);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public String getStringImage(byte[] bmp){
        String encodedImage = Base64.encodeToString(bmp, Base64.DEFAULT);
        return encodedImage;
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }
}
