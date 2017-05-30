package com.example.novan.tugasakhir.profile_activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.novan.tugasakhir.MainActivity;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.User;
import com.example.novan.tugasakhir.util.database.DataHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    EditText name, username;
    CircleImageView photo;
    FloatingActionButton button_photo;
    Button button_submit;
    String imgPath;
    String TAG = "TAGapp";
    Bitmap image;
    private static final int SELECT_PICTURE = 100;
    int count = 0;

    private DataHelper dataHelper;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //initialize database
        dataHelper = new DataHelper(this);

        //initialize user
        users = new ArrayList<>();
        users = dataHelper.getUserDetail();


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
        int id = users.get(count).getId();
        Bitmap img = image;

        if(name_string == null || username_string == null){
            Toast.makeText(this, "please fill all form", Toast.LENGTH_SHORT).show();
        }else {
            if(img == null){
                byte[] byteArray = users.get(count).getImage();
                dataHelper.update_user(id,username_string,name_string,byteArray);
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                intent.putExtra("TAG","profile");
                startActivity(intent);
                finish();
            }else{
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] byteArray = stream.toByteArray();
                dataHelper.update_user(id,username_string,name_string,byteArray);
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                intent.putExtra("TAG","profile");
                startActivity(intent);
                finish();
            }
        }
    }

    private void openImageSource() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
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
                    }
                }
            }else {
                Toast.makeText(this, "You haven't picked an image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
}
