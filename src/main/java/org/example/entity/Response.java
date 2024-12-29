package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class Response {
    private int code; //200 为成功 失败1
    private String msg;
    private Object data;

    // 无参构造器，设置默认值
    public Response() {
        this.code = 200; // 默认成功
        this.msg = "请求成功";
        this.data = null;
    }

    public Response(Object data) {
        this.code = 200; // 默认成功
        this.msg = "请求成功";
        this.data = data;
    }

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }


}
