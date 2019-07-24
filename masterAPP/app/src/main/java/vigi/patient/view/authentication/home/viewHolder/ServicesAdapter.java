package vigi.patient.view.authentication.home.viewHolder;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import vigi.patient.R;


@SuppressWarnings("FieldCanBeLocal")
public class ServicesAdapter extends PagerAdapter{

    private Activity context;
    private LayoutInflater layoutInflater;
    private ImageView imageView;
    private TextView description;
    private ArrayList<Drawable> images;
    private ArrayList<String> descriptions;
    private ArrayList<Integer> sizes;

    public ServicesAdapter(Activity context, ArrayList<Drawable> images, ArrayList<String> descriptions, ArrayList<Integer> sizes){
        this.context = context;
        this.images = images;
        this.descriptions = descriptions;
        this.sizes = sizes;
    }

    @Override
    public int getCount() {
        return  images.size(); // number of pages of the sliderAdapter
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int pos) {
        layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.adapter_authentication_initiation, container, false);

        imageView = view.findViewById(R.id.image);
        description = view.findViewById(R.id.description);

        float density = context.getResources().getDisplayMetrics().density;
        imageView.getLayoutParams().width = sizes.get(pos) * (int) density;
        imageView.getLayoutParams().height = sizes.get(pos) * (int) density;
        imageView.setImageDrawable(images.get(pos));
        description.setText(descriptions.get(pos));

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }

}


