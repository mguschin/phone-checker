package ru.mguschin.phonenumberchecker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {

    @Resource
    private final LogDao logDao;

    LogService (LogDao logDao) {
        this.logDao = logDao;
    }

    @Transactional
    public void logRequest (String phone, String requestId, CheckResult result) {
        logDao.logRequest(phone, requestId, result);
    }
}
