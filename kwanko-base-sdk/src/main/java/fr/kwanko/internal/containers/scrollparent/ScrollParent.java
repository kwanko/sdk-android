package fr.kwanko.internal.containers.scrollparent;

import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;
import android.widget.ScrollView;

import fr.kwanko.common.Preconditions;

/**
 * SourceCode
 * Created by erusu on 4/10/2017.
 */

public interface ScrollParent {

    int getScrollY();
    void scrollTo(int newX, int newY);
    void getGlobalVisibleRect(Rect out);

    class Factory{
        public static ScrollParent createScrollParent(View view){
            Preconditions.checkNotNull(view);
            if(view.getParent().getParent().getParent() instanceof ScrollView){
                return new ScrollViewParentImpl(
                        (ScrollView) view.getParent().getParent().getParent());
            }
            if(view.getParent().getParent().getParent() instanceof NestedScrollView){
                return new NestedScrollViewParentImpl(
                        (NestedScrollView) view.getParent().getParent().getParent());
            }
            throw new IllegalArgumentException("view of type = "+view.getClass().getName() +
                    " is not supported");
        }
    }
}
