package com.nisum.challenge;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.nisum.challenge", importOptions = ImportOption.DoNotIncludeTests.class)
public class HexagonalArchitectureTest {

    private static final String DOMAIN = "Domain";
    private static final String APPLICATION = "Application";
    private static final String INFRASTRUCTURE = "Infrastructure";

    @ArchTest
    public static final ArchRule domain_should_not_depend_on_other_layers = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAnyPackage("..application..", "..infrastructure..");

    @ArchTest
    public static final ArchRule hexagonal_architecture_layers_are_respected = layeredArchitecture()
            .consideringAllDependencies()
            .layer(DOMAIN).definedBy("..domain..")
            .layer(APPLICATION).definedBy("..application..")
            .layer(INFRASTRUCTURE).definedBy("..infrastructure..")

            .whereLayer(APPLICATION).mayOnlyBeAccessedByLayers(INFRASTRUCTURE)
            .whereLayer(DOMAIN).mayOnlyBeAccessedByLayers(APPLICATION, INFRASTRUCTURE);
}
