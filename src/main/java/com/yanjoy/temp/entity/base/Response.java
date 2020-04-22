package com.yanjoy.temp.entity.base;

import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpStatus;

/**
 * @author PC
 */
public class Response<T> {

    /**
     * http 状态
     */
    public HttpStatus status;

    /**
     * 业务状态
     */
    public String code;
    /**
     * 业务数据
     */
    public T data;
    /**
     * 业务消息
     */
    private String msg;

    /**
     * 内存分页
     */
    private PageVo pageVo;


    public static <T> Response SUCCESS(T data) {
        Response<T> response = new Response<>();
        response.code = "0";
        response.data = data;
        response.status = HttpStatus.OK;
        return response;
    }

    public static <T> Response SUCCESS_MSG(T data,String msg) {
        Response<T> response = new Response<>();
        response.code = "0";
        response.data = data;
        response.status = HttpStatus.OK;
        response.setMsg(msg);
        return response;
    }

    public static <T> Response ERROR(T data, String msg) {
        Response<T> response = new Response<>();
        response.code = "1";
        response.data = data;
        response.status = HttpStatus.INTERNAL_SERVER_ERROR;
        response.msg = msg;
        return response;
    }

    public static <T> Response SUCCESS_PAGEVO(T data,PageVo pageVo) {
        Response<T> response = new Response<>();
        response.code = "0";
        response.data = data;
        response.pageVo = pageVo;
        response.status = HttpStatus.OK;
        return response;
    }

    public static <T> Response ERROR_PAGEVO(T data, PageVo pageVo,String msg) {
        Response<T> response = new Response<>();
        response.code = "1";
        response.data = data;
        response.pageVo = pageVo;
        response.status = HttpStatus.INTERNAL_SERVER_ERROR;
        response.setMsg(msg);
        return response;
    }

    public Response() {
        this.status = HttpStatus.OK;
    }

    public int getStatus() {
        return status.value();
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PageVo getPageVo() {
        return pageVo;
    }

    public void setPageVo(PageVo pageVo) {
        this.pageVo = pageVo;
    }

}
