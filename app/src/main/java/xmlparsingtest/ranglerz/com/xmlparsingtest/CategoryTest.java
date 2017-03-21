package xmlparsingtest.ranglerz.com.xmlparsingtest;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import xmlparsingtest.ranglerz.com.xmlparsingtest.CustomAdapters.CustomAdapterCategoryTest;
import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Answer;
import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Category;
import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Question;


public class CategoryTest extends AppCompatActivity {

    // make a randomQuestion Array that getFilled
    // this random array get filled by random question for test
    public static ArrayList<Question> randomCategoryQuestions = new ArrayList<>();

    private static final String TAG = "Category Test";
    ListView showCategories ;
    CustomAdapterCategoryTest customAdapterCategoryTest;
    ArrayList<Category> allCategories = null;
    String correctAnswer = null;
    String correctImage = null;
    String questionImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG,"Oncreate call");
        setContentView(R.layout.activity_category_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // add all categories from folder name.. to the list...
        allCategories = new ArrayList<>();
        allCategories.add(new Category("Alertness",false));
        allCategories.add(new Category("Attitude",false));
        allCategories.add(new Category("Documents",false));
        allCategories.add(new Category("Hazard Awareness",false));
        allCategories.add(new Category("Incidents, Accidents and Emergencies",false));
        allCategories.add(new Category("Motorway Rules",false));
        allCategories.add(new Category("Other Types of Vehicle",false));
        allCategories.add(new Category("Road and Traffic Signs",false));
        allCategories.add(new Category("Rules of the Road",false));
        allCategories.add(new Category("Safety and Your Vehicle",false));
        allCategories.add(new Category("Safety Margins",false));
        allCategories.add(new Category("Vehicle Handling",false));
        allCategories.add(new Category("Vehicle Loading",false));
        allCategories.add(new Category("Vulnerable Road Users",false));


        // set the adapter for the listview...
        showCategories = (ListView) findViewById(R.id.listViewCategoryTest);
        customAdapterCategoryTest = new CustomAdapterCategoryTest(getApplicationContext(),R.layout.single_item_list_category,allCategories,1);
        showCategories.setAdapter(customAdapterCategoryTest);



    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"On Resume Call");
        randomCategoryQuestions.clear();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    public void startTest (View v)
    {


        // Make ArrayList to get the selected Category From User
        ArrayList<String> selectedCategories = new ArrayList<>();

        // call custom Adapter method to get the list of selected categories..
                ArrayList<Category> categories = customAdapterCategoryTest.getList();


                for (int i = 0; i < categories.size(); i++) {
                    Category category = categories.get(i);
                    if (category.isSelected()) {
                        selectedCategories.add(category.getName().toString().trim());
                        Log.d(TAG,"Selected Categories is "+category.getName());
                    }
                }


        if(selectedCategories.isEmpty() || selectedCategories == null )
        {
            Toast.makeText(getApplicationContext(),"Please select any Category ",Toast.LENGTH_SHORT).show();
            return;
        }


        // convert arraylist to String array to pass as argument
        // create a new array of arrayList size and convert arrayList to Array
        // to pass as argument..
        readDataFromDB(selectedCategories.toArray(new String[selectedCategories.size()]));
        Intent i = new Intent(CategoryTest.this,RandomTestCategory.class);
        i.putExtra("selectedCategories", categories);
        startActivity(i);

    }


    public void cancel (View v)
    {
        // finish the activity
        finish();
    }


    public void readDataFromDB(String[] selectedCategories)
    {

        // this is use to get random questions from database
        // do this in background
        // not load it in a MainThread

        Cursor questionRows =  Splash.dbHelperClass.getRandomCategoryQuestions(selectedCategories);
        Cursor answerRows = null;
        if(questionRows.getCount()== 0)
        {
            Log.d(TAG,"No data to show");
            return;
        }

        while (questionRows.moveToNext())
        {
            Question q = new Question();
            q.setQuestionId(questionRows.getString(0));
            q.setTopic(questionRows.getString(1));
            q.setQuestionText(questionRows.getString(2));

            // now get the list of answers for specific id of the question...
            answerRows = Splash.dbHelperClass.getAnswersForQuestionId(q.getQuestionId());
            if(answerRows.getCount()== 0)
            {
                Log.d(TAG,"No Answers to show");
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


                    Log.d(TAG, "Image Name is " + answerRows.getString(4).toString().trim());
                }

                // to check if the answerText is Empty
                a.setAnswerText(noAnswer);



                // get the correct image
                String correctImage = answerRows.getString(5);

                // check for null or empty in database...
                if(correctImage != null && !"".equals(correctImage))
                {
                    this.correctImage = correctImage.toString().trim();
                    Log.d(TAG,"Correct Image is "+correctImage);
                }


                // set the correct answer
                if(answerRows.getString(2) != null) {
                    correctAnswer = answerRows.getString(2).toString().trim();
                }

                // add it to the answer list
                q.getAnswerList().add(a);
                // answerList.add(a);
            }

            Log.d(TAG, "Answer List Size is " + q.getAnswerList().size());
            // set the answer List for Specific Question


            // get the question Image
            String questionImage = questionRows.getString(4);

            // check for null or empty in database...for question images..
            if(questionImage != null && !"".equals(questionImage))
            {
                this.questionImage = questionImage.toString().trim();
                Log.d(TAG,"Question  Image is "+this.questionImage);
            }




            //  q.setAnswerList(answerList);
            q.setCorrectAnswer(correctAnswer);
            q.setCorrectImage(correctImage);
            q.setQuestionImage(this.questionImage);


            Log.d(TAG, "Answer List Size in Question class is  " + q.getAnswerList().size());




            randomCategoryQuestions.add(q);
        }

        try {
            // close the cursor after use
            answerRows.close();
            questionRows.close();
        }
        catch (Exception e)
        {
            Log.d(TAG,"Exception Occured"+e.toString());
        }







    }





}
