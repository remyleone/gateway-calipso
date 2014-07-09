package com.thalesgroup.tcs.tai.gwcalipso;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.californium.core.coap.Response;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;


// TODO: Auto-generated Javadoc
/**
 * The Class Servers.
 */
@Path("")
public class Servers {

    /**
     * Gets the root.
     *
     * @return the root
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getRoot() {

        StringBuilder answer = new StringBuilder();
        Set<String> coap_servers = Redis.smembers("coap_servers");

        for(String s: coap_servers){
            answer.append(s).append("\n");
        }
        return answer.toString();
    }

    /**
     * Gets the well known core.
     *
     * @param host the host
     * @return the well known core
     */
    @GET @Path("{coap_server}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getWellKnownCore(@PathParam("coap_server") String host) {
        String answer = null;
        // URI construction
        URI uri = null;
        try {
            uri = new URI(String.format("coap://%s/.well-known/core", host));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assert uri != null;
        System.out.println(uri.toString());
        String key_exist = Redis.redis_lookup(uri.toString());
        System.out.println("RESULTS REDIS :" + key_exist);
        if (key_exist.isEmpty()) {
            // Cache invalidation => CoAP request
            CoAPClient coAPClient = new CoAPClient();
            Response result = CoAPClient.execute(uri, "GET");
            if (result!=null)
            {
            Redis.setex(uri.toString(), 10, result.getPayloadString());
            answer = result.getPayloadString();
            }
        } else {
            answer = key_exist;
        }
        return answer;
    }

    /**
     * Gets the co ap resource.
     *
     * @param host the host
     * @param resource the resource
     * @return the co ap resource
     */
    @GET @Path("{coap_server}/{resource}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCoAPResource(
            @PathParam("coap_server") String host,
            @PathParam("resource") String resource){
        String answer = null;
        // URI construction
        URI uri = null;
        try {
            uri = new URI(String.format("coap://%s/%s", host, resource));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assert uri != null;
        System.out.println(uri.toString());
        String key_exist = Redis.redis_lookup(uri.toString());
        System.out.println("RESULTS REDIS :" + key_exist);
        if (key_exist.isEmpty()) {
            Response result = CoAPClient.execute(uri, "GET");
            System.out.println("RESULTS REDIS ");
            if (result!=null)
            {
            Redis.setex(uri.toString(), 10, result.getPayloadString());
            answer = result.getPayloadString();
            }
        }else {
            answer = key_exist;
        }
        return answer;
    }

}
