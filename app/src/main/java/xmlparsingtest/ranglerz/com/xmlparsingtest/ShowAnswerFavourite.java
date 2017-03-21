package xmlparsingtest.ranglerz.com.xmlparsingtest;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
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
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by User-10 on 09-Nov-16.
 */
public class ShowAnswerFavourite extends AppCompatActivity implements TextToSpeech.OnInitListener{


    public static final String TAG = "Show Answer Category";

    // reference variables for views...

    TextView questionCategory , questionNumber , timer , questionText;
    Button answer1,answer2,answer3,answer4,previous,explain,back;
    ImageView questionImageView,answerImage1,answerImage2,answerImage3,answerImage4;
    ImageButton speackButton, speackButtonAsnwer1, speackButtonAsnwer2, speackButtonAsnwer3, speackButtonAsnwer4;
    String text, textAnswer1, textAnswer2, textAnswer3, textAnswer4;

    private TextToSpeech tts;

    String image1,image2 , image3,image4 = null;

    String userAnswerChoice = null;
    String correctAnswer = null;
    String correctImage = null;
    String questionImage = null;

    int currentQuestionNumber = 0;
    int position = 0;

    int currentAnswer1 = 0;
    int currentAnswer2 = 1;
    int currentAnswer3 = 2;
    int currentAnswer4 = 3;

    boolean showingImage = false;
    boolean hasQuestionImage = false;

