package helpers.models;

/**
 * Contains information about a new game. The game id, the number of rounds and how many images there are per round.
 */
public class GameIdApiResponse extends ApiResponse {
    public String gameId;
    public int numberOfRounds;
    public int imagesPerRound;
}