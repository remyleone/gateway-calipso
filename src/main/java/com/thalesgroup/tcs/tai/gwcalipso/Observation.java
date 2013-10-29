package com.thalesgroup.tcs.tai.gwcalipso;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.UUID;

@XmlRootElement
class Observation {

    /* Values */

    ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    @XmlElement(name="url")
    public String url;

    @XmlElement(name="observingStatus")
    public Boolean observingStatus;

    @XmlElement(name="uuid")
    public UUID uuid;

    @XmlElement(name="mostRecentValue")
    public String mostRecentValue;

    Date getMostRecentUpdate() {
        return mostRecentUpdate;
    }

    void setMostRecentUpdate(Date mostRecentUpdate) {
        this.mostRecentUpdate = mostRecentUpdate;
    }

    public Date mostRecentUpdate;

    /* Getter and setter */

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getObservingStatus() {
        return observingStatus;
    }

    public void setObservingStatus(Boolean observingStatus) {
        this.observingStatus = observingStatus;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


    public String getMostRecentValue() {
        return mostRecentValue;
    }

    public void setMostRecentValue(String mostRecentValue) {
        this.mostRecentValue = mostRecentValue;
    }

    @Override
    public String toString(){
        try {
            // takes advantage of toString() implementation to format {"a":"b"}
            return new JSONObject()
                    .put("uuid", uuid)
                    .put("url", url)
                    .put("observing_status", observingStatus)
                    .put("most_recent_value", mostRecentValue).toString();
        } catch (JSONException e) {
            return null;
        }
    }
}