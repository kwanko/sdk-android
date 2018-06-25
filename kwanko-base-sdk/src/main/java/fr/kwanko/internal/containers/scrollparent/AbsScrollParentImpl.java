package fr.kwanko.internal.containers.scrollparent;

import android.view.View;

/**
 * SourceCode
 * Created by erusu on 4/10/2017.
 */

abstract class AbsScrollParentImpl<T extends View> implements ScrollParent {

    final T parent;

    AbsScrollParentImpl(T parent){
        this.parent = parent;
    }

}
