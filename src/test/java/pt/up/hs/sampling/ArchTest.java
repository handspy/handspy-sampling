package pt.up.hs.sampling;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("pt.up.hs.sampling");

        noClasses()
            .that()
                .resideInAnyPackage("pt.up.hs.sampling.service..")
            .or()
                .resideInAnyPackage("pt.up.hs.sampling.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..pt.up.hs.sampling.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
