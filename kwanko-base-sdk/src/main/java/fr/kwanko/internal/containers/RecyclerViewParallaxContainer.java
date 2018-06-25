package fr.kwanko.internal.containers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * SourceCode
 * Created by erusu on 4/11/2017.
 */

@SuppressLint("ViewConstructor")
public class RecyclerViewParallaxContainer extends BaseParallaxContainer {

    private View parent;

    public RecyclerViewParallaxContainer(Context context, ParallaxParams params) {
        super(context, params);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onLayoutCreated();
        getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int [] location = new int[2];
                View parent = (View) getParent();
                parent.getLocationOnScreen(location);
                if(location[1] == 0){
                    return;
                }
                int y = location[1]-min;
                onScroll(y);
            }
        });
    }

    @Override
    protected void initConstants() {
        parallaxFormula.initConstants(params.adHeight,deltaMax,deltaMax,itemHeight);
    }

    @Override
    protected int computeNewY(int y) {
        return super.computeNewY(deltaMax-y);
    }

    @Override
    public Rect getParentVisibleRect() {
        Rect  out = new Rect();
        getRvParent().getGlobalVisibleRect(out);
        return out;
    }

    private View getRvParent(){
        if(parent == null){
            parent = (View) getParent().getParent();
        }
        return parent;
    }

}
