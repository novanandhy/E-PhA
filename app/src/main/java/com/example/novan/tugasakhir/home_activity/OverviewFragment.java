package com.example.novan.tugasakhir.home_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eralp.circleprogressview.CircleProgressView;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Medicine;
import com.example.novan.tugasakhir.util.DataHelper;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Novan on 01/02/2017.
 */

public class OverviewFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private Context context;
    DataHelper dataHelper;
    ArrayList<Medicine> medicines;
    private CustomAdapter adapter;
    private String TAG = "TAGapp";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_overview, container, false);

        dataHelper = new DataHelper(getActivity().getApplicationContext());
        medicines = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.list_medicine);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(layoutManager);


        PopulateAdapter();

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
                startActivityForResult(inten,10);
            }
        });

        return view;
    }

    private void PopulateAdapter() {
        medicines.clear();
        medicines = dataHelper.getAllMedicine();
        adapter = new CustomAdapter(context,medicines);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            PopulateAdapter();
        }
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder>{

        private Context context;
        public Holder holder;
        private ArrayList<Medicine> medicines;

        public CustomAdapter(Context context, ArrayList<Medicine> medicines){
            this.context = context;
            this.medicines = medicines;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.content_progressbar_circle,parent,false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, final int position) {
            this.holder = holder;
            float percentage ;
            int amount ;
            int remain ;
            String medicine_name ;

            amount = medicines.get(position).getAmount();
            remain = medicines.get(position).getRemain();
            percentage = (remain*100)/amount;

            holder.progressView.setProgressWithAnimation(percentage, 2000);
            if(percentage<=10){
                holder.progressView.setCircleColor(getResources().getColor(R.color.custom_progress_red_progress));
            }else if (percentage>10 && percentage<20){
                holder.progressView.setCircleColor(getResources().getColor(R.color.custom_progress_orange_progress));
            }else{
                holder.progressView.setCircleColor(getResources().getColor(R.color.custom_progress_green_progress));
            }

            holder.progressView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), MedicineActivity.class);
                    intent.putExtra("medicine",medicines.get(position));
                    intent.putExtra("id",medicines.get(position).getId());
                    intent.putExtra("name",medicines.get(position).getMedicine_name());
                    startActivityForResult(intent,10);
                }
            });

            medicine_name = medicines.get(position).getMedicine_name();
            holder.medicine_text.setText(medicine_name);

        }

        @Override
        public int getItemCount() {

            return medicines.size();
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
            outRect.left = 0;
            outRect.right = 0;
            outRect.bottom = space;

            if(parent.getChildLayoutPosition(view) == 0){
                outRect.top = space;
            }else{
                outRect.top = space;
            }
        }
    }
}