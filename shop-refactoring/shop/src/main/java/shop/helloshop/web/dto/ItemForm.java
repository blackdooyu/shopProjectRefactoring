package shop.helloshop.web.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import shop.helloshop.domain.entity.UploadFile;
import shop.helloshop.domain.entity.items.Clothes;
import shop.helloshop.domain.entity.items.ItemSize;
import shop.helloshop.domain.entity.items.Phone;
import shop.helloshop.domain.entity.items.PhoneColor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemForm {

    private String select;

    private Long id;

    @NotBlank(message = "필수 값 입니다")
    private String name;

    @Positive(message = "입력해주세요")
    private int price;

    @Positive(message = "입력해주세요")
    private int quantity;

    private ItemSize itemSize;

    private PhoneColor phoneColor;

    private List<MultipartFile> multipartFileList = new ArrayList<>();

    public static ItemForm phoneUpdateForm(Phone phone) {
        ItemForm itemForm = new ItemForm();
        itemForm.setName(phone.getName());
        itemForm.setPrice(phone.getPrice());
        itemForm.setQuantity(phone.getQuantity());
        itemForm.setPhoneColor(phone.getPhoneColor());
        return itemForm;
    }
    public static ItemForm clothesUpdateForm(Clothes clothes) {
        ItemForm itemForm = new ItemForm();
        itemForm.setName(clothes.getName());
        itemForm.setPrice(clothes.getPrice());
        itemForm.setQuantity(clothes.getQuantity());
        itemForm.setItemSize(clothes.getItemSize());
        return itemForm;
    }

    }


