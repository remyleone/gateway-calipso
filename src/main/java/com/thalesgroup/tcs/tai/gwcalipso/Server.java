package com.thalesgroup.tcs.tai.gwcalipso;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URL;
import java.util.UUID;

@XmlRootElement
class Server {

    @XmlElement(name="uuid")
    private
    UUID uuid;

    @XmlElement(name="name")
    private
    String name;

    @XmlElement(name="url")
    private
    String url;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString(){
        try {
            // takes advantage of toString() implementation to format {"a":"b"}
            return new JSONObject()
                    .put("uuid", uuid)
                    .put("url", url)
                    .put("name", name).toString();
        } catch (JSONException e) {
            return null;
        }
    }

}
