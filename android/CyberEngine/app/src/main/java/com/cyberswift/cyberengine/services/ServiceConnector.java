package com.cyberswift.cyberengine.services;

public abstract class ServiceConnector {

    //private static final String baseURL = "http://192.168.1.160/CyberEngineService_Staging/Service_Mobile.svc/";

    //private static final String baseURL = "http://192.168.1.91/CyberEngineService_Staging/Service_Mobile.svc/";

    private static final String baseURL = "http://192.168.1.77/CyberEngineService_Staging/Service_Mobile.svc/";

//    private static final String baseURL = "http://62.151.177.17/CyberEngine_Test_Service/Service_Mobile.svc/"; // Live Staging

    //private static final String baseURL = "http://62.151.177.17/CyberEngineService/Service_Mobile.svc/"; // Production


    private static final String baseImageURL = "http://192.168.1.77/CyberEngineService_Staging/PROFILE_IMAGE/";

//    private static final String baseImageURL = "http://62.151.177.17/CyberEngine_Test_Service/PROFILE_IMAGE/"; // Live Staging

    //private static final String baseImageURL = "http://62.151.177.17/CyberEngineService/PROFILE_IMAGE/"; // Production


    static String getBaseURL() {
        return baseURL;
    }

    public static String getBaseImageURL() {
        return baseImageURL;
    }

}
