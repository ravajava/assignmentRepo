package com.assignment.assignmentquestion.Tests;

import com.assignment.assignmentquestion.CamelRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class TestCamelRoute extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new CamelRoute();
    }

    @Test
    public void CheckFileExistsInOutputDirectory() throws InterruptedException
    {
        Thread.sleep(5000);
        File file = new File("data/output");
        assertTrue(file.isDirectory());
    }


}
