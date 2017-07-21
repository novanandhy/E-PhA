package com.example.novan.tugasakhir.friend_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Friends;
import com.example.novan.tugasakhir.models.User;
import com.example.novan.tugasakhir.models.UserJSON;
import com.example.novan.tugasakhir.util.UIcomponent.ErrorDialog;
import com.example.novan.tugasakhir.util.database.AppConfig;
import com.example.novan.tugasakhir.util.database.AppController;
import com.example.novan.tugasakhir.util.database.DataHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchFriendActivity extends AppCompatActivity {
    private ListView listView;
    private DataHelper dataHelper;
    private MyListAdapter adapater;

    private ArrayList<User> users;
    private ArrayList<Friends> friends;
    private ArrayList<UserJSON> userJSONs;

    ProgressDialog progressDialog;

    private Context context;
    private String TAG;
    private String stringUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submain_searchfriend);
        context = this;
        TAG = "TAGapp "+context.getClass().getSimpleName();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dataHelper = new DataHelper(this);
        userJSONs = new ArrayList<>();
        users = new ArrayList<>();

        //progress dialog show
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        listView = (ListView) findViewById(R.id.list_friend_search);

        PopulateAdapter();
    }

    private void PopulateAdapter() {
        users = dataHelper.getUserDetail();
        friends = dataHelper.getFriend();

        //get own unique id
        stringUid = users.get(0).getUnique_id();

        for (int i = 0 ; i < friends.size() ; i++){
            stringUid = stringUid + " " +friends.get(i).getUid_user();
        }

        getUserDetails(stringUid);
    }

    private void getUserDetails(final String uid_user) {
        // Tag used to cancel the request
        String tag_string_req = "req_data";

        progressDialog.setMessage("loading...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_USER_ALL, new Response.Listener<String>() {

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
                        adapater = new MyListAdapter(context,R.layout.content_friend_list,userJSONs);
                        listView.setAdapter(adapater);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(SearchFriendActivity.this, AddFriendActivity.class);
                                intent.putExtra("uid", userJSONs.get(position).getUid_user());
                                intent.putExtra("name", userJSONs.get(position).getName());
                                intent.putExtra("image", userJSONs.get(position).getImage());
                                startActivityForResult(intent,10);
                            }
                        });
                    } else {
                        // Error in getting data. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Log.d(TAG,errorMsg);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    ErrorMessage("gagal mendapatkan data", R.drawable.sad_mood);
                    Log.d(TAG,"JSON error"+e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.e(TAG, "Login Error: " + error.getMessage());
                ErrorMessage("cek kembali konseksi anda", R.drawable.no_connection);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid_user", uid_user);

                Log.d(TAG,"uid user = "+uid_user);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK){
            PopulateAdapter();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearch);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("username. . ");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    final ArrayList<UserJSON> found = new ArrayList<UserJSON>();
                    for (UserJSON param : userJSONs ){
                        if (param.getName().contains(newText) || param.getUsername().contains(newText)){
                            found.add(param);
                        }
                    }
                    adapater = new MyListAdapter(context,R.layout.content_friend_list,found);
                    listView.setAdapter(adapater);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(SearchFriendActivity.this, AddFriendActivity.class);
                            intent.putExtra("uid", found.get(position).getUid_user());
                            intent.putExtra("name", found.get(position).getName());
                            intent.putExtra("image", found.get(position).getImage());
                            startActivityForResult(intent,10);
                        }
                    });
                }else{
                    adapater = new MyListAdapter(context,R.layout.content_friend_list,userJSONs);
                    listView.setAdapter(adapater);
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
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

    private class MyListAdapter extends ArrayAdapter<UserJSON> {

        private int layout;

        public Context context;
        public ArrayList<UserJSON> original;

        public MyListAdapter(Context context, int resource, ArrayList<UserJSON> objects) {
            super(context, resource, objects);
            layout = resource;
            this.original = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;

            if(convertView == null){
                Log.d(TAG,"enter getView");
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.friendName = (TextView) convertView.findViewById(R.id.friend_name);
                viewHolder.friendName.setText(original.get(position).getName());
                viewHolder.username = (TextView) convertView.findViewById(R.id.friend_username);
                viewHolder.username.setText(original.get(position).getUsername());
                viewHolder.friendImage = (ImageView) convertView.findViewById(R.id.image_friend);
                byte[] decodedString = Base64.decode(original.get(position).getImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                viewHolder.friendImage.setImageBitmap(decodedByte);
                convertView.setTag(viewHolder);
            }

            return convertView;
        }
    }

    public class ViewHolder{
        TextView friendName,username;
        ImageView friendImage;
    }

    private void ErrorMessage(String s, int img) {
        Intent intent = new Intent(context, ErrorDialog.class);
        intent.putExtra("message",s);
        intent.putExtra("image",img);
        startActivity(intent);
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
