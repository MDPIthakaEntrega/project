package service;

public class SentimentStruct {

	
	/**
	 * "positive", "negative", "neutral", or "mixed" to describe sentiment  
	 */
	private String sentiment_feeling;
	private double sentiment_score;
	
	/**
	 * default constructor;
	 * sentiment defaults to neutral and 0.0;
	 */
	public SentimentStruct() {
		sentiment_feeling = "neutral";
		sentiment_score = 0.0;
	}
	
	
	/**
	 * constructor;
	 * @param sentiment_feeling_i
	 * @param sentiment_score_i
	 */
	public SentimentStruct(String sentiment_feeling_i, double sentiment_score_i) {
		
		sentiment_feeling = sentiment_feeling_i;
		sentiment_score = sentiment_score_i;
	}
	
	/**
	 * getter of the sentiment feeling;
	 * @return
	 */
	public String getFeeling() {
		return sentiment_feeling;
	
	}
	
	/**
	 * getter of the sentiment score;
	 * @return
	 */
	public double getScore() {
		return sentiment_score;
	}
	
	/**
	 * setter of the sentiment feeling;
	 * @return
	 */
	public void setFeeling(String sentiment_feeling_i) {
		sentiment_feeling = sentiment_feeling_i;
	}
	
	/**
	 * setter of the sentiment score;
	 * @return
	 */
	public void setScore(double sentiment_score_i) {
		sentiment_score = sentiment_score_i;
	}
}
