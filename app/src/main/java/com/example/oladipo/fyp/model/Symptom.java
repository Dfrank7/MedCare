package com.example.oladipo.fyp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Symptom implements Parcelable {
    public String symptom1,symtom2,symtom3, user_id, symp_id;
    public List<String> diagnoses;

    public Symptom() {
    }

    public Symptom(String feelings1, String feelings2, String feelings3, String user_id,
                   List<String> diagnoses) {
        this.symptom1 = feelings1;
        this.symtom2 = feelings2;
        this.symtom3 = feelings3;
        this.user_id = user_id;
        this.diagnoses = diagnoses;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symptom1);
        dest.writeString(symtom2);
        dest.writeString(symtom3);
        dest.writeString(user_id);
        dest.writeString(symp_id);
        dest.writeList(diagnoses);
    }

    protected Symptom(Parcel in){
        user_id = in.readString();
        symp_id = in.readString();
        symptom1= in.readString();
        symtom2 = in.readString();
        symtom3 = in.readString();
        //diagnoses = new ArrayList<>();
        in.readList(diagnoses,String.class.getClassLoader());
    }

    public static final Creator<Symptom> CREATOR = new Creator<Symptom>() {
        @Override
        public Symptom createFromParcel(Parcel in) {
            return new Symptom(in);
        }

        @Override
        public Symptom[] newArray(int size) {
            return new Symptom[size];
        }
    };

    public String getSymp_id() {
        return symp_id;
    }

    public void setSymp_id(String symp_id) {
        this.symp_id = symp_id;
    }

    public String getSymptom1() {
        return symptom1;
    }

    public String getSymtom2() {
        return symtom2;
    }

    public String getSymtom3() {
        return symtom3;
    }


    public String getUser_id() {
        return user_id;
    }

    public List<String> getDiagnoses() {
        return diagnoses;
    }
}
