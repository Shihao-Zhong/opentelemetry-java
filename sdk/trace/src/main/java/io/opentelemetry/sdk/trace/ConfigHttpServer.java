package io.opentelemetry.sdk.trace;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ConfigHttpServer extends Thread {
  @Override
  public void run() {
    try {
      HttpServer server = HttpServer.create(new InetSocketAddress(32154), 0);
      server.createContext("/config", new ConfigHandler());
      server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
      server.start();
    } catch (IOException e) {
      return;
    }
  }

  static class ConfigHandler implements HttpHandler {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    @Override
    public void handle(HttpExchange t) throws IOException {
      Headers headers = t.getResponseHeaders();

      if (t.getRequestMethod().equals("POST")) {
        InputStreamReader i = new InputStreamReader(t.getRequestBody(), "UTF-8");
        Scanner s = new Scanner(i).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        String[] pairs = result.split("\n");

        OpenTelemetrySdkConfig.setConfig(pairs);
        String response = "update success";
        OutputStream os = t.getResponseBody();
        t.sendResponseHeaders(200, response.getBytes(CHARSET).length);
        os.write(response.getBytes(CHARSET));
        os.close();


      } else if (t.getRequestMethod().equals("GET")) {

        headers.set("Content-Type", String.format("application/json; charset=%s", CHARSET));
        byte[] rawResponseBody = OpenTelemetrySdkConfig.toResponseString()
            .getBytes(CHARSET);
        t.sendResponseHeaders(200, rawResponseBody.length);
        t.getResponseBody().write(rawResponseBody);

      } else {
        String response = "Does not support methods " + t.getRequestMethod();
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes(CHARSET));
        os.close();
      }
    }
  }
}