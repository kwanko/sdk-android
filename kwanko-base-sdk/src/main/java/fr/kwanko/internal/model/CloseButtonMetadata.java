package fr.kwanko.internal.model;

import java.io.File;
import java.io.Serializable;

import fr.kwanko.common.KwankoLog;

/**
 * SourceCode
 * Created by erusu on 6/14/2017.
 */

public class CloseButtonMetadata implements Serializable {

    private final int DEFAULT_SIZE = 40;
    private final int DEFAULT_PADDING = 8;
    private String imageSrc;
    private String size;
    private String  padding;
    private File closeButtonFile;

    private int width = -1;
    private int height = -1;
    private int paddingValue = -1;


    public CloseButtonMetadata(String imageSrc, String size, String padding) {
        this.imageSrc = imageSrc;
        this.size = size;
        this.padding = padding;
    }

    public File getCloseButtonFile() {
        return closeButtonFile;
    }

    public void setCloseButtonFile(File closeButtonFile) {
        this.closeButtonFile = closeButtonFile;
    }

    public String getImageSrc(){
        return imageSrc;
    }

    public int getWidthDp(){
        if(width>=0){
            return width;
        }
        parseSize();
        return width;
    }

    private void parseSize(){
        try {
            String[] parts = size.split("x");
            if (parts.length == 0) {
                width = DEFAULT_SIZE;
                height = DEFAULT_SIZE;
            } else {
                width = Integer.parseInt(parts[0]);
                height = Integer.parseInt(parts[1]);
            }
        }catch (NumberFormatException e){
            KwankoLog.e(e);
            width = DEFAULT_SIZE;
            height = DEFAULT_SIZE;
        } catch (NullPointerException e){
            KwankoLog.e(e);
            width = DEFAULT_SIZE;
            height = DEFAULT_SIZE;
        }
    }

    public int getHeightDp(){
        if(height >= 0){
            return height;
        }
        parseSize();
        return height;
    }

    public int getPaddingDp(){
        if(paddingValue >= 0){
            return paddingValue;
        }
        if(padding == null || padding.length() == 0 ){
            paddingValue = DEFAULT_PADDING;
            return paddingValue;
        }

        try {
            if (padding.toLowerCase().contains("px")) {
                paddingValue = Integer.parseInt(padding.substring(0, (padding.length() - 2)));
            } else {
                paddingValue = Integer.parseInt(padding);
            }

        }catch (NumberFormatException e){
            KwankoLog.e(e);
            paddingValue = DEFAULT_PADDING;
        }
        return paddingValue;
    }
}
