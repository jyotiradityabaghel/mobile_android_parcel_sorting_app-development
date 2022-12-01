package com.zoom2uwarehouse.util;

/**@author avadhesh mourya
 * Created by ubuntu on 14/2/18.
 */

public interface Common_Interface {

    /**
     * Show the progress dialog when network operation is running in background.
     */
    void showProgress();

    /**
     * Dismiss progress dialog when network operation executed.
     */
    void hideProgress();
}
