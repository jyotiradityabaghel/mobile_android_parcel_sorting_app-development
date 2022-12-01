package com.zoom2uwarehouse.barcode;

import com.zoom2uwarehouse.bean_class.resposebean.BarcodeResponse;
import com.zoom2uwarehouse.util.Common_Interface;

public interface Barcode_view extends Common_Interface {

    /**
     * Show data on success,
     */
    void success_response(BarcodeResponse response, String scan_code);
    /**
     * Show error ,
     */
    void error(String message);
}
