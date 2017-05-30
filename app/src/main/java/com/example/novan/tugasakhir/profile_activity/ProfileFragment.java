package com.example.novan.tugasakhir.profile_activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Contact;
import com.example.novan.tugasakhir.models.User;
import com.example.novan.tugasakhir.util.database.DataHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Novan on 13/03/2017.
 */

public class ProfileFragment extends Fragment {
    private View view;
    TextView contactCount, namePreview;
    ImageView imageView;

    int contactSize;
    DataHelper dataHelper;
    ArrayList<Contact> contacts;
    String TAG ="TAGapp";
    ArrayList<User> users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        dataHelper = new DataHelper(getActivity().getApplicationContext());

        contacts = new ArrayList<>();
        contacts = dataHelper.getAllContacts();
        contactSize = contacts.size();

        contactCount = (TextView) view.findViewById(R.id.id_contacts);
        contactCount.setText(""+contactSize);

        users = new ArrayList<>();
        users = dataHelper.getUserDetail();
        int count = 0;

        String name = users.get(count).getName();
        byte[] image_data = users.get(count).getImage();
        Log.d(TAG,"image byte to string = "+image_data.toString());

        namePreview = (TextView) view.findViewById(R.id.name_profile);
        namePreview.setText(name);

        imageView = (ImageView) view.findViewById(R.id.image_profile);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(image_data,0,image_data.length));


        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_profile);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),EditProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
