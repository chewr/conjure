/*
 * (c) Copyright 2017 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.gen.python.types;

import static com.google.common.base.Preconditions.checkNotNull;

import com.palantir.conjure.defs.types.BaseObjectTypeDefinition;
import com.palantir.conjure.defs.types.ConjureTypeVisitor;
import com.palantir.conjure.defs.types.TypesDefinition;
import com.palantir.conjure.defs.types.builtin.AnyType;
import com.palantir.conjure.defs.types.builtin.BinaryType;
import com.palantir.conjure.defs.types.builtin.DateTimeType;
import com.palantir.conjure.defs.types.collect.ListType;
import com.palantir.conjure.defs.types.collect.MapType;
import com.palantir.conjure.defs.types.collect.OptionalType;
import com.palantir.conjure.defs.types.collect.SetType;
import com.palantir.conjure.defs.types.primitive.PrimitiveType;
import com.palantir.conjure.defs.types.reference.ExternalTypeDefinition;
import com.palantir.conjure.defs.types.reference.ForeignReferenceType;
import com.palantir.conjure.defs.types.reference.LocalReferenceType;

public final class DefaultTypeNameVisitor implements ConjureTypeVisitor<String> {

    private final TypesDefinition types;

    public DefaultTypeNameVisitor(TypesDefinition types) {
        this.types = types;
    }

    @Override
    public String visit(AnyType type) {
        return "object";
    }

    @Override
    public String visit(ListType type) {
        return "ListType(" + type.itemType().visit(this) + ")";
    }

    @Override
    public String visit(MapType type) {
        return "DictType(" + type.keyType().visit(this) + ", " + type.valueType().visit(this) + ")";
    }

    @Override
    public String visit(OptionalType type) {
        return "OptionalType(" + type.itemType().visit(this) + ")";
    }

    @Override
    public String visit(PrimitiveType type) {
        switch (type) {
            case STRING:
            case RID:
            case BEARERTOKEN:
                return "str";
            case BOOLEAN:
                return "bool";
            case DOUBLE:
                return "float";
            case INTEGER:
            case SAFELONG:
                return "int";
            default:
                throw new IllegalArgumentException("unknown type: " + type);
        }
    }

    @Override
    public String visit(LocalReferenceType type) {
        // Types without namespace are either defined locally in this conjure definition, or raw imports.
        BaseObjectTypeDefinition baseType = types.definitions().objects().get(type.type());
        if (baseType != null) {
            return type.type().name();
        } else {
            ExternalTypeDefinition depType = types.imports().get(type.type());
            checkNotNull(depType, "Unable to resolve type %s", type.type());
            return visit(depType.baseType());
        }
    }

    @Override
    public String visit(ForeignReferenceType type) {
        return type.type().name();
    }

    @Override
    public String visit(SetType type) {
        // TODO (bduffield): real sets
        return ListType.of(type.itemType()).visit(this);
    }

    @Override
    public String visit(BinaryType type) {
        return "BinaryType()";
    }

    @Override
    public String visit(DateTimeType type) {
        return "str";
    }

}
