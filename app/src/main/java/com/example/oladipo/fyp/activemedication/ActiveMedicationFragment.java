package com.example.oladipo.fyp.activemedication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oladipo.fyp.R;
import com.example.oladipo.fyp.adapter.MedicationsListAdapter;
import com.example.oladipo.fyp.database.MedicationLoader;
import com.example.oladipo.fyp.database.MedicationsListLoader;
import com.example.oladipo.fyp.fragments.BaseFragment;
import com.example.oladipo.fyp.model.Medication;
import com.example.oladipo.fyp.newmediacation.NewMedicationFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActiveMedicationFragment extends BaseFragment implements ActiveMedicationContract.View,
        LoaderManager.LoaderCallbacks<ArrayList<Medication>>  {

    private static final String TAG = ActiveMedicationFragment.class.getSimpleName();
    @BindView(R.id.actmed_recyclerview)
    RecyclerView medsListRecyclerView;
    MedicationsListAdapter activeMedicationsListAdapter;

    public ActiveMedicationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(100, null, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.active_meds, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbarTitle("Active Medications");
        activeMedicationsListAdapter = new MedicationsListAdapter(new ArrayList<Medication>(), false);
        medsListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        medsListRecyclerView.setAdapter(activeMedicationsListAdapter);
        getLoaderManager().restartLoader(100, null, this);
    }

    @OnClick(R.id.fab)
    void onClickFab() {
        navigateToFragment(NewMedicationFragment.newInstance(null));
    }

    void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @NonNull
    @Override
    public Loader<ArrayList<Medication>> onCreateLoader(int id, Bundle args) {
        return new MedicationsListLoader(getContext(), MedicationLoader.ACTIVE_MEDICATION_SELECTION, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Medication>> loader, ArrayList<Medication> data) {
        Log.d(TAG, data+"");
        activeMedicationsListAdapter.updateData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Medication>> loader) {
    }

    @Override
    public void moveToNextStep() {
    }
}
