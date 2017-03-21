package xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by User-10 on 20-Oct-16.
 */
public class Category implements Serializable{
    String name;
    boolean isSelected;
    int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }



    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }



    public Category(String name , boolean value)
    {
        this.name = name ;
        this.isSelected = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
