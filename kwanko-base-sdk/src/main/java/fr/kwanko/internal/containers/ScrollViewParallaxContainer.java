package fr.kwanko.internal.containers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.view.ViewTreeObserver;

import fr.kwanko.internal.containers.scrollparent.ScrollParent;

/**
 * SourceCode
 * Created by erusu on 4/11/2017.
 */

@SuppressLint("ViewConstructor")
public class ScrollViewParallaxContainer extends BaseParallaxContainer {

    private ScrollParent parent;
    private int y;

    public ScrollViewParallaxContainer(Context context, ParallaxParams params) {
        super(context, params);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        parent = ScrollParent.Factory.createScrollParent(this);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onLayoutCreated();
            }
        });
        initScrollView();
    }


    private void initScrollView(){
        getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                y = parent.getScrollY();
                if(y > itemY - deltaMax && y < itemY + itemHeight) {
                    onScroll(y);
                }
            }
        });
    }

    @Override
    public Rect getParentVisibleRect() {
        Rect out = new Rect();
        parent.getGlobalVisibleRect(out);
        return out;
    }
}
