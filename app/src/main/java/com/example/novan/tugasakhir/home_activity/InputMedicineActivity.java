package com.example.novan.tugasakhir.home_activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.Random;

public class InputMedicineActivity extends AppCompatActivity {
    EditText input1, input2, input3, input4;
    FloatingActionButton button_photo;
    ImageView photo;

    String name, uid;
    int amount, dosage, time;
    private static final int SELECT_PICTURE = 100;
    public String layout;
    Bitmap image;

    DataHelper dataHelper;
    ArrayList<Medicine> medicines;

    String TAG = "TAGapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //create connection with database
        dataHelper = new DataHelper(this);

        //get all medicines
        medicines = dataHelper.getAllMedicine();

        //declaration of form
        input1 = (EditText) findViewById(R.id.input_medicine);
        input2 = (EditText) findViewById(R.id.input_number_medicine);
        input3 = (EditText) findViewById(R.id.input_dosage_medicine);
        input4 = (EditText) findViewById(R.id.input_time_medicine);

        photo = (ImageView) findViewById(R.id.medicine_image);

        button_photo = (FloatingActionButton) findViewById(R.id.image_add_medicine_button);
        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageSource();
            }
        });

        //if submit button clicked
        Button button =(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //show toast if form not filled
                if (TextUtils.isEmpty(input1.getText()) || TextUtils.isEmpty(input2.getText()) ||
                        TextUtils.isEmpty(input3.getText()) || TextUtils.isEmpty(input4.getText())){
                    alertCreator("Tolong isi semua form");
                }else if (Integer.parseInt(input2.getText().toString()) < Integer.parseInt(input3.getText().toString())){
                    alertCreator("Dosis harus kurang dari jumlah obat");
                }else if(image == null){
                    alertCreator("Gambar belum dimasukkan");
                }else {
                    name = input1.getText().toString();
                    amount = Integer.parseInt(input2.getText().toString());
                    dosage = Integer.parseInt(input3.getText().toString());
                    time = Integer.parseInt(input4.getText().toString());
                    uid = random();
                    Log.d(TAG,"uid = "+uid);

                    //insert into database
                    try{
                        if(isNameExist(name)==false){
                            //scale down image
                            image = scaleDownBitmap(image,100,InputMedicineActivity.this);

                            //convert bitmap to byteArray
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.PNG,40,stream);
                            byte[] byteArray = stream.toByteArray();

                            Medicine medicine = dataHelper.save_medicine(uid, name,amount,dosage,amount,time,byteArray);

                            Intent intent = new Intent();
                            intent.putExtra("medicine",medicine);
                            intent.putExtra("h",10);
                            setResult(RESULT_OK,intent);
                            finish();
                        }else{
                            alertCreator("Obat ini sudah ada");
                        }

                    }catch (SQLException e){
                        e.printStackTrace();
                        alertCreator("Maaf, obat ini gagal disimpan");
                        return;
                    }
                }
            }});
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

    private boolean isNameExist(String name) {
        int count = 0;
        for (int i = 0 ; i < medicines.size() ; i++){
            if (medicines.get(i).getMedicine_name().equalsIgnoreCase(name)){
                count++;
            }
        }if(count>0){
            return true;
        }else{
            return false;
        }
    }

    private void alertCreator(String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static String random() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
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
