package xmlparsingtest.ranglerz.com.xmlparsingtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;

import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Answer;
import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Question;

public class Menu_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = Menu_Activity.class.getSimpleName() ;
    public static ArrayList<Question> randomQuestions = new ArrayList<>();
    ImageView imageView = null;


   // SliderLayout sliderShow = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.dashboard);
        navigationView.setNavigationItemSelectedListener(this);


        Log.d(TAG, "onCreate: After ");


        try {
            // create it dynamically for crashing...solution..
            View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_menu_);
        }
        catch (OutOfMemoryError e)
        {
            Log.d(TAG, "onCreate: Exception "+e.getMessage());
        }


        // to show slider of images...



//        sliderShow = (SliderLayout) findViewById(R.id.custom_indicator);
//
//
//        // fourth image...
//        DefaultSliderView textSliderView1 = new DefaultSliderView(this);
//        textSliderView1
//                .image(R.drawable.banner1);
//        sliderShow.addSlider(textSliderView1);
//
//        // second image
//        DefaultSliderView textSliderView2 = new DefaultSliderView(this);
//        textSliderView2
//        .image(R.drawable.banner2);
//
//        sliderShow.addSlider(textSliderView2);
//
//        // third image
//        DefaultSliderView textSliderView3 = new DefaultSliderView(this);
//        textSliderView3
//                .image(R.drawable.banner3);
//
//        sliderShow.addSlider(textSliderView3);
//
//


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
     //   sliderShow.stopAutoCycle();
        super.onStop();
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dashboard) {
        }
        else if (id == R.id.take_test) {
            // Handle the camera action
            Intent i = new Intent(Menu_Activity.this,RandomTest.class);
            startActivity(i);

        } else if (id == R.id.category_test) {

            Intent i = new Intent(Menu_Activity.this,CategoryTest.class);
            startActivity(i);

        } else if (id == R.id.learn_test) {

            Intent i = new Intent(Menu_Activity.this,LearnByChapter.class);
            startActivity(i);

        } else if (id == R.id.practice_favourite) {

            // TODO: Here practice favourite questions...
            // TODO: Show favourite questions to user to practice ...

            Cursor questionRows = Splash.dbHelperClass.getFavouriteQuestions();
            Cursor answerRows = null;

            if(questionRows.getCount() == 0 )
            {
                Toast.makeText(getApplicationContext(), "No Favourite Questions Exists ", Toast.LENGTH_SHORT).show();

            }
            else {


                String correctAnswer = null;
                String correctImage = null;


                while (questionRows.moveToNext()) {

                    Question q = new Question();
                    q.setQuestionId(questionRows.getString(0));
                    q.setTopic(questionRows.getString(1));
                    q.setQuestionText(questionRows.getString(2));

                    // now get the list of answers for specific id of the question...
                    answerRows = Splash.dbHelperClass.getAnswersForQuestionId(q.getQuestionId().toString());
                    if(answerRows.getCount()== 0)
                    {
                        Log.d("tag","No Answers to show");
                    }
                    while(answerRows.moveToNext())
                    {
                        Answer a = new Answer();
                        a.setAnswerId(answerRows.getInt(0));

                        // trim the string because it contains empty stirng from the database because it can contain image

                        String noAnswer = answerRows.getString(1).toString().trim();
                        if(noAnswer.isEmpty())
                        {

                            a.setImageName(answerRows.getString(4).toString().trim());


                            Log.d("tag", "Image Name is " + answerRows.getString(4).toString().trim());
                        }

                        // to check if the answerText is Empty
                        a.setAnswerText(noAnswer);



                        // get the correct image
                         correctImage = answerRows.getString(5);

                        // check for null or empty in database...
                        if(correctImage != null && !"".equals(correctImage))
                        {
                            correctImage = correctImage.toString().trim();
                            Log.d("tag","Correct Image is "+correctImage);
                        }


                        // set the correct answer
                        if(answerRows.getString(2) != null) {
                            correctAnswer = answerRows.getString(2).toString().trim();
                        }

                        // add it to the answer list
                        q.getAnswerList().add(a);
                        // answerList.add(a);
                    }

                    Log.d("tag", "Answer List Size is " + q.getAnswerList().size());
                    // set the answer List for Specific Question


                    // get the question Image
                    String questionImage = questionRows.getString(4);

                    // check for null or empty in database...for question images..
                    if(questionImage != null && !"".equals(questionImage))
                    {
                        questionImage = questionImage.toString().trim();
                        Log.d("tag","Question  Image is "+questionImage);
                    }

                    // get the question Explanation
                    String questionExplanation = questionRows.getString(5);

                    // check for null or empty in database...for question Explanation...
                    if(questionExplanation != null && !"".equals(questionExplanation))
                    {
                        questionExplanation = questionExplanation.toString().trim();
                        Log.d("tag","Question  Explanation is "+questionExplanation);
                    }


                    //  q.setAnswerList(answerList);
                    q.setCorrectAnswer(correctAnswer);
                    q.setCorrectImage(correctImage);
                    q.setQuestionImage(questionImage);
                    q.setQuestionExplaination(questionExplanation);


                    Log.d("tag", "Answer List Size in Question class is  " + q.getAnswerList().size());




                    randomQuestions.add(q);

                }

                try {
                    // close the cursor after use
                    answerRows.close();
                    questionRows.close();
                }
                catch (Exception e)
                {
                    Log.d("tag","Exception Occured"+e.toString());
                }



            }



            if(randomQuestions.size()>0 && !randomQuestions.isEmpty() && randomQuestions!=null ) {

            /* Create an Intent that will start the Main-Activity. */
                Intent mainIntent = new Intent(Menu_Activity.this, PracticeFavourite.class);
                startActivity(mainIntent);

            }
            else
            {
                Toast.makeText(getApplicationContext(), "No Favourite Questions Exists ", Toast.LENGTH_SHORT).show();

            }



        } else if (id == R.id.progress) {

            String totaltest = null;
            String totalCorrectAnswer = null;
            String totalWrongAnswer = null;
            String totalTestPass = null;

            Cursor cursorProgress = Splash.dbHelperClass.getProgress();

            if ( cursorProgress.getCount() == 0)
            {
                Toast.makeText(getApplicationContext(), "No Progress To Show ", Toast.LENGTH_SHORT).show();
            }
            else
            {



                while (cursorProgress.moveToNext()) {

                    totaltest = cursorProgress.getString(0);
                    totalCorrectAnswer = cursorProgress.getString(1);
                    totalWrongAnswer = cursorProgress.getString(2);
                    totalTestPass = cursorProgress.getString(3);

                }

                Log.d("tag", "onNavigationItemSelected: "+totaltest+"Total Test PAss"+totalTestPass);

                if(totaltest != null && totalCorrectAnswer !=null && totalWrongAnswer !=null) {
                    ShowUserProgressDialog(totaltest, totalCorrectAnswer, totalWrongAnswer, totalTestPass);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Progress To Show ", Toast.LENGTH_SHORT).show();
                }

            }

        } else if (id == R.id.about_test) {
            Intent i = new Intent(Menu_Activity.this,AboutTest.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void ShowUserProgressDialog(String totaltests , String totalCorrectAnswers , String totalWrongAnswers , String totalPassTest){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(Menu_Activity.this);
        builder.setTitle(" General Statistics ");
        builder.setCancelable(false);


        Log.d("tag", "ShowUserProgressDialog: "+totaltests +"correct Answer "+totalCorrectAnswers +"Total Wrong Answer"+totalWrongAnswers+"Pass Test"+totalPassTest);


        int totalFailTest = Integer.parseInt(totaltests) - Integer.parseInt(totalPassTest);
        builder.setMessage(" Progress \n" +
                " Total Number of Tests " + totaltests +
                " \n Total Correct Answers  " + totalCorrectAnswers +
                " \n Total Wrong Answers  " + totalWrongAnswers +
                " \n Passed Tests " + totalPassTest +
                " \n Failed Tests  " + totalFailTest +
                " \n ");



        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                dialog.dismiss();
            }
        });

        // Create the AlertDialog object and return it
        builder.create().show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(randomQuestions !=null)
        {
            randomQuestions.clear();
        }

    }
}
