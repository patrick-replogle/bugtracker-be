package com.patrickreplogle.bugtracker.models;

import com.cloudinary.utils.ObjectUtils;
import com.patrickreplogle.bugtracker.util.Constants;
import com.cloudinary.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryUploader {
    // === cloudinary config settings ===
    private Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", System.getenv(Constants.CLOUDINARY_NAME),
            "api_key", System.getenv(Constants.CLOUDINARY_API_KEY),
            "api_secret", System.getenv(Constants.CLOUDINARY_API_SECRET),
            "secure", true
    ));

    public String addNewImage(String base64) {
        try {
           Map uploadResult = cloudinary.uploader().upload(base64, ObjectUtils.emptyMap());
           if (uploadResult.containsKey("url")) {
               return (String) uploadResult.get("url");
           }
           return null;
        } catch (IOException err) {
            System.out.println(err);
            return null;
        }
    }

    public void removeImage(String url)  {
        try {
            String[] arr = url.split("/");
            String lastItem = arr[arr.length - 1];
            String public_id = lastItem.substring(0, lastItem.length() - 4);

            cloudinary.uploader().destroy(public_id, ObjectUtils.emptyMap());
        } catch(IOException err) {
            System.out.println(err);
        }
    }

    public String updateImage(String base64, String url) {
        try {
            String[] arr = url.split("/");
            String lastItem = arr[arr.length - 1];
            String public_id = lastItem.substring(0, lastItem.length() - 4);

            Map uploadResult = cloudinary.uploader().upload(base64, ObjectUtils.asMap(
                    "public_id", public_id,
                    "invalidate", true
            ));
            if (uploadResult.containsKey("url")) {
                return (String) uploadResult.get("url");
            }
            return null;
        } catch (IOException err) {
            System.out.println(err);
            return null;
        }
    }
}
