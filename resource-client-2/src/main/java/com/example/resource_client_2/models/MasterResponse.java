package com.example.resource_client_2.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MasterResponse<T> extends BaseModel {

    @JsonProperty(value = "status", required = true)
    private Status status;

    @JsonProperty(value = "data", required = false)
    private T data;

    // -----------------------------------------------
    // FIX: Typed builder method for generics
    // -----------------------------------------------
    public static <T> MasterResponseBuilder<T> builder() {
        return new MasterResponseBuilder<T>();
    }
}
