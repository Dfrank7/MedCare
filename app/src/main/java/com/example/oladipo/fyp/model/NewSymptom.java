package com.example.oladipo.fyp.model;

import java.util.List;

public class NewSymptom {
    private String symptom1,symtom2,symtom3, user_id, symp_id;
    private List<String> diagnoses;

    public NewSymptom(){}

    public NewSymptom(String symptom1, String symtom2, String symtom3,
                      String user_id, List<String> like_diagnoses) {
        this.symptom1 = symptom1;
        this.symtom2 = symtom2;
        this.symtom3 = symtom3;
        this.user_id = user_id;
        this.diagnoses = like_diagnoses;
    }

    public NewSymptom(String symptom1, String symtom2, String symtom3,
                      String user_id, String symp_id, List<String> like_diagnoses) {
        this.symptom1 = symptom1;
        this.symtom2 = symtom2;
        this.symtom3 = symtom3;
        this.user_id = user_id;
        this.symp_id = symp_id;
        this.diagnoses = like_diagnoses;
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

    public void setSymp_id(String symp_id) {
        this.symp_id = symp_id;
    }

    public String getSymp_id() {
        return symp_id;
    }

    public List<String> getDiagnoses() {
        return diagnoses;
    }
}
