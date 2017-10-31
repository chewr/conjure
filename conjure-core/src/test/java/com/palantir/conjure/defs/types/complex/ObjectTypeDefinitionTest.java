/*
 * (c) Copyright 2017 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.defs.types.complex;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.palantir.conjure.defs.types.names.FieldName;
import org.junit.Test;

public final class ObjectTypeDefinitionTest {

    private static final FieldDefinition FIELD = mock(FieldDefinition.class);

    @Test
    public void testUniqueFieldNamesValidator() throws Exception {
        ObjectTypeDefinition.Builder definition = ObjectTypeDefinition.builder()
                .putFields(FieldName.of("fooBar"), FIELD)
                .putFields(FieldName.of("foo-bar"), FIELD);

        assertThatThrownBy(definition::build)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ObjectTypeDefinition must not contain duplicate field names (modulo case normalization): "
                        + "foo-bar vs fooBar");

        definition = ObjectTypeDefinition.builder()
                .putFields(FieldName.of("foo-bar"), FIELD)
                .putFields(FieldName.of("foo_bar"), FIELD);

        assertThatThrownBy(definition::build)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ObjectTypeDefinition must not contain duplicate field names (modulo case normalization): "
                        + "foo_bar vs foo-bar");
    }
}
