package xmlparsingtest.ranglerz.com.xmlparsingtest;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Category;

public class RandomTestCategory extends AppCompatActivity implements TextToSpeech.OnInitListener{
    




    public static final int TEST_PASS_LIMIT = CategoryTest.randomCategoryQuestions.size()-10;
    public static int RANDOM_LIST_SIZE = CategoryTest.randomCategoryQuestions.size();
    private static final String TAG = "Random Category Test";
    ArrayList<Category> selectedCategories = null;

    AnimateHorizontalProgressBar progressBar;

    // reference variables for views...
    TextView questionCategory , questionNumber , timer , questionText;
    ImageButton speackButton, speackButtonAsnwer1, speackButtonAsnwer2, speackButtonAsnwer3, speackButtonAsnwer4;
    String text, textAnswer1, textAnswer2, textAnswer3, textAnswer4;
    private TextToSpeech tts;

    Button answer1,answer2,answer3,answer4,previous,flag,next;

    ImageView questionImage,answerImage1,answerImage2,answerImage3,answerImage4;

    // variable for storing current state of the question

    int currentQuestion = 0;


    UserChoiceTakeTest userChoiceTakeTest;

    boolean userSelectAnswer = false;
    boolean imageShowing = false;

    // to store the correct Answer of the Current Question..
    String correctAnswer = null;

    String correctImage = null;

    String imageName , imageName1 , imageName2 , imageName3;

