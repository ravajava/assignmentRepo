package com.assignment.camelroute.restdsl;

import com.assignment.camelroute.restdsl.Services.ProcessRemoveEvents;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.Map;

/*
ProcessRemoveEvents unit test
 */

public class RemoveEventsTest extends CamelTestSupport {
    Logger testsLog = LoggerFactory.getLogger(MatchingMessageTest.class);

    // Simple route that tests the ProcessRemoveEvents class
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:start")
                        .process(new ProcessRemoveEvents())
                        .to("mock:result");

            }
        };
    }

    @Test
    public void testRemoveEvent() throws Exception {
        ObjectMapper objectmapper = new ObjectMapper();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        // Gets the jsson with events to be cleared
        InputStream stream = loader.getResourceAsStream("Test_Payloads/event-test-payload.json");
        JSONObject testRemove = new JSONObject(objectmapper.readValue(stream, Map.class));

        //Gets the expected result
        InputStream processedStream = loader.getResourceAsStream("Test_Payloads/event-test-result.json");
        String processedRemove = IOUtils.toString(processedStream, "UTF-8");

        MockEndpoint resultEndpoint = getMockEndpoint("mock:result");
        //expects that their will only be one file and that the body of the result will match expected result
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(processedRemove.replaceAll("[^\\p{Graph}\\n\\r\\t ]", ""));
        template.sendBody("direct:start", testRemove);
        resultEndpoint.assertIsSatisfied();
    }
}
