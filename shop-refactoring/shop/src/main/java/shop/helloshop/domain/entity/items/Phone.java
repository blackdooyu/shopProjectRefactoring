package shop.helloshop.domain.entity.items;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("P")
@Getter @Setter
public class Phone extends Item{

    @Enumerated(EnumType.STRING)
    private PhoneColor phoneColor;
}
