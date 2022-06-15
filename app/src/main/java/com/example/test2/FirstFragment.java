package com.example.test2;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.example.test2.databinding.FragmentFirstBinding;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    private int silence;
    private int cannon;
    private boolean volumeOn = true;
    private ImageView btnVolume;
    private ImageView btnFire;
    private ViewPager vwScore;
    private static SoundPool soundPool;

    private CannonService cannonService;

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

//        cannonService.init(getContext());

//        binding.buttonFire.setOnClickListener(view1 -> {
//            cannonService.playCannonSound();
//        });

//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                NavHostFragment.findNavController(FirstFragment.this)
////                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
////            }
////        });

        cannonService = CannonService.getInstance(getContext());

//        setupAudio();

        btnFire = (ImageView) binding.buttonFire;
        btnVolume = (ImageView) binding.buttonVolume;
        vwScore = (ViewPager) binding.pager;

        btnVolume.setOnClickListener(new ToggleVolume());
        btnFire.setOnTouchListener(new FireCannon());

        ArrayList<Integer> imagesArray = new ArrayList<Integer>();
        for (int i : Score.Pages) {
            imagesArray.add(i);
        }
        vwScore.setAdapter(new SlidingImageAdapter(getContext(), imagesArray));
    }

    private void setupAudio() {

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
        silence = soundPool.load(getContext(), R.raw.silence_short, 1);
        cannon = soundPool.load(getContext(), R.raw.cannon4, 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
//
//        cannonService.close();

//        soundPool.release();

//        cannonService.close();
    }

    private class ToggleVolume implements View.OnClickListener {

        @Override
        public void onClick(View v) {
//            if (volumeOn) {
//                btnVolume.setBackgroundResource(R.drawable.ic_volume_off_black_24dp);
//            } else {
//                btnVolume.setBackgroundResource(R.drawable.ic_volume_up_black_24dp);
//            }

            if (cannonService.isVolumeOn()) {
                btnVolume.setBackgroundResource(R.drawable.ic_volume_off_black_24dp);
            } else {
                btnVolume.setBackgroundResource(R.drawable.ic_volume_up_black_24dp);
            }

            cannonService.toggleVolume();

//            volumeOn = !volumeOn;
        }
    }

    private class FireCannon implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
//            if (!volumeOn) return true;
//
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                try {
//                    soundPool.play(cannon, 0.5f, 0.5f, 1, 0, 1f);
//                } catch (IllegalStateException e) {
//                    e.printStackTrace();
//                }
//            }

            if (!cannonService.isVolumeOn()) return true;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                cannonService.playCannonSound();
            }

            return true;
        }
    }

}