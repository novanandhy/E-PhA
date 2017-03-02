package com.example.novan.tugasakhir.home_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eralp.circleprogressview.CircleProgressView;
import com.example.novan.tugasakhir.R;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

/**
 * Created by Novan on 01/02/2017.
 */

public class OverviewFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.overview_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.listView1);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(layoutManager);

        CustomAdapter adapter = new CustomAdapter(context);
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab.attachToRecyclerView(recyclerView, new ScrollDirectionListener() {
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
                Intent inten = new Intent(getContext(), InputMedicineActivity.class);
                startActivity(inten);
            }
        });



        return view;
    }

    private static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder>{

        private Context context;

        public CustomAdapter(Context context){
            this.context = context;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.progressbar_circle,parent,false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {

            holder.progressView.setProgressWithAnimation(20,3000);
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public static class Holder extends RecyclerView.ViewHolder{

            private CircleProgressView progressView;

            public Holder(View itemView) {
                super(itemView);
                progressView = (CircleProgressView) itemView.findViewById(R.id.circle_progress_view);
            }
        }
    }

}