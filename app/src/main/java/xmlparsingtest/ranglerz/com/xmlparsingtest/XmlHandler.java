package xmlparsingtest.ranglerz.com.xmlparsingtest;

import android.util.Log;
import android.widget.ListView;
import android.widget.Switch;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Answer;
import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Question;

/**
 * Created by User-10 on 01-Aug-16.
 */
public class XmlHandler {


    // tags for xml

    public QuestionAnswer ParseAndStoreXML(XmlPullParser xmlPullParser) throws XmlPullParserException {



        int event = 0;
        String text= null;
         event = xmlPullParser.getEventType();

        // TODO: booleans to check if the parser is in specific tag or not..

        boolean inQuestionTag = false;
        boolean inAnswerTag = false;
        boolean isCorrectAnswer = false;
        boolean inGraphicTag = false;
        boolean inGraphicTagQuestion = false;
        boolean inExplainationTag = false;
        // this tag only found in caseStudy Files...
        boolean inPartTag = false;

       // object for store data
        QuestionAnswer questionAnswer = null;
        Answer answer = null;
        Question question =  null;

        // get last inserted question id
        long lastInsertedQuestionId = 0;

        try{

            while (event != XmlPullParser.END_DOCUMENT)
            {
                String name = xmlPullParser.getName();
              //  Log.d("tag","Name is "+name);

                switch(event)
                {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.TEXT:
                       // Log.d("tag","Text Is called");
                        text = xmlPullParser.getText();
                        break;



                    case XmlPullParser.START_TAG:
                        if(name.equalsIgnoreCase("QF"))
                        {
                            // creates a new instance of the Class
                            // because a new Question is started

                            questionAnswer = new QuestionAnswer();
                            question = new Question();
                            Log.d("tag","Questions is Started");
                            question.setQuestionId(xmlPullParser.getAttributeValue(null,"id"));
                            question.setTopic(xmlPullParser.getAttributeValue(null,"topic"));
                            questionAnswer.category = xmlPullParser.getAttributeValue(null,"topic");
                            Log.d("tag","Category is "+question.getTopic());

                        }

                        else if(name.equalsIgnoreCase("question")) {
                            inQuestionTag = true;
                        }

                        else if(name.equalsIgnoreCase("explanation")) {
                            inExplainationTag = true;
                           // because we read the question  text now we only want to read ExplanationText..
                            inQuestionTag = false;
                        }

                        else if(name.equalsIgnoreCase("part")) {

                            inExplainationTag = false;
                            // because we read the question  text now we only want to read ExplanationText..
                            inQuestionTag = false;
                            inPartTag = true;

                        }


                        else if(name.equalsIgnoreCase("answer"))
                        {
                            answer = new Answer();

                            inAnswerTag = true;
                            String correctAnswer = xmlPullParser.getAttributeValue(null,"correct");
                            if(correctAnswer.equals("yes"))
                            {
                                isCorrectAnswer = true;
                            }



                        }

                        else if(name.equalsIgnoreCase("graphic"))
                        {
                            if(inQuestionTag)
                                inGraphicTagQuestion = true;

                            if(inAnswerTag)
                            inGraphicTag = true;



                        }



                        break;

                    case XmlPullParser.END_TAG:

                        if(name.equalsIgnoreCase("QF"))
                        {

                            // add object to the list
                          //  questionAnswers.add(questionAnswer);
                            Log.d("tag","Questions is End");
                         //   Log.d("tag","Size of List is "+questionAnswers.size());


                        }

                        else if(name.equalsIgnoreCase("explanation"))
                        {
                            inExplainationTag = false;
                        }

                        else if(name.equalsIgnoreCase("part"))
                        {
                            if(inPartTag)
                            {
                                question.setQuestionExplaination(text);
                                Log.d("tag","explaiantion text is"+text.toString().trim());
                            }

                            inPartTag = false;
                        }

                        else if(name.equals("text"))
                        {



                            if(inQuestionTag) {
                                String questionText = text;
                                question.setQuestionText(questionText);
                                questionAnswer.setQuestionText(questionText);
                                Log.d("tag", "Question is " + questionAnswer.getQuestionText());
                            }

                            if(inExplainationTag)
                            {
                                question.setQuestionExplaination(text);
                                Log.d("tag","explaiantion text is"+text.toString().trim());
                            }


                            if(inAnswerTag)
                            {
                                // to get the answer

                                String answerText = text;
                                answer.setAnswerText(answerText);
                                questionAnswer.setAnswerText(answerText);
                                Log.d("tag","Answer is "+questionAnswer.getAnswerText());
//

                            }




                            if(isCorrectAnswer)
                            {
                                // to get the correct answer

                                String correctAnswer = text;
                                // set the correct answer
                                answer.setCorrectAnswer(correctAnswer);

                                questionAnswer.setCorrectAnswer(correctAnswer);
                                Log.d("tag"," Correct Answer is "+questionAnswer.getCorrectAnswer());

                            }







                        }

                        else if(name.equalsIgnoreCase("question")) {
                            // add question object to list
                            inQuestionTag = false;
                            inGraphicTagQuestion = false;
                            Splash.questionList.add(question);
                           lastInsertedQuestionId =   Splash.dbHelperClass.insertQuestions(question.getQuestionId(),question.getTopic(), question.getQuestionText(),0,question.getQuestionImage(),question.getQuestionExplaination());

                        }

                        else if(name.equalsIgnoreCase("graphic"))
                        {

                            if(inQuestionTag)
                            if(inGraphicTagQuestion)
                            {
                                if(text != null && !text.isEmpty()) {
                                    Log.d("tag", "ParseAndStoreXML: In Question Graphic tag graphic name is " + text);
                                    question.setQuestionImage(text);
                                }

                            }

                            if(inAnswerTag)
                            if(inGraphicTag)
                            {

                                Log.d("tag"," Is in Graphic tag ");
                                if(text != null && !text.isEmpty())
                                {
                                    if(isCorrectAnswer)
                                    {
                                        answer.setCorrectImage(text);
                                    }
                                    answer.setImageName(text);
                                    Log.d("tag", "Image is " + answer.getImageName());
                                }
                            }


                            // because we have multiple answer and multiple images..
                            inGraphicTag = false;
                        }
                        else if(name.equalsIgnoreCase("answer"))
                        {
                            inAnswerTag = false;
                            isCorrectAnswer = false;
                            Splash.answerList.add(answer);
                            Splash.dbHelperClass.insertAnswers(answer.getAnswerText(),answer.getCorrectAnswer(),question.getQuestionId(),answer.getImageName(),answer.getCorrectImage());
                        }

                        else if(name.equalsIgnoreCase("answers"))
                    {
                       // Log.d("tag","All Answers End");
                    }

                        break;




                }

                event = xmlPullParser.next();


            }


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return questionAnswer;

    }




}
