package com.thalesgroup.tcs.tai.gwcalipso;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.coap.BlockOption;
import org.eclipse.californium.core.coap.CoAP;

import org.eclipse.californium.core.coap.LinkFormat;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.MessageObserverAdapter;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.config.NetworkConfigDefaults;
import org.eclipse.californium.core.observe.ObserveNotificationOrderer;




// TODO: Auto-generated Javadoc
/**
 * The Class CoAPClient.
 */
public class CoAPClient {

    // resource URI path used for discovery
    /** The Constant DISCOVERY_RESOURCE. */
    private static final String DISCOVERY_RESOURCE = "/.well-known/core";

    // exit codes for runtime errors
    /** The Constant ERR_MISSING_METHOD. */
    private static final int ERR_MISSING_METHOD  = 1;
    
    /** The Constant ERR_UNKNOWN_METHOD. */
    private static final int ERR_UNKNOWN_METHOD  = 2;
    
    /** The Constant ERR_MISSING_URI. */
    private static final int ERR_MISSING_URI     = 3;
    
    /** The Constant ERR_BAD_URI. */
    private static final int ERR_BAD_URI         = 4;
    
    /** The Constant ERR_REQUEST_FAILED. */
    private static final int ERR_REQUEST_FAILED  = 5;
    
    /** The Constant ERR_RESPONSE_FAILED. */
    private static final int ERR_RESPONSE_FAILED = 6;
    
    /** The Constant ERR_BAD_LINK_FORMAT. */
    private static final int ERR_BAD_LINK_FORMAT = 7;

    /**
     * New request.
     *
     * @param method the method
     * @return the request
     */
    private static Request newRequest(String method) {
        if (method.equals("GET")) {
            return new Request(CoAP.Code.GET);
        } else if (method.equals("POST")) {
            return Request.newPost();
            
        } else if (method.equals("PUT")) {
            return Request.newPut();
        }  else if (method.equals("OBSERVE")) {
            return Request.newGet();
        }
        else {
            return null;
        }
    }

    /**
     * Execute.
     *
     * @param uri the uri
     * @param method the method
     * @return the string
     * @throws InterruptedException 
     */
    public static Response execute(URI uri, String method) {

        // initialize parameters
    	byte[] payload = null;
        boolean loop;

        // check if mandatory parameters specified
        if (method == null) {
            System.err.println("Method not specified");
            System.exit(ERR_MISSING_METHOD);
        }
        if (uri == null) {
            System.err.println("URI not specified");
            System.exit(ERR_MISSING_URI);
        }

        // create request according to specified method
        Request request = newRequest(method);
        if (request == null) {
            System.err.println("Unknown method: " + method);
            System.exit(ERR_UNKNOWN_METHOD);
        }

        if (method.equals("OBSERVE")) {
        	request.setObserve();
			loop = true;
			
        }

        // set request URI
        if (method.equals("DISCOVER") && (uri.getPath() == null || uri.getPath().isEmpty() || uri.getPath().equals("/"))) {
            // add discovery resource path to URI
            try {
                uri = new URI(
                        uri.getScheme(),
                        uri.getAuthority(),
                        DISCOVERY_RESOURCE,
                        uri.getQuery());

            } catch (URISyntaxException e) {
                System.err.println("Failed to parse URI: " + e.getMessage());
                System.exit(ERR_BAD_URI);
            }
        }

        request.setURI(uri);
       request.setPayload(payload);
       // request.setToken(TokenManager.getInstance().acquireToken() );
        request.getOptions().setContentFormat(MediaTypeRegistry.TEXT_PLAIN);
        request.send();
        // enable response queue in order to use blocking I/O
        try {
        	
			Response response = request.waitForResponse();
			if (response != null) {

            	System.out.println("received " + response);
                System.out.println("Time elapsed (ms): " + response.getRTT());

                // check of response contains resources
                if (response.getOptions().hasContentFormat(MediaTypeRegistry.APPLICATION_LINK_FORMAT)) {

                    String linkFormat = response.getPayloadString();
//
//                    // create resource three from link format
//                    Resource root = RemoteResource.newRoot(linkFormat);
//                  if (root != null) {
//
//                        // output discovered resources
//                        System.out.println("\nDiscovered resources:");
//                        root.prettyPrint();
//
//                    } else {
//                        System.err.println("Failed to parse link format");
//                        System.exit(ERR_BAD_LINK_FORMAT);
//                    }
                } else {

                    // check if link format was expected by client
                    if (method.equals("DISCOVER")) {
                        System.out.println("Server error: Link format not specified");
                    }
                }

            } else {

                // no response received
                System.err.println("Request timed out");
                return null;
            }
            return response;
			
			
			
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        
        // finish
        System.out.println();

        return null;
    }
}
