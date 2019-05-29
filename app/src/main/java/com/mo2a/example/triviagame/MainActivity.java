package com.mo2a.example.triviagame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mo2a.example.triviagame.data.AnswerListAsyncResponse;
import com.mo2a.example.triviagame.data.QuestionBank;
import com.mo2a.example.triviagame.model.Question;
import com.mo2a.example.triviagame.util.Prefs;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionText;
    private TextView counterText;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private TextView scoreText;
    private TextView highscoreText;
    private List<Question> questionList;
    private Prefs prefs;
    private int currentQuestionIndex= 0;
    private int score= 0;
    private int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionText= findViewById(R.id.question_text);
        counterText= findViewById(R.id.counter_text);
        nextButton= findViewById(R.id.next_button);
        previousButton= findViewById(R.id.previous_button);
        trueButton= findViewById(R.id.true_button);
        falseButton= findViewById(R.id.false_button);
        scoreText= findViewById(R.id.score_text);
        highscoreText= findViewById(R.id.highscore_text);

        prefs= new Prefs(this);
        highscore= prefs.getHighscore();
        currentQuestionIndex= prefs.getState();
        highscoreText.setText("Highscore: "+ prefs.getHighscore());

        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);


        questionList= new QuestionBank().getQuestions(new AnswerListAsyncResponse(){
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionText.setText(questionArrayList.get(currentQuestionIndex).getQuestion());
                counterText.setText((currentQuestionIndex+1) + "/" + questionList.size());
            }
        });
    }

    @Override
    protected void onPause() {
        prefs.saveHighscore(highscore);
        prefs.setState(currentQuestionIndex);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.previous_button:
                if(currentQuestionIndex > 0){
                    currentQuestionIndex = (currentQuestionIndex - 1)% questionList.size();
                    updateQuestion();
                }
                break;
            case R.id.next_button:
                goNext();
                break;
            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;
        }
    }

    private void checkAnswer(boolean choseCorrect){
        boolean answer= questionList.get(currentQuestionIndex).isAnswerTrue();
        if(choseCorrect == answer){
            fade();
            score += 100;
            updateHighScore();
        }else{
            shake();
            if(score > 0){
                score -= 100;
            }
        }
    }

    private void updateHighScore(){
        if(score > highscore){
            highscore = score;
        }
    }

    private void updateQuestion(){
        questionText.setText(questionList.get(currentQuestionIndex).getQuestion());
        counterText.setText((currentQuestionIndex+1) + "/" + questionList.size());
        scoreText.setText("Score: " + score);
        highscoreText.setText("Highscore: "+ highscore);

    }

    private void goNext(){
        currentQuestionIndex = (currentQuestionIndex + 1)% questionList.size();
        updateQuestion();
    }

    private void fade(){
        final CardView cardView= findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation= new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shake(){
        Animation shake= AnimationUtils.loadAnimation(this, R.anim.shake_animation);
        final CardView cardView= findViewById(R.id.cardView);
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
