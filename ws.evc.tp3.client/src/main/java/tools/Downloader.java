package tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {
	public static String donwloadFile(String urlPath, boolean rewrite) {
		int pos = urlPath.lastIndexOf("/");
		String file = urlPath.substring(pos + 1);
		try {
			URL url = new URL(urlPath);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			float totalDataRead = 0;
			java.io.BufferedInputStream in = new java.io.BufferedInputStream(
					connection.getInputStream());
			if (!(new File("downloaded/" + file).exists()) | rewrite) {
				java.io.FileOutputStream fos = new java.io.FileOutputStream(
						"downloaded/" + file);
				java.io.BufferedOutputStream bout = new BufferedOutputStream(fos,
						1024);
				byte[] data = new byte[1024];
				int i = 0;
				while ((i = in.read(data, 0, 1024)) >= 0) {
					totalDataRead = totalDataRead + i;
					bout.write(data, 0, i);
				}
				bout.close();
				in.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return "downloaded/" + file;

	}
}
