package fr.kwanko.internal.controller;

import fr.kwanko.AdRequest;
import fr.kwanko.AdView;
import fr.kwanko.KwankoAdType;
import fr.kwanko.internal.close.OnCloseListener;

/**
 * SourceCode
 * Created by erusu on 5/23/2017.
 */

public interface AdController {

    void load(String slotId);

    void load(AdRequest request);

    void onDestroy();

    void onClose();

    void onResume();

    void onPause();

    boolean allowAutoShow();

    void onAttach();
}
