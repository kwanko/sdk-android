package fr.kwanko.nativeads;

import android.support.v4.util.ArrayMap;
import android.util.SparseIntArray;

import java.util.Map;

import fr.kwanko.common.Preconditions;

/**
 * SourceCode
 * Created by erusu on 4/6/2017.
 */

public class ViewBinder {

    private int layoutId;
    private int titleId;
    private int mainTextId;
    private int iconId;
    private int mainImageId;
    private int privacyInfoIconId;
    private Map<String,Integer> extras;
    private int containerId;

    private ViewBinder(Builder builder) {
        layoutId = builder.layoutId;
        titleId = builder.titleId;
        mainTextId = builder.mainTextId;
        iconId = builder.iconId;
        mainImageId = builder.mainImageId;
        privacyInfoIconId = builder.privacyInfoIconId;
        extras = builder.extras;
        containerId = builder.containerId;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public int getMainTextId() {
        return mainTextId;
    }

    public void setMainTextId(int mainTextId) {
        this.mainTextId = mainTextId;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getMainImageId() {
        return mainImageId;
    }

    public void setMainImageId(int mainImageId) {
        this.mainImageId = mainImageId;
    }

    public int getPrivacyInfoIconId() {
        return privacyInfoIconId;
    }

    public void setPrivacyInfoIconId(int privacyInfoIconId) {
        this.privacyInfoIconId = privacyInfoIconId;
    }

    public Map<String, Integer> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Integer> extras) {
        this.extras = extras;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public static final class Builder {
        private int containerId;
        private int layoutId;
        private int titleId;
        private int mainTextId;
        private int iconId;
        private int mainImageId;
        private int privacyInfoIconId;
        private Map<String,Integer> extras;

        public Builder() {
            //empty constructor
        }

        public Builder containerId(int val){
            this.containerId = val;
            return this;
        }

        public Builder layoutId(int val) {
            layoutId = val;
            return this;
        }

        public Builder titleId(int val) {
            titleId = val;
            return this;
        }

        public Builder mainTextId(int val) {
            mainTextId = val;
            return this;
        }

        public Builder iconId(int val) {
            iconId = val;
            return this;
        }

        public Builder mainImageId(int val) {
            mainImageId = val;
            return this;
        }

        public Builder privacyInfoIconId(int val) {
            privacyInfoIconId = val;
            return this;
        }

        public Builder addExtra(String key, int res){
            Preconditions.checkNotNull(key);
            if(extras == null){
                extras = new ArrayMap<>();
            }
            extras.put(key,res);
            return this;
        }

        public ViewBinder build() {
            return new ViewBinder(this);
        }
    }
}
