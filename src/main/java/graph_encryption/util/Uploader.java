package graph_encryption.util;


import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

/**
 * Controls all interface with the web and the Imgur API.
 *
 * @author DV8FromTheWorld (Austin Keener)
 * @version v1.0.0  July 16, 2014
 */
public class Uploader
{
    public static final String UPLOAD_API_URL = "https://api.imgur.com/3/image";
    public static final String ALBUM_API_URL = "https://api.imgur.com/3/album";
    public static final int MAX_UPLOAD_ATTEMPTS = 3;

    //CHANGE TO @CLIENT_ID@ and replace with buildscript.
    private final static String CLIENT_ID = "37f149bdd4b0f80";

    /**
     * Takes a file and uploads it to Imgur.
     * Does not check to see if the file is an image, this should be done
     * before the file is passed to this method.
     *
     * @param file
     *          The image to be uploaded to Imgur.
     * @return
     *          The JSON response from Imgur.
     */
    public static String upload(File file)
    {
        HttpURLConnection conn = getHttpConnection(UPLOAD_API_URL);
        writeToConnection(conn, "image=" + toBase64(file));
        String response = getResponse(conn);
        JSONObject loginJB = (JSONObject) JSONObject.parse(response);
        if (loginJB.getBoolean("success")) {
            return loginJB.getJSONObject("data").getString("link");
        } else {
            throw new WebException(StatusCode.SERVICE_UNAVAILABLE);
        }
    }

    /**
     * Creates an album on Imgur.
     * Does not check if imageIds are valid images on Imgur.
     *
     * @param imageIds
     *          A list of ids of images on Imgur.
     * @return
     *          The JSON response from Imgur.
     */
    public static String createAlbum(List<String> imageIds)
    {
        HttpURLConnection conn = getHttpConnection(ALBUM_API_URL);
        String ids = "";
        for (String id : imageIds)
        {
            if (!ids.equals(""))
            {
                ids += ",";
            }
            ids += id;
        }
        writeToConnection(conn, "ids=" + ids);
        return getResponse(conn);
    }

    /**
     * Converts a file to a Base64 String.
     *
     * @param file
     *          The file to be converted.
     * @return
     *          The file as a Base64 String.
     */
    private static String toBase64(File file)
    {
        try
        {
            byte[] b = new byte[(int) file.length()];
            FileInputStream fs = new FileInputStream(file);
            fs.read(b);
            fs.close();
            return URLEncoder.encode(DatatypeConverter.printBase64Binary(b), "UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Creates and sets up an HttpURLConnection for use with the Imgur API.
     *
     * @param url
     *          The URL to connect to. (check Imgur API for correct URL).
     * @return
     *          The newly created HttpURLConnection.
     */
    private static HttpURLConnection getHttpConnection(String url)
    {
        HttpURLConnection conn;
        try
        {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
            conn.setReadTimeout(100000);
            conn.connect();
            return conn;
        }
        catch (UnknownHostException e)
        {
            throw new WebException(StatusCode.UNKNOWN_HOST, e);
        }
        catch (IOException e)
        {
            throw new WebException(StatusCode.UNKNOWN_ERROR, e);
        }
    }

    /**
     * Sends the provided message to the connection as uploaded data.
     *
     * @param conn
     *          The connection to send the data to.
     * @param message
     *          The data to upload.
     */
    private static void writeToConnection(HttpURLConnection conn, String message)
    {
        OutputStreamWriter writer;
        try
        {
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            throw new WebException(StatusCode.UNKNOWN_ERROR, e);
        }
    }

    /**
     * Gets the response from the connection, Usually in the format of a JSON string.
     *
     * @param conn
     *          The connection to listen to.
     * @return
     *          The response, usually as a JSON string.
     */
    private static String getResponse(HttpURLConnection conn)
    {
        StringBuilder str = new StringBuilder();
        BufferedReader reader;
        try
        {
            if (conn.getResponseCode() != StatusCode.SUCCESS.getHttpCode())
            {
                throw new WebException(conn.getResponseCode());
            }
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
            {
                str.append(line);
            }
            reader.close();
        }
        catch (IOException e)
        {
            throw new WebException(StatusCode.UNKNOWN_ERROR, e);
        }
        if (str.toString().equals(""))
        {
            throw new WebException(StatusCode.UNKNOWN_ERROR);
        }
        return str.toString();
    }
}