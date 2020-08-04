package android.example.blogtalk.Adapter;

import android.content.Context;
import android.example.blogtalk.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }
    // ARRAYS
    public int[] slide_images = {
            R.drawable.groupone,
            R.drawable.grouptwo,
            R.drawable.group3,
    };
    public String[] slide_headings = {
            "EAT",
            "SLEEP",
            "CODE"
    };
    public String[] slide_descs = {
            "Lorem ipsum dolor sit amet consectetur adipiscing elit, neque purus arcu turpis himenaeos elementum, taciti diam class quisque leo lacus.",
            "Lorem ipsum dolor sit amet consectetur adipiscing elit, neque purus arcu turpis himenaeos elementum, taciti diam class quisque leo lacus.",
            "Lorem ipsum dolor sit amet consectetur adipiscing elit, neque purus arcu turpis himenaeos elementum, taciti diam class quisque leo lacus."
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        //initialize views
        ImageView slideImage = view.findViewById(R.id.imageIV);
        TextView slideHeading  = view.findViewById(R.id.headingTV);
        TextView slideDesc  = view.findViewById(R.id.descTV);

        slideImage.setImageResource(slide_images[position]); // the current position of side will pass to it and it will set image automatically
        slideHeading.setText(slide_headings[position]);
        slideDesc.setText(slide_descs[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ConstraintLayout)object);

    }
}