    public CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_random_test);

        // receive selected categories from Category Test Class...
        Intent in = getIntent();
       selectedCategories = (ArrayList<Category>) in.getSerializableExtra("selectedCategories");
                Log.d(TAG, "Selected Categories size is  " + CategoryTest.randomCategoryQuestions.size());

        questionCategory = (TextView) findViewById(R.id.questionCategory);
        questionText = (TextView) findViewById(R.id.questionText);
        tts = new TextToSpeech(this, this);
        speackButton = (ImageButton) findViewById(R.id.speack_button);
        speackButtonAsnwer1 = (ImageButton) findViewById(R.id.bt1_speaker);
        speackButtonAsnwer2 = (ImageButton) findViewById(R.id.bt2_speaker);
        speackButtonAsnwer3 = (ImageButton) findViewById(R.id.bt3_speaker);
        speackButtonAsnwer4 = (ImageButton) findViewById(R.id.bt4_speaker);

        questionNumber = (TextView) findViewById(R.id.questionNumber);
        questionImage = (ImageView) findViewById(R.id.questionImage);


        answer1 = (Button) findViewById(R.id.buttonAnswer1);
        answer2 = (Button) findViewById(R.id.buttonAnswer2);
        answer3 = (Button) findViewById(R.id.buttonAnswer3);
        answer4 = (Button) findViewById(R.id.buttonAnswer4);
        flag = (Button) findViewById(R.id.buttonFlag);

        timer = (TextView) findViewById(R.id.timer);
        next = (Button) findViewById(R.id.buttonNext);


        answerImage1 = (ImageView) findViewById(R.id.imageViewAnswer1);
        answerImage2 = (ImageView) findViewById(R.id.imageViewAnswer2);
        answerImage3 = (ImageView) findViewById(R.id.imageViewAnswer3);
        answerImage4 = (ImageView) findViewById(R.id.imageViewAnswer4);

        answerImage1.setVisibility(View.GONE);
        answerImage2.setVisibility(View.GONE);
        answerImage3.setVisibility(View.GONE);
        answerImage4.setVisibility(View.GONE);


        userChoiceTakeTest = new UserChoiceTakeTest();

        // start the timer ..
        countDownTimer = new CountDownTimer(3600000 , 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("Remaining time: "+String.format("%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            public void onFinish() {
                finishTest();
            }
        }.start();


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



        flag.setText("Flag");


        // display the current question
        // then display all the answer of that question...

        Log.d(TAG, "Current Question is " + currentQuestion);
        Log.d(TAG, "Question id is " + CategoryTest.randomCategoryQuestions.get(currentQuestion).getQuestionId());
        Log.d(TAG, "Answers List size is " + CategoryTest.randomCategoryQuestions.get(currentQuestion).getAnswerList().size());


        if(!CategoryTest.randomCategoryQuestions.get(currentQuestion).getQuestionImage().isEmpty()) {

            // get input stream
            InputStream ims = null;
            try {
                ims = getAssets().open("images/"+CategoryTest.randomCategoryQuestions.get(currentQuestion).getQuestionImage().toString().trim());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            questionImage.setImageDrawable(d);
        }
        else
        {
            questionImage.setImageDrawable(null);
        }

        // this is the area where we receive empty answer
        // and we have images as answer to show ..
        if( CategoryTest.randomCategoryQuestions.get(currentQuestion).getAnswerList().get(0).getAnswerText().isEmpty()) {

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

            questionText.setText(CategoryTest.randomCategoryQuestions.get(currentQuestion).getQuestionText().toString());
            text = CategoryTest.randomCategoryQuestions.get(currentQuestion).getQuestionText().toString();
            speackButtonClickListener(text);

            questionCategory.setText(CategoryTest.randomCategoryQuestions.get(currentQuestion).getTopic().toString());

            correctImage = CategoryTest.randomCategoryQuestions.get(currentQuestion).getCorrectImage();

            imageName = CategoryTest.randomCategoryQuestions.get(currentQuestion).getAnswerList().get(0).getImageName().toString().trim();
            Log.d(TAG, "ImageName is" + imageName);
            imageName1 = CategoryTest.randomCategoryQuestions.get(currentQuestion).getAnswerList().get(1).getImageName().toString().trim();
            Log.d(TAG,"ImageName is"+imageName1);
            imageName2 = CategoryTest.randomCategoryQuestions.get(currentQuestion).getAnswerList().get(2).getImageName().toString().trim();
            Log.d(TAG, "ImageName is"+imageName2);
            imageName3 = CategoryTest.randomCategoryQuestions.get(currentQuestion).getAnswerList().get(3).getImageName().toString().trim();
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



            Log.d(TAG,"Empty Answer show the images of the answer");



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

        correctAnswer =  CategoryTest.randomCategoryQuestions.get(currentQuestion).getCorrectAnswer().toString().trim();

        // seting the question text
        // then question category
        // then question number is displaying..
        questionText.setText(CategoryTest.randomCategoryQuestions.get(currentQuestion).getQuestionText().toString());
        text = CategoryTest.randomCategoryQuestions.get(currentQuestion).getQuestionText().toString();
        speackButtonClickListener(text);

        questionCategory.setText(CategoryTest.randomCategoryQuestions.get(currentQuestion).getTopic().toString());
        questionNumber.setText("Question " + questionNumberIncremented + " off " + CategoryTest.randomCategoryQuestions.size());


        /// setting the answer of the questions...
        answer1.setText(CategoryTest.randomCategoryQuestions.get(currentQuestion).getAnswerList().get(0).getAnswerText());
        textAnswer1 = CategoryTest.randomCategoryQuestions.get(currentQuestion).getAnswerList().get(0).getAnswerText().toString();
        ans1ButtonSpeech(textAnswer1);



        answer2.setText(CategoryTest.randomCategoryQuestions.get(currentQuestion).getAnswerList().get(1).getAnswerText());
        textAnswer2 = CategoryTest.randomCategoryQuestions.get(currentQuestion).getAnswerList().get(1).getAnswerText().toString();
        ans2ButtonSpeech(textAnswer2);



        answer3.setText(CategoryTest.randomCategoryQuestions.get(currentQuestion).getAnswerList().get(2).getAnswerText());
        textAnswer3 = CategoryTest.randomCategoryQuestions.get(currentQuestion).getAnswerList().get(2).getAnswerText().toString();
        ans3ButtonSpeech(textAnswer3);



        answer4.setText(CategoryTest.randomCategoryQuestions.get(currentQuestion).getAnswerList().get(3).getAnswerText());
        textAnswer4 = CategoryTest.randomCategoryQuestions.get(currentQuestion).getAnswerList().get(3).getAnswerText().toString();
        ans4ButtonSpeech(textAnswer4);









    }



    public void flagButtonClick(View v)
    {
        // create an ArrayList to store Flag question...


        Log.d(TAG,"Flagged Quesion Number is "+currentQuestion);



        if(getPressedStateButton(answer1))
        {
            if(flag.getText().toString().trim().equals("UnFlag"))
            {
                Log.d(TAG, "flagButtonClick: Remove the Flag Question.");
                // if ti exist remove it..
                if(userChoiceTakeTest.selectedFlagAnswerText.containsKey(currentQuestion))
                    userChoiceTakeTest.selectedFlagAnswerText.remove(currentQuestion);


                flag.setText("Flag");
            }

            else {
                userChoiceTakeTest.selectedFlagAnswerText.put(currentQuestion, answer1.getText().toString().trim());
                flag.setText("UnFlag");
                Log.d(TAG, "Flag answer is " + userChoiceTakeTest.selectedFlagAnswerText.size());
            }

        }
        else if(getPressedStateButton(answer2))
        {

            if(flag.getText().toString().trim().equals("UnFlag"))
            {
                Log.d(TAG, "flagButtonClick: Remove the Flag Question.");

                if(userChoiceTakeTest.selectedFlagAnswerText.containsKey(currentQuestion))
                    userChoiceTakeTest.selectedFlagAnswerText.remove(currentQuestion);

                flag.setText("Flag");
            }

            else {
                userChoiceTakeTest.selectedFlagAnswerText.put(currentQuestion, answer2.getText().toString().trim());
                flag.setText("UnFlag");
                Log.d(TAG, "Flag answer is " + userChoiceTakeTest.selectedFlagAnswerText.size());
            }
        }
        else if(getPressedStateButton(answer3))
        {

            if(flag.getText().toString().trim().equals("UnFlag"))
            {
                Log.d(TAG, "flagButtonClick: Remove the Flag Question.");

                if(userChoiceTakeTest.selectedFlagAnswerText.containsKey(currentQuestion))
                    userChoiceTakeTest.selectedFlagAnswerText.remove(currentQuestion);

                flag.setText("Flag");
            }
            else {

                userChoiceTakeTest.selectedFlagAnswerText.put(currentQuestion, answer3.getText().toString().trim());
                flag.setText("UnFlag");
                Log.d(TAG, "Flag answer is " + userChoiceTakeTest.selectedFlagAnswerText.size());
            }
        }
        else if(getPressedStateButton(answer4))
        {

            if(flag.getText().toString().trim().equals("UnFlag"))
            {
                Log.d(TAG, "flagButtonClick: Remove the Flag Question.");

                if(userChoiceTakeTest.selectedFlagAnswerText.containsKey(currentQuestion))
                    userChoiceTakeTest.selectedFlagAnswerText.remove(currentQuestion);

                flag.setText("Flag");
            }
            else {
                userChoiceTakeTest.selectedFlagAnswerText.put(currentQuestion, answer4.getText().toString().trim());
                flag.setText("UnFlag");
                Log.d(TAG, "Flag answer is " + userChoiceTakeTest.selectedFlagAnswerText.size());
            }

        }

    }






    public void previousButtonClick(View v)
    {
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

                }
                else
                {
                    Log.d(TAG,"Wrong Image");
                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
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
                }
                else
                {
                    Log.d(TAG,"Wrong Image");
                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
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

                }
                else
                {
                    Log.d(TAG,"Wrong Image");
                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
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
                }
                else
                {
                    Log.d(TAG,"Wrong Image");
                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
                }

            }


            answer1.setBackgroundResource(R.drawable.button);
            answer2.setBackgroundResource(R.drawable.button);
            answer3.setBackgroundResource(R.drawable.button);
            answer4.setBackgroundResource(R.drawable.button);


            imageShowing = false;


        }
        else {


            // now compute the result of user


            if (answer1.isPressed()) {


                String userAnswer = answer1.getText().toString().trim();
                userChoiceTakeTest.selectedAnswerText.put(currentQuestion, userAnswer);

                if (correctAnswer.equals(userAnswer)) {

                    Log.d(TAG, "Correct Answer ");
                    userChoiceTakeTest.numberOfCorrectAnswers.put(currentQuestion, 1);

                } else {
                    Log.d(TAG, "Wrong Answer ");
                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
                }
                Log.d(TAG, "Correct Answer is " + correctAnswer + " user anser is " + userAnswer);
            } else if (answer2.isPressed()) {

                String userAnswer = answer2.getText().toString().trim();
                userChoiceTakeTest.selectedAnswerText.put(currentQuestion, userAnswer);

                if (correctAnswer.equals(userAnswer)) {
                    Log.d(TAG, "Correct Answer ");
                    userChoiceTakeTest.numberOfCorrectAnswers.put(currentQuestion, 1);

                } else {
                    Log.d(TAG, "Wrong Answer ");
                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
                }

                Log.d(TAG, "Correct Answer is " + correctAnswer + " user anser is " + userAnswer);
            } else if (answer3.isPressed()) {


                String userAnswer = answer3.getText().toString().trim();
                userChoiceTakeTest.selectedAnswerText.put(currentQuestion, userAnswer);
                if (correctAnswer.equals(userAnswer)) {
                    Log.d(TAG, "Correct Answer ");
                    userChoiceTakeTest.numberOfCorrectAnswers.put(currentQuestion, 1);

                } else {
                    Log.d(TAG, "Wrong Answer ");
                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
                }

                Log.d(TAG, "Correct Answer is " + correctAnswer + " user anser is " + userAnswer);


            } else if (answer4.isPressed()) {


                String userAnswer = answer4.getText().toString().trim();

                userChoiceTakeTest.selectedAnswerText.put(currentQuestion, userAnswer);

                if (correctAnswer.equals(userAnswer)) {
                    Log.d(TAG, "Correct Answer ");
                    userChoiceTakeTest.numberOfCorrectAnswers.put(currentQuestion, 1);

                } else {
                    Log.d(TAG, "Wrong Answer ");
                    userChoiceTakeTest.numberOfWrongAnswers.put(currentQuestion, 1);
                }

                Log.d(TAG, "Correct Answer is " + correctAnswer + " user answer is " + userAnswer);
            }

        }


        // increment the question to fetch next question
        // from list of random questions..
        currentQuestion ++;


        // after adding the correct and wrong answer
        // check if the questions end or not
        if(currentQuestion >= CategoryTest.randomCategoryQuestions.size())
        {
           finishTest();
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







        // call display question and answer method
        // to display the question and answers...

         displayQuestionAndAnswer();

//        // this method get the selected answers from user
//        // and display there selection on the Screen..
//
        getSelectedAnswerAndDisplayToUserForNextButton();



    }








    public void getSelectedAnswerAndDisplayToUser()
    {


        // check if user already flag the question..
        // if yes then show it as Flag question..
        if(userChoiceTakeTest.selectedFlagAnswerText.containsKey(currentQuestion))
        {
            flag.setText("UnFlag");
        }


        // now show the selected answer if select by the user

        if( !userChoiceTakeTest.selectedAnswerText.isEmpty())
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

    }



    public void getSelectedAnswerAndDisplayToUserForNextButton()
    {
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




        }

    }





    public void ShowFlagDialog(){



        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(RandomTestCategory.this);
        builder.setTitle("Flagged Questions");
        builder.setCancelable(false);

        builder.setMessage(" Do you want to Review Flagged Questions ");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent i = new Intent(RandomTestCategory.this,ShowFlaggedQuestionCategory.class);
                i.putExtra("object",userChoiceTakeTest);
                startActivity(i);

                // finish the activity..
                finish();
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                        ShowUserProgressDialog();


                    }
                });

        // Create the AlertDialog object and return it
        builder.create().show();
    }





    public void ShowUserProgressDialog(){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(RandomTestCategory.this);
        builder.setTitle("Current Test");
        builder.setCancelable(false);
        if(userChoiceTakeTest.numberOfCorrectAnswers.size() > TEST_PASS_LIMIT)
        {
            builder.setMessage(" Progress \n" +
                    " Correct Answers " + userChoiceTakeTest.numberOfCorrectAnswers.size()+
                    " \n Wrong Answers "+ userChoiceTakeTest.numberOfWrongAnswers.size()+
                    " \n Test Pass ");
            Log.d(TAG, "Wrong answers are " + userChoiceTakeTest.numberOfWrongAnswers.size());
            Log.d(TAG, " Correct answers are " + userChoiceTakeTest.numberOfCorrectAnswers.size());
        }
        else
        {
            builder.setMessage(" Progress \n" +
                    " Correct Answers " + userChoiceTakeTest.numberOfCorrectAnswers.size()+
                    " \n Wrong Answers "+ userChoiceTakeTest.numberOfWrongAnswers.size()+
                    " \n Test Fail ");
            Log.d(TAG, "Wrong answers are " + userChoiceTakeTest.numberOfWrongAnswers.size());

            Log.d(TAG, " Correct answers are " + userChoiceTakeTest.numberOfCorrectAnswers.size());

        }

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                dialog.dismiss();

                // finish the activity..
                finish();
            }
        })
                .setNegativeButton("Review Answers", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        // insert data into database...
                        if (userChoiceTakeTest.numberOfCorrectAnswers.size() > TEST_PASS_LIMIT) {


                            Log.d(TAG, "Wrong answers are " + userChoiceTakeTest.numberOfWrongAnswers.size());
                            // insert test data into database
                            if (Splash.dbHelperClass.insertTestRecord("takeTest", "1", "1", getCurrentDate(), "1", Integer.toString(userChoiceTakeTest.numberOfCorrectAnswers.size()), Integer.toString(userChoiceTakeTest.numberOfWrongAnswers.size()))) {
                                Log.d(TAG, "Data inserted Successfully");
                            } else {
                                Log.d(TAG, "Data not inserted");
                            }
                        } else {

                            Log.d(TAG, " Correct answers are " + userChoiceTakeTest.numberOfCorrectAnswers.size());
                            // insert test data into database
                            if (Splash.dbHelperClass.insertTestRecord("takeTest", "1", "0", getCurrentDate(), "1", Integer.toString(userChoiceTakeTest.numberOfCorrectAnswers.size()), Integer.toString(userChoiceTakeTest.numberOfWrongAnswers.size()))) {
                                Log.d(TAG, "Data inserted Successfully");
                            } else {
                                Log.d(TAG, "Data not inserted");
                            }

                        }



                        Intent i = new Intent(RandomTestCategory.this,ReviewAnswersCategoryTest.class);
                        i.putExtra("object",userChoiceTakeTest);
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



    private void finishTest()
    {

        timer.setText("Finish!");

        // stop the timer
        countDownTimer.cancel();

        // finish the Test..
        next.setText("Finish");

        Log.d(TAG, "Number of Correct Answer is " + userChoiceTakeTest.numberOfCorrectAnswers.size());
        Log.d(TAG, "Number of Flagged Questions is " + userChoiceTakeTest.selectedFlagAnswerText.size());
        Log.d(TAG, "Number of Wrong Answer is " + userChoiceTakeTest.numberOfWrongAnswers.size());

        if(userChoiceTakeTest.selectedFlagAnswerText.size() > 0) {
            ShowFlagDialog();
        }
        else {
            ShowUserProgressDialog();
        }

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
