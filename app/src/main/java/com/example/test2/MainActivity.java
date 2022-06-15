package com.example.test2;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.test2.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private int silence;
    private int cannon;
    private boolean volumeOn = true;
    private ImageView btnVolume;
    private ImageView btnFire;
    private ViewPager vwScore;
    private static SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



//        setContentView(R.layout.activity_main);

                binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


//        injectControls();
//        setupControls();
    }

    private void injectControls() {
        btnFire = new ImageView(this);
        btnVolume = new ImageView(this);
        vwScore = new ViewPager(this);
        btnFire = (ImageView) findViewById(R.id.button_fire);
        btnVolume = (ImageView) findViewById(R.id.button_volume);
        vwScore = (ViewPager) findViewById(R.id.pager);
    }

    private void setupControls() {
        btnVolume.setOnClickListener(new ToggleVolume());
        btnFire.setOnTouchListener(new FireCannon());
        setupVwScore();
    }

    private void setupVwScore() {
        ArrayList<Integer> imagesArray = new ArrayList<Integer>();
        for (int i : Score.Pages) {
            imagesArray.add(i);
        }
        vwScore.setAdapter(new SlidingImageAdapter(MainActivity.this, imagesArray));
    }

    private class ToggleVolume implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (volumeOn) {
                btnVolume.setBackgroundResource(R.drawable.ic_volume_off_black_24dp);
            } else {
                btnVolume.setBackgroundResource(R.drawable.ic_volume_up_black_24dp);
            }

            volumeOn = !volumeOn;
        }
    }

    private class FireCannon implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (!volumeOn) return true;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                try {
                    soundPool.play(cannon, 0.5f, 0.5f, 1, 0, 1f);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }

            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        setupAudio();

        CannonService.getInstance(getApplicationContext());
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setupAudio() {

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
        silence = soundPool.load(this, R.raw.silence_short, 1);
        cannon = soundPool.load(this, R.raw.cannon4, 1);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        soundPool.release();

        CannonService.getInstance(getApplicationContext()).close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.SecondFragment);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
