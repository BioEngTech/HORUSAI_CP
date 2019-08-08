package vigi.patient.presenter.service.appointment.api;

import android.content.Context;

import com.google.firebase.database.ValueEventListener;

import java.util.List;

import vigi.patient.model.entities.Agenda;
import vigi.patient.model.entities.CareProvider;
import vigi.patient.model.services.Appointment;

public interface AppointmentService {

    void init(String currentPatientId);

    void readAppointments(ValueEventListener listener);

    void setAllAppointments(List<Appointment> allAppointments);

    void setFirebaseAppointments(Context context, Appointment appointment);

    void confirmPurchaseFirebaseAppointments(Context context, List<String> appointmentsIds);

    void removeFirebaseAppointments(Context context, String appointmentId);

    void addOnOperationCompleteListener(ValueEventListener valueEventListener);

}
