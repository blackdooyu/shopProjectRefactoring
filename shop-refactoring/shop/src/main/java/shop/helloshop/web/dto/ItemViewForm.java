package shop.helloshop.web.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;
import shop.helloshop.domain.entity.UploadFile;
import shop.helloshop.domain.entity.items.ItemSize;
import shop.helloshop.domain.entity.items.PhoneColor;

import javax.persistence.Convert;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemViewForm {

    private Long id;

    private String name;

    private int price;

    private int quantity;

    private int salesQuantity;

    private ItemSize itemSize;

    private PhoneColor phoneColor;

    private String img;

    private List<String> imgFiles = new ArrayList<>();

    private List<UploadFile> uploadFiles = new ArrayList<>();



    public static ItemViewForm createPhoneForm(Long id,String name, int price, int quantity , int salesQuantity,PhoneColor phoneColor ,List<UploadFile> uploadFiles) {
        ItemViewForm itemViewForm = new ItemViewForm();
        itemViewForm.setId(id);
        itemViewForm.setName(name);
        itemViewForm.setPrice(price);
        itemViewForm.setQuantity(quantity);
        itemViewForm.setSalesQuantity(salesQuantity);
        itemViewForm.setPhoneColor(phoneColor);
        itemViewForm.setUploadFiles(uploadFiles);

        for (UploadFile uploadFile : uploadFiles) {
            itemViewForm.getImgFiles().add(uploadFile.getUniqueURL());
        }
        return itemViewForm;

    }

    public static ItemViewForm createClothesForm(Long id,String name, int price, int quantity , int salesQuantity,ItemSize itemSize ,List<UploadFile> uploadFiles) {
        ItemViewForm itemViewForm = new ItemViewForm();
        itemViewForm.setId(id);
        itemViewForm.setName(name);
        itemViewForm.setPrice(price);
        itemViewForm.setQuantity(quantity);
        itemViewForm.setSalesQuantity(salesQuantity);
        itemViewForm.setItemSize(itemSize);
        itemViewForm.setUploadFiles(uploadFiles);

        for (UploadFile uploadFile : uploadFiles) {
            itemViewForm.getImgFiles().add(uploadFile.getUniqueURL());
        }
        return itemViewForm;

    }

    public static ItemViewForm createViewHome(Long id,String name,int price,List<UploadFile> imgFiles) {
        ItemViewForm itemViewForm = new ItemViewForm();
        itemViewForm.setId(id);
        itemViewForm.setName(name);
        itemViewForm.setPrice(price);
        itemViewForm.setImg(imgFiles.get(0).getUniqueURL());

        return itemViewForm;
    }
}
