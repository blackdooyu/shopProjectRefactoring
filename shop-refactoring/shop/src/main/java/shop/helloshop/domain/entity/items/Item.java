package shop.helloshop.domain.entity.items;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import shop.helloshop.domain.entity.CreateDate;
import shop.helloshop.domain.entity.Member;
import shop.helloshop.domain.entity.UploadFile;
import shop.helloshop.web.exception.ItemException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type",discriminatorType = DiscriminatorType.STRING)
@Getter @Setter
public abstract class Item extends CreateDate {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(insertable = false,updatable = false)
    private String type;

    private String name;

    private int price;

    private int quantity;

    private LocalDateTime localDateTime;

    private int salesQuantity;


    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL,orphanRemoval = true)
    @BatchSize(size = 16)
    private List<UploadFile> uploadFiles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //연관관계 편의 메서드
    public void addUploadFile(UploadFile uploadFile) {
        this.uploadFiles.add(uploadFile);
        uploadFile.setItem(this);
    }

    //Update 편의 메서드
    public void update(String name, int price , int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    //판매될시 수량 빼기, 판매량 증가,수량 체크 메서드
    public void sale(int quantity) {
        if(this.quantity - quantity < 0){
            throw new ItemException("남은수량이 주문수량보다 부족합니다.");
        }

        this.quantity -= quantity;
        this.salesQuantity += quantity;
    }

    //주문취소될시 수량 증가 AND 판매량 감소 메서드
    public void cancel(int quantity) {
        this.quantity += quantity;
        this.salesQuantity -= quantity;
    }

    //가격 메서드
    public int getTotalPrice(int quantity) {
        return this.price*quantity;
    }


}
