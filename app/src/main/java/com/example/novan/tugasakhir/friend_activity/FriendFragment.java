package com.example.novan.tugasakhir.friend_activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.history_activity.HistoryActivity;
import com.example.novan.tugasakhir.models.Friends;
import com.example.novan.tugasakhir.models.UserJSON;
import com.example.novan.tugasakhir.util.database.AppConfig;
import com.example.novan.tugasakhir.util.database.AppController;
import com.example.novan.tugasakhir.util.database.DataHelper;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Novan on 05/03/2017.
 */

public class FriendFragment extends Fragment {
    private View view;
    private ListView listView;
    private DataHelper dataHelper;
    private ArrayList<Friends> friends;
    private ArrayList<UserJSON> userJSONs;

    ProgressDialog progressDialog;
    MyListAdapter adapter;

    private Context context;
    private String TAG;
    private String stringUid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend, container, false);
        context = getActivity().getApplicationContext();
        TAG = "TAGapp "+context.getClass().getSimpleName();
        dataHelper = new DataHelper(context);

        friends = new ArrayList<>();
        userJSONs = new ArrayList<>();

        //progress dialog show
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        listView = (ListView) view.findViewById(R.id.list_friend);

        PopulateAdapter();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_friend);
        fab.attachToListView(listView, new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {

            }

            @Override
            public void onScrollUp() {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),SearchFriendActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        return view;
    }

    private void PopulateAdapter() {
        stringUid = null;
        friends.clear();

        friends = dataHelper.getFriend();

        //create string uid user parameter
        for (int i = 0; i < friends.size() ; i ++){
            Log.d(TAG, "friends uid user = "+friends.get(i).getUid_user());
            if (i == 0){
                stringUid = friends.get(i).getUid_user();
            }else {
                stringUid = stringUid + " " + friends.get(i).getUid_user();
            }
        }

        getUserDetails(stringUid);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK){
            PopulateAdapter();
        }
    }

    private class MyListAdapter extends ArrayAdapter<UserJSON> {
        private int layout;
        public MyListAdapter(Context context, int resource, ArrayList<UserJSON> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.friendName = (TextView) convertView.findViewById(R.id.friend_name);
                viewHolder.friendName.setText(userJSONs.get(position).getName());
                viewHolder.username = (TextView) convertView.findViewById(R.id.friend_username);
                viewHolder.username.setText(userJSONs.get(position).getUsername());
                viewHolder.friendImage = (ImageView) convertView.findViewById(R.id.image_friend);
                byte[] decodedString = Base64.decode(userJSONs.get(position).getImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                viewHolder.friendImage.setImageBitmap(decodedByte);
                convertView.setTag(viewHolder);
            }else {
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.friendName.setText(userJSONs.get(position).getName());
            }

            return convertView;
        }
    }

    public class ViewHolder{
        TextView friendName,username;
        ImageView friendImage;
    }

    private void getUserDetails(final String uid_user) {
        // Tag used to cancel the request
        String tag_string_req = "req_data";

        progressDialog.setMessage("loading...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_USER_DETAIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // Now store the user in SQLite
                        JSONArray user = jObj.getJSONArray("user");
                        userJSONs.clear();

                        for (int i = 0 ; i < user.length() ; i++){
                            JSONObject details = user.getJSONObject(i);

                            UserJSON data = new UserJSON();

                            data.setUid_user(details.getString("unique_id"));
                            data.setUsername(details.getString("username"));
                            data.setName(details.getString("name"));
                            data.setImage(details.getString("image"));

                            userJSONs.add(data);
                        }

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), HistoryActivity.class);
                                Log.d(TAG,"uid = "+userJSONs.get(position).getUid_user());
                                intent.putExtra("uid",userJSONs.get(position).getUid_user());
                                startActivity(intent);
                            }
                        });
                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                final String unique = friends.get(position).getUid_user();
                                String name = userJSONs.get(position).getName();

                                Log.d(TAG,"uid user = "+unique);
                                Log.d(TAG,"name = "+name);

                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Apakah anda yakin ingin menghapus "+name+" ?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dataHelper.delete_friend(unique);
                                                PopulateAdapter();
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                return true;
                            }
                        });

                        adapter = new MyListAdapter(context,R.layout.content_friend_list,userJSONs);
                        listView.setAdapter(adapter);
                    } else {
                        // Error in getting data. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Log.d(TAG,errorMsg);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Log.d(TAG,"JSON error"+e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.e(TAG, "Login Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid_user", uid_user);

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

}
