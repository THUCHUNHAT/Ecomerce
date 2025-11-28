package com.example.ecomerce.Response;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class ApiResponse {
    private String message;
    private Object data;
    private String errorCode;

    public ApiResponse(String message, Object data, String errorCode) {
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;

    }
}
