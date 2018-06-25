package fr.kwanko.internal.mraid.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import fr.kwanko.internal.video.BaseVideoActivity;
import fr.kwanko.internal.video.BaseVideoController;

/**
 * SourceCode
 * Created by erusu on 4/24/2017.
 */

public class MraidVideoActivity extends BaseVideoActivity implements BaseVideoController.VideoControllerListener{

    public static void startActivity(Context context, String videoSource){
        Intent intent = new Intent(context,MraidVideoActivity.class);
        intent.putExtra(MraidVideoController.VIDEO_SOURCE,videoSource);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }

    @Override
    public BaseVideoController createController() {
        return new MraidVideoController(this,getIntent(),this);
    }

    @Override
    public void setRequestOrientation(int orientation) {
        setRequestedOrientation(orientation);
    }

    @Override
    public void onFinish() {
        finish();
    }

    @Override
    public void onBackPressed() {
        //ignore
    }
}
