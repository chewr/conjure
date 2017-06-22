/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */

package com.palantir.conjure.gen.typescript.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.CharStreams;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.defs.ConjureDefinition;
import com.palantir.conjure.defs.types.names.ConjurePackage;
import com.palantir.conjure.gen.typescript.poet.ExportStatement;
import com.palantir.conjure.gen.typescript.poet.TypescriptFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public final class ConjureTypescriptServiceTest {

    @Test
    public void testTypescriptServiceGenerator_generate_testService() throws IOException {
        ConjureDefinition def = Conjure.parse(new File("src/test/resources/services/test-service.yml"));

        Set<TypescriptFile> files = new DefaultServiceGenerator().generate(def);

        for (TypescriptFile file : files) {
            assertThat(file.writeToString()).isEqualTo(CharStreams.toString(new InputStreamReader(
                    getClass().getResourceAsStream("/services/" + StringUtils.uncapitalize(file.name()) + ".ts.output"),
                    StandardCharsets.UTF_8)));
        }
    }

    @Test
    public void testTypescriptServiceGenerator_generateExports_testService() {
        ConjureDefinition def = Conjure.parse(new File("src/test/resources/services/test-service.yml"));
        Map<ConjurePackage, Collection<ExportStatement>> exports = new DefaultServiceGenerator().generateExports(def);

        assertThat(exports).containsExactly(entry(
                ConjurePackage.of("com.palantir.foundry.catalog.api"),
                ImmutableSet.of(
                        ExportStatement.builder()
                                .addNamesToExport("ITestService")
                                .filepathToExport("./testService")
                                .build(),
                        ExportStatement.builder()
                                .addNamesToExport("TestService")
                                .filepathToExport("./testServiceImpl")
                                .build())));
    }

    @Test
    public void testTypescriptServiceGenerator_generateExports_testServiceDuplicate() {
        ConjureDefinition def = Conjure.parse(new File("src/test/resources/services/test-service-duplicates.yml"));
        Map<ConjurePackage, Collection<ExportStatement>> exports = new DefaultServiceGenerator().generateExports(def);

        // duplicate names are fine at this level; they are expected to be handled by caller
        assertThat(exports).containsExactly(entry(
                ConjurePackage.of("com.palantir.test.api"),
                ImmutableSet.of(ExportStatement.builder()
                        .addNamesToExport("IDuplicateService")
                        .filepathToExport("./duplicateService")
                        .build(),
                        ExportStatement.builder()
                                .addNamesToExport("DuplicateService")
                                .filepathToExport("./duplicateServiceImpl")
                                .build(),
                        ExportStatement.builder()
                                .addNamesToExport("IMyDuplicateService")
                                .filepathToExport("./myDuplicateService")
                                .build(),
                        ExportStatement.builder()
                                .addNamesToExport("MyDuplicateService")
                                .filepathToExport("./myDuplicateServiceImpl")
                                .build())));
    }

    @Test
    public void testTypescriptServiceGenerator_doesNotProduceMultipleSlashes() throws IOException {
        ConjureDefinition def = Conjure.parse(new File("src/test/resources/services/test-service-root-path.yml"));
        Set<TypescriptFile> files = new DefaultServiceGenerator().generate(def);
        String implFile = files.stream().filter(file -> file.name().equals("MyServiceImpl"))
                .findFirst().get().writeToString();
        assertThat(implFile).contains("/foo");
        assertThat(implFile).doesNotContain("//foo");
    }
}
