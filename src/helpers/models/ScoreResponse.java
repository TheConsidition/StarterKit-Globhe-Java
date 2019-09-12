package helpers.models;
import java.util.ArrayList;

/**
 * Contains the current total score for this game, the individual score for the last round of images, any errors and
 * how many rounds are left in this game.
 */
public class ScoreResponse extends ApiResponse {
	public int totalScore; 
    public ArrayList<ImageScoreModel> imageScores = new ArrayList<>();
    public String[] errors ;
    public Integer roundsLeft;
}



