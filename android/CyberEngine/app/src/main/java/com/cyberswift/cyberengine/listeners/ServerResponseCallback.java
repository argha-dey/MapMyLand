package com.cyberswift.cyberengine.listeners;

import org.json.JSONObject;

public interface ServerResponseCallback {
    void onSuccess(JSONObject resultJsonObject);
    void onError();
}
