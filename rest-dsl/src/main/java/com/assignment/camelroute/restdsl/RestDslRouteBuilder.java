package com.assignment.camelroute.restdsl;

import com.assignment.camelroute.restdsl.Services.ProcessRecords;
import com.assignment.camelroute.restdsl.Services.ProcessRemoveEvents;
import com.assignment.camelroute.restdsl.Services.StringAggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;


@Component
public class RestDslRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        /**
         * Use 'restlet', which is a very simple component for providing REST
         * services. Ensure that camel-restlet or camel-restlet-starter is
         * included as a Maven dependency first.
         */
        restConfiguration()
                .component("restlet")
                .host("localhost").port("8080")
                .bindingMode(RestBindingMode.auto);

        /**
         * Configure the REST API (POST, GET, etc.)
         */


        rest().path("/api").consumes("application/json")
                .post("/convert").to("direct:CSVconvert");

        from("direct:CSVconvert")
                .log("Started JSON to CSV conversion route...")
                .split().jsonpathWriteAsString("$.records")
                //this isn't necessary, but it's being done in case it is wanted regardless
                .process(new ProcessRemoveEvents())
                .setHeader(Exchange.FILE_NAME, new SimpleExpression("${exchangeId}.csv"))
                .process(new ProcessRecords())
                //json node delete
                .aggregate (constant(true), new StringAggregationStrategy())
                .completionSize(10)
                .completionInterval(15000)
                .log("Writing to file...")
                //Writes result to relative data/output folder
                .to("file:data/output");


    }
}
