/*
 * (c) Copyright 2016 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.defs.services;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.palantir.conjure.defs.ConjureImmutablesStyle;
import com.palantir.conjure.defs.types.ConjureType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.immutables.value.Value;

@JsonDeserialize(as = ImmutableEndpointDefinition.class)
@Value.Immutable
@ConjureImmutablesStyle
public interface EndpointDefinition {

    RequestLineDefinition http();

    Optional<AuthDefinition> auth();

    Optional<Map<ParameterName, ArgumentDefinition>> args();

    /**
     * Returns the arguments for this endpoint where all instances of ParamType.AUTO have been set to
     * ParamType.PATH or ParamType.BODY.
     *
     * @return the arguments for this endpoint
     */
    @Value.Derived
    default Optional<Map<ParameterName, ArgumentDefinition>> argsWithAutoDefined() {
        if (!args().isPresent()) {
            return Optional.empty();
        }

        Set<ParameterName> pathArgs = http().pathArgs();
        Map<ParameterName, ArgumentDefinition> outputMap = new LinkedHashMap<>();
        args().orElse(ImmutableMap.of()).entrySet().stream()
                .map(entry -> {
                    ArgumentDefinition origArgDef = entry.getValue();
                    ArgumentDefinition.Builder builder = ArgumentDefinition.builder().from(origArgDef);
                    if (origArgDef.paramType() == ArgumentDefinition.ParamType.AUTO) {
                        if (pathArgs.contains(origArgDef.paramId().orElse(entry.getKey()))) {
                            // argument exists in request line -- it is a path arg
                            builder.paramType(ArgumentDefinition.ParamType.PATH);
                        } else {
                            // argument does not exist in request line -- it is a body arg
                            builder.paramType(ArgumentDefinition.ParamType.BODY);
                        }
                    }
                    return Maps.immutableEntry(entry.getKey(), builder.build());
                })
                .forEachOrdered(entry -> outputMap.put(entry.getKey(), entry.getValue()));
        return Optional.of(outputMap);
    }

    Optional<ConjureType> returns();

    Optional<String> docs();

    Optional<String> deprecated();

    @Value.Check
    default void check() {
        for (EndpointDefinitionValidator validator : EndpointDefinitionValidator.values()) {
            validator.validate(this);
        }
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableEndpointDefinition.Builder {}

}
