package nl.quintor.declaration.controller;

import nl.quintor.declaration.config.WithMockUserCustomPrincipal;
import nl.quintor.declaration.model.Image;
import nl.quintor.declaration.service.ImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ImageControllerSecurityTest {

    @MockBean
    private ImageService imageService;

    @Autowired
    private ImageController imageController;

    @Test
    @WithMockUserCustomPrincipal(username = "employee", roles = {"EMPLOYEE"})
    public void create_withEmployeeRoleAndValidInput_shouldCreateImage() {
        var expectedOutputId = 1l;
        var inputImage = Image.builder().fileName("testFile").name("test").build();
        var outputImage = Image.builder().id(expectedOutputId).fileName("testFile").name("test").build();

        when(imageService.save(inputImage)).thenReturn(outputImage);

        var imageId = imageController.create(inputImage);

        assertThat(imageId).isEqualTo(expectedOutputId);
    }

    @Test
    @WithMockUserCustomPrincipal(username = "employee", roles = {"SOME_ROLE"})
    public void create_withNoEmployeeRoleAndValidInput_shouldCreateImage() {
       var inputImage = Image.builder().build();

       assertThatThrownBy(() -> imageController.create(inputImage))
               .isExactlyInstanceOf(AccessDeniedException.class)
               .hasMessage("Access is denied");
    }

    @Test
    @WithMockUserCustomPrincipal(username = "employee", roles = {"EMPLOYEE"})
    public void findImage_withEmployeeRoleAndUserIsOwner_shouldReturnImage() {
        var inputId = 1;
        var imageOwner = "employee";

        when(imageService.findById(inputId)).thenReturn(Optional.of(Image.builder().id(1).employee(imageOwner).file("hello".getBytes()).build()));

        var img = imageController.findImage(inputId);

        assertThat(img).isNotNull();
        assertThat(new String(img)).isEqualTo("hello");

    }

    @Test
    @WithMockUserCustomPrincipal(username = "employee", roles = {"EMPLOYEE"})
    public void findImage_withEmployeeRoleAndUserIsNotOwner_shouldThrowException() {
        var inputId = 1;
        var imageOwner = "otherEmployee";

        when(imageService.findById(inputId)).thenReturn(Optional.of(Image.builder().id(1).employee(imageOwner).file("hello".getBytes()).build()));

        assertThatThrownBy(() -> imageController.findImage(inputId))
        .isExactlyInstanceOf(AccessDeniedException.class)
        .hasMessage("Access is denied");
    }

    @Test
    @WithMockUserCustomPrincipal(username = "manager", roles = {"MANAGER"})
    public void findImage_withManagerRoleAndUserIsNotOwner_shouldReturnImage() {
        var inputId = 1;
        var imageOwner = "employee";

        when(imageService.findById(inputId)).thenReturn(Optional.of(Image.builder().id(1).employee(imageOwner).file("hello".getBytes()).build()));

        var img = imageController.findImage(inputId);

        assertThat(img).isNotNull();
        assertThat(new String(img)).isEqualTo("hello");

    }

    @Test
    @WithMockUserCustomPrincipal(username = "assistantManager", roles = {"ASSISTANT_MANAGER"})
    public void findImage_withAssistantManagerRoleAndUserIsNotOwner_shouldReturnImage() {
        var inputId = 1;
        var imageOwner = "employee";

        when(imageService.findById(inputId)).thenReturn(Optional.of(Image.builder().id(1).employee(imageOwner).file("hello".getBytes()).build()));

        var img = imageController.findImage(inputId);

        assertThat(img).isNotNull();
        assertThat(new String(img)).isEqualTo("hello");

    }
}
