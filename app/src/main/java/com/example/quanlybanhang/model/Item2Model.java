package com.example.quanlybanhang.model;

import java.util.List;

public class Item2Model {
    boolean success;
    String message;
    List<Item2> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Item2> getResult() {
        return result;
    }

    public void setResult(List<Item2> result) {
        this.result = result;
    }
}
