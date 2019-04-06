package vigi.patient.presenter.service.treatment.impl;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.security.auth.callback.Callback;

import vigi.patient.model.entities.Patient;
import vigi.patient.model.firebase.FirebaseConstants;
import vigi.patient.model.services.Treatment;
import vigi.patient.presenter.service.treatment.api.TreatmentService;


public class FirebaseTreatmentService {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageReference imageStorageReference;

    DatabaseReference currentReference;

    Treatment treatment;
    ArrayList<String> categories;
    ArrayList<Treatment> treatments;

    boolean successfullOperation = false;


    public void readCategories(final TreatmentService TSer ) {
        //this.categories = new ArrayList<>();
        TSer.onStart();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Treatment");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TSer.onSuccess(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                TSer.onFailed(databaseError);
            }
        });

    }

    public void readTreatments(String category, final TreatmentService TSer) {

        // TODO Get category selected in the spinner and then go to the database and pick treatments related to that category

        //this.categories = new ArrayList<>();
        TSer.onStart();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Treatment");

        databaseReference.child(category).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TSer.onSuccess(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                TSer.onFailed(databaseError);
            }
        });

    }

    public void readTreatment(String category, String treatment_id, final TreatmentService TSer) {

        // TODO Get category selected in the spinner and then go to the database and pick treatments related to that category

        //this.categories = new ArrayList<>();
        TSer.onStart();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Treatment");

        databaseReference.child(category).child(treatment_id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TSer.onSuccess(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                TSer.onFailed(databaseError);
            }
        });

    }

}
