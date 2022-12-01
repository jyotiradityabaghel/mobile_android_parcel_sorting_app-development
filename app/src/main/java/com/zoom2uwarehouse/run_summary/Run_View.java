package com.zoom2uwarehouse.run_summary;

import com.zoom2uwarehouse.bean_class.resposebean.Route_Pickup_Response;
import com.zoom2uwarehouse.bean_class.resposebean.Route_Progress_Response;
import com.zoom2uwarehouse.util.Common_Interface;

public interface Run_View extends Common_Interface {


    /**
     * Show data on success,
     */
    void success_progress(Route_Progress_Response data);

    /**
     * Show data on success,
     */
    void success_Pickup(Route_Pickup_Response data);
    /**
     * Show error ,
     */
    void error(String message);
}
