package com.example.novan.tugasakhir.home_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.eralp.circleprogressview.CircleProgressView;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Medicine;
import com.example.novan.tugasakhir.util.UIcomponent.TutorialDialog;
import com.example.novan.tugasakhir.util.database.DataHelper;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Novan on 01/02/2017.
 */

public class OverviewFragment extends Fragment {
    private View view;
    private GridView gridView;
    private Context context;
    private DataHelper dataHelper;
    private ArrayList<Medicine> medicines;
    private CustomAdapter adapter;
    private String TAG = "TAGapp";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_overview, container, false);
        gridView = (GridView) view.findViewById(R.id.list_medicine);

        dataHelper = new DataHelper(getActivity().getApplicationContext());
        medicines = new ArrayList<>();

        PopulateAdapter();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.farb_overview);
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

        //show tutorial dialog if size 0
        if (medicines.size() == 0){
            Intent intent = new Intent(context, TutorialDialog.class);
            intent.putExtra("title", "Panduan Halaman Daftar Obat");
            intent.putExtra("description", getResources().getString(R.string.tutorial_overview_information));
            intent.putExtra("image", R.drawable.gif_daftar_obat);
            startActivity(intent);
        }

        adapter = new CustomAdapter(context,medicines);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, MedicineActivity.class);
                intent.putExtra("uid_name_medicine",medicines.get(position).getUid());
                startActivityForResult(intent,10);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            PopulateAdapter();
        }
    }

    private class CustomAdapter extends BaseAdapter{
        private Context context;
        private ArrayList<Medicine> medic;

        private CustomAdapter(Context context, ArrayList<Medicine> medicines){
            this.context = context;
            medic = medicines;
        }

        @Override
        public int getCount() {
            return medic.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.content_progressbar_circle, null);
            }

            CircleProgressView progressView = (CircleProgressView) convertView.findViewById(R.id.circle_progress_view);
            TextView medicine_text = (TextView) convertView.findViewById(R.id.medicine_text);

            float percentage ;
            int amount ;
            int remain ;
            String medicine_name ;

            amount = medic.get(position).getAmount();
            remain = medic.get(position).getRemain();
            percentage = (remain*100)/amount;

            progressView.setProgressWithAnimation(percentage,2000);
            if (percentage <= 10){
                progressView.setCircleColor(getResources().getColor(R.color.custom_progress_red_progress));
            }else if(percentage > 10 && percentage < 20){
                progressView.setCircleColor(getResources().getColor(R.color.custom_progress_orange_progress));
            }else{
                progressView.setCircleColor(getResources().getColor(R.color.custom_progress_green_progress));
            }

            medicine_name = medic.get(position).getMedicine_name();
            medicine_text.setText(medicine_name);

            return convertView;
        }
    }
}