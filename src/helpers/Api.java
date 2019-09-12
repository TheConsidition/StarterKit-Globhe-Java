package helpers;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import com.google.gson.*;

import helpers.models.ScoreResponse;
import helpers.models.ScoreSolutionRequest;
import helpers.models.ApiResponse;
import helpers.models.ErrorApiResponse;
import helpers.models.GameIdApiResponse;

public class Api {

	private static final String BasePath = "https://api.theconsidition.se/api/game";
	private static Gson gson;

	static {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ApiResponse.class, new ApiResponseDeserializer());
		gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
			@Override
			public LocalDateTime deserialize(JsonElement json, Type type,
					JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
				return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime();
			}
		});
		gson = gsonBuilder.create();
	}

	/**
	 * Closes any active game and starts a new one
	 * @param apiKey The teams API key
	 * @return An object containing information about the new game
	 */
	public static GameIdApiResponse initGame(String apiKey) {
		try {
			URL url = new URL(BasePath + "/init");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			con.setRequestProperty("x-api-key", apiKey);

			ApiResponse response = readApiResponse(con);

			con.disconnect();
			return (GameIdApiResponse) response;
		} catch (Exception ex) {
			System.out.println("Fatal error: could not start a new game");
			System.out.println("Error: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	/**
	 * Gets a zip-file containing images for the current round. Does 3 attempts.
	 * @return a byte array containing the zip file.
	 */
	public static byte[] getImages(String apiKey) throws InterruptedException {
		int tries = 1;
		while(tries <= 3) {
			try {
				URL url = new URL(BasePath + "/images");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.setDoOutput(true);
				con.setRequestProperty("Content-Type", "application/zip");
				con.setRequestProperty("x-api-key", apiKey);

				InputStream in = con.getInputStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();

				int bytesRead = -1;
				byte[] buffer = new byte[4096];
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}

				return out.toByteArray();
			} catch (IOException ex) {
				System.out.println("Error: " + ex.getMessage());
				System.out.println("Attempt " + tries + " failed, waiting 2 sec and attempting again");
				Thread.sleep(2000);
				tries++;
				if(tries >= 3) {
					ex.printStackTrace();
				}
			}
		}
		System.out.println("Fatal error: could not get images");
		System.exit(2);
		return null;
	}

	/**
	 * Submits a solution to the api for scoring. Does three attempts to submit
	 * @param solution The solution to be scored.
	 * @return A summary of the score and the current game state.
	 */
	public static ScoreResponse ScoreSolution(String apiKey, ScoreSolutionRequest solution) throws InterruptedException {
		int tries = 1;
		while(tries <= 3) {
			try {
				URL url = new URL(BasePath + "/solution");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setDoOutput(true);
				con.setDoInput(true);
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("x-api-key", apiKey);
				Gson gson = new Gson();
				String jsonData = gson.toJson(solution);
				OutputStream os = con.getOutputStream();
				os.write(jsonData.getBytes());
				os.flush();
				os.close();
				ScoreResponse response = readPostResponse(con);
				gson.fromJson(jsonData, ScoreResponse.class);
				con.disconnect();
				return response;
			} catch (Exception ex) {
				System.out.println("Error: " + ex.getMessage());
				System.out.println("Attempt " + tries + " failed, waiting 2 sec and attempting again");
				Thread.sleep(2000);
				tries++;
				if(tries >= 3) {
					ex.printStackTrace();
				}
			}
		}
		System.out.println("Fatal error: could not post solution");
		System.exit(3);
		return null;
	}

	/**
	 * Returns an ApiResponse or throws an error if an error was encountered.
	 * @param con
	 * @return
	 * @throws IOException
	 */
	private static ApiResponse readApiResponse(HttpURLConnection con) throws IOException { 
		InputStream stream;
		if (con.getResponseCode() == -1) {
			throw new IOException("Did not receive a valid response from the server");
		}else if (con.getResponseCode() < 400) {
			stream = con.getInputStream();
		} else {
			stream = con.getErrorStream();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));

		StringBuilder received = new StringBuilder();
		String chunk;
		while ((chunk = br.readLine()) != null) {
			received.append(chunk);
		}

		if (con.getResponseCode() >= 400) {
			throw new IOException("Http error with code " + con.getResponseCode() + " and message: " + received.toString());
		}

		ApiResponse response = gson.fromJson(received.toString(), ApiResponse.class);
		if (response instanceof ErrorApiResponse) {
			ErrorApiResponse error = (ErrorApiResponse) response;
			throw new IOException(error.message);
		}
		return response;
	}

	/**
	 * Returns a ScoreResponse or throws an error if an error was encountered
 	 * @param con
	 * @return
	 * @throws IOException
	 */
	private static ScoreResponse readPostResponse(HttpURLConnection con) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
		StringBuilder response = new StringBuilder();
		String responseLine = null;
		while ((responseLine = br.readLine()) != null) {
			response.append(responseLine.trim());
		}
		ScoreResponse scoreResponse = gson.fromJson(response.toString(), ScoreResponse.class);
		return scoreResponse;
	}

}
