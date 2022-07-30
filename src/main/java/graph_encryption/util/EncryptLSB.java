package graph_encryption.util;

import org.springframework.stereotype.Component;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class EncryptLSB {
    /**
     * Encrypt method encrypts the LSB of the image pixels
     * @param imageUrl image source passed in
     * @param message message we want to encrypt (string)
     * @return
     */
    public static String Encrypt(String imageUrl, String message) {
//        String directory = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
//        String newImageFileString = "/Users/chenyian261/Documents/NEUSummer/CS6760/" + "\\export.png";
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
        BufferedImage imageToEncrypt = GetImageToEncrypt(image); //use a buffer copy of the original image
        Pixel[] pixels = GetPixelArray(imageToEncrypt);
        String[] messageInBinary = ConvertMessageToBinary(message);
        EncodeMessageBinaryInPixels(pixels, messageInBinary);
        ReplacePixelsInNewBufferedImage(pixels, image);
        String encryptedUrl = saveInDB(image);
//        SaveNewFile(image, newImageFile); //**Write into DB
        return encryptedUrl;


//        return "encrypted image url";

    }

    private static String saveInDB(BufferedImage image) {
        // create a file name based on the current time
        Date date = new Date();
        String fileName = "image" + date.getTime() + ".png";
        File file = new File(fileName);
        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uploader uploader = new Uploader();
        String url = uploader.upload(file);
        return url;
    }
    /**
     * Copies the image into a new buffered image
     * Color Model determines how colors are represented within AWT in integer formats(RGB)
     * WriteableRaster provides pixel writing capabilities (top right to bottom left)
     * @param image
     * @return copy of Buffer image
     */
    private static BufferedImage GetImageToEncrypt(BufferedImage image) {
        ColorModel colorModel = image.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    /**
     * Gets 2D array of colors from the image to encrypt (Pixel class)
     * loop through 2D array to get each pixel
     * @param imageToEncrypt
     * @return pixels (instance of Pixel[])
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
     * Converting the message into binary
     * converts message to ascii then to binary
     * @param message
     * @return binary
     */
    private static String[] ConvertMessageToBinary(String message) {
        int[] messageInAscii = ConvertMessageToAscii(message);
        String[] binary = ConvertAsciiToBinary(messageInAscii);
        return binary;
    }

    /**
     * Converting the message into ASCII
     * @param message
     * @return converts char to ascii
     */
    private static int[] ConvertMessageToAscii(String message) {
        int[] messageCharactersInAscii = new int[message.length()];
        for(int i = 0; i < message.length(); i++) {
            int asciiValue = (int) message.charAt(i);
            messageCharactersInAscii[i] = asciiValue;
        }
        return messageCharactersInAscii;
    }

    /**
     * Converting the ASCII code to Binary
     * @param messageInAscii
     * @return message in binary
     */
    private static String[] ConvertAsciiToBinary(int[] messageInAscii) {
        String[] messageInBinary = new String[messageInAscii.length];
        for(int i = 0; i < messageInAscii.length; i++) {
            String asciiBinary = LeftPadZeros(Integer.toBinaryString(messageInAscii[i]));
            messageInBinary[i] = asciiBinary;
        }
        return messageInBinary;
    }

    /**
     * ASCII only gives us 7 binary bits so we need to add padded 0
     * Left padding the binary value with zeros to make an 8 digit string
     * @param value
     * @return string
     */
    private static String LeftPadZeros(String value) {
        StringBuilder paddedValue = new StringBuilder("00000000");
        int offSet = 8 - value.length();
        for(int i = 0 ; i < value.length(); i++) {
            paddedValue.setCharAt(i+offSet, value.charAt(i));
        }
        return paddedValue.toString();
    }


    /**
     * Encoding the message in the pixels
     * @param pixels
     * @param messageBinary
     */
    private static void EncodeMessageBinaryInPixels(Pixel[] pixels, String[] messageBinary) {
        int pixelIndex = 0;
        boolean isLastCharacter = false;
        for(int i = 0; i < messageBinary.length; i++) {
            Pixel[] currentPixels = new Pixel[] {pixels[pixelIndex], pixels[pixelIndex+1], pixels[pixelIndex+2]};
            if(i+1 == messageBinary.length) {
                isLastCharacter = true;
            }
            ChangePixelsColor(messageBinary[i], currentPixels, isLastCharacter);
            pixelIndex = pixelIndex +3;
        }
    }

    /**
     * method that changes pixel colors
     * use boolean to check if it's last character
     * @param messageBinary
     * @param pixels
     * @param isLastCharacter
     */
    private static void ChangePixelsColor(String messageBinary, Pixel[] pixels, boolean isLastCharacter) {
        int messageBinaryIndex = 0;
        for(int i =0; i < pixels.length-1; i++) {
            char[] messageBinaryChars = new char[] {messageBinary.charAt(messageBinaryIndex), messageBinary.charAt(messageBinaryIndex+1), messageBinary.charAt(messageBinaryIndex+2)};
            String[] pixelRGBBinary = GetPixelsRGBBinary(pixels[i], messageBinaryChars);
            pixels[i].setColor(GetNewPixelColor(pixelRGBBinary));
            messageBinaryIndex = messageBinaryIndex + 3;
        }
        if(isLastCharacter == false) {
            char[] messageBinaryChars = new char[] {messageBinary.charAt(messageBinaryIndex), messageBinary.charAt(messageBinaryIndex+1), '1'};
            String[] pixelRGBBinary = GetPixelsRGBBinary(pixels[pixels.length-1], messageBinaryChars);
            pixels[pixels.length-1].setColor(GetNewPixelColor(pixelRGBBinary));
        }else {
            char[] messageBinaryChars = new char[] {messageBinary.charAt(messageBinaryIndex), messageBinary.charAt(messageBinaryIndex+1), '0'};
            String[] pixelRGBBinary = GetPixelsRGBBinary(pixels[pixels.length-1], messageBinaryChars);
            pixels[pixels.length-1].setColor(GetNewPixelColor(pixelRGBBinary));
        }
    }

    /**
     * Turn pixels(RGB) to integer value to binary string and change LSB of the binary to message binary
     * @param pixel
     * @param messageBinaryChars
     * @return pixel binary as array of strings
     */
    private static String[] GetPixelsRGBBinary(Pixel pixel, char[] messageBinaryChars) {
        String[] pixelRGBBinary = new String[3];
        pixelRGBBinary[0] = ChangePixelBinary(Integer.toBinaryString(pixel.getColor().getRed()), messageBinaryChars[0]);
        pixelRGBBinary[1] = ChangePixelBinary(Integer.toBinaryString(pixel.getColor().getGreen()), messageBinaryChars[1]);
        pixelRGBBinary[2] = ChangePixelBinary(Integer.toBinaryString(pixel.getColor().getBlue()), messageBinaryChars[2]);
        return pixelRGBBinary;
    }

    /**
     * changes LSB of binary string to message character
     * @param pixelBinary
     * @param messageBinaryChar
     * @return string
     */
    private static String ChangePixelBinary(String pixelBinary, char messageBinaryChar) {
        StringBuilder sb = new StringBuilder(pixelBinary);
        sb.setCharAt(pixelBinary.length()-1, messageBinaryChar);
        return sb.toString();
    }

    /**
     * Gets new pixel color, takes in string array
     * parses string as int, need to add 2
     * @param colorBinary
     * @return
     */
    private static Color GetNewPixelColor(String[] colorBinary) {
        return new Color(Integer.parseInt(colorBinary[0], 2), Integer.parseInt(colorBinary[1], 2), Integer.parseInt(colorBinary[2], 2));
    }

    /**
     * replaces pixels in the buffered image (message encoded)
     * @param newPixels
     * @param newImage
     */
    private static void ReplacePixelsInNewBufferedImage(Pixel[] newPixels, BufferedImage newImage) {
        for(int i = 0; i < newPixels.length; i++) {
            newImage.setRGB(newPixels[i].getX(), newPixels[i].getY(), newPixels[i].getColor().getRGB());
        }
    };

    //This part will change to save in DB
    private static void SaveNewFile(BufferedImage newImage, File newImageFile) {
        try {
            ImageIO.write(newImage, "png", newImageFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
