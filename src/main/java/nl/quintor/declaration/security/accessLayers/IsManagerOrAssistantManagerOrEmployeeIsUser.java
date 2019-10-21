package nl.quintor.declaration.security.accessLayers;


import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("#employee == authentication.principal or hasAnyRole('ROLE_MANAGER', 'ROLE_ASSISTANT_MANAGER')")
public @interface IsManagerOrAssistantManagerOrEmployeeIsUser {
}
