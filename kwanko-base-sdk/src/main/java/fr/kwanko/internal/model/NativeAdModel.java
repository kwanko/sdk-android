package fr.kwanko.internal.model;

/**
 * SourceCode
 * Created by erusu on 4/6/2017.
 */

public class NativeAdModel extends AdModel{

    public String mainImage;
    public String title;
    public String text;
    public String privacyInfoIcon;
    public String urlToOpen;

    public NativeAdModel(){
        //empty constructor
    }

    public String getMainImage() {
        return mainImage;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getPrivacyInfoIcon() {
        return privacyInfoIcon;
    }

    public String getUrlToOpen() {
        return urlToOpen;
    }
}
