package com.zoom2uwarehouse.web_services_retrofit;

public interface Constants {

     String token = "token";

  //   String BASE_URL = "https://api.zoom2u.com";			        //	 Live for production
     String BASE_URL = "https://api-staging.zoom2u.com";			//   Staging
//   String BASE_URL = "https://api-test.zoom2u.com";               //   Test Url

     String DEVICE_TYPE = "android";

     String progress_run_summary="api/mobile/warehouse/ProgessRunsummary";

     String pickup="api/mobile/warehouse/PickupAwaitingRunsummary";

     String BookingDetailsByAWB="api/mobile/warehouse/BookingDetailsByAWBForWarehouse";

     String ForgotPassword="api/account/ForgotPassword";

     String Customers_Details="api/mobile/warehouse/CurrentUser";

     String teamListAPIForReAssign ="api/mobile/warehouse/TeamListForReassign";

     String reAssignToSelectedCourier ="api/mobile/warehouse/ReassignBarcodeToTeamMember";
}
