package nl.quintor.declaration.controller;

import nl.quintor.declaration.config.WithMockUserCustomPrincipal;
import nl.quintor.declaration.model.Declaration;
import nl.quintor.declaration.service.DeclarationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DeclarationControllerSecurityTest {

    @MockBean
    private DeclarationService declarationService;

    @Autowired
    private DeclarationController declarationController;

    private List<Declaration> dummyData;

    @Before
    public void init() {


        dummyData = Arrays.asList(
                Declaration.builder().id(1).costs(12.0).employee("employee").build(),
                Declaration.builder().id(2).costs(13.0).employee("employee").build(),
                Declaration.builder().id(3).costs(14.0).employee("manager").build(),
                Declaration.builder().id(4).costs(15.0).employee("manager").build(),
                Declaration.builder().id(5).costs(16.0).employee("assistantManager").build()
        );
    }

    @Test
    @WithMockUserCustomPrincipal(username = "manager", roles = {"MANAGER"})
    public void getAll_withManagerRoleAndNoQueryParam_shouldReturnAllData() {
        String noQueryParam = null;

        when(declarationService.findAll()).thenReturn(dummyData);

        var declarations = declarationController.getAll(noQueryParam);

        verify(declarationService).findAll();
        assertThat(declarations).size().isEqualTo(5);
    }

    @Test
    @WithMockUserCustomPrincipal(username = "assistantManager", roles = {"ASSISTANT_MANAGER"})
    public void getAll_withAssistantManagerRoleAndNoQueryParam_shouldReturnAllData() {
        String noQueryParam = null;

        when(declarationService.findAll()).thenReturn(dummyData);

        var declarations = declarationController.getAll(noQueryParam);

        verify(declarationService).findAll();
        assertThat(declarations).size().isEqualTo(5);
    }

    @Test
    @WithMockUserCustomPrincipal(username = "employee", roles = {"EMPLOYEE"})
    public void getAll_withEmployeeRoleAndNoQueryParam_shouldThrowException() {
        String noQueryParam = null;

        assertThatThrownBy(() -> declarationController.getAll(noQueryParam))
                .isExactlyInstanceOf(AccessDeniedException.class)
                .hasMessage("Access is denied");
    }

    @Test
    @WithMockUserCustomPrincipal(username = "manager", roles = {"MANAGER"})
    public void getAll_withManagerRoleAndQueryParamForHimself_shouldReturnHisData() {
        var inputEmployee = "manager";
        List<Declaration> expectedOutput = dummyData.stream().filter(d -> d.getEmployee().equals(inputEmployee)).collect(Collectors.toList());

        when(declarationService.findAllByEmployee(inputEmployee))
                .thenReturn(expectedOutput);

        var declarations = declarationController.getAll(inputEmployee);

        verify(declarationService).findAllByEmployee(inputEmployee);
        assertThat(declarations).size().isEqualTo(2);
        assertThat(declarations).containsExactlyInAnyOrder(expectedOutput.toArray(Declaration[]::new));
    }

    @Test
    @WithMockUserCustomPrincipal(username = "assistantManager", roles = {"ASSISTANT_MANAGER"})
    public void getAll_withAssistantManagerRoleAndQueryParamForHimself_shouldReturnHisData() {
        var inputEmployee = "assistantManager";
        List<Declaration> expectedOutput = dummyData.stream().filter(d -> d.getEmployee().equals(inputEmployee)).collect(Collectors.toList());

        when(declarationService.findAllByEmployee(inputEmployee))
                .thenReturn(expectedOutput);

        var declarations = declarationController.getAll(inputEmployee);

        verify(declarationService).findAllByEmployee(inputEmployee);
        assertThat(declarations).size().isEqualTo(1);
        assertThat(declarations).containsExactlyInAnyOrder(expectedOutput.toArray(Declaration[]::new));
    }

    @Test
    @WithMockUserCustomPrincipal(username = "manager", roles = {"MANAGER"})
    public void getAll_withManagerRoleAndQueryParamForSomeoneElse_shouldReturnSomeoneElseData() {
        var inputEmployee = "employee";
        List<Declaration> expectedOutput = dummyData.stream().filter(d -> d.getEmployee().equals(inputEmployee)).collect(Collectors.toList());

        when(declarationService.findAllByEmployee(inputEmployee))
                .thenReturn(expectedOutput);

        var declarations = declarationController.getAll(inputEmployee);

        verify(declarationService).findAllByEmployee(inputEmployee);
        assertThat(declarations).size().isEqualTo(2);
        assertThat(declarations).containsExactlyInAnyOrder(expectedOutput.toArray(Declaration[]::new));
    }

    @Test
    @WithMockUserCustomPrincipal(username = "assistantManager", roles = {"ASSISTANT_MANAGER"})
    public void getAll_withAssistantManagerRoleAndQueryParamForSomeoneElse_shouldReturnSomeoneElseData() {
        var inputEmployee = "employee";
        List<Declaration> expectedOutput = dummyData.stream().filter(d -> d.getEmployee().equals(inputEmployee)).collect(Collectors.toList());

        when(declarationService.findAllByEmployee(inputEmployee))
                .thenReturn(expectedOutput);

        var declarations = declarationController.getAll(inputEmployee);

        verify(declarationService).findAllByEmployee(inputEmployee);
        assertThat(declarations).size().isEqualTo(2);
        assertThat(declarations).containsExactlyInAnyOrder(expectedOutput.toArray(Declaration[]::new));
    }

    @Test
    @WithMockUserCustomPrincipal(username = "employee", roles = {"EMPLOYEE"})
    public void getAll_withEmployeeRoleAndQueryParamForSomeoneElse_shouldThrowException() {
        var inputEmployee = "anotherEmployee";

        assertThatThrownBy(() -> declarationController.getAll(inputEmployee))
                .isExactlyInstanceOf(AccessDeniedException.class)
                .hasMessage("Access is denied");
    }



    @Test
    @WithMockUserCustomPrincipal(username = "manager", roles = {"MANAGER"})
    public void findById_withManagerRoleAndValidId_shouldReturnDeclaration() throws Exception {
        var inputId = 1;
        var expectedOutput = dummyData.get(inputId - 1);

        when(declarationService.findById(inputId)).thenReturn(Optional.of(expectedOutput));

        var declaraton = declarationController.findById(inputId);

        assertThat(declaraton).isNotNull();
        assertThat(declaraton.getId()).isEqualTo(inputId);
        assertThat(declaraton.getEmployee()).isEqualTo(expectedOutput.getEmployee());
        assertThat(declaraton.getCosts()).isEqualTo(expectedOutput.getCosts());
    }

    @Test
    @WithMockUserCustomPrincipal(username = "assistantManager", roles = {"ASSISTANT_MANAGER"})
    public void findById_withAssistantManagerRoleAndValidId_shouldReturnDeclaration() throws Exception {
        var inputId = 2;
        var expectedOutput = dummyData.get(inputId - 1);

        when(declarationService.findById(inputId)).thenReturn(Optional.of(expectedOutput));

        var declaraton = declarationController.findById(inputId);

        assertThat(declaraton).isNotNull();
        assertThat(declaraton.getId()).isEqualTo(inputId);
        assertThat(declaraton.getEmployee()).isEqualTo(expectedOutput.getEmployee());
        assertThat(declaraton.getCosts()).isEqualTo(expectedOutput.getCosts());
    }

    @Test
    @WithMockUserCustomPrincipal(username = "employee", roles = {"EMPLOYEE"})
    public void findById_withEmployeeRoleAndValidId_shouldReturnDeclarationIfHeIsTheEmployee() throws Exception {
        var inputId = 1;
        var expectedOutput = dummyData.get(inputId - 1);

        when(declarationService.findById(inputId)).thenReturn(Optional.of(expectedOutput));

        var declaration = declarationController.findById(inputId);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getId()).isEqualTo(inputId);
        assertThat(declaration.getEmployee()).isEqualTo(expectedOutput.getEmployee());
        assertThat(declaration.getCosts()).isEqualTo(expectedOutput.getCosts());
    }

    @Test
    @WithMockUserCustomPrincipal(username = "employee", roles = {"EMPLOYEE"})
    public void findById_withEmployeeRoleAndValidId_shouldThrowExceptionIfHeIsNotTheEmployee() throws Exception {
        var inputId = 3;
        var expectedOutput = dummyData.get(inputId - 1);

        when(declarationService.findById(3)).thenReturn(Optional.of(expectedOutput));

        assertThatThrownBy(() -> declarationController.findById(inputId))
                .isExactlyInstanceOf(AccessDeniedException.class)
                .hasMessage("Access is denied");
    }

    @Test
    @WithMockUserCustomPrincipal(username = "manager", roles = {"MANAGER"})
    public void findById_withManagerRoleAndInvalidId_shouldReturnException()  {
        var invalidInputId = 1000;

        when(declarationService.findById(invalidInputId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> declarationController.findById(invalidInputId))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find declaration for given Id");
    }

    @Test
    @WithMockUserCustomPrincipal(username = "assistantManager", roles = {"ASSISTANT_MANAGER"})
    public void findById_withAssistantManagerRoleAndInvalidId_shouldReturnException()  {
        var invalidInputId = 1000;

        when(declarationService.findById(invalidInputId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> declarationController.findById(invalidInputId))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find declaration for given Id");
    }

    @Test
    @WithMockUserCustomPrincipal(username = "employee", roles = {"EMPLOYEE"})
    public void findById_withEmployeeRoleAndInvalidId_shouldReturnException()  {
        var invalidInputId = 1000;

        when(declarationService.findById(invalidInputId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> declarationController.findById(invalidInputId))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find declaration for given Id");
    }


    @Test
    @WithMockUserCustomPrincipal(username = "manager", roles = {"MANAGER"})
    public void update_withManagerRoleAndAnyDeclaration_shouldUpdateDeclaration() {
        var inputDeclaration = Declaration.builder()
                .id(1)
                .costs(15.0)
                .employee("anyEmployee")
                .build();

        when(declarationService.save(inputDeclaration)).thenReturn(inputDeclaration);

        var declaration = declarationController.update(inputDeclaration);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getId()).isEqualTo(inputDeclaration.getId());
        assertThat(declaration.getCosts()).isEqualTo(inputDeclaration.getCosts());
        assertThat(declaration.getEmployee()).isEqualTo(inputDeclaration.getEmployee());
    }

    @Test
    @WithMockUserCustomPrincipal(username = "assistantManager", roles = {"ASSISTANT_MANAGER"})
    public void update_withAssistantManagerRoleAndAnyDeclaration_shouldUpdateDeclaration() {
        var inputDeclaration = Declaration.builder()
                .id(1)
                .costs(15.0)
                .employee("anyEmployee")
                .build();

        when(declarationService.save(inputDeclaration)).thenReturn(inputDeclaration);

        var declaration = declarationController.update(inputDeclaration);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getId()).isEqualTo(inputDeclaration.getId());
        assertThat(declaration.getCosts()).isEqualTo(inputDeclaration.getCosts());
        assertThat(declaration.getEmployee()).isEqualTo(inputDeclaration.getEmployee());
    }

    @Test
    @WithMockUserCustomPrincipal(username = "employee", roles = {"EMPLOYEE"})
    public void update_withEmployeeRoleAndHisOwnDeclaration_shouldUpdateDeclaration() {
        var inputDeclaration = Declaration.builder()
                .id(1)
                .costs(15.0)
                .employee("employee")
                .build();

        when(declarationService.save(inputDeclaration)).thenReturn(inputDeclaration);

        var declaration = declarationController.update(inputDeclaration);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getId()).isEqualTo(inputDeclaration.getId());
        assertThat(declaration.getCosts()).isEqualTo(inputDeclaration.getCosts());
        assertThat(declaration.getEmployee()).isEqualTo(inputDeclaration.getEmployee());
    }

    @Test
    @WithMockUserCustomPrincipal(username = "EMPLOYEE", roles = {"EMPLOYEE"})
    public void update_withEmployeeRoleAndSomeoneElseDeclaration_shouldThrowException() {
        var inputDeclaration = Declaration.builder()
                .id(1)
                .costs(15.0)
                .employee("anyEmployee")
                .build();

        assertThatThrownBy(() -> declarationController.update(inputDeclaration))
                .isExactlyInstanceOf(AccessDeniedException.class)
                .hasMessage("Access is denied");

    }

    @Test
    @WithMockUserCustomPrincipal(username = "manager", roles = {"MANAGER"})
    public void create_withManagerRoleAndAnyDeclaration_shouldCreateDeclaration() {
        var inputDeclaration = Declaration.builder()
                .costs(15.0)
                .employee("anyEmployee")
                .build();

        var expectedOutput = Declaration.builder()
                .id(1)
                .costs(15.0)
                .employee("anyEmployee")
                .build();

        when(declarationService.save(inputDeclaration)).thenReturn(expectedOutput);

        var declaration = declarationController.create(inputDeclaration);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getId()).isEqualTo(expectedOutput.getId());
        assertThat(declaration.getCosts()).isEqualTo(expectedOutput.getCosts());
        assertThat(declaration.getEmployee()).isEqualTo(expectedOutput.getEmployee());

    }

    @Test
    @WithMockUserCustomPrincipal(username = "assistantManager", roles = {"ASSISTANT_MANAGER"})
    public void create_withAssistantManagerRoleAndAnyDeclaration_shouldCreateDeclaration() {
        var inputDeclaration = Declaration.builder()
                .costs(15.0)
                .employee("anyEmployee")
                .build();

        var expectedOutput = Declaration.builder()
                .id(1)
                .costs(15.0)
                .employee("anyEmployee")
                .build();

        when(declarationService.save(inputDeclaration)).thenReturn(expectedOutput);

        var declaration = declarationController.create(inputDeclaration);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getId()).isEqualTo(expectedOutput.getId());
        assertThat(declaration.getCosts()).isEqualTo(expectedOutput.getCosts());
        assertThat(declaration.getEmployee()).isEqualTo(expectedOutput.getEmployee());

    }

    @Test
    @WithMockUserCustomPrincipal(username = "employee", roles = {"EMPLOYEE"})
    public void create_withEmployeeRoleAndDeclarationEmployeeIsHimself_shouldCreateDeclaration() {
        var inputDeclaration = Declaration.builder()
                .costs(15.0)
                .employee("employee")
                .build();

        var expectedOutput = Declaration.builder()
                .id(1)
                .costs(15.0)
                .employee("employee")
                .build();

        when(declarationService.save(inputDeclaration)).thenReturn(expectedOutput);

        var declaration = declarationController.create(inputDeclaration);

        assertThat(declaration).isNotNull();
        assertThat(declaration.getId()).isEqualTo(expectedOutput.getId());
        assertThat(declaration.getCosts()).isEqualTo(expectedOutput.getCosts());
        assertThat(declaration.getEmployee()).isEqualTo(expectedOutput.getEmployee());
    }


    @Test
    @WithMockUserCustomPrincipal(username = "employee", roles = {"EMPLOYEE"})
    public void create_withEmployeeRoleAndDeclarationEmployeeIsSomeoneElse_shouldThrowException() {
        var inputDeclaration = Declaration.builder()
                .costs(15.0)
                .employee("someoneElse")
                .build();

        assertThatThrownBy(() -> declarationController.create(inputDeclaration))
                .isExactlyInstanceOf(AccessDeniedException.class)
                .hasMessage("Access is denied");
    }

}
