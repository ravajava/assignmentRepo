package com.assignment.camelroute.restdsl.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
This class takes a json file and removes an Event objects
 */
public class ProcessRemoveEvents implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        Logger recordsLog = LoggerFactory.getLogger(ProcessRecords.class);
        //get string from exchange
        String myString = exchange.getIn().getBody(String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(myString);
        JsonNode copiedObj = actualObj.deepCopy();

        int count = 0;
        for(JsonNode eventNode : copiedObj.findParents("event"))
        {
            if (eventNode instanceof ObjectNode)
            {
                recordsLog.info("found json parent of event <" + eventNode.toString() + ">");
                ObjectNode parent = (ObjectNode) eventNode;
                JsonNode deletedNode = parent.remove("event");
                if (deletedNode != null)
                    count++;
            }

        }

        recordsLog.info(count + " event nodes deleted");
        exchange.getIn().setBody(copiedObj.toString());
    }


}
