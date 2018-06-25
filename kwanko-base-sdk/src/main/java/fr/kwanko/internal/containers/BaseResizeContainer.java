package fr.kwanko.internal.containers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import fr.kwanko.common.KwankoDimensionUtils;
import fr.kwanko.common.ViewUtils;
import fr.kwanko.internal.close.CloseEvents;
import fr.kwanko.internal.close.OnCloseListener;
import fr.kwanko.internal.model.AdModel;

/**
 * SourceCode
 * Created by erusu on 3/29/2017.
 */
@SuppressLint("ViewConstructor")
public class BaseResizeContainer extends BaseContainer {

    private BaseResizeParams params;

    public BaseResizeContainer(Context context, BaseResizeParams params) {
        super(context);
        this.params = params;
    }

    @Override
    protected void onSetContentController() {
    }

    @Override
    public void show() {
        if (params == null) {
            return;
        }
        if (respectServerSize(params)) {
            int[] dimens = ViewUtils.getSize(getContext(),
                    params.getAdWidth(), params.getAdHeight(),
                    params.getAdSizeStrategy());
            resizeAdWith(dimens[0], dimens[1]);
        }
        super.show();
    }

    private boolean respectServerSize(BaseResizeParams model) {
        if (model == null) {
            return false;
        }
        return model.getAdSizeStrategy() != null
                && model.getAdHeight() > 0
                && model.getAdWidth() > 0;
    }

    private void resizeAdWith(int width, int height) {
        View parentView = (View) getParent();
        parentView.getLayoutParams().height =
                KwankoDimensionUtils.scaleToPx(getContext(), height);
        parentView.getLayoutParams().width =
                KwankoDimensionUtils.scaleToPx(getContext(), width);
        parentView.requestLayout();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        params = null;
    }

    public static class BaseResizeParams {

        private int adHeight;
        private int adWidth;
        private String adSizeStrategy;

        public BaseResizeParams(AdModel model) {
            adHeight = model.getAdHeight();
            adWidth = model.getAdWidth();
            adSizeStrategy = model.getAdSizeStrategy();
        }

        int getAdHeight() {
            return adHeight;
        }

        int getAdWidth() {
            return adWidth;
        }

        String getAdSizeStrategy() {
            return adSizeStrategy;
        }
    }
}
