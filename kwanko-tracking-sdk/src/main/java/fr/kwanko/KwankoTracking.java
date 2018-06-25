package fr.kwanko;

import android.content.Context;
import android.support.annotation.NonNull;

import fr.kwanko.common.KwankoLog;
import fr.kwanko.common.Preconditions;
import fr.kwanko.database.Conversion;
import fr.kwanko.database.ConversionDatabase;
import fr.kwanko.params.KwankoConversion;
import fr.kwanko.params.KwankoConversionParamsEvaluator;
import fr.kwanko.params.KwankoRemarketing;
import fr.kwanko.params.KwankoRemarketingParamsEvaluator;
import fr.kwanko.rest.TrackingCloud;

/**
 * SourceCode
 * Created by erusu on 3/16/2017.
 */

public class KwankoTracking {

    private Context context;
    private ConversionDatabase conversionDatabase;
    private KwankoTracking(Context context){
        this.context = context;
        this.conversionDatabase = ConversionDatabase.Factory.getConversionDatabase(context);
    }

    public static KwankoTracking with(Context context){
        return new KwankoTracking(context);
    }

    public  void notifySimpleAction(@NonNull KwankoConversion conversion){
        notify(conversion,new KwankoConversionParamsEvaluator());
    }

    private void notify(KwankoConversion conversion, KwankoConversionParamsEvaluator evaluator){
        Preconditions.checkNotNull(conversion);
        evaluator.process(context, conversion);
        String trackingId = conversion.get(KwankoConversion.TRACKING_ID).toString();
        boolean conversionIsRepeatable = Boolean
                .parseBoolean(conversion.get(KwankoConversion.IS_REPEATABLE).toString());
        boolean conversionExists = conversionDatabase.existConversion(trackingId);
        if(!conversionExists){
            TrackingCloud.sendConversion(conversion);
            conversionDatabase.updateConversion(new Conversion(trackingId,conversionIsRepeatable));
            return;
        }
        boolean isRepeatable = conversionDatabase.isConversionRepeatable(trackingId);
        if(isRepeatable){
            TrackingCloud.sendConversion(conversion);
            return;
        }
        if(conversionIsRepeatable){
            TrackingCloud.sendConversion(conversion);
            conversionDatabase.updateConversion(new Conversion(trackingId,true));
        }
    }

    public void notifySaleAction(@NonNull KwankoRemarketing remarketing){
        notify(remarketing,new KwankoRemarketingParamsEvaluator());
    }
}
