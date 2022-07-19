//package graph_encryption.util;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//
//public class ExceptionUtil {
//
//    public static final String getStackTrace(Exception e) {
//        StringWriter stringWriter = null;
//        PrintWriter printWriter = null;
//
//        try {
//            stringWriter = new StringWriter();
//            printWriter = new PrintWriter(stringWriter);
//            printWriter.flush();
//            stringWriter.flush();
//
//            return stringWriter.toString();
//        } finally {
//            if (stringWriter != null) {
//                try {
//                    stringWriter.close();
//                } catch (IOException ioe) {
//                }
//            }
//            if (printWriter != null) {
//                printWriter.close();
//            }
//        }
//    }
//}
