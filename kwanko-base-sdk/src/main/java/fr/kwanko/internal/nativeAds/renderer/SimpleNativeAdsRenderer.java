package fr.kwanko.internal.nativeAds.renderer;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fr.kwanko.common.ImageLoader;
import fr.kwanko.internal.model.NativeAdModel;
import fr.kwanko.nativeads.ViewBinder;

/**
 * SourceCode
 * Created by erusu on 4/6/2017.
 */

public class SimpleNativeAdsRenderer implements NativeAdsRenderer {


    private ImageLoader loader;
    private View.OnClickListener clickListener;

    public SimpleNativeAdsRenderer(){
        loader = new ImageLoader();
    }

    @Override
    public void render(Activity context, ViewBinder binder, NativeAdModel model) {
        ViewGroup container = RendererUtils.findView(context,binder.getContainerId());
        container.setVisibility(View.VISIBLE);
        TextView title = RendererUtils.findView(context,binder.getTitleId());
        title.setText(model.getTitle());
        ImageView img = RendererUtils.findView(context,binder.getMainImageId());
        loader.loadImage(img,model.getMainImage());
        ImageView privacy = RendererUtils.findView(context,binder.getPrivacyInfoIconId());
        if(model.getPrivacyInfoIcon() != null){
            loader.loadImage(privacy,model.getPrivacyInfoIcon());
        }
        TextView mainText = RendererUtils.findView(context,binder.getMainTextId());
        mainText.setText(model.getText());
        if(clickListener != null){
            container.setOnClickListener(clickListener);
        }
    }

    @Override
    public View createView(ViewBinder binder) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        this.clickListener = listener;
    }
}
