package helpers.models;

public class ImageSolutionRequest
{
    public String ImageName;
    public float BuildingPercentage;
    public float RoadPercentage;
    public float WaterPercentage;
    
    public ImageSolutionRequest(String imageName, float buildingPercentage, float roadPercentage, float waterPercentage) {
        ImageName = imageName;
        BuildingPercentage = buildingPercentage;
        RoadPercentage = roadPercentage;
        WaterPercentage = waterPercentage;
    }
}
