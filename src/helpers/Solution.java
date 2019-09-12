package helpers;

import helpers.models.ImageSolution;
import helpers.models.ImageSolutionRequest;
import helpers.models.ScoreSolutionRequest;

import java.util.*;

public class Solution {
    private ScoreSolutionRequest request;

    /**
     * Creates a new solution to populate
     */
    public Solution() {
        request = new ScoreSolutionRequest();
        request.solutions = new ArrayList<>();
    }

    /**
     * Adds a new solution for an image to the total solution
     * @param name The name of the image
     * @param imageSolution The solution for the image
     */
    public void add(String name, ImageSolution imageSolution) {
        request.solutions.add(new ImageSolutionRequest(name, imageSolution.BuildingPercentage, imageSolution.RoadPercentage, imageSolution.WaterPercentage));
    }

    /**
     * Gets a ScoreSolutionRequest
     * @return A request that can be sent to the api for scoring
     */
    public ScoreSolutionRequest getRequest() {
        return request;
    }
}
