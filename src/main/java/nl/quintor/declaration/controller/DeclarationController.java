package nl.quintor.declaration.controller;

import nl.quintor.declaration.model.Declaration;
import nl.quintor.declaration.security.accessLayers.IsManagerOrAssistantManagerOrDeclarationEmployeeIsUser;
import nl.quintor.declaration.security.accessLayers.IsManagerOrAssistantManagerOrEmployeeIsUser;
import nl.quintor.declaration.security.accessLayers.IsManagerOrAssistantManagerOrIdMatchesDeclarationWhereUserIsOwner;
import nl.quintor.declaration.service.DeclarationService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class DeclarationController {

    private DeclarationService declarationService;

    public DeclarationController(DeclarationService declarationService) {
        this.declarationService = declarationService;
    }

    @GetMapping
    @IsManagerOrAssistantManagerOrEmployeeIsUser
    public List<Declaration> getAll(@RequestParam(value = "employee", required = false) String employee) {
        return Optional.ofNullable(employee).map(declarationService::findAllByEmployee)
                .orElse(declarationService.findAll());
    }

    @GetMapping("/{id}")
    @IsManagerOrAssistantManagerOrIdMatchesDeclarationWhereUserIsOwner
    public Declaration findById(@PathVariable final long id) {
        return declarationService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find declaration for given Id"));
    }

    @PutMapping
    @IsManagerOrAssistantManagerOrDeclarationEmployeeIsUser
    public Declaration update(@RequestBody final Declaration declaration) {
        return declarationService.save(declaration);
    }

    @PostMapping
    @IsManagerOrAssistantManagerOrDeclarationEmployeeIsUser
    public Declaration create(@RequestBody final Declaration declaration) {
        return declarationService.save(declaration);
    }

}
