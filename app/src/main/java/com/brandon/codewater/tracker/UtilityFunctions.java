package com.brandon.codewater.tracker;

import android.app.Dialog;
import android.graphics.Color;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;

/**
 * Created by brand on 2017-07-06.
 */

public class UtilityFunctions {

    /**
     * This function will activate or deactivate the BOOK NOW button based on
     * boolean parameters ?
     */
    public void buttonActivation(Button mBookNow, boolean timeCheck, boolean barberCheck){
        if(timeCheck == true && barberCheck == true){
            mBookNow.setBackgroundResource(R.color.violet_continue);
            mBookNow.setTextColor(Color.WHITE);
            mBookNow.setEnabled(true);
        }
        else{
            mBookNow.setBackgroundResource(R.color.transparent_gray);
            //mBookNow.setTextColor(getResources().getColor(R.color.gray_border));
            mBookNow.setEnabled(false);
        }
    }

    /**
     * Mode 0 = Match_parent for height and width. Mode 1 = match_parent for width ONLY
     * @param mDialogName
     * @param mode
     */
    public void setDialogAnimation(Dialog mDialogName, int mode){
        mDialogName.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        //mDialogName.getWindow().setGravity(Gravity.BOTTOM);

        Window wind = mDialogName.getWindow();
        if(mode == 0) {
            wind.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
        else{
            //mDialogName.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mDialogName.setCanceledOnTouchOutside(false);
            wind.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

}
