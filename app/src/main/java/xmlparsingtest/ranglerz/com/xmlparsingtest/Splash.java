package xmlparsingtest.ranglerz.com.xmlparsingtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Answer;
import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Question;

public class Splash extends AppCompatActivity {

    private static final String TAG = Splash.class.getName() ;

    static DBHelperClass dbHelperClass ;
    public static File DB_PATH = null;

    ProgressBar percentageProgressBar;

    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    private long fileSize = 0;


    Integer count =1;

    XmlHandler xmlHandler ;
    XmlPullParserFactory xmlPullParserFactory;
    XmlPullParser parser;

    long startTime = 0l;
    long endTime = 0l;
    long executionTime = 0l;


    String correctAnswer = null;
    String correctImage = null;
    String questionImage = null;
    String questionExplanation = null;

    public static List<Question> questionList = new ArrayList<>();
    public static List<Answer> answerList = new ArrayList<>();
    public  List<Answer> listOfAnswers = new ArrayList<>();

    ImageView img;
    DilatingDotsProgressBar mDilatingDotsProgressBar;


    // make a randomQuestion Array that getFilled when app is loaded
    // this randon array get filled by random question for test
    public static ArrayList<Question> randomQuestions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);


        dbHelperClass = new DBHelperClass(this);
        img = (ImageView) findViewById(R.id.imageViewSplash);


        percentageProgressBar = (ProgressBar) findViewById(R.id.percentage_prgrogras_bar);
        percentageProgressBar.setMax(100);
        percentageProgressBar.setProgress(0);
        //reset progress bar status
        progressBarStatus = 0;

        //reset filesize
        fileSize = 0;


        mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progressBar1);

         // show progress bar and start animating
        mDilatingDotsProgressBar.showNow();




        // get Database File Name..
        DB_PATH = this.getDatabasePath(dbHelperClass.getDatabaseName());
        Log.d(TAG, "Db Path is " + DB_PATH.getAbsolutePath());




        // this is used to store data into database from xml..
        xmlHandler = new XmlHandler();

        try {

            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(false);
            parser = xmlPullParserFactory.newPullParser();


            // get the current system time
            startTime = System.currentTimeMillis();

            Log.d("tag"," Milli Seconds Starts "+ TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime)
            ));

        }catch (Exception e) {
            e.printStackTrace();
        }




        if(checkDbExist())
        {

            if(checkTableExist()) {
                Log.d(TAG, "Database Exist Get data from database");
                readDataFromDB();
            }
            else
            {
                // database exist but no data in Tables or Tables Not exist
                // now read data from xml files
                readDataFromXML();
            }

        }
        else
        {
            Log.d(TAG,"Read Data from Xml");
            readDataFromXML();

        }






    }


    public boolean checkDbExist()
    {
        File dbFile = new File(DB_PATH.getPath());
        Log.d(TAG,"File is " +dbFile.getAbsolutePath());
        return dbFile.exists();
    }

    public boolean checkTableExist()
    {
        // first check question table
        // then check answer table for
        //exist
        boolean isQuestionTableExist = dbHelperClass.isTableExists(dbHelperClass.TABLE_QUESTIONS);
        if(isQuestionTableExist)
        {
            boolean isAnswerTableExist = dbHelperClass.isTableExists(dbHelperClass.TABLE_ANSWERS);
            if(isAnswerTableExist) {

                boolean isTableDataExist = dbHelperClass.isTableContainsData(dbHelperClass.TABLE_QUESTIONS);
                if(isTableDataExist) {
                    boolean isTableDataExistAnswer = dbHelperClass.isTableContainsData(dbHelperClass.TABLE_ANSWERS);
                    if(isTableDataExistAnswer)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                    return false;

            }
            else
                return false;
        }

        else
            return false;

    }








    /********************************/
        /* This method is use to read Data from Database
        * and store in to array these data contain random questions from database
        * */
    /********************************/


    public void readDataFromDB()
    {


        Log.d(TAG, "readDataFromDB: ");
        // this is use to get random questions from database
        // do this in background
        // not load it in a MainThread

        Cursor questionRows =  dbHelperClass.getRandomQuestions();
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
            answerRows = dbHelperClass.getAnswersForQuestionId(q.getQuestionId().toString());
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

            // get the question Explanation
            String questionExplanation = questionRows.getString(5);

            // check for null or empty in database...for question Explanation...
            if(questionExplanation != null && !"".equals(questionExplanation))
            {
                this.questionExplanation = questionExplanation.toString().trim();
                Log.d(TAG,"Question  Explanation is "+this.questionExplanation);
            }


            //  q.setAnswerList(answerList);
            q.setCorrectAnswer(correctAnswer);
            q.setCorrectImage(correctImage);
            q.setQuestionImage(this.questionImage);
            q.setQuestionExplaination(this.questionExplanation);


            Log.d(TAG, "Answer List Size in Question class is  " + q.getAnswerList().size());




            randomQuestions.add(q);
        }

        try {
            // close the cursor after use
            answerRows.close();
            questionRows.close();

              /* Create an Intent that will start the Main-Activity. */
            Intent mainIntent = new Intent(Splash.this,Menu_Activity.class);
            startActivity(mainIntent);
            Splash.this.finish();
        }
        catch (Exception e)
        {
            Log.d(TAG,"Exception Occured"+e.toString());
        }














    }







    /********************************/
        /* This method is use to read Data from Xml Files
        * and store in to Database
                * * */
    /********************************/

    public void readDataFromXML()
    {
        // to add data from xml files to database


        Log.d(TAG, "readDataFromXML: ");



        new RetreiveXmlInBackground().execute(100);

    }




    /********************************/
        /* Create an Asynctak to load the xml files in the background
        * */
    /********************************/


    public class RetreiveXmlInBackground extends AsyncTask<Integer, Integer, Void> {





        @Override
        protected void onPreExecute() {

            percentageProgressBar.setProgress(0);


            super.onPreExecute();


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            percentageProgressBar.setVisibility(View.VISIBLE);
            percentageProgressBar.setProgress(values[0]);




            super.onProgressUpdate(values);

        }



        @Override
        protected Void doInBackground(Integer... params) {




            try {

                while (progressBarStatus<100) {

                    Thread.sleep(2000);
                   progressBarStatus =  readandParseXmlFromAssets();
                    setProgress(progressBarStatus);
                    publishProgress(progressBarStatus);

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            // get the current system time
            endTime = System.currentTimeMillis();

            executionTime = endTime - startTime;

            int seconds = (int) (executionTime / 1000) % 60 ;
            int minutes = (int) ((executionTime / (1000*60)) % 60);

            Log.d(TAG,"Minutes Took "+ minutes);


            Log.d(TAG," Seconds Took "+  seconds);


            Log.d(TAG, "Asnyc Task Completed");



            readDataFromDB();



        }


    }








    /********************************/
        /* This method is used for getting all xml files from assets folder
        * then after that get its length using it and then open the file using inputStream and return the stream
        * and then parse the xml and it will return the object that contain question and answer text and
        * then add the object to the list
        * */
    /********************************/


    private int readandParseXmlFromAssets() throws IOException, XmlPullParserException {


        InputStream inputStream = null;
        AssetManager assetManager = getApplicationContext().getAssets();


        String [] list1 = assetManager.list("Alertness");
        Log.d(TAG, "List size is " + list1.length);

        for (int i=0; i<list1.length;i++)
        {

            inputStream = assetManager.open("Alertness/"+list1[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }

        String [] list2 = assetManager.list("Attitude");
        Log.d(TAG, "List size is " + list2.length);

        for (int i=0; i<list2.length;i++)
        {

            inputStream = assetManager.open("Attitude/" +list2[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }


        String [] list3 = assetManager.list("Documents");
        Log.d(TAG, "List size is " + list3.length);

        for (int i=0; i<list3.length;i++)
        {

            inputStream = assetManager.open("Documents/" +list3[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }




        String [] list4 = assetManager.list("Hazard Awareness");
        Log.d(TAG, "List size is " + list4.length);

        for (int i=0; i<list4.length;i++)
        {

            inputStream = assetManager.open("Hazard Awareness/" +list4[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }


        String [] list5 = assetManager.list("Incidents, Accidents and Emergencies");
        Log.d(TAG, "List size is " + list5.length);

        for (int i=0; i<list5.length;i++)
        {

            inputStream = assetManager.open("Incidents, Accidents and Emergencies/" +list5[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }



        String [] list6 = assetManager.list("Motorway Rules");
        Log.d(TAG, "List size is " + list6.length);

        for (int i=0; i<list6.length;i++)
        {

            inputStream = assetManager.open("Motorway Rules/" +list6[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }



        String [] list7 = assetManager.list("Other Types of Vehicle");
        Log.d(TAG, "List size is " + list7.length);

        for (int i=0; i<list7.length;i++)
        {

            inputStream = assetManager.open("Other Types of Vehicle/" +list7[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }


        String [] list8 = assetManager.list("Road and Traffic Signs");
        Log.d(TAG, "List size is " + list8.length);

        for (int i=0; i<list8.length;i++)
        {

            inputStream = assetManager.open("Road and Traffic Signs/" +list8[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }


        String [] list9 = assetManager.list("Rules of the Road");
        Log.d(TAG, "List size is " + list9.length);

        for (int i=0; i<list9.length;i++)
        {

            inputStream = assetManager.open("Rules of the Road/" +list9[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }



        String [] list10 = assetManager.list("Safety and Your Vehicle");
        Log.d(TAG, "List size is " + list10.length);

        for (int i=0; i<list10.length;i++)
        {

            inputStream = assetManager.open("Safety and Your Vehicle/" +list10[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }


        String [] list11 = assetManager.list("Safety Margins");
        Log.d(TAG, "List size is " + list11.length);

        for (int i=0; i<list11.length;i++)
        {

            inputStream = assetManager.open("Safety Margins/" +list11[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }


        String [] list12 = assetManager.list("Vehicle Handling");
        Log.d(TAG, "List size is " + list12.length);

        for (int i=0; i<list12.length;i++)
        {

            inputStream = assetManager.open("Vehicle Handling/" +list12[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }

        String [] list13 = assetManager.list("Vehicle Loading");
        Log.d(TAG, "List size is " + list13.length);

        for (int i=0; i<list13.length;i++)
        {

            inputStream = assetManager.open("Vehicle Loading/" +list13[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }


        String [] list14 = assetManager.list("Vulnerable Road Users");
        Log.d(TAG, "List size is " + list14.length);

        for (int i=0; i<list14.length;i++)
        {

            inputStream = assetManager.open("Vulnerable Road Users/" +list14[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }


        // TODO:  Here we read Data from CaseStudy Folders..
        // TODO: Case Study also contains 14 categories..


        String [] list15 = assetManager.list("CaseStudy/Alertness");
        Log.d(TAG, "List size is " + list15.length);

        for (int i=0; i<list15.length;i++)
        {

            inputStream = assetManager.open("CaseStudy/Alertness/" +list15[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);

        }

        String [] list16 = assetManager.list("CaseStudy/Attitude");
        Log.d(TAG, "List size is " + list16.length);

        for (int i=0; i<list16.length;i++)
        {
            inputStream = assetManager.open("CaseStudy/Attitude/" +list16[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);
        }




        String [] list17 = assetManager.list("CaseStudy/Documents");
        Log.d(TAG, "List size is " + list17.length);

        for (int i=0; i<list17.length;i++)
        {
            inputStream = assetManager.open("CaseStudy/Documents/" +list17[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);
        }



        String [] list18 = assetManager.list("CaseStudy/Hazard Awareness");
        Log.d(TAG, "List size is " + list18.length);

        for (int i=0; i<list18.length;i++)
        {
            inputStream = assetManager.open("CaseStudy/Hazard Awareness/" +list18[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);
        }




        String [] list19 = assetManager.list("CaseStudy/Incidents");
        Log.d(TAG, "List size is " + list19.length);

        for (int i=0; i<list19.length;i++)
        {
            inputStream = assetManager.open("CaseStudy/Incidents/" +list19[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);
        }



        String [] list20 = assetManager.list("CaseStudy/Motorway Rules");
        Log.d(TAG, "List size is " + list20.length);

        for (int i=0; i<list20.length;i++)
        {
            inputStream = assetManager.open("CaseStudy/Motorway Rules/" +list20[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);
        }


        String [] list21 = assetManager.list("CaseStudy/Other Types of Vehicle");
        Log.d(TAG, "List size is " + list21.length);

        for (int i=0; i<list21.length;i++)
        {
            inputStream = assetManager.open("CaseStudy/Other Types of Vehicle/" +list21[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);
        }




        String [] list22 = assetManager.list("CaseStudy/Road and Traffic Signs");
        Log.d(TAG, "List size is " + list22.length);

        for (int i=0; i<list22.length;i++)
        {
            inputStream = assetManager.open("CaseStudy/Road and Traffic Signs/" +list22[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);
        }





        String [] list23 = assetManager.list("CaseStudy/Rules of the Road");
        Log.d(TAG, "List size is " + list23.length);

        for (int i=0; i<list23.length;i++)
        {
            inputStream = assetManager.open("CaseStudy/Rules of the Road/" +list23[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);
        }




        String [] list24 = assetManager.list("CaseStudy/Safety and Your Vehicle");
        Log.d(TAG, "List size is " + list24.length);

        for (int i=0; i<list24.length;i++)
        {
            inputStream = assetManager.open("CaseStudy/Safety and Your Vehicle/" +list24[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);
        }




        String [] list25 = assetManager.list("CaseStudy/Safety Margins");
        Log.d(TAG, "List size is " + list25.length);

        for (int i=0; i<list25.length;i++)
        {
            inputStream = assetManager.open("CaseStudy/Safety Margins/" +list25[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);
        }



        String [] list26 = assetManager.list("CaseStudy/Vehicle Handling");
        Log.d(TAG, "List size is " + list26.length);

        for (int i=0; i<list26.length;i++)
        {
            inputStream = assetManager.open("CaseStudy/Vehicle Handling/" +list26[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);
        }




        String [] list27 = assetManager.list("CaseStudy/Vehicle Loading");
        Log.d(TAG, "List size is " + list27.length);

        for (int i=0; i<list27.length;i++)
        {
            inputStream = assetManager.open("CaseStudy/Vehicle Loading/" +list27[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);
        }




        String [] list28 = assetManager.list("CaseStudy/Vulnerable Road Users");
        Log.d(TAG, "List size is " + list28.length);

        for (int i=0; i<list28.length;i++)
        {
            inputStream = assetManager.open("CaseStudy/Vulnerable Road Users/" +list28[i]);
            parser.setInput(inputStream, null);
            QuestionAnswer answer =  xmlHandler.ParseAndStoreXML(parser);
        }





        //   Log.d(TAG,"Size of Question List is "+questionList.size()+"Answer List size"+answerList.size());


        return 100;


    }







}
