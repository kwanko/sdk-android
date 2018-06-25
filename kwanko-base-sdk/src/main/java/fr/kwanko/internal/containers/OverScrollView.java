package fr.kwanko.internal.containers;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * SourceCode
 * Created by erusu on 5/4/2017.
 */

public class OverScrollView extends ScrollView {

    private View topView;
    private View bottomView;
    private int contentHeight = -1;

    public OverScrollView(Context context) {
        super(context);
    }

    public OverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void scrollYTo( int y) {
        if(!scrollPossible()){
            super.scrollTo(0,y);
            return;
        }
        if(contentHeight == -1){
            computeContentHeight();
        }
        if(y < 0){
            underScrollY(y);
        }else if(y > contentHeight){
            overScrollY(y);
        }else {
            int newY = viewHeight(topView) + y;
            super.scrollTo(0, newY);
        }
    }

    private int viewHeight(View view){
        if(view == null){
            return 0;
        }
        return view.getLayoutParams().height;
    }

    private void computeContentHeight(){
        getChildAt(0).measure(0,0);
        contentHeight = getChildAt(0).getMeasuredHeight();
    }

    private boolean scrollPossible(){
        return getChildCount() >0 && getChildAt(0) instanceof LinearLayout;
    }

    private void underScrollY(int i){
        if(topView == null){
            topView = createAndInjectView(0);
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) topView.getLayoutParams();
        params.height = Math.abs(i);
        topView.setLayoutParams(params);
        super.scrollTo(0,0);
    }

    private View createAndInjectView(int index){
        View view = new View(getContext());
        view.setBackgroundColor(Color.RED);
        ((ViewGroup)getChildAt(0)).addView(view,index);
        return view;
    }

    private void overScrollY(int i){
        if(bottomView == null){
            bottomView = createAndInjectView(((ViewGroup)getChildAt(0)).getChildCount());
        }

        int newY = viewHeight(topView) + i;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bottomView.getLayoutParams();
        params.height = newY - contentHeight;
        super.scrollTo(0,newY);
    }
}
