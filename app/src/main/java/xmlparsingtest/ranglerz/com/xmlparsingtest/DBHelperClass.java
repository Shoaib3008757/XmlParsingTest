package xmlparsingtest.ranglerz.com.xmlparsingtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Category;

/**
 * Created by User-10 on 04-Oct-16.
 */
public class DBHelperClass extends SQLiteOpenHelper {

    // Database Version change when the changes in Database
    // Always change number when changes in database otherwise changes will not occur
    public static final int DATABASE_VERSION = 44;

    // Database Name
    public static final String DATABASE_NAME = "abel.sqlite";


    // table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_USER_TEST = "user_test"; // bridge table for many to many relationship
    public static final String TABLE_TESTS = "tests";
    public static final String TABLE_TEST_QUESTIONS = "test_question";
    public static final String TABLE_QUESTIONS = "questions";
    public static final String TABLE_ANSWERS = "answers";
    public static final String TABLE_PROGRESS = "progress";




    // User Table Columns names
    public static final String U_ID = "uId";
    public static final String U_NAME = "uName";
    public static final String U_PASSWORD = "uPassword";
    public static final String U_EMAIL = "uEmail";
    public static final String U_CONTACT_NO = "uContactNo";
    public static final String U_ADDRESS = "uAddress";




    // User_test Table Columns names
    public static final String U_T_ID = "id";




    // Test Table Columns names
    public static final String T_ID = "tId";
    public static final String T_TYPE = "tType";
    public static final String T_COMPLETE = "tComplete";
    public static final String T_PASS = "tPass";
    public static final String T_DATE = "tDate";
    public static final String T_ISVISIBLE = "isVisible";
    public static final String T_CORRECTANSWERS = "totalCorrectAnswers";
    public static final String T_WRONANSWERS = "totalWrongAnswers";



    // Test_question Table Columns names
    public static final String T_Q_ID = "id";



    // Questions Table Columns names
    public static final String Q_ID = "qId";
    public static final String Q_TOPIC = "qTopic";
    public static final String Q_TEXT = "qText";
    public static final String Q_FAVOURITE = "qFavourite";
    public static final String Q_IMAGE = "qImage";
    public static final String Q_EXPLANATIION = "qExplaination";





    // Answers Table Columns names
    public static final String A_ID = "aId";
    public static final String A_TEXT = "aText";
    public static final String CORRECT_ANSWER = "correctAnswer";
    public static final String IMAGENAME = "imageName";
    public static final String CORRECT_IMAGE = "correctImage";









    // variables to store last inserted id for the row

    long last_inserted_id_question;
    long last_inserted_id_answer;



