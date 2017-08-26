package com.adeglo.lagosJavaDevelopers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class HUinvoker {

    public String getResponseFromUrl(String requestURL,
                                   HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
          //  conn.setDoInput(true);
          //  conn.setDoOutput(true);


          /**  OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostStr(postDataParams));

            writer.flush();
            writer.close();
            os.flush();
            os.close();**/
            InputStream is = new BufferedInputStream(conn.getInputStream());
               BufferedReader br=new BufferedReader(new InputStreamReader(is));
               StringBuilder bui = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                bui.append(line);
            }

            response = bui.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getPostStr(HashMap<String, String> params)  {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            try{
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));}catch(Exception e){}
        }

        return result.toString();
    }
}
