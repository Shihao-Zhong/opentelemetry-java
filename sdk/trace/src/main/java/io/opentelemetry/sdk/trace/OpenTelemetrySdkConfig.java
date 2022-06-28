package io.opentelemetry.sdk.trace;

import java.util.concurrent.ConcurrentHashMap;
import org.json.simple.JSONObject;


public class OpenTelemetrySdkConfig {

  private OpenTelemetrySdkConfig() {
    ConfigHttpServer server = new ConfigHttpServer();
    server.setDaemon(true);
    server.start();

  }

  private static final OpenTelemetrySdkConfig sdkConfig = new OpenTelemetrySdkConfig();
  private static final ConcurrentHashMap<String, Boolean> config = new ConcurrentHashMap<String, Boolean>();

  public static OpenTelemetrySdkConfig getSdkConfig() {
    return sdkConfig;
  }

  public static void setConfig(JSONObject obj) {
/*
    Iterator<?> keys = obj.keySet().iterator();
    while(keys.hasNext()) {
      String key = (String) keys.next();
      OpenTelemetrySdkConfig.config.put(key, (Boolean)obj.get(key));
    }
    */
    for (Object key : obj.keySet()) {
      OpenTelemetrySdkConfig.config.put((String)key, (Boolean)obj.get(key));
    }
    /*
    obj.keySet().forEach(key ->
    {
      Boolean value = (Boolean) obj.get(key);
      OpenTelemetrySdkConfig.config.put((String)key, value);
    });
*/
  }


  public static ConcurrentHashMap<String, Boolean> getConfig() {
    return OpenTelemetrySdkConfig.config;
  }

  public static boolean get(String spanName) {
    if (OpenTelemetrySdkConfig.config.containsKey(spanName)) {
      return OpenTelemetrySdkConfig.config.get(spanName);
    } else {
      return true;
    }
  }
}


