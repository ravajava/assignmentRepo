package com.assignment.assignmentquestion.Services;


import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
This class aggregates exchanges into a single string and outputs the string as a single appended exchange
 */

public class StringAggregationStrategy implements AggregationStrategy {

    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Logger aggregationLog = LoggerFactory.getLogger(ProcessRecords.class);
        aggregationLog.info("Reached the aggregator!");

        //old exchange is always null the first time
        if(oldExchange == null) {
            aggregationLog.info("Old exchange is null.");
            aggregationLog.info("This is what the newExchange is: " + newExchange.getIn().getBody(String.class));
            return newExchange;
        }
        aggregationLog.info("This is what the oldExchange is: " + oldExchange.getIn().getBody(String.class));
        aggregationLog.info("This is what the newExchange is: " + newExchange.getIn().getBody(String.class));

        //local string to build one masters string out of. old exchange is appended and grows each time
       String finalStr = "";
       String oldStr = oldExchange.getIn().getBody().toString();
       String newStr = newExchange.getIn().getBody().toString();

       finalStr = oldStr + "\n" + newStr;
       oldExchange.getIn().setBody(finalStr);
       return oldExchange;
    }
}
