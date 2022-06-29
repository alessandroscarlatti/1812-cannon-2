package io.github.cannon1812;

import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import io.github.cannon1812.databinding.FragmentFirstBinding;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    private Button btnVolume;
    private Button btnDeviceVolume;
    private ImageButton btnFire;
    private ViewPager vwScore;
    private Button btnNextBookmark;

    private AudioContentObserver audioContentObserver;

    private CannonService cannonService;

    private SlidingImageAdapter2 slidingImageAdapter2;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cannonService = CannonService.getInstance(getContext());

        audioContentObserver = new AudioContentObserver(new Handler());

        getContext().getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, audioContentObserver);


        btnFire = binding.buttonFire;
        btnVolume = binding.buttonVolume;
        btnDeviceVolume = binding.buttonDeviceVolume;
        vwScore = binding.pager;
        btnNextBookmark = binding.buttonNextBookmark;

        btnVolume.setOnClickListener(new ToggleVolume());
        btnDeviceVolume.setOnClickListener(new ToggleDeviceMute());
        btnFire.setOnTouchListener(new FireCannon());
        btnNextBookmark.setOnClickListener(new NextBookmark());

        configureDeviceVolumeButton();
        configureCannonVolumeButton();
        configureFireCannonButton();

        ArrayList<Integer> imagesArray = new ArrayList<Integer>();
        for (int i : Score.Pages) {
            imagesArray.add(i);
        }

        slidingImageAdapter2 = new SlidingImageAdapter2(getContext(), imagesArray);
        vwScore.setAdapter(slidingImageAdapter2);
    }

    private void configureDeviceVolumeButton() {
        btnDeviceVolume.setText("device volume " + cannonService.getDeviceVolume());
        if (cannonService.isDeviceMuted()) {
            btnDeviceVolume.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_volume_off_sm, 0, 0, 0);
//            btnDeviceVolume.setBackgroundResource(R.drawable.ic_volume_off_sm);
        } else {
            btnDeviceVolume.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_volume_on_sm, 0, 0, 0);
//            btnDeviceVolume.setBackgroundResource(R.drawable.ic_volume_on_sm);
        }
    }

    private void configureCannonVolumeButton() {
        if (cannonService.isVolumeOn()) {
            btnVolume.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_volume_on, 0, 0, 0);
            btnVolume.setText("enabled");
        } else {
            btnVolume.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_volume_off, 0, 0, 0);
            btnVolume.setText("disabled");
        }
    }

    private void configureFireCannonButton() {
        if (cannonService.isVolumeOn()) {
            btnFire.setImageResource(R.drawable.fire_button_on);
        } else {
            btnFire.setImageResource(R.drawable.fire_button);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        getContext().getApplicationContext().getContentResolver().unregisterContentObserver(audioContentObserver);
    }

    private class AudioContentObserver extends ContentObserver {
        public AudioContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            configureDeviceVolumeButton();
            configureFireCannonButton();
        }
    }

    private class ToggleVolume implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            cannonService.toggleVolume();
            configureCannonVolumeButton();
            configureFireCannonButton();
        }
    }

    private class ToggleDeviceMute implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            cannonService.toggleDeviceMute();

            configureDeviceVolumeButton();
            configureFireCannonButton();
        }
    }

    private class FireCannon implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (!cannonService.isVolumeOn()) return true;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                cannonService.playCannonSound();
            }

            return true;
        }
    }

    private class NextBookmark implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            slidingImageAdapter2.setImage(10);
        }
    }

}