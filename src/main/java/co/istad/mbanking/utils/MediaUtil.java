package co.istad.mbanking.utils;

import org.springframework.web.multipart.MultipartFile;

public class MediaUtil {

    public static String extractExtension(String name){
        // extract extension from file
        int lastDotIndex = name
                .lastIndexOf(".");

        return name
                .substring(lastDotIndex + 1);
    }

}

