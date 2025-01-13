package de.unistuttgart.iste.pe2.api;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

/**
 * Custom annotation to mark API endpoints as version 1.
 * This annotation combines @Component and @RequestMapping("/api/v1") annotations.
 * It is used to standardize the API versioning across controllers.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@RequestMapping("/api/v1")
public @interface ApiVersion1 {
    /**
     * Alias for the value attribute of @Component annotation.
     *
     * @return the component name
     */
    @AliasFor(annotation = Component.class)
    String value() default "";
}
