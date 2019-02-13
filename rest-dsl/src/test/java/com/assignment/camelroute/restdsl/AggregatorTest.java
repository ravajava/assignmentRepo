package com.assignment.camelroute.restdsl;

import com.assignment.camelroute.restdsl.Services.StringAggregationStrategy;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import java.io.File;
import java.io.InputStream;

/*
    StringAggregationStrategy unit test
 */

public class AggregatorTest extends CamelTestSupport {
    Logger testsLog = LoggerFactory.getLogger(MatchingMessageTest.class);

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            //Simple route that takes in a series of JSON objects and then calls the StringAggregationStrategy
            @Override
            public void configure() {
                from("direct:start")
                        .aggregate (constant(true), new StringAggregationStrategy())
                        .completionSize(10)
                        .completionInterval(5000)
                        .to("mock:result");

            }
        };
    }

    @Test
    public void testSendMatchingMessage() throws Exception {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        //Gets the expected result
        InputStream csvTeststream = loader.getResourceAsStream("Test_Payloads/aggregated-test-payload.csv");
        String csvTestString = IOUtils.toString(csvTeststream, "UTF-8");
        csvTestString = csvTestString.replaceAll("[^\\p{Graph}\n\r\t ]", "");

        //Expects that result will be the same as expected result
            //Makes sure only one file is put out
        MockEndpoint resultEndpoint = getMockEndpoint("mock:result");
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(csvTestString);


        //Gets the series of three separate objects that will be tested against
        File folder = new ClassPathResource("Test_Payloads/AggregatorPayload").getFile();
        File [] files = folder.listFiles();
        String folderPath = folder.toURI().toString();
        //Loops through folder and sends each file into the route
            //Converts file path into a relative URI to get files
        for(File f: files)
        {
            String relativePath = f.toURI().toString();
            relativePath = relativePath.replaceAll(folderPath, "");
            relativePath = "Test_Payloads/AggregatorPayload/" + relativePath;

            testsLog.info("Relative path = " + relativePath);

            InputStream csvProcessedStream = loader.getResourceAsStream(relativePath);
            String csvProcessedString = IOUtils.toString(csvProcessedStream, "UTF-8");
            csvProcessedString = csvProcessedString.replaceAll("[^\\p{Graph}\n\r\t ]", "");
            template.sendBody("direct:start", csvProcessedString);
        }



        resultEndpoint.assertIsSatisfied();
    }
}
