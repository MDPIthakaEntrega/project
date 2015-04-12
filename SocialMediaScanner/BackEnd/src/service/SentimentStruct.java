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
	
	
	String getFeeling() {
		return sentiment_feeling;
	
	}
	
	double getScore() {
		return sentiment_score;
	}
	
	void setFeeling(String sentiment_feeling_i) {
		sentiment_feeling = sentiment_feeling_i;
	}
	
	void setScore(double sentiment_score_i) {
		sentiment_score = sentiment_score_i;
	}
}
