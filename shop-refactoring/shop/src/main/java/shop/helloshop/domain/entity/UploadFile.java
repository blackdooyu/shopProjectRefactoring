package shop.helloshop.domain.entity;

import lombok.Getter;
import lombok.Setter;
import shop.helloshop.domain.entity.items.Item;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter @Setter
public class UploadFile {


    public UploadFile(String originalURL, String uniqueURL) {
        this.originalURL = originalURL;
        this.uniqueURL = uniqueURL;
    }

    public UploadFile() {
    }

    @Id @GeneratedValue
    @Column(name = "upload_file_id")
    private Long id;

    private String originalURL;

    private String uniqueURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;





}
