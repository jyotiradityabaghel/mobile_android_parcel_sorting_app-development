package com.zoom2uwarehouse.home;

import com.zoom2uwarehouse.bean_class.resposebean.Details_Customer;

import java.util.List;

public interface User_detail_view {


    /**
     * Show the progress dialog when network operation is running in background.
     */
    void user_success(Details_Customer details_customer);

    /**
     * Dismiss progress dialog when network operation executed.
     */
    void user_error(String s);
}
