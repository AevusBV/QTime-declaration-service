package nl.quintor.declaration.repository;

import nl.quintor.declaration.model.Declaration;
import org.dom4j.dtd.Decl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {
    List<Declaration> findAllByEmployee(String employee);

}
