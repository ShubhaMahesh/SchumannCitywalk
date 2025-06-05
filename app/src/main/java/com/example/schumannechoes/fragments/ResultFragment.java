package com.example.schumannechoes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.schumannechoes.R;

public class ResultFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        TextView resultTitle = view.findViewById(R.id.resultTitle);
        resultTitle.setText(getString(R.string.resultTitle));
        // Get the score and total questions passed via arguments
        Bundle arguments = getArguments();
        int score = arguments != null ? arguments.getInt("score", 0) : 0;
        int totalQuestions = arguments != null ? arguments.getInt("totalQuestions", 0) : 0;

        // Set the result message
        TextView resultMessage = view.findViewById(R.id.resultMessage);
        resultMessage.setText("You correctly identified " + score + " out of " + totalQuestions + " statements.");

        LottieAnimationView animationView = view.findViewById(R.id.resultAnimation);

        String animationFile;
        switch (score) {
            case 0:
                animationFile = "animation_0.json";
                break;
            case 1:
                animationFile = "animation_2.json";
                break;
            case 2:
                animationFile = "animation_2.json";
                break;
            case 3:
                animationFile = "animation_3.json";
                break;
            default:
                animationFile = "animation_3.json"; // fallback/default animation
                break;
        }
        animationView.setAnimation(animationFile);
        animationView.playAnimation();
        return view;
    }
}