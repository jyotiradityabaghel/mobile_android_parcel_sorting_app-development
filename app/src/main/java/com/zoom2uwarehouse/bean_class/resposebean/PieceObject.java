package com.zoom2uwarehouse.bean_class.resposebean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PieceObject {

    @SerializedName("barcode")
    @Expose
    private String pieceIdBarcode;

    @SerializedName("pieceId")
    @Expose
    private String pieceId;

    @SerializedName("customerPieceId")
    @Expose
    private String customerPieceId;

    @SerializedName("status")
    @Expose
    private String status;


    public String getPieceIdBarcode() {
        return pieceIdBarcode;
    }

    public String getPieceId() {
        return pieceId;
    }

    public String getCustomerPieceId() {
        return customerPieceId;
    }

    public String getStatus() {
        return status;
    }
}
