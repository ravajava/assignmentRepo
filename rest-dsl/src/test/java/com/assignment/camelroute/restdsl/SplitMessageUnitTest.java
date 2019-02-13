package com.assignment.camelroute.restdsl;

import com.assignment.camelroute.restdsl.Services.ProcessRecords;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class SplitMessageUnitTest extends CamelTestSupport {
    Logger testsLog = LoggerFactory.getLogger(MatchingMessageTest.class);

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:start")
                        .log("Test Started")
                        .split()
                        .jsonpathWriteAsString("$.records")
                        .log("Split completed")
                        .to("mock:result");

            }
        };
    }

    @Test
    public void testMessageSplit() throws Exception {
        ObjectMapper objectmapper = new ObjectMapper();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("Test_Payloads/split-test-payload.json");
        String testJson = IOUtils.toString(stream, "UTF-8");


        MockEndpoint resultEndpoint = getMockEndpoint("mock:result");
        resultEndpoint.expectedMessageCount(3);
        template.sendBody("direct:start", testJson);
        resultEndpoint.assertIsSatisfied();
    }
}
