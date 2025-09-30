package gr1.fpt.bambikitchen.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString();
    }

    //viết hàm upload lại có lưu thêm public_id để xóa ảnh trên cloud khi có update
    public Map<String,String> uploadImg(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "my_app/images",
                "resource_type", "image",
                "allowed_formats", new String[]{"jpg", "jpeg", "png", "gif", "webp"}));
        //Map result này sẽ nhận tất cả tham số của ảnh khi upload thành công trong đó thường chỉ cần: public_id( phục vụ cho việc xóa ảnh hay update) + secure_url
        // upload có 2 tham số:
        // 1.Dữ liệu file ( cloudinary SDK nhận là kiểu byte arrays nên convert qua kiểu này)
        // 2. Các tham số khác (ở đây không có nên dùng ObjectUtils.emptyMap())
        // uploadResult là 1 Map chứa các thông tin về file vừa upload lên Cloudinary ( public_id, url,secure_url,..)
        // trong đó có 1 key là "secure_url" chứa đường dẫn đến file vừa upload
        String public_id = (String) uploadResult.get("public_id");
        String secure_url = uploadResult.get("secure_url").toString();
        Map<String,String> result = new HashMap<String,String>();
        result.put("public_id",public_id);
        result.put("secure_url",secure_url);
        return result;
    }

    public Map deleteImage(String publicId) throws IOException {
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate",true));
        //có thể xóa cache CDN hoặc ko ( thường thì sẽ bị mất sau 1 thời gian vài phút - vài giờ
        return result;
    }

}
