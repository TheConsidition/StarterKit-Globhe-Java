package helpers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import helpers.models.ImageScoreModel;
import helpers.models.ScoreResponse;

public class SolutionHelper {

	private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());
		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}

	/**
	 * Prints any errors encountered in the solution. Prints nothing if there are no errors.
 	 * @param response The score response to check for errors
	 */
	public static void printErrors(ScoreResponse response) {
		if (response.errors == null || response.errors.length == 0) {
			return;
		}

		String[] errors = response.errors;
		System.out.println("Encountered some errors with the solution:");
		for (String error : errors) {
			System.out.println(error);
		}
	}

	/**
	 * Prints the scores for this round
	 * @param response The response object for this round
	 */
	public static void printScores(ScoreResponse response) {
		System.out.println("Total score: " + response.totalScore);
		for (ImageScoreModel score : response.imageScores) {
			System.out.println("Image " + String.format("%25s", score.imageName) + " got a score of " + score.score);
		}
	}

	/**
	 * Unzips a zip file and stores the images in the specified path.
	 * @param zip A byte array representing a zip file
	 * @param imageFolderPath The folder the images should be stored in. The folder is created if it does not exist
	 * @return A list of names of the stored images
	 */
	public static ArrayList<String> saveImagesToDisk(byte[] zip, String imageFolderPath) {
		try {
			ArrayList<String> imageNames = new ArrayList<String>();
			InputStream in = new ByteArrayInputStream(zip);
			ZipInputStream zis = new ZipInputStream(in);

			if (!Files.exists(Paths.get(imageFolderPath))) {
				Files.createDirectories(Paths.get(imageFolderPath));
			}
			File destDir = new File(imageFolderPath);

			byte[] buffer = new byte[1024];
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				File newFile = newFile(destDir, zipEntry);
				imageNames.add(newFile.getName());
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				zipEntry = zis.getNextEntry();
			}
			in.close();
			zis.close();

			return imageNames;
		} catch(IOException ex) {
			System.out.println("Fatal error: Could not unzip the images to the specified path");
			System.out.println("Error: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	/**
	 * Removes all .jpg images from the specified folder
	 * @param imageFolderPath The folder to clean
	 */
	public static void clearImagesFromFolder(String imageFolderPath) {
		File dir = new File(imageFolderPath);

		for(File file : dir.listFiles()) {
			if(!file.isDirectory() && file.getName().toLowerCase().contains(".jpg")) {
				file.delete();
			}
		}
	}
}
















