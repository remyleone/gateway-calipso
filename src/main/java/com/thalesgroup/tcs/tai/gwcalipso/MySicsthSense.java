package com.thalesgroup.tcs.tai.gwcalipso;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Hashtable;

class MySicsthSense {

    public static void createHashTable()
    {
        Hashtable resourceList = new Hashtable();
    }

    public static void postData(String score) {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://sense.sics.se/api/streams/71f1355b-740b-4053-be2f-cf4a4f605720");

            StringEntity input = new StringEntity(score);
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            httpClient.getConnectionManager().shutdown();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}