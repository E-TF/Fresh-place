package individual.freshplace.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
public class Code {

    @Id
    private String codeValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_type")
    private CodeType codeType;

    private String codeName;
}
