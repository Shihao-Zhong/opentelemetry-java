package io.opentelemetry.sdk.trace;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class OpenTelemetrySdkConfig {

  private OpenTelemetrySdkConfig() {
    ConfigHttpServer server = new ConfigHttpServer();
    server.setDaemon(true);
    server.start();

  }

  private static final OpenTelemetrySdkConfig sdkConfig = new OpenTelemetrySdkConfig();
  private final static ConcurrentHashMap<String, Boolean> config = new ConcurrentHashMap<>();
  private static final Logger LOGGER = Logger.getLogger( OpenTelemetrySdkConfig.class.getName() );

  public static OpenTelemetrySdkConfig getSdkConfig() {
    return sdkConfig;
  }

  public static void setConfig(String[] pairs) {
    for (String pair: pairs) {
      String[] data = pair.split("=");
      if (data.length == 2) {
        OpenTelemetrySdkConfig.config.put(data[0], Integer.parseInt(data[1]) == 1);
      }
    }
  }

  public static ConcurrentHashMap<String, Boolean> getConfig() {
    return OpenTelemetrySdkConfig.config;
  }

  public static boolean get(String spanName) {
    if (OpenTelemetrySdkConfig.config.containsKey("*")) {
      LOGGER.warning("incoming spanName: [" + spanName + "]");
      return OpenTelemetrySdkConfig.config.get("*");
    } else if (OpenTelemetrySdkConfig.config.containsKey(spanName)) {
      LOGGER.warning("incoming spanName: [" + spanName + "]" + OpenTelemetrySdkConfig.config.get(spanName));

      return OpenTelemetrySdkConfig.config.get(spanName);
    } else {
      LOGGER.warning("incoming spanName: [" + spanName + "]not set");
      LOGGER.warning(
          OpenTelemetrySdkConfig.config.toString() + OpenTelemetrySdkConfig.config.containsKey(
              spanName));
      return true;
    }
  }

  public static String toResponseString() {
    return OpenTelemetrySdkConfig.config.toString();
  }

}