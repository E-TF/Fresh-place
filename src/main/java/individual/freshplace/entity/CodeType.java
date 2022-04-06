package individual.freshplace.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class CodeType {

    @Id
    @Column(name = "code_type")
    private String id;

    @Column(name = "code_type_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_code_type")
    private CodeType parent;

    @OneToMany(mappedBy = "parent")
    private List<CodeType> child = new ArrayList<>();

    @OneToMany(mappedBy = "codeType")
    private List<Code> codeList = new ArrayList<>();

}
