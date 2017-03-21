package xmlparsingtest.ranglerz.com.xmlparsingtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;

import xmlparsingtest.ranglerz.com.xmlparsingtest.CustomAdapters.CustomAdapterLearning;
import xmlparsingtest.ranglerz.com.xmlparsingtest.CustomAdapters.CustomAdapterReviewAnswers;

/**
 * Created by User-10 on 03-Nov-16.
 */
public class ReviewAnswerLearning extends AppCompatActivity {



    ListView reviewAnswersLearning ;
    CustomAdapterLearning customAdapterReviewAnswers;

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
        setContentView(R.layout.review_answer_learning);

        userChoiceTakeTest = (UserChoiceTakeTest) getIntent().getSerializableExtra("object");

        Log.d(TAG, "Total Correct Answer is " + userChoiceTakeTest.numberOfCorrectAnswers);


        reviewAnswersLearning = (ListView) findViewById(R.id.listViewReviewAnswersLearning);
        customAdapterReviewAnswers = new CustomAdapterLearning(getApplicationContext(),R.layout.single_item_listview,userChoiceTakeTest,0);
        reviewAnswersLearning.setAdapter(customAdapterReviewAnswers);

        // make listview scroll to slow down
        reviewAnswersLearning.setFriction(ViewConfiguration.getScrollFriction() * SCROLLING_FACTOR);


        reviewAnswersLearning.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                correctAnswer = LearnByChapter.randomLearningQuestions.get(position).getCorrectAnswer().toString().trim();
                correctImage = LearnByChapter.randomLearningQuestions.get(position).getCorrectImage().toString().trim();

                // check for image null because we are dealing with caseStudy that have no questions..
                if(LearnByChapter.randomLearningQuestions.get(position).getQuestionImage() !=null) {
                    questionImage = LearnByChapter.randomLearningQuestions.get(position).getQuestionImage().toString().trim();
                }
                userAnswerChoice = userChoiceTakeTest.selectedAnswerText.get(position).toString().trim();
                questionNumber = position +1;

                Log.d(TAG," Correct Answer is "+correctAnswer+" User Answer is "+userAnswerChoice +"Question number is "+questionNumber);


                // make a new activity to display the current question with user choice answer and also it's correct answer

                Intent i = new Intent(ReviewAnswerLearning.this,ShowAnswerLearning.class);
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
