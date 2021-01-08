package edu.utec.tools.stressify.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.utec.tools.stressify.common.AssertsHelper;
import edu.utec.tools.stressify.common.SmartHttpClient;
import edu.utec.tools.stressify.common.TimeHelper;
import edu.utec.tools.stressify.core.BaseScriptExecutor;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class ParrallelRestClient extends Thread implements BaseScriptExecutor {

  private final Logger logger = LogManager.getLogger(this.getClass());

  public String[] output;
  private CountDownLatch countDownLatch;
  private String method, url, assertScript, body;
  private HashMap<String, String> headers;

  private SmartHttpClient smartHttpClient = new SmartHttpClient();
  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd-HH:mm:ss:SSS");
  private HashMap<String, Object> response = null;

  @Override
  public void run() {
    performRequest();
    this.countDownLatch.countDown();
  }

  public void performRequest() {

    String id = UUID.randomUUID().toString();
    Date dateOnError = new Date();
    try {
      response = smartHttpClient.performRequest(method, url, body, headers);
    } catch (Exception e) {
      logger.error("Failed to execute http invocation with id: " + id, e);
      response = new HashMap<String, Object>();
      response.put("startDate", dateFormat.format(TimeHelper.millisToDate(dateOnError.getTime())));
      response.put("log", "Connection error:" + e.getMessage());
      return;
    }

    response.put("id", id);
    response.put("startDate",
        dateFormat.format(TimeHelper.millisToDate((Long) response.get("startMillisDate"))));
    response.put("endDate",
        dateFormat.format(TimeHelper.millisToDate((Long) response.get("endMillisDate"))));

    try {
      AssertsHelper.evaluateSimpleAssert((String) response.get("responseBody"), assertScript);
      response.put("asserts", true);
    } catch (Exception e) {
      logger.error("Failed to execute asserts on http response with id: " + id, e);
      logger.error("http response with id: \n" + (String) response.get("responseBody"), e);
      response.put("asserts", false);
      response.put("log", "Assert error:" + e.getMessage());
      return;
    }
  }

  public void performRequest(String url, ArrayList<HashMap<String, String>> headers, String body,
      String assertScript, String method) {
    try {

      Date start = new Date();
      long startMillis = start.getTime();

      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setDoOutput(true);

      con.setRequestMethod(method);

      // add request header
      for (HashMap<String, String> headerData : headers) {
        Iterator<?> it = headerData.entrySet().iterator();
        while (it.hasNext()) {
          Map.Entry<?, ?> pair = (Map.Entry<?, ?>) it.next();
          con.setRequestProperty("" + pair.getKey(), "" + pair.getValue());
        }
      }

      try (OutputStream os = con.getOutputStream()) {
        byte[] input = body.getBytes("utf-8");
        os.write(input, 0, input.length);
      }

      int responseCode = con.getResponseCode();
      System.out.println("\nSending '" + method + "' request to URL : " + url);
      System.out.println("Response Code : " + responseCode);

      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }

      Date now = new Date();
      long nowMillis = now.getTime();

      in.close();

      SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy:MM:ddd");
      output[0] = dateFormat1.format(start);

      SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss-SSS");
      output[1] = dateFormat2.format(start);

      output[2] = dateFormat2.format(now);
      output[3] = "" + responseCode;

      if (assertScript != null && !assertScript.equals("")) {
        output[4] = "" + evaluateAssert(response.toString(), assertScript);
      } else {
        output[4] = "-";
      }

      output[5] = "" + (nowMillis - startMillis);

    } catch (Exception ex) {
      ex.printStackTrace();
      output[3] = "Error:" + ex.getMessage();
    }
  }

  public boolean evaluateAssert(String response, String script) {

    Binding binding = new Binding();
    binding.setVariable("response", response);
    GroovyShell shell = new GroovyShell(binding);
    Boolean assertResult = (Boolean) shell.evaluate(script);

    return assertResult.booleanValue();
  }

  public void performGetRequest(String url, ArrayList<HashMap<String, String>> headers,
      String assertScript) {

    try {

      Date start = new Date();
      long startMillis = start.getTime();

      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      // optional default is GET
      con.setRequestMethod("GET");

      // add request header
      for (HashMap<String, String> headerData : headers) {
        Iterator<?> it = headerData.entrySet().iterator();
        while (it.hasNext()) {
          Map.Entry<?, ?> pair = (Map.Entry<?, ?>) it.next();
          con.setRequestProperty("" + pair.getKey(), "" + pair.getValue());
        }
      }

      int responseCode = con.getResponseCode();
      System.out.println("\nSending 'GET' request to URL : " + url);
      System.out.println("Response Code : " + responseCode);

      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }

      Date now = new Date();
      long nowMillis = now.getTime();

      in.close();

      SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy:MM:ddd");
      output[0] = dateFormat1.format(start);

      SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss-SSS");
      output[1] = dateFormat2.format(start);

      output[2] = dateFormat2.format(now);
      output[3] = "" + responseCode;

      if (assertScript != null && !assertScript.equals("")) {
        output[4] = "" + evaluateAssert(response.toString(), assertScript);
      } else {
        output[4] = "-";
      }

      output[5] = "" + (nowMillis - startMillis);

    } catch (Exception ex) {
      ex.printStackTrace();
      output[3] = "Error:" + ex.getMessage();
    }
  }

  @Override
  public Object getOutput() {
    return output;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setAssertScript(String assertScript) {
    this.assertScript = assertScript;
  }

  public void setHeaders(HashMap<String, String> headers) {
    this.headers = headers;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public void setCountDownLatch(CountDownLatch countDownLatch) {
    this.countDownLatch = countDownLatch;
  }

  @Override
  public Object getResponse() {
    return this.response;
  }

}