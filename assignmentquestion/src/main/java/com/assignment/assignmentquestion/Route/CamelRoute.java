package com.assignment.assignmentquestion.Route;
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

/*
This class contains the camel route definition.
    Json files are taken from the input directory
    The file is then split on the records entry
    A CSV file is created
    JSON entries are converted into CSV format
    Entries are aggregated back together
        (with a completion size of 10 or every minute)
    Output is written to output directory
 */
@Component
public class CamelRoute extends RouteBuilder {
    Logger routeLog = LoggerFactory.getLogger(CamelRoute.class);

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
