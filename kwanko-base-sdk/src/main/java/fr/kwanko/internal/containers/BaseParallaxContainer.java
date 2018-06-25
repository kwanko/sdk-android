package fr.kwanko.internal.containers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import fr.kwanko.common.KwankoLog;
import fr.kwanko.internal.KwankoWebView;
import fr.kwanko.internal.model.AdModel;

/**
 * SourceCode
 * Created by erusu on 3/30/2017.
 */

public abstract class BaseParallaxContainer extends BaseContainer {

    private static final boolean USE_COLORS = false;
    private KwankoWebView webView;
    protected int itemHeight;
    protected int min;
    private int max;
    protected int itemY;
    private ScrollView innerScrollView;
    private LinearLayout innerLayout;
    protected ParallaxParams params;
    protected int deltaMax;
    protected float calculatedTopHeight;
    private LinearLayout.LayoutParams compensationViewLayoutParams;
    private LinearLayout.LayoutParams topCompensationViewLayoutParams;
    private int newY;
    protected ParallaxFormula parallaxFormula;

    public BaseParallaxContainer(Context context, ParallaxParams params) {
        super(context);
        this.params = params;
        parallaxFormula = new ParallaxFormula();
    }

    public void onLayoutCreated() {
        updateMinMax(getParentVisibleRect());
        updateHeight();
        updateItemY();
        updateLayoutParams();
        updateHeight();
        if(innerScrollView != null){
            innerScrollView.scrollTo(0,deltaMax + itemHeight);//for initial scroll
        }
    }

    private void updateItemY(){
        itemY = getAdView().getTop();
    }

    private void updateLayoutParams(){
        calculatedTopHeight = parallaxFormula.ih(itemHeight);
        if(topCompensationViewLayoutParams != null){
            topCompensationViewLayoutParams.height = (int)calculatedTopHeight;
        }
        if(compensationViewLayoutParams != null){
            compensationViewLayoutParams.height = (int)calculatedTopHeight;
        }
    }

    private void updateMinMax(Rect out){
        min = out.top;
        max = out.bottom;
        deltaMax = max - min;
    }

    private void updateHeight(){
        itemHeight = getAdView().getLayoutParams().height;
    }

    @Override
    public void addContentView(View view) {
        webView = (KwankoWebView) view;
        configureWebView();
        updateHeight();
        innerScrollView = new ScrollView(getContext());
        innerScrollView.setVerticalScrollBarEnabled(false);
        innerLayout = new LinearLayout(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerScrollView.addView(innerLayout,layoutParams);
        final LinearLayout.LayoutParams webViewLayoutParams =
                new LinearLayout.LayoutParams(
                        params.adWidth,
                        params.adHeight);
        webViewLayoutParams.gravity = Gravity.CENTER;
        innerLayout.addView(webView,webViewLayoutParams);
        View compensationViewBottom = new View(getContext());
        compensationViewLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int)calculatedTopHeight);
        innerLayout.addView(compensationViewBottom,compensationViewLayoutParams);
        View compensationViewTop = new View(getContext());
        topCompensationViewLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int)calculatedTopHeight);
        innerLayout.addView(compensationViewTop,0,topCompensationViewLayoutParams);

        if(USE_COLORS){
            compensationViewTop.setBackgroundColor(Color.BLUE);
            compensationViewBottom.setBackgroundColor(Color.RED);
        }
        innerScrollView.scrollTo(0,deltaMax + itemHeight);
        super.addContentView(innerScrollView);
    }

    public void onScroll(int y) {
        updateMinMax(getParentVisibleRect());
        updateItemY();
        newY = computeNewY(y);
        innerScrollView.scrollTo(0, newY);
    }

    protected void initConstants(){
        parallaxFormula.initConstants(params.adHeight ,deltaMax,itemY,itemHeight);
    }

    protected int computeNewY(int x){
        initConstants();
        int newY = Math.round(parallaxFormula.compute(x));
        KwankoLog.d("f("+x+") = "+newY);
        return newY;
    }

    private void configureWebView(){
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
    }

    public static class ParallaxParams{

        int adWidth;
        protected int adHeight;

        public ParallaxParams(AdModel model){
            adWidth = model.getAdWidth();
            adHeight = model.getAdHeight();
        }
    }

    public abstract Rect getParentVisibleRect();

    @Override
    protected void onSetContentController() {
        //ignore
    }
}
