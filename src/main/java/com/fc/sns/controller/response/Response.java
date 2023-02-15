package com.fc.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {
    private String resultCode;
    private T result;

    //RESULT 객체 타입을 특정하지 않고 제네릭 타입으로 선언해서 response를 러프하게 리턴한다.
    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", result);
    }

    public static <T> Response<Void> error(String errorCode) {
        return new Response<>(errorCode, null);
    }

    public static Response<Void> success() {
        return new Response<Void>("SUCCESS", null);
    }

    public String toStream() {
        if (result == null) {
            return "{" +
                    "\"resultCode\":" + "\"" + resultCode + "\"," +
                    "\"result\":" + null + "}";
        }
        return "{" +
                "\"resultCode\":" + "\"" + resultCode + "\"," +
                "\"result\":" + result + "}";
    }
}
