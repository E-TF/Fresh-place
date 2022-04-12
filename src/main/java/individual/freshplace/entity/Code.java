package individual.freshplace.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(Code.CodeId.class)
@Getter
public class Code {

    @Id
    @Column(name = "code_value")
    private String id;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_type")
    private CodeType codeType;

    @Column(name = "code_name")
    private String name;

    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    static class CodeId implements Serializable {

        private String id;

        private String codeType;

    }
}
