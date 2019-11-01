package com.duojia.messager.entity;

import java.util.ArrayList;
import java.util.List;

public class LoadResult {

    private int loadTotalNum;
    private int loadSuccessNum;
    private int loadFailureNum;
    private int loadMultiNum;
    private List<Phone> errorList = new ArrayList<>();

    public int getLoadTotalNum() {
        return loadTotalNum;
    }

    public void setLoadTotalNum(int loadTotalNum) {
        this.loadTotalNum = loadTotalNum;
    }

    public int getLoadSuccessNum() {
        return loadSuccessNum;
    }

    public void setLoadSuccessNum(int loadSuccessNum) {
        this.loadSuccessNum = loadSuccessNum;
    }

    public int getLoadFailureNum() {
        return loadFailureNum;
    }

    public void setLoadFailureNum(int loadFailureNum) {
        this.loadFailureNum = loadFailureNum;
    }

    public List<Phone> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<Phone> errorList) {
        this.errorList = errorList;
    }

    public int getLoadMultiNum() {
        return loadMultiNum;
    }

    public void setLoadMultiNum(int loadMultiNum) {
        this.loadMultiNum = loadMultiNum;
    }

    public void addErrorPhone(Phone phone) {
        if (errorList == null) {
            errorList = new ArrayList<>();
        }
        errorList.add(phone);
    }
}
