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
import java.util.ArrayList;

public class EditMedineActivity extends AppCompatActivity {
    private EditText input1,input2,input3,input4;
    private Medicine medicine;
    private FloatingActionButton button_photo;
    private ImageView photo;

    private ArrayList<Medicine> medicines;

    private String name;
    private int amount, dosage, time, id, remain;
    private static final int SELECT_PICTURE = 100;
    private byte[] img;
    private Bitmap image;
    private String name_medicine, uid_name_medicine;
    private int dosage_medicine, time_medicine;
    private int amount_medicine, remain_medicine;

    private DataHelper dataHelper;
    private String TAG = "TAGapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataHelper = new DataHelper(this);
        medicines = new ArrayList<>();
        medicines = dataHelper.getAllMedicine();

        uid_name_medicine = getIntent().getExtras().getString("uid_medicine");

        //get value of form
        for (int i = 0 ; i < medicines.size() ; i++){
            if (medicines.get(i).getUid().contains(uid_name_medicine)){
                name_medicine = medicines.get(i).getMedicine_name();
                dosage_medicine = medicines.get(i).getDosage();
                time_medicine = medicines.get(i).getCount();
                id = medicines.get(i).getId();
                amount_medicine = medicines.get(i).getAmount();
                remain_medicine = medicines.get(i).getRemain();
                img = medicines.get(i).getImage();
            }
        }



        input1 = (EditText) findViewById(R.id.edit_medicine);
        input2 = (EditText) findViewById(R.id.edit_number_medicine);
        input3 = (EditText) findViewById(R.id.edit_dosage_medicine);
        input4 = (EditText) findViewById(R.id.edit_time_medicine);
        photo = (ImageView) findViewById(R.id.medicine_image_edit);

        image = BitmapFactory.decodeByteArray(img,0,img.length);

        //SetText to form
        input1.setText(""+name_medicine);
        input2.setText(""+amount_medicine);
        input3.setText(""+dosage_medicine);
        input4.setText(""+time_medicine);
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
                    remain = remain_medicine;

                    //insert into database
                    try{
                        //convert bitmap to byteArray
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.PNG,40,stream);
                        byte[] byteArray = stream.toByteArray();

                        //update data medicine
                        Medicine newMedicine = dataHelper.update_medicine(id,name,amount,dosage,remain,time, byteArray);
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
                        image = scaleDownBitmap(image,70,EditMedineActivity.this);
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
