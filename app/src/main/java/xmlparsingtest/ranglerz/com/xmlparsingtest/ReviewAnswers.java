package xmlparsingtest.ranglerz.com.xmlparsingtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;

import xmlparsingtest.ranglerz.com.xmlparsingtest.CustomAdapters.CustomAdapterReviewAnswers;

public class ReviewAnswers extends AppCompatActivity {

    ListView reviewAnswers ;
    CustomAdapterReviewAnswers customAdapterReviewAnswers;

    UserChoiceTakeTest userChoiceTakeTest;
    String userAnswerChoice = null;
    String correctAnswer = null;
    String correctImage = null;
    String questionImage = null;
    int questionNumber = 0;


    public static final String TAG ="Review Answers";
    public static final int SCROLLING_FACTOR = 32;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_answers);

        userChoiceTakeTest = (UserChoiceTakeTest) getIntent().getSerializableExtra("object");
        Log.d(TAG,"Total Correct Answer is "+userChoiceTakeTest.numberOfCorrectAnswers);


        reviewAnswers = (ListView) findViewById(R.id.listViewReviewAnswers);
        customAdapterReviewAnswers = new CustomAdapterReviewAnswers(getApplicationContext(),R.layout.single_item_listview,userChoiceTakeTest);
        reviewAnswers.setAdapter(customAdapterReviewAnswers);

        // make listview scroll to slow down
        reviewAnswers.setFriction(ViewConfiguration.getScrollFriction() * SCROLLING_FACTOR);


        reviewAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                correctAnswer = Splash.randomQuestions.get(position).getCorrectAnswer().toString().trim();
                correctImage = Splash.randomQuestions.get(position).getCorrectImage().toString().trim();
                questionImage = Splash.randomQuestions.get(position).getQuestionImage().toString().trim();
                userAnswerChoice = userChoiceTakeTest.selectedAnswerText.get(position).toString().trim();
                questionNumber = position +1;

                Log.d(TAG," Correct Answer is "+correctAnswer+" User Answer is "+userAnswerChoice +"Question number is "+questionNumber);


                // make a new activity to display the current question with user choice answer and also it's correct answer

                Intent i = new Intent(ReviewAnswers.this,ShowAnswer.class);
                i.putExtra("correctAnswer",correctAnswer);
                i.putExtra("correctImage",correctImage);
                i.putExtra("userAnswerChoice",userAnswerChoice);
                i.putExtra("position",position);
                i.putExtra("questionNumber",questionNumber);
                i.putExtra("questionImage",questionImage);
                startActivity(i);



            }
        });



    }

}
