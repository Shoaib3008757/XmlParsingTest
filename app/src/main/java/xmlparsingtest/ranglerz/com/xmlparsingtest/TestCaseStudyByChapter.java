package xmlparsingtest.ranglerz.com.xmlparsingtest;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daasuu.ahp.AnimateHorizontalProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Category;

/**
 * Created by User-10 on 08-Nov-16.
 */
public class TestCaseStudyByChapter extends AppCompatActivity implements TextToSpeech.OnInitListener{



    public static final int TEST_PASS_LIMIT = LearnByChapter.randomLearningQuestions.size() - 10 ;
    public static int RANDOM_LIST_SIZE = LearnByChapter.randomLearningQuestions.size();
    private static final String TAG = "Random Category Test";
    ArrayList<Category> selectedCategories = null;



    AnimateHorizontalProgressBar progressBar;

    // reference variables for views...
    TextView questionCategory , questionNumber , timer , questionText;
    ImageButton speackButton, speackButtonAsnwer1, speackButtonAsnwer2, speackButtonAsnwer3, speackButtonAsnwer4;
    String text, textAnswer1, textAnswer2, textAnswer3, textAnswer4;
    private TextToSpeech tts;

    Button answer1,answer2,answer3,answer4,previous,flag,next;

    ImageView answerImage1,answerImage2,answerImage3,answerImage4;

    // variable for storing current state of the question

    int currentQuestion = 0;


    UserChoiceTakeTest userChoiceTakeTest;

    boolean userSelectAnswer = false;
    boolean imageShowing = false;

    // to store the correct Answer of the Current Question..
    String correctAnswer = null;

    String correctImage = null;

