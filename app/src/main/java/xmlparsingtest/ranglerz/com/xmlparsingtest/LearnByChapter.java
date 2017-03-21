package xmlparsingtest.ranglerz.com.xmlparsingtest;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

import xmlparsingtest.ranglerz.com.xmlparsingtest.CustomAdapters.CustomAdapterCategoryTest;
import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Answer;
import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Category;
import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Question;

public class LearnByChapter extends AppCompatActivity {

    // make a randomQuestion Array that getFilled
    // this random array get filled by random question for Learning
    public static ArrayList<Question> randomLearningQuestions = new ArrayList<>();


    private static final String TAG = "Learn By Chapter";
    ListView showCategories ;
    CustomAdapterCategoryTest customAdapterCategoryTest;
    ArrayList<Category> allCategories = null;
    String correctAnswer = null;
    String correctImage = null;
    String questionImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_by_chapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // add all categories from folder name.. to the list...
        allCategories = new ArrayList<>();
        allCategories.add(new Category("Alertness", false));
        allCategories.add(new Category("Attitude", false));
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
        showCategories = (ListView) findViewById(R.id.listViewLearnByChapter);
        showCategories.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        customAdapterCategoryTest = new CustomAdapterCategoryTest(getApplicationContext(),R.layout.single_list_item_learning,allCategories,0);
        showCategories.setAdapter(customAdapterCategoryTest);




    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }





    public void startTestLearnByChapter(View v)
    {

        // Make ArrayList to get the selected Category From User
        ArrayList<String> selectedCategories = new ArrayList<>();

        // call custom Adapter method to get the list of selected categories..
        ArrayList<Category> categories = customAdapterCategoryTest.getList();



        // add the selected category to selected category List... from customAdapter List...
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            if (category.isSelected()) {
                selectedCategories.add(category.getName().toString());
              //  Log.d(TAG, "Selected Categories is " + category.getName());
            }
        }

        // now check whether the list is empty or not
        // if empty user donot select any category
        // prompt user to select category...
        if(selectedCategories.isEmpty() || selectedCategories == null )
        {
            Toast.makeText(getApplicationContext(), "Please select any Category ", Toast.LENGTH_SHORT).show();
            return;
        }



        String selectedCategory = selectedCategories.get(selectedCategories.size()-1).toString();
        Log.d(TAG, "Selected Category  is " + selectedCategory);

        // convert arraylist to String array to pass as argument
        // create a new array of arrayList size and convert arrayList to Array
        // to pass as argument..
        readDataFromDB(selectedCategory);
        Log.d(TAG, "Random Question size is" + randomLearningQuestions.size());

        Intent i = new Intent(LearnByChapter.this,TestLearnByChapter.class);
        i.putExtra("selectedCategories", categories);
        startActivity(i);



    }





    public void readDataFromDB(String selectedCategories)
    {

        // this is use to get random questions from database
        // do this in background
        // not load it in a MainThread
        Log.d(TAG, "readDataFromDB: Selected category is "+selectedCategories);

        if(selectedCategories !=null || !selectedCategories.isEmpty()) {
            Splash.dbHelperClass = new DBHelperClass(this);
            Cursor questionRows = Splash.dbHelperClass.getLearningQuestionByCategory(selectedCategories);
            Cursor answerRows = null;
            if (questionRows.getCount() == 0) {
                Log.d(TAG, "No data to show");
                return;
            }

            while (questionRows.moveToNext()) {
                Question q = new Question();
                q.setQuestionId(questionRows.getString(0));
                q.setTopic(questionRows.getString(1));
                q.setQuestionText(questionRows.getString(2));

                // now get the list of answers for specific id of the question...
                answerRows = Splash.dbHelperClass.getAnswersForQuestionId(q.getQuestionId());
                if (answerRows.getCount() == 0) {
                    Log.d(TAG, "No Answers to show");
                }

                while (answerRows.moveToNext()) {

                    Answer a = new Answer();
                    a.setAnswerId(answerRows.getInt(0));

                    // trim the string because it contains empty stirng from the database because it can contain image

                    String noAnswer = answerRows.getString(1).toString().trim();
                    if (noAnswer.isEmpty()) {

                        a.setImageName(answerRows.getString(4).toString().trim());


                        Log.d(TAG, "Image Name is " + answerRows.getString(4).toString().trim());
                    }

                    // to check if the answerText is Empty
                    a.setAnswerText(noAnswer);


                    // get the correct image
                    String correctImage = answerRows.getString(5);

                    // check for null or empty in database...
                    if (correctImage != null && !"".equals(correctImage)) {
                        this.correctImage = correctImage.toString().trim();
                        Log.d(TAG, "Correct Image is " + correctImage);
                    }


                    // set the correct answer
                    if (answerRows.getString(2) != null) {
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
                if (questionImage != null && !"".equals(questionImage)) {
                    this.questionImage = questionImage.toString().trim();
                    Log.d(TAG, "Question  Image is " + this.questionImage);
                }


                // get the question Explanation
                String questionExplanation = questionRows.getString(5);

                // check for null or empty in database...for question Explanation...
                if (questionExplanation != null && !"".equals(questionExplanation)) {
                    questionExplanation = questionExplanation.toString().trim();
                    Log.d(TAG, "Question  Explanation is " + questionExplanation.toString().trim());
                }


                //  q.setAnswerList(answerList);
                q.setCorrectAnswer(correctAnswer);
                q.setCorrectImage(correctImage);
                q.setQuestionImage(this.questionImage);
                q.setQuestionExplaination(questionExplanation);


                Log.d(TAG, "Answer List Size in Question class is  " + q.getAnswerList().size());


                randomLearningQuestions.add(q);
            }

            try {
                // close the cursor after use
                answerRows.close();
                questionRows.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception Occured" + e.toString());
            }


        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Select any Category",Toast.LENGTH_SHORT).show();
        }




    }







    // this method is for Getting CaseStudy Data
    public void readDataFromDBForCaseStudy(String selectedCategories)
    {

        // this is use to get random questions from database
        // do this in background
        // not load it in a MainThread
        Log.d(TAG, "readDataFromDB: Selected category is "+selectedCategories);

        if(selectedCategories !=null || !selectedCategories.isEmpty()) {
            Cursor questionRows = Splash.dbHelperClass.getCaseStudyQuestionByCategory(selectedCategories);
            Cursor answerRows = null;
            if (questionRows.getCount() == 0) {
                Log.d(TAG, "No data to show");
                return;
            }

            while (questionRows.moveToNext()) {

                Question q = new Question();
                q.setQuestionId(questionRows.getString(0));
                q.setTopic(questionRows.getString(1));
                q.setQuestionText(questionRows.getString(2));

                // now get the list of answers for specific id of the question...
                answerRows = Splash.dbHelperClass.getAnswersForQuestionId(q.getQuestionId());
                if (answerRows.getCount() == 0) {
                    Log.d(TAG, "No Answers to show");
                }

                while (answerRows.moveToNext()) {

                    Answer a = new Answer();
                    a.setAnswerId(answerRows.getInt(0));

                    // trim the string because it contains empty stirng from the database because it can contain image

                    String noAnswer = answerRows.getString(1).toString().trim();
                    if (noAnswer.isEmpty()) {

                        a.setImageName(answerRows.getString(4).toString().trim());


                        Log.d(TAG, "Image Name is " + answerRows.getString(4).toString().trim());
                    }

                    // to check if the answerText is Empty
                    a.setAnswerText(noAnswer);


                    // get the correct image
                    String correctImage = answerRows.getString(5);

                    // check for null or empty in database...
                    if (correctImage != null && !"".equals(correctImage)) {
                        this.correctImage = correctImage.toString().trim();
                        Log.d(TAG, "Correct Image is " + correctImage);
                    }


                    // set the correct answer
                    if (answerRows.getString(2) != null) {
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
                if (questionImage != null && !"".equals(questionImage)) {
                    this.questionImage = questionImage.toString().trim();
                    Log.d(TAG, "Question  Image is " + this.questionImage);
                }


                // get the question Explanation
                String questionExplanation = questionRows.getString(5);

                // check for null or empty in database...for question Explanation...
                if (questionExplanation != null && !"".equals(questionExplanation)) {
                    questionExplanation = questionExplanation.toString().trim();
                    Log.d(TAG, "Question  Explanation is " + questionExplanation.toString().trim());
                }


                //  q.setAnswerList(answerList);
                q.setCorrectAnswer(correctAnswer);
                q.setCorrectImage(correctImage);
                q.setQuestionImage(this.questionImage);
                q.setQuestionExplaination(questionExplanation);


                Log.d(TAG, "Answer List Size in Question class is  " + q.getAnswerList().size());


                randomLearningQuestions.add(q);
            }

            try {
                // close the cursor after use
                answerRows.close();
                questionRows.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception Occured" + e.toString());
            }


        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Select any Category",Toast.LENGTH_SHORT).show();
        }




    }





    public void cancelLearnByChapter(View v)
    {
        // finish the activity
        finish();
    }


    public void startCaseStudy(View v)
    {
        // Start Test For CaseStudy
        // Make ArrayList to get the selected Category From User
        ArrayList<String> selectedCategories = new ArrayList<>();

        // call custom Adapter method to get the list of selected categories..
        ArrayList<Category> categories = customAdapterCategoryTest.getList();



        // add the selected category to selected category List... from customAdapter List...
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            if (category.isSelected()) {
                selectedCategories.add(category.getName().toString());
                  Log.d(TAG, "Selected Categories is " + category.getName());
            }
        }

        // now check whether the list is empty or not
        // if empty user donot select any category
        // prompt user to select category...
        if(selectedCategories.isEmpty() || selectedCategories == null )
        {
            Toast.makeText(getApplicationContext(), "Please select any Category ", Toast.LENGTH_SHORT).show();
            return;
        }



        String selectedCategory = selectedCategories.get(selectedCategories.size()-1).toString();
        Log.d(TAG, "Selected Category  is " + selectedCategory);


        readDataFromDBForCaseStudy(selectedCategory);
        Log.d(TAG, "Random Question size is" + randomLearningQuestions.size());


        // TODO: make new activity to Display Questions from Case Study..
        // TODO: Also add option for review questions After Finish the Test...
        // TODO: we pass CS for option to check if the questions come from CaseStudy
        // TODO: because caseStudy  have different questions...

        Intent i = new Intent(LearnByChapter.this,TestCaseStudyByChapter.class);
        i.putExtra("selectedCategories", categories);
        startActivity(i);



    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "On Resume Call");
        randomLearningQuestions.clear();
    }


}
