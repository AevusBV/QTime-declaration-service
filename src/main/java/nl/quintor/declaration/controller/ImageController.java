package nl.quintor.declaration.controller;

import nl.quintor.declaration.model.Image;
import nl.quintor.declaration.security.accessLayers.IsEmployee;
import nl.quintor.declaration.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("/image")
public class ImageController {

    private ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    @IsEmployee
    public long create(@RequestBody Image image) {
        return imageService.save(image).getId();
    }

    @PermitAll
    @PreAuthorize("@securityHelperFunctions.isOwnerOfImage(#id, principal) or hasAnyRole('ROLE_MANAGER', 'ROLE_ASSISTANT_MANAGER')")
    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] findImage(@PathVariable long id) {
        return imageService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find image"))
                .getFile();
    }
}
