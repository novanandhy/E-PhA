package com.example.novan.tugasakhir.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.novan.tugasakhir.util.DataHelper;

/**
 * Created by Novan on 12/04/2017.
 */

public class Medicine implements Parcelable {

    private String medicine_name;
    private int amount;
    private int remain;
    private int dosage;
    private int count;
    private int id;

    public Medicine(String medicine_name, int amount, int remain, int dosage, int count) {
        this.medicine_name = medicine_name;
        this.amount = amount;
        this.remain = remain;
        this.dosage = dosage;
        this.count = count;
    }

    public Medicine(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_ID_MEDICINE));
        this.medicine_name = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_NAME_MEDICINE));
        this.amount = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_AMOUNT_MEDICINE));
        this.remain = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_REMAINS_MEDICINE));
        this.dosage = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_DOSAGE_MEDICINE));
        this.count = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_COUNT_MEDICINE));
    }

    protected Medicine(Parcel in) {
        medicine_name = in.readString();
        amount = in.readInt();
        remain = in.readInt();
        dosage = in.readInt();
        count = in.readInt();
        id = in.readInt();
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public int getAmount() {
        return amount;
    }

    public int getRemain() {
        return remain;
    }

    public int getDosage() {
        return dosage;
    }

    public int getCount() {
        return count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(medicine_name);
        dest.writeInt(amount);
        dest.writeInt(remain);
        dest.writeInt(dosage);
        dest.writeInt(count);
        dest.writeInt(id);
    }
}
