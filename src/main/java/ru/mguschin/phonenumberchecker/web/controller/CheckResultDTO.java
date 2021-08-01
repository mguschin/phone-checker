package ru.mguschin.phonenumberchecker.web.controller;

import ru.mguschin.phonenumberchecker.service.CheckResult;

public class CheckResultDTO {
    private CheckResult result;

    public CheckResult getResult() {
        return result;
    }

    public void setResult(CheckResult result) {
        this.result = result;
    }
}
