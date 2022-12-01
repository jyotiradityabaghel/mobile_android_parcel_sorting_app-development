package com.zoom2uwarehouse.re_assign_delivery;

import com.zoom2uwarehouse.bean_class.resposebean.BarcodeResponse;
import com.zoom2uwarehouse.util.Common_Interface;

public interface ReAssignDelivery_View extends Common_Interface {

    /**
     * Show data on success,
     */
    void success_response(ResponseModel_TeamListFor_ReAssign response);

    void success_response_To_ReassignCourier(ResponseModel_ReAssignCourier responseModelReAssignCourier, String barcode);
    /**
     * Show error ,
     */
    void error(String message);
}
