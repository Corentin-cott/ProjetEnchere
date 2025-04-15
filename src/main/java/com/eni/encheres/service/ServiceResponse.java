package com.eni.encheres.service;

public class ServiceResponse<T> {
    public String code;
    public String message;
    public T data;

    static <T> ServiceResponse<T> buildResponse(String code, String message, T data) {
        ServiceResponse<T> response = new ServiceResponse();
        response.code = code;
        response.message = message;
        response.data = data;

        // Log
        System.out.println(String.format("Code : %s | Message : %s", code, message));

        return response;
    }
}
