package test;

import java.io.IOException;

import org.json.JSONException;
import org.junit.Test;

public class TestRunner {

    @Test
    public void test() throws IOException, JSONException {
	GrabberTest grabberTest = new GrabberTest();
	SentimentAnalysisTest sentTest = new SentimentAnalysisTest();
	//DataServiceTest dataServiceTest = new DataServiceTest();
	grabberTest.test();
	sentTest.test();
	//dataServiceTest.test();
    }
}
