package com.thalesgroup.tcs.tai.gwcalipso;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.Vector;

@Singleton
@Path("servers")
public class ServersListing {

    private final Vector<Server> serverVector = new Vector<Server>();

    public ServersListing() {
        reload();
    }

    private void reload() {
        Server localhost = new Server();
        localhost.setName("localhost");
        localhost.setUuid(UUID.randomUUID());
        localhost.setUrl("coap://localhost");
        serverVector.addElement(localhost);

        Server coapMe = new Server();
        coapMe.setName("coap.me");
        coapMe.setUuid(UUID.randomUUID());
        coapMe.setUrl("coap://coap.me");
        serverVector.addElement(coapMe);

        Server mkovatsc = new Server();
        mkovatsc.setName("mkovatsc");
        mkovatsc.setUuid(UUID.randomUUID());
        mkovatsc.setUrl("coap://vs0.inf.ethz.ch");
        serverVector.addElement(mkovatsc);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRoot() {
        System.out.println(serverVector.toString());
        return serverVector.toString();
    }

    @GET @Path("{coap_server}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getWellKnownCore(@PathParam("coap_server") String host) {
        String answer;
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
            String result = CoAPClient.execute(uri, "GET");
            Redis.setex(uri.toString(), 10, result);
            answer = result;
        } else {
            answer = key_exist;
        }
        return answer;
    }

    @GET @Path("{coap_server}/{resource}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCoAPResource(
            @PathParam("coap_server") String host,
            @PathParam("resource") String resource){
        String answer;
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
            CoAPClient coAPClient = new CoAPClient();
            String result = CoAPClient.execute(uri, "GET");
            Redis.setex(uri.toString(), 10, result);
            answer = result;
        }else {
            answer = key_exist;
        }
        return answer;
    }

}