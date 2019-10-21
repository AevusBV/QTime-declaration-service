package nl.quintor.declaration.security.accessLayers.helper;

import nl.quintor.declaration.service.DeclarationService;
import nl.quintor.declaration.service.ImageService;
import org.springframework.stereotype.Component;

@Component
public class SecurityHelperFunctions {

    private ImageService imageService;
    private DeclarationService declarationService;

    public SecurityHelperFunctions(ImageService imageService, DeclarationService declarationService) {
        this.imageService = imageService;
        this.declarationService = declarationService;
    }

    public boolean isOwnerOfImage(long id, String principal) {
        return imageService.findById(id)
                .map(image -> image.getEmployee().equals(principal) )
                .orElse(false);
    }

}
