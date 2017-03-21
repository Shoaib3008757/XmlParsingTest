package xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses;

/**
 * Created by User-10 on 04-Oct-16.
 */
public class Answer {

    int answerId;
    String answerText;
    String imageName;
    String  correctAnswer;
    String correctImage;

    public String getCorrectImage() {
        return correctImage;
    }

    public void setCorrectImage(String correctImage) {
        this.correctImage = correctImage;
    }




    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }






    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }



    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }



}
