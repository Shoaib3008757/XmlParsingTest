package xmlparsingtest.ranglerz.com.xmlparsingtest;

import android.util.ArrayMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by User-10 on 10-Oct-16.
 */
public class UserChoiceTakeTest implements Serializable{


    //    public int numberOfCorrectAnswers;
//    public int numberOfWrongAnswers;
    HashMap<Integer,Integer> numberOfCorrectAnswers = new HashMap<>(49);
    HashMap<Integer,Integer> numberOfWrongAnswers = new HashMap<>(49);


    // because we have to save 50 answers of the 50 questions in list..
    public LinkedHashMap<Integer , String> selectedAnswerText = new LinkedHashMap<>(49);
    // this list store the selected asnwer as flag answer and question number as key and value as answer text...
    public LinkedHashMap<Integer , String> selectedFlagAnswerText = new LinkedHashMap<>(49);



    public void setNumberOfCorrectAnswers(HashMap<Integer,Integer> numberOfCorrectAnswers) {
        this.numberOfCorrectAnswers = numberOfCorrectAnswers;
    }

    public void setNumberOfWrongAnswers(HashMap<Integer,Integer> numberOfWrongAnswers) {
        this.numberOfWrongAnswers = numberOfWrongAnswers;
    }

    public HashMap<Integer, String> getSelectedAnswerText() {
        return selectedAnswerText;
    }

    public void setSelectedAnswerText(LinkedHashMap<Integer, String> selectedAnswerText) {
        this.selectedAnswerText = selectedAnswerText;
    }











}
