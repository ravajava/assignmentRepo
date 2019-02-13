package com.assignment.camelroute.restdsl.Services;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
This class takes exchanges of records and formats them properly for CSV
 */
public class ProcessRecords implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        Logger recordsLog = LoggerFactory.getLogger(ProcessRecords.class);



        //get string from exchange
        String myString = exchange.getIn().getBody(String.class);

        //convert string from exchange to a read context for easily fetching records
        ReadContext ctx = JsonPath.parse(myString);

       //Convert records into CSV
        //.replaceAll is required to remove [ and ] from entries
        StringBuffer sb = new StringBuffer();
        sb.append((String)ctx.read("$..transId").toString()
                .replaceAll("\\]", "")
                .replaceAll("\\[", "") + ",");
        sb.append((String)ctx.read("$..transTms").toString()
                .replaceAll("\\]", "")
                .replaceAll("\\[", "") + ",");
        sb.append((String)ctx.read("$..rcNum").toString()
                .replaceAll("\\]", "")
                .replaceAll("\\[", "") + ",");
        sb.append((String)ctx.read("$..clientId").toString()
                .replaceAll("\\]", "")
                .replaceAll("\\[", ""));
        //Event list is not appended as to get rid of it

        recordsLog.info("Record " + ctx.read("$..transId") + " conversion complete." );
        exchange.getIn().setBody(sb.toString());
    }


}
