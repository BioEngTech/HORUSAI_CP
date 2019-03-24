package vigi.patient.presenter.service.patient.impl;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import vigi.patient.model.entities.Patient;
import vigi.patient.model.firebase.FirebaseConstants;
import vigi.patient.presenter.service.patient.api.PatientService;


public class FirebasePatientService implements PatientService {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageReference imageStorageReference;

    DatabaseReference currentReference;

    Patient patient;

    boolean successfullOperation = false;

    @Override
    public void init() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseDatabase.getReference(Patient.class.getSimpleName());
        storageReference = firebaseStorage.getReference();
        patient = new Patient();
    }

    @Override
    public boolean createPatient(Patient patient) {
        this.patient = patient;
        try {
            currentReference = databaseReference.push();

            uploadPatientImage(patient);

            savePatientInDatabase(patient);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void uploadPatientImage(Patient patient) throws ExecutionException, InterruptedException {
        storageReference.child(FirebaseConstants.IMAGE_PATH);

        //save image in firestore
        imageStorageReference = storageReference.child(currentReference.getKey() + FirebaseConstants.JPG_EXTENSION);
        UploadTask uploadTask = imageStorageReference.putFile(Uri.parse(patient.getImage()));

        CompletableFuture<StorageTask> completableFuture
                = CompletableFuture.supplyAsync(() -> uploadTask.addOnCompleteListener(new OnImageUploadCompleteListener()));

        //Wait for it to end
        completableFuture.get();
    }

    private class OnImageUploadCompleteListener implements OnCompleteListener {
        @Override
        public void onComplete(@NonNull Task task) {
            if (task.isSuccessful()) {
                imageStorageReference.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    patient.setImage(downloadUri.toString()); //get path to firestore
                }).addOnFailureListener(exception -> {
                    patient.setImage("No image provided");
                });
            } else {
                Log.d(FirebasePatientService.class.getName(), "image not uploaded successfully, exception");
                Log.e(FirebasePatientService.class.getName(), task.getException().toString());
            }
        }
    }

    private void savePatientInDatabase(Patient patient) {
        databaseReference.child(currentReference.getKey()).setValue(patient);
    }

    @Override
    public Patient readPatient() {
        return new Patient();
    }


    @Override
    public Patient updatePatient() {
        return new Patient();
    }

    @Override
    public boolean deletePatient() {
        return true;
    }
}
