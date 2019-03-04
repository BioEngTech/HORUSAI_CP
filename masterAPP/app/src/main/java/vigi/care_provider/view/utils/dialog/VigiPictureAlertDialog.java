package vigi.care_provider.view.utils.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;


import static vigi.care_provider.view.utils.activity.ActivityUtils.jumpToGalleryActivity;
import static vigi.care_provider.view.utils.activity.ActivityUtils.jumpToPhoneCameraActivity;

public class VigiPictureAlertDialog {
    private Activity activity;

    private VigiPictureAlertDialog(){}

    public VigiPictureAlertDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(String galleryMsg, String cameraMsg) {
        String[] pictureDialogItems = {galleryMsg, cameraMsg};

        new AlertDialog.Builder(activity)
                .setTitle("Select Action")
                .setItems(pictureDialogItems, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            choosePhotoFromGallery();
                            break;
                        case 1:
                            takePhotoFromCamera();
                            break;
                    }
                }).show();
    }

    private void choosePhotoFromGallery() {
        jumpToGalleryActivity(activity);
    }

    private void takePhotoFromCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                jumpToPhoneCameraActivity(activity);
            } else {
                if (activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    //TODO: add string to strings.xml for further translations
                    Toast.makeText(activity, "Your Permission is needed to get access the camera", Toast.LENGTH_LONG).show();
                }
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);

            }
        } else {
            jumpToPhoneCameraActivity(activity);
        }
    }
}
