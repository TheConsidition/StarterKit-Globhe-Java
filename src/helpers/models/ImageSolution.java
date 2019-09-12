package helpers.models;

/**
 * A solution for a single image
 */
public class ImageSolution {
	
	 	public float BuildingPercentage;
	    public float RoadPercentage;
	    public float WaterPercentage;
	    
	    public ImageSolution(float BuildingPercentage, float RoadPercentage, float WaterPercentage) {
	    	this.BuildingPercentage = BuildingPercentage;
	    	this.RoadPercentage = RoadPercentage;
	    	this.WaterPercentage = WaterPercentage;
	    }
	     
}