    public DBHelperClass(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("tag", "Construtor call of db helper class");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        Log.d("tag","on Create Call of DB HELPER CLASS");

        db.execSQL("PRAGMA foreign_keys = ON;");


        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + U_NAME + " TEXT," + U_PASSWORD + " TEXT," + U_EMAIL + " TEXT,"
                + U_CONTACT_NO + " INTEGER," + U_ADDRESS + " TEXT" + ")";


        String CREATE_USERS_TEST_TABLE = "CREATE TABLE " + TABLE_USER_TEST + "("
                + U_T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + U_ID + " INTEGER ," + T_ID + " INTEGER,"
                + " FOREIGN KEY (" + U_ID + ") REFERENCES " + TABLE_USERS + "(" + U_ID + "),"
                + " FOREIGN KEY (" + T_ID + ") REFERENCES " + TABLE_TESTS + "(" + T_ID + "))";


        String CREATE_TESTS_TABLE = "CREATE TABLE " + TABLE_TESTS + "("
                + T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + T_TYPE + " TEXT,"
                + T_COMPLETE + " TEXT,"
                + T_PASS + " TEXT,"
                + T_DATE + " TEXT,"
                + T_ISVISIBLE + " TEXT,"
                + T_CORRECTANSWERS + " TEXT,"
                + T_WRONANSWERS + " TEXT )";



        String CREATE_TEST_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_TEST_QUESTIONS + "("
                + T_Q_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + T_ID + " INTEGER ," + Q_ID + " TEXT,"
                + " FOREIGN KEY (" + T_ID + ") REFERENCES " + TABLE_TESTS + "(" + T_ID + "),"
                + " FOREIGN KEY (" + Q_ID + ") REFERENCES " + TABLE_QUESTIONS + "(" + Q_ID + "))";





        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + Q_ID + " TEXT PRIMARY KEY ,"
                + Q_TOPIC + " TEXT,"
                + Q_TEXT + " TEXT,"
                + Q_FAVOURITE + " TEXT,"
                + Q_IMAGE + " TEXT ,"
                + Q_EXPLANATIION + " TEXT"
                + ")";



        String CREATE_ANSWERS_TABLE = "CREATE TABLE " + TABLE_ANSWERS + "("
                + A_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + A_TEXT + " TEXT NOT NULL ," + CORRECT_ANSWER + " TEXT," + Q_ID + " TEXT,"
                + IMAGENAME + " TEXT," + CORRECT_IMAGE + " TEXT,"
                + " FOREIGN KEY (" + Q_ID + ") REFERENCES " + TABLE_QUESTIONS + "(" + Q_ID + "))";






        // here we execute the query for create tables

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_USERS_TEST_TABLE);
        db.execSQL(CREATE_TESTS_TABLE);
        db.execSQL(CREATE_TEST_QUESTIONS_TABLE);
        db.execSQL(CREATE_QUESTIONS_TABLE);
        db.execSQL(CREATE_ANSWERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



        Log.d("tag", "OnUpgrade Call");

        // Drop older table if existed

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_TEST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS);



