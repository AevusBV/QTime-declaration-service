package nl.quintor.declaration.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Declaration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double costs;
    private boolean approvedLocal;
    private boolean approvedGlobal;
    private String employee;
    private String instanceId;
    private long imageId;
}
