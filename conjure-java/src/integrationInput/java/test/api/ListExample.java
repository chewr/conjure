package test.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;

@JsonDeserialize(
        builder = ListExample.Builder.class
)
@Generated("com.palantir.conjure.gen.java.types.BeanGenerator")
public final class ListExample {
    private final List<String> items;

    private ListExample(List<String> items) {
        validateFields(items);
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
    }

    @JsonProperty("items")
    public List<String> getItems() {
        return this.items;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof ListExample && equalTo((ListExample) other));
    }

    private boolean equalTo(ListExample other) {
        return this.items.equals(other.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    @Override
    public String toString() {
        return new StringBuilder("ListExample").append("{")
                .append("items").append(": ").append(items)
            .append("}")
            .toString();
    }

    public static ListExample of(List<String> items) {
        return builder()
            .items(items)
            .build();
    }

    private static void validateFields(List<String> items) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
        if (missingFields != null) {
            throw new IllegalStateException("Some required fields have not been set: " + missingFields);
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(1);
            }
            missingFields.add(fieldName);
        }
        return missingFields;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonIgnoreProperties(
            ignoreUnknown = true
    )
    public static final class Builder {
        private List<String> items = new ArrayList<>();

        private Builder() {
        }

        public Builder from(ListExample other) {
            items(other.getItems());
            return this;
        }

        @JsonSetter("items")
        public Builder items(Collection<String> items) {
            this.items.clear();
            this.items.addAll(Objects.requireNonNull(items, "items cannot be null"));
            return this;
        }

        public Builder addAllItems(Collection<String> items) {
            this.items.addAll(Objects.requireNonNull(items, "items cannot be null"));
            return this;
        }

        public Builder items(String items) {
            this.items.add(items);
            return this;
        }

        public ListExample build() {
            return new ListExample(items);
        }
    }
}