        // Create tables again
        onCreate(db);

    }

    // inserting data to db

    public long insertQuestions(String questionId , String questionTopic, String questionText, int favourite,String imageName,String explanation) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(Q_ID, questionId);
        value.put(Q_TOPIC, questionTopic);
        value.put(Q_TEXT, questionText);
        value.put(Q_FAVOURITE, favourite);
        value.put(Q_IMAGE, imageName);
        value.put(Q_EXPLANATIION, explanation);

        last_inserted_id_question = db.insert(TABLE_QUESTIONS, null, value);
        if (last_inserted_id_question == -1)
            return last_inserted_id_question;
        else
            return last_inserted_id_question;
    }


    public boolean insertAnswers(String answerText, String correctAnswer, String  questionId , String imageName, String correctImage) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(A_TEXT, answerText);
        value.put(CORRECT_ANSWER, correctAnswer);
        value.put(Q_ID, questionId);
        value.put(IMAGENAME, imageName);
        value.put(CORRECT_IMAGE, correctImage);

        last_inserted_id_answer = db.insert(TABLE_ANSWERS, null, value);
        if (last_inserted_id_answer == -1)
            return false;
        else
            return true;
    }


    public boolean insertFavouriteQuestion(String questionId)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(Q_FAVOURITE, 1);

        int rowsUpdated = db.update(TABLE_QUESTIONS,value,"qId = ?",new String[]{ String.valueOf(questionId) });
        Log.d("tag","rows Updated"+rowsUpdated);
        if(rowsUpdated == 0)
            return false;
        else
            return true;

    }



    public boolean insertTestRecord(String testType, String testComplete, String  testPass , String testDate, String isVisible , String totalCorrectAnswers , String totalWrongAnswers) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(T_TYPE, testType);
        value.put(T_COMPLETE, testComplete);
        value.put(T_PASS, testPass);
        value.put(T_DATE, testDate);
        value.put(T_ISVISIBLE, isVisible);
        value.put(T_CORRECTANSWERS, totalCorrectAnswers);
        value.put(T_WRONANSWERS, totalWrongAnswers);


        last_inserted_id_answer = db.insert(TABLE_TESTS, null, value);
        if (last_inserted_id_answer == -1)
            return false;
        else
            return true;
    }


    public Cursor getRandomQuestions()
    {

        SQLiteDatabase db = getReadableDatabase();
        String MY_QUERY = "SELECT * FROM " + TABLE_QUESTIONS + " where qId NOT LIKE 'CS%' order by RANDOM() limit 50 ";

        Cursor res = db.rawQuery(MY_QUERY, null);
  //      Log.d("tag","Size of Result Set is "+res.getCount());
        return res;

    }

    public Cursor getRandomCategoryQuestions(String[] selectedCategories)
    {
        Log.d("Tag","List size is "+selectedCategories[0]);


        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_QUESTIONS + " WHERE qId NOT LIKE 'CS%' and qTopic = ? OR qTopic = ? OR qTopic = ? OR qTopic = ? OR qTopic = ? " +
                "OR qTopic = ? OR qTopic = ? OR qTopic = ? OR qTopic = ? OR qTopic = ? OR qTopic = ? OR qTopic = ? OR qTopic = ? OR qTopic = ?   " +
                "ORDER BY RANDOM() LIMIT 50";
        Cursor res = db.rawQuery(query, selectedCategories);

              Log.d("tag","Size of Result Set is "+res.getCount());
        return res;

    }


    public Cursor getLearningQuestionByCategory(String selectedCategories)
    {
        Log.d("Tag"," Category Selected is "+selectedCategories);


        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.query(TABLE_QUESTIONS, null, " qTopic = ? and qId NOT LIKE 'CS%' ", new String[]{selectedCategories}, null, null, null);

        Log.d("tag","Size of Result Set is "+res.getCount());
        return res;

    }


    // select the casestudy questions from database..
    // caseStudy questions starts from CS

    public Cursor getCaseStudyQuestionByCategory(String selectedCategories)
    {


        Log.d("Tag"," Category Selected is "+selectedCategories);


        SQLiteDatabase db = getReadableDatabase();

        //String whereClause = "qId LIKE CS% AND qTopic = ?"

        Cursor res = db.query(TABLE_QUESTIONS, null, " qTopic = ? and qId LIKE 'CS%' ", new String[] { selectedCategories }, null, null, null);

        Log.d("tag","Size of Result Set is "+res.getCount());
        return res;

    }

    public Cursor getAnswersForQuestionId(String qId)
    {

        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.query(TABLE_ANSWERS , null , "qId = ?",  new String[] { qId }, null, null, null);
        Log.d("tag","Size of Result Set is "+res.getCount());
        return res;

    }

    public Cursor getProgress()
    {

        SQLiteDatabase db = getReadableDatabase();
        String MY_QUERY = "select  sum(tComplete) as totaltests  , sum(totalCorrectAnswers) as correctAnswers , sum(totalWrongAnswers)as wrongAnswers ,\n" +
                "sum(tPass) as totalPass  from  " + TABLE_TESTS + " where "+ T_ISVISIBLE +" = 1 AND "+ T_COMPLETE + " =  1 " ;
        Cursor res = db.rawQuery(MY_QUERY, null);
          Log.d("tag","Size of Result Set is "+res.getCount());
        return res;

    }

    public Cursor getFavouriteQuestions()
    {

        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.query(TABLE_QUESTIONS, null, " qFavourite == 1 ", null, null, null, null);
        Log.d("tag","Size of Result Set is "+res.getCount());
        return res;


    }

    public boolean isTableExists(String tableName) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public boolean isTableContainsData(String tableName) {

        SQLiteDatabase db = getReadableDatabase();
        String count = "SELECT count(*) FROM "+ tableName;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0) {
            Log.d("Tag"," Number of rows return"+icount);
            return true;
        }
        else {
//populate table
            Log.d("tag"," No data exist in tables ");
            return false;
        }


    }

}
