package com.example.schumannechoes.fragments;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.schumannechoes.R;

public class ChallengesFragment extends Fragment {

    private TextView questionText;
    private RadioGroup optionsGroup;
    private Button nextButton;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private String[] questions;
    private String[][] options;
    private int[] correctAnswers = {1, 1, 0}; // index of the correct answer

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenges, container, false);

        questionText = view.findViewById(R.id.questionText);
        optionsGroup = view.findViewById(R.id.optionsGroup);
        nextButton = view.findViewById(R.id.nextButton);


        questions = new String[]{
                getString(R.string.question_1),
                getString(R.string.question_2),
                getString(R.string.question_3)
        };

        options = new String[][]{
                {getString(R.string.answer_1_1), getString(R.string.answer_1_2), getString(R.string.answer_1_3)},
                {getString(R.string.answer_2_1), getString(R.string.answer_2_2), getString(R.string.answer_2_3)},
                {getString(R.string.answer_3_1), getString(R.string.answer_3_2), getString(R.string.answer_3_3)}
        };

        loadQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedOptionId = optionsGroup.getCheckedRadioButtonId();
                if (selectedOptionId == -1) {
                    Toast.makeText(getActivity(), "Please select an answer", Toast.LENGTH_SHORT).show();
                    return;
                }

                int selectedAnswerIndex = optionsGroup.indexOfChild(view.findViewById(selectedOptionId));
                if (selectedAnswerIndex == correctAnswers[currentQuestionIndex]) {
                    score++;
                }

                currentQuestionIndex++;
                if (currentQuestionIndex < questions.length) {
                    loadQuestion();
                } else {
                    navigateToResultFragment();
                }
            }
        });

        return view;
    }

    private void loadQuestion() {
        questionText.setText(questions[currentQuestionIndex]);
        optionsGroup.clearCheck();
        optionsGroup.removeAllViews();

        for (int i = 0; i < options[currentQuestionIndex].length; i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(options[currentQuestionIndex][i]);
            optionsGroup.addView(radioButton);
        }

        if (currentQuestionIndex == questions.length - 1) {
            nextButton.setText(getString(R.string.submit));
        } else {
            nextButton.setText(getString(R.string.next));
        }
    }

    private void navigateToResultFragment() {
        ResultFragment resultFragment = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("score", score);
        bundle.putInt("totalQuestions", questions.length);
        resultFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, resultFragment)
                .addToBackStack(null)
                .commit();
    }
}