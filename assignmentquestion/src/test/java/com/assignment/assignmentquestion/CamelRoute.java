package com.assignment.assignmentquestion;

import com.assignment.assignmentquestion.Services.ProcessRecords;
import com.assignment.assignmentquestion.Services.StringAggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.language.SimpleExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class CamelRoute extends RouteBuilder {
    Logger routeLog = LoggerFactory.getLogger(com.assignment.assignmentquestion.Route.CamelRoute.class);

    @Autowired
    Environment enviro;

    @Override
    public void configure() throws Exception {

        from( "file:data/dev/input?noop=true")
                .log("Timer Invoked in " + enviro.getProperty("message"))
                .split().jsonpathWriteAsString ("$.records")
                .setHeader(Exchange.FILE_NAME, new SimpleExpression("${exchangeId}.json"))
                .process(new ProcessRecords())

                .aggregate (constant(true), new StringAggregationStrategy())
                .completionSize(10)
                .completionInterval(15000)

                .to("file:data/output");




    }
}
