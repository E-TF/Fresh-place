package individual.freshplace.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class CodeType {

    @Id
    private String codeType;

    private String codeTypeName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_code_type")
    private CodeType parent;

    @OneToMany(mappedBy = "codeType")
    private List<Code> codeList = new ArrayList<>();
}
