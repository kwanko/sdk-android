package fr.kwanko.internal.overly;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import fr.kwanko.SupportedMediationTargets;
import fr.kwanko.common.KwankoLog;
import fr.kwanko.internal.model.AdModel;
import fr.kwanko.overly.OverlyAdListener;

/**
 * SourceCode
 * Created by erusu on 5/23/2017.
 */

public interface OverlyAdDisplayControllerFactory {

    BaseOverlyAdDisplayController createDisplayController(Context context,
                                                          AdModel adModel,
                                                          OverlyAdListener listener);

    OverlyAdDisplayControllerFactory DEFAULT_FACTORY =
            new OverlyAdDisplayControllerFactory() {
                @Override
                public BaseOverlyAdDisplayController
                                createDisplayController(Context context,
                                                        AdModel adModel,
                                                        OverlyAdListener listener) {
                    if(adModel.getMediation() != null){
                        return createMediationDisplayController(context,adModel,listener);
                    }else{
                        return new OverlyAdDisplayController(context,adModel,listener);
                    }
                }

                private BaseOverlyAdDisplayController createMediationDisplayController(
                                                            Context context,
                                                            AdModel model,
                                                            OverlyAdListener listener){
                    switch (model.getMediation().getMediationTarget()){
                        case SupportedMediationTargets.ADMOB:
                            return reflectAdMobOverlyController(context,model,listener);
                        default:
                            return null;
                    }
                }

                @SuppressWarnings("unchecked")
                private BaseOverlyAdDisplayController
                    reflectAdMobOverlyController(Context context,
                                             AdModel adModel,
                                             OverlyAdListener listener){
                    try {
                        Class<? extends BaseOverlyAdDisplayController> clazz =
                                (Class<? extends BaseOverlyAdDisplayController>)
                                        Class.forName("com.ads.kwankoandroidsdk.mediation.admob" +
                                                ".internal.AdMobOverlyController");
                        Constructor<? extends BaseOverlyAdDisplayController> conts =
                                clazz.getConstructor(Context.class,AdModel.class,
                                    OverlyAdListener.class);
                        return conts.newInstance(context,adModel,listener);
                    } catch (ClassNotFoundException e) {
                        KwankoLog.e(e);
                    }catch (ClassCastException e){
                        KwankoLog.e(e);
                    } catch (NoSuchMethodException e) {
                        KwankoLog.e(e);
                    } catch (IllegalAccessException e) {
                        KwankoLog.e(e);
                    } catch (InstantiationException e) {
                        KwankoLog.e(e);
                    } catch (InvocationTargetException e) {
                        KwankoLog.e(e);
                    }

                    return null;
                }


            };
}
