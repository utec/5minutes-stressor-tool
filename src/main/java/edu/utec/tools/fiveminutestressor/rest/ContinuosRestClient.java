package edu.utec.tools.fiveminutestressor.rest;

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

import com.jayway.jsonpath.JsonPath;

import edu.utec.tools.fiveminutestressor.core.BaseScriptExecutor;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class ContinuosRestClient implements BaseScriptExecutor {

  public String[] output;

  public void performRequest(String method, String url, String body,
          ArrayList<HashMap<String, String>> headers, String assertScript) {

    output = new String[6];

    switch (method) {

    case "GET":
      performGetRequest(url, headers, assertScript);
      break;
    case "POST":
      performPostRequest(url, headers, body, assertScript);
      break;
    default:
      output[3] = "Error: Method not implemented yet";
      break;
    }
  }

  public void performPostRequest(String url, ArrayList<HashMap<String, String>> headers,
          String body, String assertScript) {
    try {

      Date start = new Date();
      long startMillis = start.getTime();

      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setDoOutput(true);

      con.setRequestMethod("POST");

      // add request header
      for (HashMap<String, String> headerData : headers) {
        Iterator it = headerData.entrySet().iterator();
        while (it.hasNext()) {
          Map.Entry pair = (Map.Entry) it.next();
          con.setRequestProperty("" + pair.getKey(), "" + pair.getValue());
        }
      }

      try (OutputStream os = con.getOutputStream()) {
        byte[] input = body.getBytes("utf-8");
        os.write(input, 0, input.length);
      }

      int responseCode = con.getResponseCode();
      System.out.println("\nSending 'POST' request to URL : " + url);
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
        Iterator it = headerData.entrySet().iterator();
        while (it.hasNext()) {
          Map.Entry pair = (Map.Entry) it.next();
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

}