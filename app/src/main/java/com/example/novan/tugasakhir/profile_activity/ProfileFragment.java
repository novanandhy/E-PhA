package com.example.novan.tugasakhir.profile_activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Contact;
import com.example.novan.tugasakhir.models.Friends;
import com.example.novan.tugasakhir.models.User;
import com.example.novan.tugasakhir.util.database.DataHelper;

import java.util.ArrayList;

/**
 * Created by Novan on 13/03/2017.
 */

public class ProfileFragment extends Fragment {
    private View view;
    TextView contactCount, friendCount, namePreview, usernamePreview;
    ImageView imageView;

    int contactSize, friedSize;
    DataHelper dataHelper;
    ArrayList<Contact> contacts;
    ArrayList<Friends> friends;
    String TAG ="TAGapp";
    ArrayList<User> users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        dataHelper = new DataHelper(getActivity().getApplicationContext());

        contacts = new ArrayList<>();
        contacts = dataHelper.getAllContacts();
        contactSize = contacts.size();

        friends = new ArrayList<>();
        friends = dataHelper.getFriend();
        friedSize = friends.size();

        contactCount = (TextView) view.findViewById(R.id.id_contacts);
        contactCount.setText(""+contactSize);

        friendCount = (TextView) view.findViewById(R.id.id_friendlist);
        friendCount.setText(""+friedSize);

        users = new ArrayList<>();

        namePreview = (TextView) view.findViewById(R.id.name_profile);
        usernamePreview = (TextView) view.findViewById(R.id.username_profile);
        imageView = (ImageView) view.findViewById(R.id.image_profile);

        PopulateAdapter();


        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_profile);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),EditProfileActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK){
            PopulateAdapter();
        }
    }

    private void PopulateAdapter() {
        users = dataHelper.getUserDetail();

        String name = users.get(0).getName();
        String username = users.get(0).getUsername();
        byte[] image_data = users.get(0).getImage();

        namePreview.setText(name);
        usernamePreview.setText(username);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(image_data,0,image_data.length));
    }
}
