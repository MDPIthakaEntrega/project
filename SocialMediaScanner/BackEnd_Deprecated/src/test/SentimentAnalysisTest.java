package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import service.SentimentStruct;
import service.Server;

public class SentimentAnalysisTest {

    @Test
    public void test() throws AssertionError {

	String positiveFlag = "positive";
	String negativeFlag = "negative";
	String positiveReview = "Food at Zingermans is delicious!";
	String negativeReview = "Food at Zingermans is aweful!";
	SentimentStruct sentStructPositive = Server.sentimentAnalyze(positiveReview);
	SentimentStruct sentStructNegative = Server.sentimentAnalyze(negativeReview);
	try {
	    assertTrue("The sentiment result for '" + positiveReview + "' should be " + positiveFlag,
		    sentStructPositive.getFeeling().equals(positiveFlag));
	    assertTrue("The sentiment result for '" + negativeReview + "' should be " + negativeFlag,
		    sentStructNegative.getFeeling().equals(negativeFlag));
	} catch (AssertionError e) {
	    System.out.println(e.getMessage());
	    throw e;
	}
    }

}