    String imageName , imageName1 , imageName2 , imageName3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_random_test);

        // receive selected categories from Category Test Class...
        Intent in = getIntent();
        selectedCategories = (ArrayList<Category>) in.getSerializableExtra("selectedCategories");


        Log.d(TAG, "Selected Categories size is  " + LearnByChapter.randomLearningQuestions.size());

        questionCategory = (TextView) findViewById(R.id.questionCategory);
        questionText = (TextView) findViewById(R.id.questionText);
        tts = new TextToSpeech(this, this);
        speackButton = (ImageButton) findViewById(R.id.speack_button);
        speackButtonAsnwer1 = (ImageButton) findViewById(R.id.bt1_speaker);
        speackButtonAsnwer2 = (ImageButton) findViewById(R.id.bt2_speaker);
        speackButtonAsnwer3 = (ImageButton) findViewById(R.id.bt3_speaker);
        speackButtonAsnwer4 = (ImageButton) findViewById(R.id.bt4_speaker);


        questionNumber = (TextView) findViewById(R.id.questionNumber);
        timer = (TextView) findViewById(R.id.timer);
        timer.setVisibility(View.INVISIBLE);


        answer1 = (Button) findViewById(R.id.buttonAnswer1);
        answer2 = (Button) findViewById(R.id.buttonAnswer2);
        answer3 = (Button) findViewById(R.id.buttonAnswer3);
        answer4 = (Button) findViewById(R.id.buttonAnswer4);
        flag = (Button) findViewById(R.id.buttonFlag);
        flag.setText("Favourite");





        answerImage1 = (ImageView) findViewById(R.id.imageViewAnswer1);
        answerImage2 = (ImageView) findViewById(R.id.imageViewAnswer2);
        answerImage3 = (ImageView) findViewById(R.id.imageViewAnswer3);
        answerImage4 = (ImageView) findViewById(R.id.imageViewAnswer4);

        answerImage1.setVisibility(View.GONE);
        answerImage2.setVisibility(View.GONE);
        answerImage3.setVisibility(View.GONE);
        answerImage4.setVisibility(View.GONE);

        speackButtonAsnwer1.setVisibility(View.VISIBLE);
        speackButtonAsnwer2.setVisibility(View.VISIBLE);
        speackButtonAsnwer3.setVisibility(View.VISIBLE);
        speackButtonAsnwer4.setVisibility(View.VISIBLE);


        userChoiceTakeTest = new UserChoiceTakeTest();


        progressBar = (AnimateHorizontalProgressBar) findViewById(R.id.animate_progress_bar);
        progressBar.setMax(RANDOM_LIST_SIZE);
        progressBar.setProgress(currentQuestion);

        // use touch listener for button pressed state

        answer1.setOnTouchListener(new View.OnTouchListener() {



            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.d(TAG,"Button Touch Call");



                // set the selection image becuase we are displaying images..
                // we donot want to set his background..
                if(imageShowing)
                {

                    // answer button is pressed set it to true...
                    answer1.setPressed(true);
                    answer2.setPressed(false);
                    answer3.setPressed(false);
                    answer4.setPressed(false);

                    answerImage1.setImageResource(R.drawable.ic_action_tick);
                    answerImage2.setImageResource(0);
                    answerImage3.setImageResource(0);
                    answerImage4.setImageResource(0);
                    // user select the answer set it to true
                    userSelectAnswer = true;
                    return  true;
                }




                answer1.setPressed(true);
                answer2.setPressed(false);
                answer3.setPressed(false);
                answer4.setPressed(false);

                answer1.setBackgroundResource(R.drawable.button);

                // user select the answer set it to true
                userSelectAnswer = true;



                return true;
            }
        });

        answer2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "Button Touch Call");


                // set the selection image becuase we are displaying images..
                // we donot want to set his background..
                if(imageShowing)
                {

                    answer1.setPressed(false);
                    answer2.setPressed(true);
                    answer3.setPressed(false);
                    answer4.setPressed(false);


                    answerImage1.setImageResource(0);
                    answerImage2.setImageResource(R.drawable.ic_action_tick);
                    answerImage3.setImageResource(0);
                    answerImage4.setImageResource(0);
                    // user select the answer set it to true
                    userSelectAnswer = true;
                    return  true;
                }



                answer1.setPressed(false);
                answer2.setPressed(true);
                answer3.setPressed(false);
                answer4.setPressed(false);

                answer2.setBackgroundResource(R.drawable.button);
                // user select the answer set it to true
                userSelectAnswer = true;


                return true;
            }
        });
        answer3.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.d(TAG, "Button Touch Call");


                // set the selection image becuase we are displaying images..
                // we donot want to set his background..
                if(imageShowing)
                {

                    answer1.setPressed(false);
                    answer2.setPressed(false);
                    answer3.setPressed(true);
                    answer4.setPressed(false);

                    answerImage1.setImageResource(0);
                    answerImage2.setImageResource(0);
                    answerImage3.setImageResource(R.drawable.ic_action_tick);
                    answerImage4.setImageResource(0);

                    // user select the answer set it to true
                    userSelectAnswer = true;
                    return  true;
                }





                answer1.setPressed(false);
                answer2.setPressed(false);
                answer3.setPressed(true);
                answer4.setPressed(false);

                answer3.setBackgroundResource(R.drawable.button);
                // user select the answer set it to true
                userSelectAnswer = true;


                return true;
            }
        });
        answer4.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.d(TAG, "Button Touch Call");


                // set the selection image becuase we are displaying images..
                // we donot want to set his background..
                if(imageShowing)
                {

                    answer1.setPressed(false);
                    answer2.setPressed(false);
                    answer3.setPressed(false);
                    answer4.setPressed(true);


                    answerImage1.setImageResource(0);
                    answerImage2.setImageResource(0);
                    answerImage3.setImageResource(0);
                    answerImage4.setImageResource(R.drawable.ic_action_tick);

                    // user select the answer set it to true
                    userSelectAnswer = true;
                    return  true;
                }





                answer1.setPressed(false);
                answer2.setPressed(false);
                answer3.setPressed(false);
                answer4.setPressed(true);

                answer4.setBackgroundResource(R.drawable.button);
                // user select the answer set it to true
                userSelectAnswer = true;


                return true;
            }
        });



        displayQuestionAndAnswer();



    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void displayQuestionAndAnswer()
    {

        flag.setText("Favourite");

        Log.d(TAG, "displayQuestionAndAnswer: ");

        // display the current question
        // then display all the answer of that question...

        Log.d(TAG, "Current Question is " + currentQuestion);
        Log.d(TAG, "Question id is " + LearnByChapter.randomLearningQuestions.get(currentQuestion).getQuestionId());
        Log.d(TAG, "Answers List size is " + LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().size());




        // this is the area where we receive empty answer
        // and we have images as answer to show ..
        if( LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(0).getAnswerText().isEmpty()) {

            // set the flag to true
            imageShowing = true;

            // set the images to visible to show small image as selection of the button background image..
            answerImage1.setVisibility(View.VISIBLE);
            answerImage2.setVisibility(View.VISIBLE);
            answerImage3.setVisibility(View.VISIBLE);
            answerImage4.setVisibility(View.VISIBLE);

            speackButtonAsnwer1.setVisibility(View.GONE);
            speackButtonAsnwer2.setVisibility(View.GONE);
            speackButtonAsnwer3.setVisibility(View.GONE);
            speackButtonAsnwer4.setVisibility(View.GONE);




            // show the images for the answer

            questionText.setText(LearnByChapter.randomLearningQuestions.get(currentQuestion).getQuestionText().toString() +"\n"+ "\n"
            +LearnByChapter.randomLearningQuestions.get(currentQuestion).getQuestionExplaination().toString()
            );
            text = LearnByChapter.randomLearningQuestions.get(currentQuestion).getQuestionText().toString() +"\n"+ "\n"
                    +LearnByChapter.randomLearningQuestions.get(currentQuestion).getQuestionExplaination().toString();
            speackButtonClickListener(text);

            questionCategory.setText(LearnByChapter.randomLearningQuestions.get(currentQuestion).getTopic().toString());

            correctImage = LearnByChapter.randomLearningQuestions.get(currentQuestion).getCorrectImage();

            imageName = LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(0).getImageName().toString().trim();
            Log.d(TAG, "ImageName is" + imageName);
            imageName1 = LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(1).getImageName().toString().trim();
            Log.d(TAG,"ImageName is"+imageName1);
            imageName2 = LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(2).getImageName().toString().trim();
            Log.d(TAG, "ImageName is"+imageName2);
            imageName3 = LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(3).getImageName().toString().trim();
            Log.d(TAG,"ImageName is"+imageName3);


            Log.d(TAG,"Correct Image is "+correctImage);



            // remove the text from the buttons
            answer1.setText("");
            answer2.setText("");
            answer3.setText("");
            answer4.setText("");



            // load image to background of the button
            // set the tag is use for storing information with the view
            // here we are storing image name to retreive the name of the image..
            // when images are shown...
            try {

                // get input stream
                InputStream ims = getAssets().open("images/"+imageName);
                // load image as Drawable
                Drawable d = Drawable.createFromStream(ims, null);
                answer1.setTag(imageName);
                answer1.setBackground(d);

                // get input stream
                InputStream ims1 = getAssets().open("images/"+imageName1);
                // load image as Drawable
                Drawable d1 = Drawable.createFromStream(ims1, null);
                answer2.setTag(imageName1);
                answer2.setBackground(d1);


                // get input stream
                InputStream ims2 = getAssets().open("images/"+imageName2);
                // load image as Drawable
                Drawable d2 = Drawable.createFromStream(ims2, null);
                answer3.setTag(imageName2);
                answer3.setBackground(d2);


                // get input stream
                InputStream ims3 = getAssets().open("images/"+imageName3);
                // load image as Drawable
                Drawable d3 = Drawable.createFromStream(ims3, null);
                answer4.setTag(imageName3);
                answer4.setBackground(d3);


            }


            catch(IOException ex) {
                Log.d(TAG,"Exception occure"+ex);
            }

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            // for check the DPI(Dot per inch ) of the device
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int densityDpi = (int) (metrics.density * 160f);
            Toast.makeText(getApplicationContext(),"Device dpi is "+densityDpi,Toast.LENGTH_SHORT).show();





            // TODO: Here we changes the answer image / button size..
            // because we have to make button small becuase images are small
            // get the screen dpi and width and height ..
            // then set the margin and button size..with these dimesions...

            if (densityDpi > 560) {

                // for 7 inch
                if (width >= 710 && width <= 950) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,200,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);


                }

                // for 10 inch

                else if (width >= 1000&& width <= 1390) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250,250);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,350,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);


                }

                // for 10 inch

                else if (width >= 1400) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(320,320);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,550,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);

                }
                else {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,200,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);



                }


            } else if (densityDpi > 480) {

                // for 7 inch
                if (width >= 710 && width <= 950) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,200,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);

                }

                // for 10 inch

                else if (width >= 1000&& width <= 1390) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250,250);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,350,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);
                }

                // for 10 inch
                else if (width >= 1400) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(320,320);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,550,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);

                }
                else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,200,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);
                }


            } else if (densityDpi > 420) {



                // for 7 inch
                if (width >= 710 && width <= 950) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,200,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);

                }


                // for 10 inch

                else if (width >= 1000 && width <= 1390) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250,250);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,350,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);


                } else if (width >= 1400) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(320,320);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,550,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);


                } else {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,200,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);


                }


            } else if (densityDpi > 320) {

                // for 5 inch
                if (width >= 500 && width <= 590) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,200,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);

                }

                // for 6 inch
                else if (width >= 600 && width <= 700) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,200,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);

                }

                // for 7 inch
                else if (width >= 710 && width <= 950) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,200,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);


                }


                // for 10 inch

                else if (width >= 1000 && width <= 1390) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250,250);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,350,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);

                }


                // for 10 inch

                else if (width >= 1400) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300,300);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,500,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);

                } else {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,200,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);

                }


            } else if (densityDpi > 240) {

                // for 5 inch
                if (width >= 500 && width <= 590) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,200,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);


                }

                // for 6 inch
                else if (width >= 600 && width <= 700) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120,120);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,200,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);

                }

                // for 7 inch
                else if (width >= 710 && width <= 950) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150,150);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,250,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);

                }

                // for 10 inch

                else if (width >= 1000) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250,250);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,400,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);



                } else {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150,150);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,150,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);


                }


            }
            else if (densityDpi <= 240) {

                // for 5 inch
                if (width >= 500 && width <= 590) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150,150);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,200,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);


                }

                // for 6 inch
                else if (width >= 600 && width <= 700) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200,200);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,250,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);


                }

                // for 7 inch
                else if (width >= 710 && width <= 950) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200,200);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,250,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);


                }

                // for 10 inch

                else if (width >= 1000) {


                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300,300);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,350,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);


                } else {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150,150);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(0,0,150,0);
                    answer1.setLayoutParams(params);
                    answer2.setLayoutParams(params);
                    answer3.setLayoutParams(params);
                    answer4.setLayoutParams(params);



                }


            }


            Log.d(TAG,"Empty Answer show the images of the answer");

            return;

        }



        // set layout paramaert to default..

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(8,8,8,8);
        answer1.setLayoutParams(params);
        answer2.setLayoutParams(params);
        answer3.setLayoutParams(params);
        answer4.setLayoutParams(params);


        // hide the imageViews...

        answerImage1.setVisibility(View.GONE);
        answerImage2.setVisibility(View.GONE);
        answerImage3.setVisibility(View.GONE);
        answerImage4.setVisibility(View.GONE);

        speackButtonAsnwer1.setVisibility(View.VISIBLE);
        speackButtonAsnwer2.setVisibility(View.VISIBLE);
        speackButtonAsnwer3.setVisibility(View.VISIBLE);
        speackButtonAsnwer4.setVisibility(View.VISIBLE);




        // increment the currentQuestion Number to display in TextView for Question number out off 50 Questions..
        int questionNumberIncremented = currentQuestion +1;


        // store the correct Answer in variable to
        // compare with userChoice

        correctAnswer =  LearnByChapter.randomLearningQuestions.get(currentQuestion).getCorrectAnswer().toString().trim();

        // seting the question text
        // then question category
        // then question number is displaying..
        questionText.setText(LearnByChapter.randomLearningQuestions.get(currentQuestion).getQuestionText().toString()
                +"\n"+ "\n"
                +LearnByChapter.randomLearningQuestions.get(currentQuestion).getQuestionExplaination().toString()
        );

        text = LearnByChapter.randomLearningQuestions.get(currentQuestion).getQuestionText().toString()
                +"\n"+ "\n"
                +LearnByChapter.randomLearningQuestions.get(currentQuestion).getQuestionExplaination().toString();
        speackButtonClickListener(text);


        questionCategory.setText(LearnByChapter.randomLearningQuestions.get(currentQuestion).getTopic().toString());
        questionNumber.setText("Question " + questionNumberIncremented + " off " + LearnByChapter.randomLearningQuestions.size());


        /// setting the answer of the questions...
        answer1.setText(LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(0).getAnswerText());
        textAnswer1 = LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(0).getAnswerText().toString();
        ans1ButtonSpeech(textAnswer1);


        answer2.setText(LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(1).getAnswerText());
        textAnswer2 = LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(0).getAnswerText().toString();
        ans2ButtonSpeech(textAnswer2);


        answer3.setText(LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(2).getAnswerText());
        textAnswer3 = LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(0).getAnswerText().toString();
        ans3ButtonSpeech(textAnswer3);


        answer4.setText(LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(3).getAnswerText());
        textAnswer4 = LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(3).getAnswerText().toString();
        ans4ButtonSpeech(textAnswer4);








    }



    public void flagButtonClick(View v)
    {
        // TODO: Favourite the question in the database and practice favourite Questions
        if(flag.getText().toString().equals("Favourite")) {
            flag.setText("NonFavourite");

            String questionToFavourite = LearnByChapter.randomLearningQuestions.get(currentQuestion).getQuestionId();

            Log.d(TAG, "flagButtonClick: Question id is"+questionToFavourite);

            // TODO: set the question flag to 1 in database to make it favourite for the user

            boolean isUpdated = Splash.dbHelperClass.insertFavouriteQuestion(questionToFavourite);
            Log.d(TAG, "flagButtonClick: Rows Updated" + isUpdated);

        }
        else if(flag.getText().toString().equals("NonFavourite"))
        {
            flag.setText("Favourite");
        }




    }






    public void previousButtonClick(View v)
    {

        Log.d(TAG, "previousButtonClick: ");

        if(currentQuestion == 0)
        {
            return;
        }

        currentQuestion --;

        // increment the progress bar
        progressBar.setProgress(currentQuestion);


        answer1.setBackgroundResource(R.drawable.button);
        answer2.setBackgroundResource(R.drawable.button);
        answer3.setBackgroundResource(R.drawable.button);
        answer4.setBackgroundResource(R.drawable.button);




        // call display question and answer method
        // to display the question and answers...

        displayQuestionAndAnswer();

        // this method get the selected answers from user
        // and display there selection on the Screen..

        getSelectedAnswerAndDisplayToUser();


    }







    public void nextButtonClick(View v) throws IOException {

        // if user don't select any answer return from there and show message...
        if(!userSelectAnswer)
        {
            Toast.makeText(getApplicationContext(), "Please select one Answer", Toast.LENGTH_SHORT).show();
            return;
        }

        // increment the progress bar
        progressBar.setProgress(currentQuestion);

        if(imageShowing)
        {

            Log.d(TAG, "nextButtonClick: In Image Showing "+imageShowing);


            // check which button is pressed by the user
            if(answer1.isPressed())
            {

                // get the tag from the button and store it in user choiceAnswer
                String imageName =  answer1.getTag().toString().trim();
                userChoiceTakeTest.selectedAnswerText.put(currentQuestion, imageName);
                Log.d(TAG, "nextButtonClick: User Answer is "+ userChoiceTakeTest.selectedAnswerText.get(currentQuestion));

                if(imageName.equals(correctImage))
                {
                    Log.d(TAG, " You select correct Image Name is" + imageName);

                    userChoiceTakeTest.numberOfCorrectAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfWrongAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfWrongAnswers.remove(currentQuestion);


                    showOption(3);

                }
                else
                {
                    Log.d(TAG,"Wrong Image");
                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfCorrectAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfCorrectAnswers.remove(currentQuestion);

                    showOption(2);
                }

            }
            else if(answer2.isPressed())
            {

                String imageName =  answer2.getTag().toString().trim();
                userChoiceTakeTest.selectedAnswerText.put(currentQuestion, imageName);
                Log.d(TAG, "nextButtonClick: User Answer is "+ userChoiceTakeTest.selectedAnswerText.get(currentQuestion));

                if(imageName.equals(correctImage))
                {
                    Log.d(TAG,"Correct Image Name is"+imageName);
                    userChoiceTakeTest.numberOfCorrectAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfWrongAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfWrongAnswers.remove(currentQuestion);


                    showOption(3);
                }
                else
                {
                    Log.d(TAG,"Wrong Image");
                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfCorrectAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfCorrectAnswers.remove(currentQuestion);

                    showOption(2);
                }

            }
            else if(answer3.isPressed())
            {

                String imageName = answer3.getTag().toString().trim();
                userChoiceTakeTest.selectedAnswerText.put(currentQuestion, imageName);
                Log.d(TAG, "nextButtonClick: User Answer is "+ userChoiceTakeTest.selectedAnswerText.get(currentQuestion));

                if(imageName.equals(correctImage))
                {
                    Log.d(TAG,"Correct Image Name is"+imageName);
                    userChoiceTakeTest.numberOfCorrectAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfWrongAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfWrongAnswers.remove(currentQuestion);


                    showOption(3);

                }
                else
                {
                    Log.d(TAG,"Wrong Image");
                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfCorrectAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfCorrectAnswers.remove(currentQuestion);

                    showOption(2);
                }

            }
            else if(answer4.isPressed())
            {
                String imageName =  answer4.getTag().toString().trim();
                userChoiceTakeTest.selectedAnswerText.put(currentQuestion, imageName);
                Log.d(TAG, "nextButtonClick: User Answer is "+ userChoiceTakeTest.selectedAnswerText.get(currentQuestion));

                if(imageName.equals(correctImage))
                {
                    Log.d(TAG,"Correct Image Name is"+imageName);
                    userChoiceTakeTest.numberOfCorrectAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfWrongAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfWrongAnswers.remove(currentQuestion);


                    showOption(3);
                }
                else
                {
                    Log.d(TAG,"Wrong Image");
                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfCorrectAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfCorrectAnswers.remove(currentQuestion);

                    showOption(2);
                }

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Select Any Answer",Toast.LENGTH_SHORT).show();
            }


//            answer1.setBackgroundResource(0);
//            answer2.setBackgroundResource(0);
//            answer3.setBackgroundResource(0);
//            answer4.setBackgroundResource(0);
//
//
//            imageShowing = false;


        }
        else {

            Log.d(TAG, "nextButtonClick: in text area imageShowing"+imageShowing);

            // now compute the result of user

            answer1.setBackgroundResource(R.drawable.button);
            answer2.setBackgroundResource(R.drawable.button);
            answer3.setBackgroundResource(R.drawable.button);
            answer4.setBackgroundResource(R.drawable.button);



            if (answer1.isPressed()) {


                String userAnswer = answer1.getText().toString().trim();
                userChoiceTakeTest.selectedAnswerText.put(currentQuestion, userAnswer);

                if (correctAnswer.equals(userAnswer)) {

                    Log.d(TAG, "Correct Answer ");

                    userChoiceTakeTest.numberOfCorrectAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfWrongAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfWrongAnswers.remove(currentQuestion);


                    showOption(1);

                } else {
                    Log.d(TAG, "Wrong Answer ");

                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfCorrectAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfCorrectAnswers.remove(currentQuestion);

                    showOption(0);
                }
                Log.d(TAG, "Correct Answer is " + correctAnswer + " user anser is " + userAnswer);
            } else if (answer2.isPressed()) {

                String userAnswer = answer2.getText().toString().trim();
                userChoiceTakeTest.selectedAnswerText.put(currentQuestion, userAnswer);

                if (correctAnswer.equals(userAnswer)) {
                    Log.d(TAG, "Correct Answer ");

                    userChoiceTakeTest.numberOfCorrectAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfWrongAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfWrongAnswers.remove(currentQuestion);

                    showOption(1);

                } else {
                    Log.d(TAG, "Wrong Answer ");

                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfCorrectAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfCorrectAnswers.remove(currentQuestion);

                    showOption(0);
                }

                Log.d(TAG, "Correct Answer is " + correctAnswer + " user anser is " + userAnswer);
            } else if (answer3.isPressed()) {


                String userAnswer = answer3.getText().toString().trim();
                userChoiceTakeTest.selectedAnswerText.put(currentQuestion, userAnswer);
                if (correctAnswer.equals(userAnswer)) {
                    Log.d(TAG, "Correct Answer ");

                    userChoiceTakeTest.numberOfCorrectAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfWrongAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfWrongAnswers.remove(currentQuestion);

                    showOption(1);

                } else {
                    Log.d(TAG, "Wrong Answer ");

                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfCorrectAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfCorrectAnswers.remove(currentQuestion);

                    showOption(0);
                }

                Log.d(TAG, "Correct Answer is " + correctAnswer + " user anser is " + userAnswer);


            } else if (answer4.isPressed()) {
                String userAnswer = answer4.getText().toString().trim();

                userChoiceTakeTest.selectedAnswerText.put(currentQuestion, userAnswer);

                if (correctAnswer.equals(userAnswer)) {
                    Log.d(TAG, "Correct Answer ");

                    userChoiceTakeTest.numberOfCorrectAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfWrongAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfWrongAnswers.remove(currentQuestion);

                    showOption(1);

                } else {
                    Log.d(TAG, "Wrong Answer ");

                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
                    // remove the answer from the list...
                    if(userChoiceTakeTest.numberOfCorrectAnswers.containsKey(currentQuestion))
                        userChoiceTakeTest.numberOfCorrectAnswers.remove(currentQuestion);

                    showOption(0);
                }

                Log.d(TAG, "Correct Answer is " + correctAnswer + " user answer is " + userAnswer);
            }








        }








    }





    // TODO this method is used to go to next question after correct selection of the question...

    public void goToNextQuestion()
    {


        Log.d(TAG, "goToNextQuestion: "+imageShowing);


        answer1.setBackgroundResource(R.drawable.button);
        answer2.setBackgroundResource(R.drawable.button);
        answer3.setBackgroundResource(R.drawable.button);
        answer4.setBackgroundResource(R.drawable.button);


        imageShowing = false;


        // increment the question to fetch next question
        // from list of random questions..
        currentQuestion ++;


        // after adding the correct and wrong answer
        // check if the questions end or not
        if(currentQuestion >= LearnByChapter.randomLearningQuestions.size())
        {
            // ((Button)v).setText("Finish");

            Log.d(TAG, "Number of Correct Answer is " + userChoiceTakeTest.numberOfCorrectAnswers.size());
            Log.d(TAG, "Number of Wrong Answer is " + userChoiceTakeTest.numberOfWrongAnswers.size());


            ShowUserProgressDialog();



            return;
        }

        Log.d(TAG,"Current Question Number is "+currentQuestion);










        // now reset all button pressed state
        // because we are moving to next question
        // so a answer must be selected in order to proceed the question

        answer1.setPressed(false);
        answer2.setPressed(false);
        answer3.setPressed(false);
        answer4.setPressed(false);


        // also reset the answer selected state of the user
        // because we are moving to next question
        // so a answer must be selected in order to proceed the question

        userSelectAnswer = false;



        imageShowing = false;




        // call display question and answer method
        // to display the question and answers...

        displayQuestionAndAnswer();

//        // this method get the selected answers from user
//        // and display there selection on the Screen..
//
        getSelectedAnswerAndDisplayToUserForNextButton();



    }


    // TODO: this method is used to show the correct answer when user selects the Show answer option
    // TODO: this method is used to show the correct answer if it is image or answer
    // TODO: if 0 or 1 then Text display not image
    // TODO: if 2 or 3 then Display Image not text
    public void showAnswer(int showImageOrAnswer){



        if((showImageOrAnswer == 0) || (showImageOrAnswer == 1)) {

            Log.d(TAG, "showAnswer:  Answer"+imageShowing);


            String userAnswerChoice = userChoiceTakeTest.selectedAnswerText.get(currentQuestion).toString().trim();
            Log.d(TAG, "showAnswer: User choice is  " + userAnswerChoice);
            String correctAnswer = LearnByChapter.randomLearningQuestions.get(currentQuestion).getCorrectAnswer().toString().trim();
            Log.d(TAG, "showAnswer: CorrectAnswer is  " + correctAnswer);

            // to show border around images
            GradientDrawable greenColorBorder = new GradientDrawable();
            greenColorBorder.setColor(getResources().getColor(R.color.android_green_color)); // Changes this drawbale to use a single color instead of a gradient
            greenColorBorder.setCornerRadius(5);
            greenColorBorder.setStroke(1, getResources().getColor(R.color.android_green_color));


            // to show border around images
            GradientDrawable redColorBorder = new GradientDrawable();
            redColorBorder.setColor(getResources().getColor(R.color.android_red_color)); // Changes this drawbale to use a single color instead of a gradient
            redColorBorder.setCornerRadius(5);
            redColorBorder.setStroke(1, getResources().getColor(R.color.android_red_color));


            // if user has correct answer then show green box
            // otherwise show red box that indicates its wrong answer

            if (userAnswerChoice.equals(correctAnswer)) {
                if (answer1.getText().toString().trim().equals(userAnswerChoice)) {
                    answer1.setBackgroundDrawable(greenColorBorder);
                } else if (answer2.getText().toString().trim().equals(userAnswerChoice)) {
                    answer2.setBackgroundDrawable(greenColorBorder);
                } else if (answer3.getText().toString().trim().equals(userAnswerChoice)) {
                    answer3.setBackgroundDrawable(greenColorBorder);
                } else if (answer4.getText().toString().trim().equals(userAnswerChoice)) {
                    answer4.setBackgroundDrawable(greenColorBorder);
                }
            } else {
                if (answer1.getText().toString().trim().equals(userAnswerChoice)) {
                    answer1.setBackgroundDrawable(redColorBorder);
                } else if (answer2.getText().toString().trim().equals(userAnswerChoice)) {
                    answer2.setBackgroundDrawable(redColorBorder);
                } else if (answer3.getText().toString().trim().equals(userAnswerChoice)) {
                    answer3.setBackgroundDrawable(redColorBorder);
                } else if (answer4.getText().toString().trim().equals(userAnswerChoice)) {
                    answer4.setBackgroundDrawable(redColorBorder);
                }

            }


            // show the actual correct answer of the relevant question...
            // and show the green box around it..
            if (answer1.getText().toString().trim().equals(correctAnswer)) {
                answer1.setBackgroundDrawable(greenColorBorder);
            } else if (answer2.getText().toString().trim().equals(correctAnswer)) {
                answer2.setBackgroundDrawable(greenColorBorder);
            } else if (answer3.getText().toString().trim().equals(correctAnswer)) {
                answer3.setBackgroundDrawable(greenColorBorder);
            } else if (answer4.getText().toString().trim().equals(correctAnswer)) {
                answer4.setBackgroundDrawable(greenColorBorder);
            }


        }

        else if((showImageOrAnswer == 2) || (showImageOrAnswer == 3))
        {

            imageShowing = true ;

            Log.d(TAG, "showAnswer: Image Showing"+imageShowing);



            String userAnswerChoice = userChoiceTakeTest.selectedAnswerText.get(currentQuestion).toString().trim();
            Log.d(TAG, "showAnswer: User choice is  " + userAnswerChoice);
            String correctImage = LearnByChapter.randomLearningQuestions.get(currentQuestion).getCorrectImage().toString().trim();
            Log.d(TAG, "showAnswer: CorrectImage  is  " + correctImage);


            String image1;
            String image2;
            String image3;
            String image4;


            // set the imageName from Random List of Questions....
            image1 = LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(0).getImageName();
            image2 = LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(1).getImageName();
            image3 = LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(2).getImageName();
            image4 = LearnByChapter.randomLearningQuestions.get(currentQuestion).getAnswerList().get(3).getImageName();


            showImageViews();

            // get input stream
            InputStream ims = null;
            try {
                ims = getAssets().open("images/" + image1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            answer1.setTag(image1);
            answer1.setBackground(d);

            // get input stream
            InputStream ims1 = null;
            try {
                ims1 = getAssets().open("images/" + image2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable
            Drawable d1 = Drawable.createFromStream(ims1, null);
            answer2.setTag(image2);
            answer2.setBackground(d1);


            // get input stream
            InputStream ims2 = null;
            try {
                ims2 = getAssets().open("images/" + image3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable
            Drawable d2 = Drawable.createFromStream(ims2, null);
            answer3.setTag(image3);
            answer3.setBackground(d2);


            // get input stream
            InputStream ims3 = null;
            try {
                ims3 = getAssets().open("images/" + image4);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable
            Drawable d3 = Drawable.createFromStream(ims3, null);
            answer4.setTag(image4);
            answer4.setBackground(d3);



            if(userAnswerChoice.equals(correctImage))
            {
                if(answer1.getTag().toString().trim().equals(userAnswerChoice))
                {
//                        answer1.setPadding(10,10,10,10);
//                        answer1.setBackgroundDrawable(greenColorBorder);
                    answerImage1.setImageResource(R.drawable.ic_action_tick);
                }
                else if(answer2.getTag().toString().trim().equals(userAnswerChoice))
                {
//                        answer2.setPadding(10,10,10,10);
//                        answer2.setBackgroundDrawable(greenColorBorder);
                    answerImage2.setImageResource(R.drawable.ic_action_tick);
                }
                else if(answer3.getTag().toString().trim().equals(userAnswerChoice))
                {
//                        answer3.setPadding(10,10,10,10);
//                        answer3.setBackgroundDrawable(greenColorBorder);
                    answerImage3.setImageResource(R.drawable.ic_action_tick);
                }
                else if(answer4.getTag().toString().trim().equals(userAnswerChoice))
                {
//                        answer4.setPadding(10,10,10,10);
//                        answer4.setBackgroundDrawable(greenColorBorder);
                    answerImage4.setImageResource(R.drawable.ic_action_tick);
                }

            }

            else
            {

                // for wron image selection

                if(answer1.getTag().toString().trim().equals(userAnswerChoice))
                {
//                        answer1.setPadding(10,10,10,10);
//                        answer1.setBackgroundDrawable(redColorBorder);
                    answerImage1.setImageResource(R.drawable.ic_action_cancel);
                }
                else if(answer2.getTag().toString().trim().equals(userAnswerChoice))
                {
//                        answer2.setPadding(10,10,10,10);
//                        answer2.setBackgroundDrawable(redColorBorder);
                    answerImage2.setImageResource(R.drawable.ic_action_cancel);
                }
                else if(answer3.getTag().toString().trim().equals(userAnswerChoice))
                {
//                        answer3.setPadding(10,10,10,10);
//                        answer3.setBackgroundDrawable(redColorBorder);
                    answerImage3.setImageResource(R.drawable.ic_action_cancel);
                }
                else if(answer4.getTag().toString().trim().equals(userAnswerChoice))
                {
//                        answer4.setPadding(10,10,10,10);
//                        answer4.setBackgroundDrawable(redColorBorder);
                    answerImage4.setImageResource(R.drawable.ic_action_cancel);
                }



            }


            // show the correct image if user selected the wrong image...


            if(answer1.getTag().toString().trim().equals(correctImage))
            {
//                    answer1.setPadding(10,10,10,10);
//                    answer1.setBackgroundDrawable(greenColorBorder);
                answerImage1.setImageResource(R.drawable.ic_action_tick);

            }
            else if(answer2.getTag().toString().trim().equals(correctImage))
            {
//                    answer2.setPadding(10,10,10,10);
//                    answer2.setBackgroundDrawable(greenColorBorder);
                answerImage2.setImageResource(R.drawable.ic_action_tick);
            }
            else if(answer3.getTag().toString().trim().equals(correctImage))
            {
//                    answer3.setPadding(10,10,10,10);
//                    answer3.setBackgroundDrawable(greenColorBorder);
                answerImage3.setImageResource(R.drawable.ic_action_tick);
            }
            else if(answer4.getTag().toString().trim().equals(correctImage))
            {
//                    answer4.setPadding(10,10,10,10);
//                    answer4.setBackgroundDrawable(greenColorBorder);
                answerImage4.setImageResource(R.drawable.ic_action_tick);
            }







        }





    }






    // TODO: argument for this method is the choice if user selected wrong answer or correct answer..
    // TODO: if 0 or 1 it is answer
    // TODO: if 2 or 3 it is an image..

    public void showOption(final int correctAnswer)
    {

        Log.d(TAG, "showOption: "+imageShowing);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(TestCaseStudyByChapter.this);

        if (correctAnswer == 0)
            builder.setTitle("Wrong Answer");
        else if (correctAnswer == 1) {
            builder.setTitle("Correct Answer");
            goToNextQuestion();
            return;
        }
        else if(correctAnswer == 2) {
            builder.setTitle("Wrong Answer");
        }
        else if(correctAnswer == 3) {
            builder.setTitle("Correct Answer");
            goToNextQuestion();
            return;
        }

        builder.setCancelable(false);


        builder.setPositiveButton("ShowAnswer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                showAnswer(correctAnswer);
            }
        });
        builder.setNegativeButton("Explain", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                String explanation = LearnByChapter.randomLearningQuestions.get(currentQuestion).getQuestionExplaination().toString().trim();
                Log.d(TAG, "Explanation is " + explanation);

                if(explanation != null && !explanation.isEmpty())
                    showExplanationDialog(explanation);


            }
        });

        builder.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                dialog.dismiss();
                // display the selection of the user
                getSelectedAnswerAndDisplayToUser();
            }
        });


        // Create the AlertDialog object and return it
        builder.create().show();

    }




    // TODO: this method show the dialog to show the explaination of the given question...

    public void showExplanationDialog(String explainationText){

        Log.d(TAG, "showExplanationDialog: "+imageShowing);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(TestCaseStudyByChapter.this);
        builder.setTitle("Explanation");
        builder.setCancelable(false);

        builder.setMessage(explainationText);




        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                dialog.dismiss();
                // display the selection of the user
                getSelectedAnswerAndDisplayToUser();
            }
        });

        // Create the AlertDialog object and return it
        builder.create().show();
    }


    public void getSelectedAnswerAndDisplayToUser()
    {



        Log.d(TAG, "getSelectedAnswerAndDisplayToUser: "+imageShowing);


        // now show the selected answer if select by the user

        if( !userChoiceTakeTest.selectedAnswerText.isEmpty() && userChoiceTakeTest.selectedAnswerText.containsKey(currentQuestion))
        {
            String userAnswerSelected =  userChoiceTakeTest.selectedAnswerText.get(currentQuestion);

            // check that user has selected this answer
            userSelectAnswer = true;


            Log.d(TAG,"User previously selected answer is "+userAnswerSelected);
            if(answer1.getText().toString().trim().equals(userAnswerSelected))
            {
                answer1.setPressed(true);
                answer2.setPressed(false);
                answer3.setPressed(false);
                answer4.setPressed(false);

                answer1.setBackgroundResource(R.drawable.button);

            }

            else if(answer2.getText().toString().trim().equals(userAnswerSelected))
            {

                answer1.setPressed(false);
                answer2.setPressed(true);
                answer3.setPressed(false);
                answer4.setPressed(false);

                answer2.setBackgroundResource(R.drawable.button);

            }
            else  if(answer3.getText().toString().trim().equals(userAnswerSelected))
            {


                answer1.setPressed(false);
                answer2.setPressed(false);
                answer3.setPressed(true);
                answer4.setPressed(false);

                answer3.setBackgroundResource(R.drawable.button);
            }
            else if(answer4.getText().toString().trim().equals(userAnswerSelected))
            {
                answer1.setPressed(false);
                answer2.setPressed(false);
                answer3.setPressed(false);
                answer4.setPressed(true);

                answer4.setBackgroundResource(R.drawable.button);
            }

        }


        else {

            Log.d(TAG, "getSelectedAnswerAndDisplayToUser: No Answer is Matched");
        }

    }



    public void getSelectedAnswerAndDisplayToUserForNextButton()
    {




        Log.d(TAG, "getSelectedAnswerAndDisplayToUserForNextButton: "+imageShowing);

        // now show the selected answer if select by the user

        if( !userChoiceTakeTest.selectedAnswerText.isEmpty() && userChoiceTakeTest.selectedAnswerText.containsKey(currentQuestion))
        {
            String userAnswerSelected =  userChoiceTakeTest.selectedAnswerText.get(currentQuestion);

            // check that user has selected this answer
            userSelectAnswer = true;


            Log.d(TAG,"User Next selected answer is "+userAnswerSelected+" Question Number is "+currentQuestion +"list size is "+userChoiceTakeTest.selectedAnswerText.size());

            if(answer1.getText().toString().trim().equals(userAnswerSelected))
            {
                answer1.setPressed(true);
                answer2.setPressed(false);
                answer3.setPressed(false);
                answer4.setPressed(false);

                answer1.setBackgroundResource(R.drawable.button);

            }

            else if(answer2.getText().toString().trim().equals(userAnswerSelected))
            {

                answer1.setPressed(false);
                answer2.setPressed(true);
                answer3.setPressed(false);
                answer4.setPressed(false);

                answer2.setBackgroundResource(R.drawable.button);

            }
            else  if(answer3.getText().toString().trim().equals(userAnswerSelected))
            {


                answer1.setPressed(false);
                answer2.setPressed(false);
                answer3.setPressed(true);
                answer4.setPressed(false);

                answer3.setBackgroundResource(R.drawable.button);
            }
            else if(answer4.getText().toString().trim().equals(userAnswerSelected))
            {
                answer1.setPressed(false);
                answer2.setPressed(false);
                answer3.setPressed(false);
                answer4.setPressed(true);

                answer4.setBackgroundResource(R.drawable.button);

            }

            else {

                Log.d(TAG, "getSelectedAnswerAndDisplayToUserForNextButton: No match found");

            }




        }

    }

    public void ShowUserProgressDialog(){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(TestCaseStudyByChapter.this);
        builder.setTitle("Current Test");
        builder.setCancelable(false);
        if(userChoiceTakeTest.numberOfCorrectAnswers.size() > TEST_PASS_LIMIT)
        {
            builder.setMessage(" Progress \n" +
                    " Correct Answers " + userChoiceTakeTest.numberOfCorrectAnswers.size() +
                    " \n Wrong Answers " + userChoiceTakeTest.numberOfWrongAnswers.size() +
                    " \n Test Pass ");
            Log.d(TAG, "Wrong answers are " + userChoiceTakeTest.numberOfWrongAnswers.size());
            Log.d(TAG, " Correct answers are " + userChoiceTakeTest.numberOfCorrectAnswers.size());

            // TODO: insert test data into database when student pass the test

            if (Splash.dbHelperClass.insertTestRecord("caseStudy", "1", "1", getCurrentDate(), "1", Integer.toString(userChoiceTakeTest.numberOfCorrectAnswers.size()), Integer.toString(userChoiceTakeTest.numberOfWrongAnswers.size()))) {
                Log.d(TAG, "Data inserted Successfully");
            } else {
                Log.d(TAG, "Data not inserted");
            }


        }
        else
        {
            builder.setMessage(" Progress \n" +
                    " Correct Answers " + userChoiceTakeTest.numberOfCorrectAnswers.size()+
                    " \n Wrong Answers "+ userChoiceTakeTest.numberOfWrongAnswers.size()+
                    " \n Test Fail ");
            Log.d(TAG, "Wrong answers are " + userChoiceTakeTest.numberOfWrongAnswers.size());

            Log.d(TAG, " Correct answers are " + userChoiceTakeTest.numberOfCorrectAnswers.size());

            // TODO: insert test data into database when student failed

            if (Splash.dbHelperClass.insertTestRecord("caseStudy", "1", "0", getCurrentDate(), "1", Integer.toString(userChoiceTakeTest.numberOfCorrectAnswers.size()), Integer.toString(userChoiceTakeTest.numberOfWrongAnswers.size()))) {
                Log.d(TAG, "Data inserted Successfully");
            } else {
                Log.d(TAG, "Data not inserted");
            }

        }

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                dialog.dismiss();

                // finish the activity..
                finish();
            }
        });

        builder.setNegativeButton("Review Answers", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                dialog.dismiss();

                Intent i = new Intent(TestCaseStudyByChapter.this, ReviewAnswerLearningCaseStudy.class);
                i.putExtra("object", userChoiceTakeTest);
                startActivity(i);

                // finish the activity..
                finish();
            }
        });

        // Create the AlertDialog object and return it
        builder.create().show();
    }




    public String getCurrentDate()
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }



    public boolean getPressedStateButton(Button b)
    {
        if(b.isPressed())
            return true;
        else return false;

    }



    private void hideImageViews()
    {
        // hide the imageView that show correct and incorrect Images
        // these imageview are only for reviewing of imageView...
        answerImage1.setVisibility(View.GONE);
        answerImage2.setVisibility(View.GONE);
        answerImage3.setVisibility(View.GONE);
        answerImage4.setVisibility(View.GONE);


    }

    private void showImageViews()
    {
        // show the imageView that show correct and incorrect Images
        // these imageview are only for reviewing of Images...
        answerImage1.setVisibility(View.VISIBLE);
        answerImage2.setVisibility(View.VISIBLE);
        answerImage3.setVisibility(View.VISIBLE);
        answerImage4.setVisibility(View.VISIBLE);

        speackButtonAsnwer1.setVisibility(View.GONE);
        speackButtonAsnwer2.setVisibility(View.GONE);
        speackButtonAsnwer3.setVisibility(View.GONE);
        speackButtonAsnwer4.setVisibility(View.GONE);

    }


    public void speackButtonClickListener(final String text){
        speackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speackThisText(text);
            }
        });
    }
    public void ans1ButtonSpeech(final String text){
        speackButtonAsnwer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speackThisText(text);
            }
        });
    }

    public void ans2ButtonSpeech(final String text){
        speackButtonAsnwer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speackThisText(text);
            }
        });
    }

    public void ans3ButtonSpeech(final String text){
        speackButtonAsnwer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speackThisText(text);
            }
        });
    }

    public void ans4ButtonSpeech(final String text){
        speackButtonAsnwer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speackThisText(text);
            }
        });
    }


    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.US);
            tts.setPitch(1.1f);


            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", " This Languate is not Supported");

            }else {
                speackButton.setEnabled(true);
                //speackThisText(text);
            }
        }
    }

    public void speackThisText(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(text);
        } else {
            ttsUnder20(text);
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    @Override
    protected void onDestroy() {
        if (tts != null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }









}
