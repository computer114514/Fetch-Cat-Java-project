package com.github.computer114514.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyResult<T> {
    private int code;
    private String msg;
    private T data;

    public static MyResult<Void> success(){
        return new MyResult<>(200,"success",null);
    }
    public static <T> MyResult<T> success(T data){
        return new MyResult<>(200,"success",data);
    }
    public static <T> MyResult<T> error(int code,String msg){
        return new MyResult<>(code,msg,null);
    }
    public static <T> MyResult<T> error(String msg){
        return new MyResult<>(500,msg,null);
    }
}
