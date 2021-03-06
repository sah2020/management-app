package ecma.ai.hrapp.entity;

import ecma.ai.hrapp.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity//TURNSTILE
@EntityListeners(AuditingEntityListener.class)
public class Turniket extends AbsEntity {

    @ManyToOne
    private Company company;

    @OneToOne
    private User owner;

    private String number = UUID.randomUUID().toString();

    private boolean enabled = true;

}
