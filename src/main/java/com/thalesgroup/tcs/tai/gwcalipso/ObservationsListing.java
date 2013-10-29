package com.thalesgroup.tcs.tai.gwcalipso;

import org.codehaus.jackson.map.ObjectMapper;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

@Path("observations")
@Singleton
public class ObservationsListing {

    /**
     * This is the class that is going to provide the list of all
     * observations and their status. Observations is simply doing a
     * request to a redis hash map to see what are the currently running
     * observations.
     */

    private final HashMap<Observation, Runnable> hashMap = new HashMap<Observation, Runnable>();

    public void reload() {
        Observation obs = new Observation();
        obs.setUrl("coap://localhost/currentTime");
        obs.setUuid(UUID.randomUUID());
        obs.setObservingStatus(true);

        hashMap.put(obs, new Runnable() {
            @Override
            public void run() {
                fetching("localhost", "currentTime");
            }
        });
    }

    public void fetching (String host, String resource) {
        // URI construction
        URI uri = null;
        try {
            uri = new URI(String.format("coap://%s/%s", host, resource));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assert uri != null;

//        String key_exist = Redis.redis_lookup(uri.toString());

//        if (key_exist.isEmpty()) {
//            CoAPClient coAPClient = new CoAPClient();
//            String result = coAPClient.execute(uri, "GET");
//            Redis.setex(uri.toString(), 10, result);
//            answer = result;
//        }else {
//            answer = key_exist;
//        }
//        return answer;
    }

    public ObservationsListing() {
        reload();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getObservationList() {
        return hashMap.keySet().toString();
    }

//    @GET @Path("{resource}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getCoAPResource(
//            @PathParam("coap_server") String host,
//            @PathParam("resource") String resource){
//        String answer = null;
//
//    }

        /* REST annotations */

//    @POST
//    public String addObservation(String data) throws IOException {
//        Observation obs = mapper.readValue(data, Observation.class);
//        hashMap.put(UUID.randomUUID().toString(), obs);
//        System.out.println("HERE IS OUR DATA => " + hashMap.toString());
//        return String.valueOf(hashMap.size());
//    }
}
