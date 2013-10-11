gateway-calipso
===============

This gateway offers an interface between HTTP requests and CoAP
resources. When an HTTP requests is received by the gateway, it's
translated into a CoAP request. Then the gateway is going to lookup in
the cache if the information is already available. If it is then the
cached version is sent back as an answer and a communication to the
wireless sensor network is avoided. If a cached version of the
information is missing, a CoAP request is issued. When the result of the
CoAP request is available, it's stored in the cache for later use and
delivered to the client.

We use Java to generate CoAP requests and receive HTTP requests. We use
redis as a back-end to our cache.

To manage feeds of information that are managed in CoAP by using
OBSERVE, we use the pub sub feature of redis.

Because there is bindings for redis in various language, it's easy to
use redis as a data store and plug new service around it. In particular
to manage the publish subscribe.

Installation
------------

You need to have a redis server running on localhost to make it work.

  sudo apt-get install redis-server

Running
-------

To get the code running, you should use the following line:

  mvn clean compile exec:java
