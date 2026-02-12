package com.github.computer114514.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data){
        return new Result<>(200,"success",data);
    }
    public static Result<Void> success(){
        return new Result<>(200,"success",null);
    }
    public static <T> Result<T> error(int code,String msg){
        return new Result<>(code,msg,null);
    }
    public static <T> Result<T> error(String msg){
        return new Result<>(500,msg,null);
    }
}
