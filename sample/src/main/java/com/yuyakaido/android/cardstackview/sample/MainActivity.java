package com.yuyakaido.android.cardstackview.sample;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Quadrant;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private CardStackView cardStackView;
    private TouristSpotCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
        reload();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_main_reload:
                reload();
                break;
            case R.id.menu_activity_main_swipe_left:
                swipeLeft();
                break;
            case R.id.menu_activity_main_swipe_right:
                swipeRight();
                break;
            case R.id.menu_activity_main_reverse:
                reverse();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<TouristSpot> createTouristSpots() {
        List<TouristSpot> spots = new ArrayList<>();
        spots.add(new TouristSpot("Yasaka Shrine", "Kyoto", "https://source.unsplash.com/Xq1ntWruZQI"));
        spots.add(new TouristSpot("Fushimi Inari Shrine", "Kyoto", "https://source.unsplash.com/NYyCqdBOKwc"));
        spots.add(new TouristSpot("Bamboo Forest", "Kyoto", "https://source.unsplash.com/buF62ewDLcQ"));
        spots.add(new TouristSpot("Brooklyn Bridge", "New York", "https://source.unsplash.com/THozNzxEP3g"));
        spots.add(new TouristSpot("Empire State Building", "New York", "https://source.unsplash.com/USrZRcRS2Lw"));
        spots.add(new TouristSpot("The statue of Liberty", "New York", "https://source.unsplash.com/PeFk7fzxTdk"));
        spots.add(new TouristSpot("Louvre Museum", "Paris", "https://source.unsplash.com/LrMWHKqilUw"));
        spots.add(new TouristSpot("Eiffel Tower", "Paris", "https://source.unsplash.com/HN-5Z6AmxrM"));
        spots.add(new TouristSpot("Big Ben", "London", "https://source.unsplash.com/CdVAUADdqEc"));
        spots.add(new TouristSpot("Great Wall of China", "China", "https://source.unsplash.com/AWh9C-QjhE4"));
        return spots;
    }

    private TouristSpotCardAdapter createTouristSpotCardAdapter() {
        final TouristSpotCardAdapter adapter = new TouristSpotCardAdapter(getApplicationContext());
        adapter.addAll(createTouristSpots());
        return adapter;
    }

    private void setup() {
        progressBar = (ProgressBar) findViewById(R.id.activity_main_progress_bar);

        cardStackView = (CardStackView) findViewById(R.id.activity_main_card_stack_view);
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
                Log.d("CardStackView", "onCardDragging");
            }

            @Override
            public void onCardSwiped(Quadrant quadrant) {
                Log.d("CardStackView", "onCardSwiped: " + quadrant.toString());
                if (cardStackView.getTopIndex() == adapter.getCount() - 5) {
                    Log.d("CardStackView", "Paginate: " + cardStackView.getTopIndex());
                    paginate();
                }
            }

            @Override
            public void onCardReversed() {
                Log.d("CardStackView", "onCardReversed");
            }

            @Override
            public void onCardMovedToOrigin() {
                Log.d("CardStackView", "onCardMovedToOrigin");
            }

            @Override
            public void onCardClicked(int index) {
                Log.d("CardStackView", "onCardClicked: " + index);
            }
        });
    }

    private void reload() {
        cardStackView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter = createTouristSpotCardAdapter();
                cardStackView.setAdapter(adapter);
                cardStackView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }, 1000);
    }

    private void paginate() {
        adapter.addAll(createTouristSpots());
        adapter.notifyDataSetChanged();
    }

    public void swipeLeft() {
        View target = cardStackView.getTopView();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", -10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, -2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translateX, translateY);

        cardStackView.swipe(SwipeDirection.Left, set);
    }

    public void swipeRight() {
        View target = cardStackView.getTopView();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", 10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, 2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translateX, translateY);

        cardStackView.swipe(SwipeDirection.Right, set);
    }

    private void reverse() {
        cardStackView.reverse();
    }

}
