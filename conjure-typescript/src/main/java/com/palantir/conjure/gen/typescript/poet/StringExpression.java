/*
 * (c) Copyright 2016 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.gen.typescript.poet;

import com.palantir.conjure.defs.ConjureImmutablesStyle;
import org.immutables.value.Value;

@ConjureImmutablesStyle
@Value.Immutable
public interface StringExpression extends TypescriptExpression {
    String content();

    @Override
    default void emit(TypescriptPoetWriter writer) {
        writer.write(emitToString());
    }

    default String emitToString() {
        return "\"" + content().replaceAll("\"", "\\\"") + "\"";
    }

    static StringExpression of(String content) {
        return ImmutableStringExpression.builder().content(content).build();
    }
}
