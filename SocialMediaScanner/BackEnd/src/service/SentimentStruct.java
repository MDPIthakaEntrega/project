package service;

public class SentimentStruct {

	private String sentiment_feeling;
	private double sentiment_score;
	
	SentimentStruct() {
		sentiment_feeling = "neutral";
		sentiment_score = 0.0;
	}
	
	SentimentStruct(String sentiment_feeling_i, double sentiment_score_i) {
		
		sentiment_feeling = sentiment_feeling_i;
		sentiment_score = sentiment_score_i;
	}

	/**
	 * 
	 * @return
	 */
	public String getSentiment_feeling() {
		return sentiment_feeling;
	}

	public void setSentiment_feeling(String sentiment_feeling) {
		this.sentiment_feeling = sentiment_feeling;
	}

	public double getSentiment_score() {
		return sentiment_score;
	}

	public void setSentiment_score(double sentiment_score) {
		this.sentiment_score = sentiment_score;
	}
}
