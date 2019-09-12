import java.util.ArrayList;

import helpers.Api;
import helpers.Solution;
import helpers.models.*;
import helpers.SolutionHelper;

public class Main {

	private static String apiKey = "YOUR-API-KEY-HERE"; 				// TODO: Enter your API key here
	private static String imageFolderPath = "path/to/imagefolder";		// TODO: Enter the path to your image folder here

	public static void main(String[] args) throws InterruptedException {
		GameIdApiResponse initResponse = Api.initGame(apiKey);
		String gameId = initResponse.gameId;
		int roundsLeft = initResponse.numberOfRounds;
		System.out.println("Starting a new game with id: " + gameId);
		System.out.println("The game has " + roundsLeft + " rounds and " + initResponse.imagesPerRound + " images per round");
		while(roundsLeft > 0) {
			System.out.println("Starting new round " + roundsLeft + " rounds left");
			Solution solution = new Solution();

			byte[] zip = Api.getImages(apiKey);

			ArrayList<String> imageNames =  SolutionHelper.saveImagesToDisk(zip, imageFolderPath);
			for (String name : imageNames) {
				String imagePath = imageFolderPath + "/" + name;
				ImageSolution imageSolution = analyzeImage(imagePath);
				solution.add(name, imageSolution);
			}
			ScoreResponse response = Api.ScoreSolution(apiKey, solution.getRequest());
			SolutionHelper.printErrors(response);
			SolutionHelper.printScores(response);
			roundsLeft = response.roundsLeft;
		}
		SolutionHelper.clearImagesFromFolder(imageFolderPath);
	}

	private static ImageSolution analyzeImage(String imagePath) {
		/**
		 * ----------------------------------------------------
		 * TODO Implement your image recognition algorithm here
		 * ----------------------------------------------------
		 */

		float buildingPercentage = 30.0f;
		float roadPercentage = 30.0f;
		float waterPercentage = 30.0f;
		ImageSolution percentages = new ImageSolution(buildingPercentage, roadPercentage, waterPercentage);
		
		return percentages;
	}
}
