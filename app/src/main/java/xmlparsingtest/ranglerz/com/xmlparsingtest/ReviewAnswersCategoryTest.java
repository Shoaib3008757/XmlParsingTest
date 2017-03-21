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
import xmlparsingtest.ranglerz.com.xmlparsingtest.CustomAdapters.CustomAdapterReviewAnswersCategoryTest;

public class ReviewAnswersCategoryTest extends AppCompatActivity {


    ListView reviewAnswersCategoryTest ;
    CustomAdapterReviewAnswersCategoryTest customAdapterReviewAnswersCategoryTest;

    UserChoiceTakeTest userChoiceTakeTest;
    String userAnswerChoice = null;
    String correctAnswer = null;
    String correctImage = null;
    String questionImage = null;
    int questionNumber = 0;

    public static final String TAG ="Review Answers Category";
    public static final int SCROLLING_FACTOR = 40;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_answers_category_test);


        userChoiceTakeTest = (UserChoiceTakeTest) getIntent().getSerializableExtra("object");
        Log.d(TAG, "Total Correct Answer is" + userChoiceTakeTest.numberOfCorrectAnswers);


        reviewAnswersCategoryTest = (ListView) findViewById(R.id.listViewReviewAnswersCategoryTest);
        customAdapterReviewAnswersCategoryTest = new CustomAdapterReviewAnswersCategoryTest(getApplicationContext(),R.layout.single_item_listview,userChoiceTakeTest);
        reviewAnswersCategoryTest.setAdapter(customAdapterReviewAnswersCategoryTest);

        // make listview scroll to slow down
        reviewAnswersCategoryTest.setFriction(ViewConfiguration.getScrollFriction() * SCROLLING_FACTOR);



        reviewAnswersCategoryTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                correctAnswer = CategoryTest.randomCategoryQuestions.get(position).getCorrectAnswer().toString().trim();
                correctImage = CategoryTest.randomCategoryQuestions.get(position).getCorrectImage().toString().trim();
                questionImage = CategoryTest.randomCategoryQuestions.get(position).getQuestionImage().toString().trim();
                userAnswerChoice = userChoiceTakeTest.selectedAnswerText.get(position).toString().trim();
                questionNumber = position + 1;

                Log.d(TAG, " Correct Answer is " + correctAnswer + " User Answer is " + userAnswerChoice + "Question number is " + questionNumber);


                // make a new activity to display the current question with user choice answer and also it's correct answer

                Intent i = new Intent(ReviewAnswersCategoryTest.this, ShowAnswerCategoryTest.class);
                i.putExtra("correctAnswer", correctAnswer);
                i.putExtra("correctImage", correctImage);
                i.putExtra("userAnswerChoice", userAnswerChoice);
                i.putExtra("position", position);
                i.putExtra("questionNumber", questionNumber);
                i.putExtra("questionImage", questionImage);
                startActivity(i);


            }
        });



    }

}
