package gr1.fpt.bambikitchen.Utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public  class FileUtil {


    public static String saveFile (MultipartFile file) throws IOException {
        String uploadDir = System.getProperty("user.dir") + "/uploads/"; // root project/uploads
        File dir = new File(uploadDir);
        if (!dir.exists())
            dir.mkdirs();
        String originalName = file.getOriginalFilename();
        String newName = System.currentTimeMillis() + "_" + originalName;
        File destination = new File(dir, newName);
        file.transferTo(destination);
        return destination.getAbsolutePath();
    }

    public static File getFileByPath(String absolutePath) {
        File file = new File(absolutePath);
        return file;
    }

    public static  MultipartFile convertFileToMultipart(File file) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            return new MockMultipartFile(
                    file.getName(),
                    file.getName(),
                    "application/octet-stream",
                    input
            );
        }
    }
}
