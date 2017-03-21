package xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User-10 on 04-Oct-16.
 */
public class Question {

    String questionId;
    String topic;
    String questionText;
    String questionFavourite;
    String correctAnswer;
    String correctImage;
    String questionImage;
    String questionExplaination;
    List<Answer> answerList = new ArrayList<>();

    public String getQuestionExplaination() {
        return questionExplaination;
    }

    public void setQuestionExplaination(String questionExplaination) {
        this.questionExplaination = questionExplaination;
    }




    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }



    public String getCorrectImage() {
        return correctImage;
    }

    public void setCorrectImage(String correctImage) {
        this.correctImage = correctImage;
    }





    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }



    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }





    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionFavourite() {
        return questionFavourite;
    }

    public void setQuestionFavourite(String questionFavourite) {
        this.questionFavourite = questionFavourite;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }




}
