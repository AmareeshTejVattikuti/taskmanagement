package com.simplesystems.taskmanagement.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.simplesystems.taskmanagement.deserializer.CustomLongDeserializer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class UpdateDescription {

    @NotNull(message = "Task id is required")
    @Positive(message = "Task id must be greater than 0")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long id;

    @NotNull(message = "Description is required")
    @NotEmpty(message = "Description cannot be Empty")
    private String description;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UpdateDescription that = (UpdateDescription) obj;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }
}
