package com.thalesgroup.tcs.tai.gwcalipso;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("observations")
public class Observations {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getRoot() {
        return "BONJOUR";
//        StringBuilder answer = new StringBuilder();
//        Set<String> coap_servers = Redis.smembers("observations");
//        for(String s: coap_servers){
//            answer.append(s).append("\n");
//        }
//        return answer.toString();
    }
}