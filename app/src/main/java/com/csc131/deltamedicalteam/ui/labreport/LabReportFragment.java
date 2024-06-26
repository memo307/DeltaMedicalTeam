package com.csc131.deltamedicalteam.ui.labreport;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csc131.deltamedicalteam.R;
import com.csc131.deltamedicalteam.adapter.CurrentAllergiesList;
import com.csc131.deltamedicalteam.adapter.CurrentIllnessList;
import com.csc131.deltamedicalteam.databinding.FragmentHomeBinding;
import com.csc131.deltamedicalteam.databinding.FragmentLabReportBinding;
import com.csc131.deltamedicalteam.model.HealthConditions;
import com.csc131.deltamedicalteam.model.Patient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LabReportFragment extends Fragment {

    private com.csc131.deltamedicalteam.databinding.FragmentLabReportBinding binding;

    // Initialize Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Reference to the "patients" collection
    private CollectionReference patientsRef = db.collection("patients");
    private Button mAddEditButton;
    private Spinner patientSpinner;
    RecyclerView recyclerViewReport, recyclerViewPhoto, recyclerViewVideo;
    TabLayout tabLayout;
    private CurrentIllnessList mAdapter;
    private CurrentAllergiesList mAllergiesAdapter;
    List<Patient> patientNames = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lab_report, container, false);

        patientSpinner = rootView.findViewById(R.id.lab_report_patient_list_spinner);
        getPatientList();

        // Find RecyclerViews
        recyclerViewReport = rootView.findViewById(R.id.RecyclerView_report);
        recyclerViewPhoto = rootView.findViewById(R.id.RecyclerView_photo);
        recyclerViewVideo = rootView.findViewById(R.id.RecyclerView_current_video);

        //button
//        mAddEditButton = rootView.findViewById(R.id.health_condition_edit);

        // Find TabLayout
        tabLayout = rootView.findViewById(R.id.labReportTabs);

//        recyclerViewCurrentIllness.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerViewSpecificAllergies.setLayoutManager(new LinearLayoutManager(getActivity()));

        //detects when spinner item is selected
//        patientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //retrieves the patient at the given spinner position
//                Patient patient = (Patient) parent.getItemAtPosition(position);
//                String ID = patient.getDocumentId();
//                //uses patient id from spinner and to display current illnesses
//                patientsRef.document(ID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        List<String> currIllness = (List<String>) documentSnapshot.get("currentIllnesses");
//                        List<HealthConditions> items = new ArrayList<>();
//                        //used to check if array is empty or not
//                        if (currIllness != null) {
//                            for (int i = 0; i < currIllness.size(); i++) {
//                                HealthConditions hCons = new HealthConditions();
//                                hCons.setCurrentIllnesses(currIllness.get(i));
//                                items.add(hCons);
//                            }
//                        }
//
//                        mAdapter = new CurrentIllnessList(getActivity(), items);
//                        recyclerViewCurrentIllness.setAdapter(mAdapter);
//
//                        //currentAllegies
//
//                        List<String> currAllergies = (List<String>) documentSnapshot.get("specificAllergies");
//                        List<HealthConditions> allergiesitems = new ArrayList<>();
//                        //used to check if array is empty or not
//                        if (currAllergies != null) {
//                            for (int i = 0; i < currAllergies.size(); i++) {
//                                HealthConditions hCons = new HealthConditions();
//                                hCons.setSpecificAllergies(currAllergies.get(i));
//                                allergiesitems.add(hCons);
//                            }
//                        }
//                        mAllergiesAdapter = new CurrentAllergiesList(getActivity(), allergiesitems);
//                        recyclerViewSpecificAllergies.setAdapter(mAllergiesAdapter);
//                    }
//                });
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        // Set up OnTabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Show corresponding RecyclerView and hide others
                switch (tab.getPosition()) {
                    case 0:
                        recyclerViewReport.setVisibility(View.VISIBLE);
                        recyclerViewPhoto.setVisibility(View.GONE);
                        recyclerViewVideo.setVisibility(View.GONE);
                        break;
                    case 1:
                        recyclerViewReport.setVisibility(View.GONE);
                        recyclerViewPhoto.setVisibility(View.VISIBLE);
                        recyclerViewVideo.setVisibility(View.GONE);
                        break;
                    case 2:
                        recyclerViewReport.setVisibility(View.GONE);
                        recyclerViewPhoto.setVisibility(View.GONE);
                        recyclerViewVideo.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No action needed
                recyclerViewReport.setVisibility(View.VISIBLE);
                recyclerViewPhoto.setVisibility(View.GONE);
                recyclerViewVideo.setVisibility(View.GONE);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No action needed
            }
        });

        //health_condition_edit button onClick
//        mAddEditButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText resetMail = new EditText(v.getContext());
//                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
//                passwordResetDialog.setTitle("Add more field");
//                passwordResetDialog.setMessage("Enter email address to receive reset link");
//                passwordResetDialog.setView(resetMail);
//
//                passwordResetDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //extract email and send reset link
//                        String mail = resetMail.getText().toString();
//                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(Login.this, "Reset Link Sent to Your Email.", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(Login.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    }
//                });
//
//
//                passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Close
//
//                    }
//                });
//                passwordResetDialog.create().show();
//            }
//        });

        return rootView;
    }

    // Method to fetch the list of patients, spinner is populated with patient objects
    private void getPatientList() {
        patientsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Get patient name from DocumentSnapshot
                        // Get patient id from DocumentSnapshot
                        Patient patient = documentSnapshot.toObject(Patient.class);
                        String documentId = documentSnapshot.getId();
                        patient.setDocumentId(documentId);

                        // Add patient name to the list
                        patientNames.add(patient);
                    }

                    // Pass the list of patient names to populate the spinner
                    populateSpinner(patientNames);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching documents: " + e.getMessage());
                });
    }

    // Method to populate the spinner with patient names
    private void populateSpinner(List<Patient> patientNames) {
        // Create an ArrayAdapter using the patientNames list and a default spinner layout
        ArrayAdapter<Patient> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, patientNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        patientSpinner.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}