package com.example.novan.tugasakhir.information_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;

import java.util.ArrayList;

/**
 * Created by Novan on 17/07/2017.
 */

public class InformationFragment extends Fragment {
    private View view;
    private Context context;

    private ArrayList<String> text = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_information, container, false);
        context = getActivity().getApplicationContext();

        PopulateInformation();

        return view;
    }

    private void PopulateInformation() {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);

        text.add("tes1");
        text.add("tes2");
        text.add("tes3");
        text.add("tes2");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new DataAdapter(text);
        recyclerView.setAdapter(adapter);
    }

    private class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
        private ArrayList<String> data;

        public DataAdapter(ArrayList<String> data) {
            this.data = data;
        }


        @Override
        public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_cardview_information, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {
            viewHolder.name.setText(data.get(i));

            viewHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, InformationActivity.class);
                    startActivityForResult(intent,10);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView name;

            public ViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.title_information);
            }
        }
    }
}
