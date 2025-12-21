package com.example.resource_client_2.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Status extends BaseModel {

    @JsonProperty(value = "status_code", required = true)
    private int statusCode;

    @JsonProperty(value = "message", required = true)
    private String message;

    @JsonProperty(value = "helper_message", required = false)
    private String helperMessage;


    // -------------------------------------------------------------
    // Static Factory Method (Needed for ExceptionAdvisor)
    // -------------------------------------------------------------
    public static Status of(int code, String message, String helperMessage) {
        return Status.builder()
                .statusCode(code)
                .message(message)
                .helperMessage(helperMessage)
                .build();
    }


    // -------------------------------------------------------------
    // Predefined Helpers (Optional but recommended)
    // -------------------------------------------------------------
    public static Status success() {
        return of(200, "SUCCESS", null);
    }

    public static Status badRequest(String helperMessage) {
        return of(400, "BAD_REQUEST", helperMessage);
    }

    public static Status notFound(String helperMessage) {
        return of(404, "NOT_FOUND", helperMessage);
    }

    public static Status internalError(String helperMessage) {
        return of(500, "INTERNAL_SERVER_ERROR", helperMessage);
    }
}
