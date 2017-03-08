package com.example.novan.tugasakhir.home_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;
import com.example.novan.tugasakhir.MainActivity;
import com.example.novan.tugasakhir.R;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import java.util.Random;

/**
 * Created by Novan on 01/02/2017.
 */

public class OverviewFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_overview, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list_medicine);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(layoutManager);
        CustomAdapter adapter = new CustomAdapter(context);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.farb_overview);
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
                Intent inten = new Intent(getActivity().getApplicationContext(), InputMedicineActivity.class);
                startActivity(inten);
            }
        });



        return view;
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder>{

        private Context context;
        public Holder holder;

        public CustomAdapter(Context context){
            this.context = context;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.content_progressbar_circle,parent,false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            this.holder = holder;
            float max = 100;
            float min = 0;
            int count= 1;
            Random random = new Random();
            float randomvalue = min+(max-min)*random.nextFloat();
            int randomvalue2=random.nextInt(10);
            holder.progressView.setProgressWithAnimation(randomvalue, 3000);
            if(randomvalue<=10){
                holder.progressView.setCircleColor(getResources().getColor(R.color.custom_progress_red_progress));
            }else if (randomvalue>10 && randomvalue<20){
                holder.progressView.setCircleColor(getResources().getColor(R.color.custom_progress_orange_progress));
            }else{
                holder.progressView.setCircleColor(getResources().getColor(R.color.custom_progress_blue_progress));
            }
            holder.progressView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), MedicineActivity.class);
                    startActivity(intent);
                }
            });
            holder.medicine_text.setText("Obat "+randomvalue2);
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        public class Holder extends RecyclerView.ViewHolder{
            private CircleProgressView progressView;
            private TextView medicine_text;

            public Holder(View itemView) {
                super(itemView);
                progressView = (CircleProgressView) itemView.findViewById(R.id.circle_progress_view);
                medicine_text = (TextView) itemView.findViewById(R.id.medicine_text);

            }
        }
    }
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration{
        private int space;

        public SpacesItemDecoration(int space){
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            if(parent.getChildLayoutPosition(view) == 0){
                outRect.top = space;
            }else{
                outRect.top = space;
            }
        }
    }
}