    AnimateHorizontalProgressBar progressBar;





    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_random_test);

        Intent i = getIntent();
        userAnswerChoice = i.getStringExtra("userAnswerChoice");
        correctAnswer = i.getStringExtra("correctAnswer");
        correctImage = i.getStringExtra("correctImage");
        questionImage = i.getStringExtra("questionImage");

        currentQuestionNumber = i.getIntExtra("questionNumber", 0);
        position = i.getIntExtra("position", 0);

        if (correctImage != null && !correctImage.isEmpty()) {
            Log.d(TAG, "onCreate: Correct Image is " + correctImage);
            showingImage = true;

        }

        if (questionImage != null && !questionImage.isEmpty()) {
            Log.d(TAG, "onCreate: Question Image is " + questionImage);
            hasQuestionImage = true;

        }

        progressBar = (AnimateHorizontalProgressBar) findViewById(R.id.animate_progress_bar);
        progressBar.setMax(Menu_Activity.randomQuestions.size());
        progressBar.setProgress(currentQuestionNumber);

        Log.d(TAG, "User choice is" + userAnswerChoice + " Correct Answer is " + correctAnswer + " Current Question is " + currentQuestionNumber + "position is " + position);
        Log.d(TAG, "Question from random array is " + Menu_Activity.randomQuestions.get(position).getQuestionText());
        Log.d(TAG, "Correct Answer from random array is " + Menu_Activity.randomQuestions.get(position).getCorrectAnswer());


        // setting the imageview for the question that has image...

        questionImageView = (ImageView) findViewById(R.id.questionImage);
        answerImage1 = (ImageView) findViewById(R.id.imageViewAnswer1);
        answerImage2 = (ImageView) findViewById(R.id.imageViewAnswer2);
        answerImage3 = (ImageView) findViewById(R.id.imageViewAnswer3);
        answerImage4 = (ImageView) findViewById(R.id.imageViewAnswer4);
        timer = (TextView) findViewById(R.id.timer);
        timer.setVisibility(View.INVISIBLE);


        if(hasQuestionImage) {

            // get input stream
            InputStream ims = null;
            try {
                ims = getAssets().open("images/" + questionImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);

            questionImageView.setImageDrawable(d);
        }

        questionCategory = (TextView) findViewById(R.id.questionCategory);
        questionCategory.setText(Menu_Activity.randomQuestions.get(position).getTopic());

        tts = new TextToSpeech(this, this);
        speackButton = (ImageButton) findViewById(R.id.speack_button);
        speackButtonAsnwer1 = (ImageButton) findViewById(R.id.bt1_speaker);
        speackButtonAsnwer2 = (ImageButton) findViewById(R.id.bt2_speaker);
        speackButtonAsnwer3 = (ImageButton) findViewById(R.id.bt3_speaker);
        speackButtonAsnwer4 = (ImageButton) findViewById(R.id.bt4_speaker);


        questionText = (TextView) findViewById(R.id.questionText);
        questionText.setText(Menu_Activity.randomQuestions.get(position).getQuestionText());
        text = Menu_Activity.randomQuestions.get(position).getQuestionText().toString();
        speackButtonClickListener(text);




        questionNumber = (TextView) findViewById(R.id.questionNumber);
        questionNumber.setText(currentQuestionNumber + " Off "+Menu_Activity.randomQuestions.size());

        // inivisible the timer because we donot want to show timer here
        timer = (TextView) findViewById(R.id.timer);
        timer.setVisibility(View.INVISIBLE);

        previous = (Button) findViewById(R.id.buttonPrevious);
        previous.setVisibility(View.INVISIBLE);

        back = (Button) findViewById(R.id.buttonNext);
        back.setText("Back");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showingImage = false;
                finish();
            }
        });


        // because explanation tag is in the bottom and we have to added it into question TAG
        // hide the button because explaination is not used..
        explain = (Button) findViewById(R.id.buttonFlag);
        explain.setText("Explain");
        explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show dialog box with explanation of the question..
                String explanationText = Splash.randomQuestions.get(position).getQuestionExplaination();
                if (explanationText != null && !explanationText.isEmpty())
                    showExplanationDialog(explanationText.toString().trim());
                else
                {
                    Toast.makeText(getApplicationContext(),"No Explaination To Show ", Toast.LENGTH_SHORT).show();
                }
            }
        });



        answer1 = (Button) findViewById(R.id.buttonAnswer1);
        answer2 = (Button) findViewById(R.id.buttonAnswer2);
        answer3 = (Button) findViewById(R.id.buttonAnswer3);
        answer4 = (Button) findViewById(R.id.buttonAnswer4);



        disableButtons();

        hideImageViews();



        // set the imageName from Random List of Questions....
        image1 = Menu_Activity.randomQuestions.get(position).getAnswerList().get(0).getImageName();
        image2 = Menu_Activity.randomQuestions.get(position).getAnswerList().get(1).getImageName();
        image3 = Menu_Activity.randomQuestions.get(position).getAnswerList().get(2).getImageName();
        image4 = Menu_Activity.randomQuestions.get(position).getAnswerList().get(3).getImageName();


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

        // if we have images to show as answers..
        // so we show images as background
        if (showingImage)
        {


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



            // return becasue we donot want to show text on images..
            return;

        }


        Log.d(TAG,"-"+currentAnswer1+"-"+currentAnswer2+"-"+currentAnswer3+"-"+currentAnswer4+"size is"+Menu_Activity.randomQuestions.get(position).getAnswerList().size());






        // set the text of the answer
        answer1.setText(Menu_Activity.randomQuestions.get(position).getAnswerList().get(0).getAnswerText());
        textAnswer1 = Menu_Activity.randomQuestions.get(position).getAnswerList().get(0).getAnswerText().toString();
        ans1ButtonSpeech(textAnswer1);


        answer2.setText(Menu_Activity.randomQuestions.get(position).getAnswerList().get(1).getAnswerText());
        textAnswer2 = Menu_Activity.randomQuestions.get(position).getAnswerList().get(1).getAnswerText().toString();
        ans2ButtonSpeech(textAnswer2);


        answer3.setText(Menu_Activity.randomQuestions.get(position).getAnswerList().get(2).getAnswerText());
        textAnswer3 = Menu_Activity.randomQuestions.get(position).getAnswerList().get(2).getAnswerText().toString();
        ans3ButtonSpeech(textAnswer3);


        answer4.setText(Menu_Activity.randomQuestions.get(position).getAnswerList().get(3).getAnswerText());
        textAnswer4 = Menu_Activity.randomQuestions.get(position).getAnswerList().get(3).getAnswerText().toString();
        ans4ButtonSpeech(textAnswer4);




        // set layout paramaert to default..

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(8,8,8,8);
        answer1.setLayoutParams(params);
        answer2.setLayoutParams(params);
        answer3.setLayoutParams(params);
        answer4.setLayoutParams(params);

        // if user has correct answer then show green box
        // otherwise show red box that indicates its wrong answer

        if(userAnswerChoice.equals(correctAnswer))
        {
            if(answer1.getText().toString().trim().equals(userAnswerChoice))
            {
                answer1.setBackgroundDrawable(greenColorBorder);
            }
            else if(answer2.getText().toString().trim().equals(userAnswerChoice))
            {
                answer2.setBackgroundDrawable(greenColorBorder);
            }
            else if(answer3.getText().toString().trim().equals(userAnswerChoice))
            {
                answer3.setBackgroundDrawable(greenColorBorder);
            }
            else if(answer4.getText().toString().trim().equals(userAnswerChoice))
            {
                answer4.setBackgroundDrawable(greenColorBorder);
            }
        }

        else
        {
            if(answer1.getText().toString().trim().equals(userAnswerChoice))
            {
                answer1.setBackgroundDrawable(redColorBorder);
            }
            else if(answer2.getText().toString().trim().equals(userAnswerChoice))
            {
                answer2.setBackgroundDrawable(redColorBorder);
            }
            else if(answer3.getText().toString().trim().equals(userAnswerChoice))
            {
                answer3.setBackgroundDrawable(redColorBorder);
            }
            else if(answer4.getText().toString().trim().equals(userAnswerChoice))
            {
                answer4.setBackgroundDrawable(redColorBorder);
            }






        }


        // show the actual correct answer of the relevant question...
        // and show the green box around it..
        if(answer1.getText().toString().trim().equals(correctAnswer))
        {
            answer1.setBackgroundDrawable(greenColorBorder);
        }
        else if(answer2.getText().toString().trim().equals(correctAnswer))
        {
            answer2.setBackgroundDrawable(greenColorBorder);
        }
        else if(answer3.getText().toString().trim().equals(correctAnswer))
        {
            answer3.setBackgroundDrawable(greenColorBorder);
        }
        else if(answer4.getText().toString().trim().equals(correctAnswer))
        {
            answer4.setBackgroundDrawable(greenColorBorder);
        }








    }





    public void showExplanationDialog(final String explainationText){
        // Use the Builder class for convenient dialog construction
        /*AlertDialog.Builder builder = new AlertDialog.Builder(ShowAnswerFavourite.this);
        builder.setTitle("Explanation");
        builder.setCancelable(false);

        builder.setMessage(explainationText);




        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                dialog.dismiss();
            }
        });

        // Create the AlertDialog object and return it
        builder.create().show();*/

        final Dialog dialog = new Dialog(ShowAnswerFavourite.this);
        dialog.setContentView(R.layout.explanation_dialog);
        dialog.setTitle("Explanation");
        TextView tv_explanation = (TextView)dialog.findViewById(R.id.tv_explanation);
        Button tv_ok = (Button) dialog.findViewById(R.id.tv_ok);
        ImageButton speackerIamge = (ImageButton) dialog.findViewById(R.id.speek_button);

        tv_explanation.setText(explainationText);
        tv_ok.setText("OK");
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        speackerIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speackThisText(explainationText);
            }
        });

        dialog.setCancelable(false);
        dialog.show();

    }//end of dilaog



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showingImage = false;
    }



    private void hideImageViews()
    {
        // hide the imageView that show correct and incorrect Images
        // these imageview are only for reviewing of imageView...
        answerImage1.setVisibility(View.GONE);
        answerImage2.setVisibility(View.GONE);
        answerImage3.setVisibility(View.GONE);
        answerImage4.setVisibility(View.GONE);

        speackButtonAsnwer1.setVisibility(View.VISIBLE);
        speackButtonAsnwer2.setVisibility(View.VISIBLE);
        speackButtonAsnwer3.setVisibility(View.VISIBLE);
        speackButtonAsnwer4.setVisibility(View.VISIBLE);


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

    private void disableButtons()
    {
        // disable the button click on all Answers button....
        answer1.setEnabled(false);
        answer2.setEnabled(false);
        answer3.setEnabled(false);
        answer4.setEnabled(false);

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
