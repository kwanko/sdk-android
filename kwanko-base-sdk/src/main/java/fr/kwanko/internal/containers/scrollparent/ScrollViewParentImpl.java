package fr.kwanko.internal.containers.scrollparent;

import android.graphics.Rect;
import android.widget.ScrollView;

/**
 * SourceCode
 * Created by erusu on 4/10/2017.
 */

class ScrollViewParentImpl extends AbsScrollParentImpl<ScrollView> {

    ScrollViewParentImpl(ScrollView parent) {
        super(parent);
    }

    @Override
    public int getScrollY() {
        return parent.getScrollY();
    }

    @Override
    public void scrollTo(int newX, int newY) {
        parent.scrollTo(newX,newY);
    }

    @Override
    public void getGlobalVisibleRect(Rect out) {
        parent.getGlobalVisibleRect(out);
    }
}
