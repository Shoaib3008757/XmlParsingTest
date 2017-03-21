package xmlparsingtest.ranglerz.com.xmlparsingtest.CustomAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import xmlparsingtest.ranglerz.com.xmlparsingtest.CategoryTest;
import xmlparsingtest.ranglerz.com.xmlparsingtest.MainActivity;
import xmlparsingtest.ranglerz.com.xmlparsingtest.R;
import xmlparsingtest.ranglerz.com.xmlparsingtest.ReviewAnswers;
import xmlparsingtest.ranglerz.com.xmlparsingtest.UserChoiceTakeTest;

/**
 * Created by User-10 on 21-Oct-16.
 */
public class CustomAdapterReviewAnswersCategoryTest extends ArrayAdapter<ReviewAnswers> {


    private static final String TAG = "Custom Adapter";
    Context context;
    public  int layoutResourceId;
    UserChoiceTakeTest userChoiceTakeTest;
    String questionText;
    String questionAnswer;
    String userAnswer;
    String correctImageName;

    LinearLayout parent;

    public CustomAdapterReviewAnswersCategoryTest(Context context, int resource,UserChoiceTakeTest userChoiceTakeTest) {
        super(context, resource);
        this.context  = context;
        this.layoutResourceId = resource;
        this.userChoiceTakeTest = userChoiceTakeTest;

    }



    @Override
    public int getCount() {
        if(userChoiceTakeTest.selectedAnswerText != null)
        {
            return userChoiceTakeTest.selectedAnswerText.size();
        }
        else
            return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;
        ViewHolder holder = null;

        try {
            if (item == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                item = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                holder.questionText = (TextView) item.findViewById(R.id.textViewQuestionTextReviewAnswers);
                holder.correctImage = (ImageView) item.findViewById(R.id.imageViewCorrectImage);
                parent = (ViewGroup) item.findViewById(R.id.parentLayoutSingleItemReviewAnswers);




                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }


            // set the question text ..
            questionText = CategoryTest.randomCategoryQuestions.get(position).getQuestionText().toString().trim();
            holder.questionText.setText(questionText);

            // get the correct answer of the question

            questionAnswer = CategoryTest.randomCategoryQuestions.get(position).getCorrectAnswer().toString().trim();

            userAnswer = userChoiceTakeTest.selectedAnswerText.get(position).toString().trim();

            // get correct image name
            correctImageName = CategoryTest.randomCategoryQuestions.get(position).getCorrectImage().toString().trim();






            Log.d(TAG, " getView: Image Name is " + correctImageName);



            if(userAnswer.equals(questionAnswer))
            {
                //   Log.d("tag","Correct Answer");

                holder.correctImage.setImageResource(R.drawable.ic_action_tick);
                //    parent.setBackgroundColor(context.getResources().getColor(R.color.android_green_color));

            }
            else if(userAnswer.equals(correctImageName))
            {
                //  Log.d("tag","Wrong Answer");
                holder.correctImage.setImageResource(R.drawable.ic_action_tick);
                //   parent.setBackgroundColor(context.getResources().getColor(R.color.android_red_color));
            }
            else {
                holder.correctImage.setImageResource(R.drawable.ic_action_cancel);
            }


        }

        catch (NullPointerException ex)
        {
            Log.d("tag", "Exception Occured" + ex.toString());
        }

        return item;




    }



    static class ViewHolder {
        TextView questionText;
        ImageView correctImage;
    }









}
