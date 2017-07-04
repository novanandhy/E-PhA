package com.example.novan.tugasakhir.home_activity;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Medicine;
import com.example.novan.tugasakhir.util.database.DataHelper;

import java.io.ByteArrayOutputStream;

public class EditMedineActivity extends AppCompatActivity {
    EditText input1,input2,input3,input4;
    private Medicine medicine;
    FloatingActionButton button_photo;
    ImageView photo;

    String name;
    int amount, dosage, time, id, remain;
    private static final int SELECT_PICTURE = 100;
    byte[] img;
    Bitmap image;

    DataHelper dataHelper;
    String TAG = "TAGapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        medicine = getIntent().getParcelableExtra("medicine");
        id = getIntent().getIntExtra("id",0);
        dataHelper = new DataHelper(this);

        input1 = (EditText) findViewById(R.id.edit_medicine);
        input2 = (EditText) findViewById(R.id.edit_number_medicine);
        input3 = (EditText) findViewById(R.id.edit_dosage_medicine);
        input4 = (EditText) findViewById(R.id.edit_time_medicine);
        photo = (ImageView) findViewById(R.id.medicine_image_edit);

        img = medicine.getImage();
        image = BitmapFactory.decodeByteArray(img,0,img.length);

//        SetText to form
        input1.setText(""+medicine.getMedicine_name());
        input2.setText(""+medicine.getAmount());
        input3.setText(""+medicine.getDosage());
        input4.setText(""+medicine.getCount());
        photo.setImageBitmap(image);

        button_photo = (FloatingActionButton) findViewById(R.id.image_edit_medicine_button);
        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageSource();
            }
        });

        Button button =(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(input1.getText()) || TextUtils.isEmpty(input2.getText()) || TextUtils.isEmpty(input3.getText()) || TextUtils.isEmpty(input4.getText())){
                    Toast.makeText(EditMedineActivity.this,"Tolong isi semua form", Toast.LENGTH_SHORT).show();
                } else {
                    name = input1.getText().toString();
                    amount = Integer.parseInt(input2.getText().toString());
                    dosage = Integer.parseInt(input3.getText().toString());
                    time = Integer.parseInt(input4.getText().toString());
                    remain = medicine.getRemain();

                    //insert into database
                    try{
                        //convert bitmap to byteArray
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.PNG,40,stream);
                        byte[] byteArray = stream.toByteArray();

                        //try Log
                        Medicine newMedicine = dataHelper.update_medicine(id,name,amount,dosage,remain,time, byteArray); //update data medicine
                        Intent intent = new Intent();
                        intent.putExtra("medicine",newMedicine);
                        setResult(RESULT_OK,intent);
                        finish();
                    }catch (SQLException e){
                        e.printStackTrace();
                        Toast.makeText(EditMedineActivity.this, "Data gagal dimasukkan", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });
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
                        image = scaleDownBitmap(image,100,EditMedineActivity.this);
                    }
                }
            }else {
                Toast.makeText(this, "Anda belum memilih gambar", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }
}
