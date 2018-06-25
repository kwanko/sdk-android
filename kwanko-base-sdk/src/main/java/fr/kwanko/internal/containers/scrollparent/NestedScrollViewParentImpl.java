package fr.kwanko.internal.containers.scrollparent;

import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;

/**
 * SourceCode
 * Created by erusu on 4/10/2017.
 */

class NestedScrollViewParentImpl extends AbsScrollParentImpl<NestedScrollView> {

    NestedScrollViewParentImpl(NestedScrollView parent) {
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
