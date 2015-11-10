package social_media_scanner.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import social_media_scanner.backend.service.SentimentStruct;
import social_media_scanner.backend.service.Server;

/*
 * Unit tests for Alchemy sentiment analysis.
 */
public class SentimentAnalysisTest extends TestCase {

	public SentimentAnalysisTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(SentimentAnalysisTest.class);
	}

	public void testSentimentAnalysis() {

		String positiveFlag = "positive";
		String negativeFlag = "negative";
		String positiveReview = "Food at Zingermans is delicious!";
		String negativeReview = "Food at Zingermans is aweful!";
		SentimentStruct sentStructPositive = Server.sentimentAnalyze(positiveReview);
		SentimentStruct sentStructNegative = Server.sentimentAnalyze(negativeReview);
			assertTrue("The sentiment result for '" + positiveReview + "' should be " + positiveFlag,
					sentStructPositive.getFeeling().equals(positiveFlag));
			assertTrue("The sentiment result for '" + negativeReview + "' should be " + negativeFlag,
					sentStructNegative.getFeeling().equals(negativeFlag));

	}

}
