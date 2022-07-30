package graph_encryption.util;

import org.springframework.stereotype.Component;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

@Component
public class DecryptLSB {
    /**
     * Decrypt method to decrypt secret message
     * @param imageUrl
     * @return string message
     */
    public static String Decrypt(String imageUrl) {
//        String directory = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
//        String newImageFileString = "/Users/chenyian261/Documents/NEUSummer/CS6760/"+ "\\export.png";
//        File newImageFile = new File(newImageFileString);
        HttpURLConnection connection = null;
        BufferedImage image = null;
        try {
            connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            connection.connect();
            image = ImageIO.read(connection.getInputStream());
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        image = ImageIO.read(imageFile);
        Pixel[] pixels = GetPixelArray(image);
        System.out.println(DecodeMessageFromPixels(pixels));

        return DecodeMessageFromPixels(pixels) ;
    }


    /**
     * Gets pixel array to return pixels
     * @param imageToEncrypt
     * @return pixels
     */
    private static Pixel[] GetPixelArray(BufferedImage imageToEncrypt){
        int height = imageToEncrypt.getHeight();
        int width = imageToEncrypt.getWidth();
        Pixel[] pixels = new Pixel[height * width];

        int count = 0;
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Color colorToAdd = new Color(imageToEncrypt.getRGB(x, y));
                pixels[count] = new Pixel(x, y, colorToAdd);
                count++;
            }
        }
        return pixels;
    };

    /**
     * Decode message from pixels, keeps iterating through pixels array
     * appends message to end of message
     * @param pixels
     * @return string message
     */
    private static String DecodeMessageFromPixels(Pixel[] pixels) {
        boolean completed = false;
        int pixelArrayIndex = 0;
        StringBuilder messageBuilder = new StringBuilder("");
        while(completed == false) {
            Pixel[] pixelsToRead = new Pixel[3];
            for(int i = 0; i < 3; i++) {
                pixelsToRead[i] = pixels[pixelArrayIndex];
                pixelArrayIndex++;
            }
            messageBuilder.append(ConvertPixelsToCharacter(pixelsToRead));
            if(IsEndOfMessage(pixelsToRead[2]) == true) {
                completed = true;
            }
        }
        return messageBuilder.toString();
    }

    /**
     * converts pixels to character
     * @param pixelsToRead
     * @return
     */
    private static char ConvertPixelsToCharacter(Pixel[] pixelsToRead) {
        ArrayList<String> binaryValues = new ArrayList<String>();
        for(int i = 0; i < pixelsToRead.length; i++) {
            String[] currentBinary = TurnPixelIntegersToBinary(pixelsToRead[i]);
            binaryValues.add(currentBinary[0]);
            binaryValues.add(currentBinary[1]);
            binaryValues.add(currentBinary[2]);
        }
        return ConvertBinaryValuesToCharacter(binaryValues);
    }

    /**
     * pixel integers change to binary
     * @param pixel
     * @return string values
     */
    private static String[] TurnPixelIntegersToBinary(Pixel pixel) {
        String[] values = new String[3];
        values[0] = Integer.toBinaryString(pixel.getColor().getRed());
        values[1] = Integer.toBinaryString(pixel.getColor().getGreen());
        values[2] = Integer.toBinaryString(pixel.getColor().getBlue());
        return values;
    }

    /**
     * check if is end of message
     * @param pixel
     * @return boolean
     */
    private static boolean IsEndOfMessage(Pixel pixel) {
        if(TurnPixelIntegersToBinary(pixel)[2].endsWith("1")) {
            return false;
        }
        return true;
    }

    /**
     * Converts binary values to characters
     * @param binaryValues
     * @return
     */
    private static char ConvertBinaryValuesToCharacter(ArrayList<String> binaryValues) {
        StringBuilder endBinary = new StringBuilder("");
        for(int i = 0; i < binaryValues.size()-1; i++) {
            endBinary.append(binaryValues.get(i).charAt(binaryValues.get(i).length()-1));
        }
        String endBinaryString = endBinary.toString();
        String noZeros = RemovePaddedZeros(endBinaryString);
        int ascii = Integer.parseInt(noZeros, 2);
        return (char) ascii;
    }

    /**
     * removes the padded zeroes that we added in encrypt
     * @param endBinary
     * @return string
     */
    private static String RemovePaddedZeros(String endBinary) {
        StringBuilder builder = new StringBuilder(endBinary);
        int paddedZeros = 0;
        for(int i = 0; i < builder.length(); i++) {
            if(builder.charAt(i) == '0') {
                paddedZeros++;
            }
            else {
                break;
            }
        }
        for(int i = 0 ; i < paddedZeros; i++) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }
}

