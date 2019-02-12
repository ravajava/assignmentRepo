package com.assignment.camelroute.restdsl.Containers;

import java.util.List;

public class Record {
    public Record(String transID, String transTMS, int rcNUM, String clientID, List Events) {
        this.transID = transID;
        this.transTMS = transTMS;
        this.rcNUM = rcNUM;
        this.clientID = clientID;
        this.EventList = Events;
    }



    public String getTransID() {
        return transID;
    }

    private String transID;

    public String getTransTMS() {
        return transTMS;
    }

    private String transTMS;

    public int getRcNUM() {
        return rcNUM;
    }

    private int rcNUM;

    public String getClientID() {
        return clientID;
    }

    private String clientID;



    public List getEventList() {
        return EventList;
    }

    public void setEventList(List eventList) {
        EventList = eventList;
    }

    private List EventList;

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("TransID [id=");
        builder.append(transID);
        builder.append("TransTMS=");
        builder.append(transTMS);
        builder.append("rcNUM=");
        builder.append(rcNUM);
        builder.append("ClientID=");
        builder.append(clientID);
        builder.append("]");
        return builder.toString();

    }
}
