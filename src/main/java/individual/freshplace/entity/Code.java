package individual.freshplace.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
public class Code {

    @Id
    @Column(name = "code_value")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_type")
    private CodeType codeType;

    @Column(name = "code_name")
    private String name;
}
