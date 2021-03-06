package com.assignment.camelroute.restdsl;

import com.assignment.camelroute.restdsl.Services.ProcessRecords;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;

/*
ProcessRecords unit test
 */

public class MatchingMessageTest extends CamelTestSupport {

    Logger testsLog = LoggerFactory.getLogger(MatchingMessageTest.class);
//Simple route that calls ProcessRecords
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:start")
                        .process(new ProcessRecords())
                        .to("mock:result");

            }
        };
    }

    @Test
    public void testSendMatchingMessage() throws Exception {
        ObjectMapper objectmapper = new ObjectMapper();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        //Gets json with events to be cleared
        InputStream stream = loader.getResourceAsStream("Test_Payloads/event-test-result.json");
        JSONObject testJson = new JSONObject(objectmapper.readValue(stream, Map.class));

        //Gets expected CSV format result
        InputStream csvstream = loader.getResourceAsStream("Test_Payloads/processed-test-payload.csv");
        String csvString = IOUtils.toString(csvstream, "UTF-8");
        csvString = csvString.replaceAll("[^\\p{Graph}\\n\\r\\t ]", "");

        MockEndpoint resultEndpoint = getMockEndpoint("mock:result");

        //Expects that their will be only one message received and that its body will be the same as expected body
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(csvString);
        template.sendBody("direct:start", testJson);
        resultEndpoint.assertIsSatisfied();
    }


}
