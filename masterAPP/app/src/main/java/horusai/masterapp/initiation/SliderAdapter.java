package horusai.masterapp.initiation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import horusai.masterapp.R;

public class SliderAdapter extends PagerAdapter implements View.OnFocusChangeListener,TextView.OnEditorActionListener,TextView.OnKeyListener{

    private Activity context;
    private LayoutInflater inflater;

    // Variables from the slideadapter - profile_number_1_layout

    private CardView CP;
    private CardView Patient;
    private ImageView tickCP;
    private ImageView tickPatient;

    // Variables from the slideadapter - profile_number_2_layout

    private EditText nameText;
    private RelativeLayout man;
    private RelativeLayout woman;
    private ImageView manTick;
    private ImageView womanTick;
    private TextView mrMsIdentification;
    private ImageView photo;
    private TextView birth;
    private DatePickerDialog.OnDateSetListener dateSetListener;


    public SliderAdapter(Activity context){

        this.context = context;

    }

    @Override
    public int getCount() {
        return 3 ; // number of pages of the slideadapter
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == (LinearLayout)   object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = null;

        switch (position){

        case 0:

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.initiation_profile_number_1,container,false);

            CP = view.findViewById(R.id.initiationProfileNumber1_CardviewCP);
            Patient = view.findViewById(R.id.initiationProfileNumber1_CardviewPatient);
            tickCP = view.findViewById(R.id.initiationProfileNumber1_TickCP);
            tickPatient = view.findViewById(R.id.initiationProfileNumber1_TickPatient);

            // Make buttons and views respond to a click

            CP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tickCP.setVisibility(View.VISIBLE);
                    tickPatient.setVisibility(View.GONE);

                }
            });

            Patient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tickCP.setVisibility(View.GONE);
                    tickPatient.setVisibility(View.VISIBLE);
                }
            });

            container.addView(view);

            break;

        case 1:

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.initiation_profile_number_2,container, false);

            nameText = view.findViewById(R.id.initiationProfileNumber2_Name);
            man = view.findViewById(R.id.initiationProfileNumber2_Man);
            manTick = view.findViewById(R.id.initiationProfileNumber2_ManTick);
            woman = view.findViewById(R.id.initiationProfileNumber2_Woman);
            womanTick = view.findViewById(R.id.initiationProfileNumber2_WomanTick);
            mrMsIdentification = view.findViewById(R.id.initiationProfileNumber2_MrMs);
            photo = view.findViewById(R.id.initiationProfileNumber2_ImageUser);
            birth = view.findViewById(R.id.initiationProfileNumber2_Birth);

            // Make name text respond to focus && Keyboard

            nameText.setOnFocusChangeListener(this);
            nameText.setOnEditorActionListener(this);
            nameText.setOnKeyListener(this);

            // Make buttons and views respond to a click

            man.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    manTick.setVisibility(View.VISIBLE);
                    womanTick.setVisibility(View.GONE);
                    mrMsIdentification.setText(R.string.mr_text);
                }
            });

            woman.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    manTick.setVisibility(View.GONE);
                    womanTick.setVisibility(View.VISIBLE);
                    mrMsIdentification.setText(R.string.ms_text);

                }
            });

            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // TAKE A PICTURE HERE AND REPLACE IT ON IMAGE VIEW!!!
                }
            });

            birth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Calendar cal = Calendar.getInstance();

                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener,year,month,day);

                        long hundred_ten_years = 1000*60*60*24*39600L;

                        dialog.getDatePicker().setMinDate(cal.getTimeInMillis() - hundred_ten_years);

                        dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                }
            });

            dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    month = month + 1;

                    String date = dayOfMonth + "/" + month + "/" + year;

                    birth.setText(date);

                }
            };

            container.addView(view);
            break;

        case 2:

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.initiation_profile_number_3,container, false);

        container.addView(view);
        break;

        }

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            InputMethodManager input =(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){

        // Try to Log In when enter is pressed in password text

        if (actionId == EditorInfo.IME_ACTION_GO) {

            return true;
        }
        return false;

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        // Try to Log In when enter is pressed in password text from the enter button on

        if (keyCode == KeyEvent.KEYCODE_ENTER) {

            InputMethodManager input =(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(v.getWindowToken(), 0);

            return true;

        }

        return false;
    }

}


