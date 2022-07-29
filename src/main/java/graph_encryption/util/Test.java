package graph_encryption.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

class Test {
    public static void main(String[]args){
        Uploader uploader = new Uploader();
        String url = uploader.upload(new File("/Users/pengqing/Documents/WechatIMG8.jpeg"));
        System.out.println(url);
    }
    public static String getImgurContent(String clientID) throws Exception {

        URL url;
        url = new URL("https://www.getpostman.com/oauth2/callback");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        String data = URLEncoder.encode("image", "UTF-8") + "="
                + URLEncoder.encode("fileimage", "UTF-8");

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID " + clientID);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        conn.connect();
        StringBuilder stb = new StringBuilder();
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

        // Get the response
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            stb.append(line).append("\n");
        }
        wr.close();
        rd.close();

        return stb.toString();
    }



}


