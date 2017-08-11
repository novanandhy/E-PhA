package com.example.novan.tugasakhir.information_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Information;
import com.example.novan.tugasakhir.util.UIcomponent.ErrorDialog;
import com.example.novan.tugasakhir.util.database.AppConfig;
import com.example.novan.tugasakhir.util.database.AppController;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Novan on 17/07/2017.
 */

public class InformationFragment extends Fragment {
    private View view;
    private Context context;
    private ProgressDialog progressDialog;

    private ArrayList<Information> info = new ArrayList<>();

    private String TAG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_information, container, false);
        context = getActivity().getApplicationContext();

        //progress dialog show
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        TAG = "TAGapp "+context.getClass().getSimpleName();

        PopulateInformation();

        return view;
    }

    private void PopulateInformation() {
        // Tag used to cancel the request
        String tag_string_req = "req_data";

        progressDialog.setMessage("loading...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_INFORMATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // Now store the user in SQLite
                        JSONArray information = jObj.getJSONArray("information");

                        for (int i = 0 ; i < information.length() ; i++){
                            JSONObject details = information.getJSONObject(i);

                            Information data = new Information();

                            data.setId(details.getInt("id"));
                            data.setTitle(details.getString("title"));
                            data.setSubtitle(details.getString("subtitle"));
                            data.setImage(details.getString("image"));

                            info.add(data);
                        }

                        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.card_recycler_view);
                        recyclerView.setHasFixedSize(true);

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(layoutManager);

                        RecyclerView.Adapter adapter = new DataAdapter(info);
                        recyclerView.setAdapter(adapter);
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
                ErrorMessage("cek kembali koneksi anda", R.drawable.no_connection);
                Log.e(TAG, "Login Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
        private ArrayList<Information> data;

        public DataAdapter(ArrayList<Information> data) {
            this.data = data;
        }


        @Override
        public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_cardview_information, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.title.setText(data.get(i).getTitle());
            viewHolder.subtitle.setText(data.get(i).getSubtitle());

            Picasso.with(context).invalidate("http://"+ AppConfig.DOMAIN+"/android_api/upload/"+data.get(i).getImage());
            Picasso.with(context).load("http://"+AppConfig.DOMAIN+"/android_api/upload/"+data.get(i).getImage()).into(viewHolder.image);

            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, InformationActivity.class);
                    intent.putExtra("title",data.get(i).getTitle());
                    intent.putExtra("subtitle",data.get(i).getSubtitle());
                    intent.putExtra("image",data.get(i).getImage());
                    startActivityForResult(intent,10);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView title,subtitle;
            ImageView image;

            public ViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title_information);
                subtitle = (TextView) view.findViewById(R.id.text_information);
                image = (ImageView) view.findViewById(R.id.image_cardview_information);
            }
        }
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
