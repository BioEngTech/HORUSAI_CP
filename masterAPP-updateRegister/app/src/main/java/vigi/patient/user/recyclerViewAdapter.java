package vigi.patient.user;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import vigi.patient.R;

public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.ViewHolder>{

    private static final String TAG = "recyclerViewAdapter";

    private Context mContext;
    private ArrayList<String> mCareProviderNames = new ArrayList<>();
    private ArrayList<Integer> mDays = new ArrayList<>();
    private ArrayList<String> mMonths = new ArrayList<>();
    private ArrayList<Integer> mTimes = new ArrayList<>();
    private ArrayList<String> mProcedures = new ArrayList<>();

    public recyclerViewAdapter(Context context,ArrayList<String> careProviderNames,ArrayList<Integer> days,ArrayList<String> months,ArrayList<Integer> times,ArrayList<String> procedures) {
        mContext = context;
        mCareProviderNames = careProviderNames;
        mDays = days;
        mMonths = months;
        mTimes = times;
        mProcedures = procedures;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_list_appointment,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.day.setText(mDays.get(i));
        viewHolder.month.setText(mMonths.get(i));
        viewHolder.time.setText(mTimes.get(i));
        viewHolder.procedure.setText(mProcedures.get(i));
        viewHolder.careProviderName.setText(mCareProviderNames.get(i));
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do something
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProcedures.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView day;
        TextView month;
        TextView time;
        TextView procedure;
        TextView careProviderName;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.userHomeFragment_Day);
            month = itemView.findViewById(R.id.userHomeFragment_Month);
            time = itemView.findViewById(R.id.userHomeFragment_Time);
            procedure = itemView.findViewById(R.id.userHomeFragment_Procedure);
            careProviderName = itemView.findViewById(R.id.userHomeFragment_CareProviderName);
            parentLayout = itemView.findViewById(R.id.userHomeFragment_ParentLayout);
        }
    }
}
