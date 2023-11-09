package com.knj.mirou.base.rsData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@AllArgsConstructor
public class RsData<T> {

    private String resultCode;
    private String msg;
    private T data;

    public static <T> RsData<T> of(String resultCode, String msg, T data) {
        return new RsData<>(resultCode, msg, data);
    }

    public static <T> RsData<T> of(String resultCode, String msg) {
        return of(resultCode, msg, null);
    }

    public static <T> RsData<T> successOf(T data) {
        return of("S-1", "성공", data);
    }

    public static <T> RsData<T> failOf(T data) {
        return of("F-1", "실패", data);
    }

    public boolean isSuccess() {
        return resultCode.startsWith("S-");
    }

    public void printResult() {
        if(isSuccess()) {
            log.info(resultCode + ": " + msg);
        } else {
            log.error(resultCode + ": " + msg);
        }
    }

    public boolean isFail() {
        return !isSuccess();
    }
}