package android.example.blogtalk.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.example.blogtalk.Adapter.SliderAdapter;
import android.example.blogtalk.R;
import android.os.Bundle;
import android.text.Html;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WalkthroughActivity extends AppCompatActivity {

    private ViewPager mSlideViewpager;
    private LinearLayout mDotLayout;
    private Button mPrev, mNext, finish;
    private SliderAdapter sliderAdapter;
    private TextView[] dots;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        mSlideViewpager = findViewById(R.id.mainVP);
        mDotLayout = findViewById(R.id.dotsLL);
        mNext = findViewById(R.id.nextBtn);
        mPrev = findViewById(R.id.previousBtn);

        sliderAdapter = new SliderAdapter(this);
        mSlideViewpager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewpager.addOnPageChangeListener(viewListener);
        mSlideViewpager.addOnPageChangeListener(viewListener);
    }
    public void addDotsIndicator(int position){

        dots = new TextView[3];
        mDotLayout.removeAllViews();

        for(int i=0;i<dots.length;i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextColor(getResources().getColor(R.color.transparent));
            mDotLayout.addView(dots[i]);
        }
        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);

            mCurrentPage = position;
            if (position == 0) {
                mNext.setEnabled(true);
                mPrev.setEnabled(false);
                mPrev.setVisibility(View.INVISIBLE);
                mNext.setText("Next");
                mPrev.setText("");
            }

            else if (position == dots.length - 1) {
                mPrev.setEnabled(true);
                mPrev.setVisibility(View.VISIBLE);
                mNext.setText("Next");
                mPrev.setText("Back");
            }

            else {
                mNext.setEnabled(true);
                mPrev.setEnabled(true);
                mPrev.setVisibility(View.VISIBLE);
                mNext.setText("Next");
                mPrev.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    public void prev(View view) {
        mSlideViewpager.setCurrentItem(mCurrentPage - 1);
    }

    public void next(View view) {
        mSlideViewpager.setCurrentItem(mCurrentPage + 1);
    }

    /*public void FinishAct(View view){
        Intent homeIntent = new Intent(WalkthroughActivity.this,Home.class);
        startActivity(homeIntent);
        finish();
    }*/
}
