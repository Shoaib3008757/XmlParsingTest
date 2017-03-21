package xmlparsingtest.ranglerz.com.xmlparsingtest.CustomAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xmlparsingtest.ranglerz.com.xmlparsingtest.CategoryTest;
import xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses.Category;
import xmlparsingtest.ranglerz.com.xmlparsingtest.R;

/**
 * Created by User-10 on 20-Oct-16.
 */


public class CustomAdapterCategoryTest extends ArrayAdapter<CategoryTest> {

    public int layoutResourceId;
    public Context context;
    ArrayList<Category> allCategories = new ArrayList<>();

    public int chacheCheckBox = 0;

    private int mSelectedPosition = -1;
    private RadioButton mSelectedRB;
    Category category;

    // last parameter ios for where the layout is used for displaying checkbox
    // in category test or whether it is used for displaying radio buttons in learn by chapter..
    public CustomAdapterCategoryTest(Context context, int resource, List<Category> allCategories,int isCacheCheckBox) {
        super(context, resource);
        this.layoutResourceId = resource;
        this.context = context;
        this.allCategories.addAll(allCategories);
        this.chacheCheckBox = isCacheCheckBox;
    }


    @Override
    public int getCount() {
        if (allCategories != null) {
            return allCategories.size();
        } else
            return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            Log.v("ConvertView", String.valueOf(position));
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();


            // set on click lsitener here because when the view is null
            // create it first time and save
            // means that the checkbox is in the mobile screen..

            if(chacheCheckBox == 1)
            {

                holder.selection = (CheckBox) convertView.findViewById(R.id.checkBoxCategoryTest);
                convertView.setTag(holder);

                holder.selection.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Category country = (Category) cb.getTag();
//                    Toast.makeText(context,
//                            "Clicked on Checkbox: " + cb.getText() +
//                                    " is " + cb.isChecked(),
//                            Toast.LENGTH_LONG).show();
                        country.setIsSelected(cb.isChecked());
                    }
                });
            }

            else
            {
                holder.selectLearning = (RadioButton) convertView.findViewById(R.id.radioButtonLearnByChapter);
                convertView.setTag(holder);

            }


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // this get method return the reference of the object
        // so any changes to the object will reflect in the arrayList...
        category = allCategories.get(position);

        if(chacheCheckBox == 1) {
            holder.selection.setText(category.getName());
            holder.selection.setChecked(category.isSelected());
            holder.selection.setTag(category);
        }
        else
        {

            // get the name from the category and display in radio Button
            // and select radio button whether it is selected or not..
            // set the tag to save the value in radioButton
            holder.selectLearning.setText(category.getName().toString());
            holder.selectLearning.setChecked(category.isSelected());
            holder.selectLearning.setTag(category);


            // now get the position of the selected radio button
            // and save the redio button that was selected by the user...
            // and set the category to selected to save it for user selection...
            holder.selectLearning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton rb = (RadioButton) v;

                    if (position != mSelectedPosition && mSelectedRB != null) {
                        mSelectedRB.setChecked(false);
                        category.setIsSelected(mSelectedRB.isChecked());
                    }

                    // use the selected RadioButton
                    // and store the selected Position..
                    mSelectedPosition = position;
                    mSelectedRB = rb;

                    // get tag and get the value and save it
                    category = (Category) mSelectedRB.getTag();
                    category.setIsSelected(mSelectedRB.isChecked());


                    // check which category is selected...
                    for (int i = 0; i < allCategories.size(); i++) {
                        Category category1 = allCategories.get(i);
                        if (category1.isSelected()) {
                            Log.d("tag","Selected Categories is "+category1.getName());
                        }
                    }

                }
            });


            // set the checking and unchecking of the radioButton...
            if(mSelectedPosition != position){
                holder.selectLearning.setChecked(false);
            }else{
                holder.selectLearning.setChecked(true);
                if(mSelectedRB != null && holder.selectLearning != mSelectedRB){
                    mSelectedRB = holder.selectLearning;
                }
            }


        }

        return convertView;

    }


    private class ViewHolder {
        CheckBox selection;
        RadioButton selectLearning;
    }

    public ArrayList<Category> getList()
    {
        return allCategories;
    }



}