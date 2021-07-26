package ru.mguschin.phonenumberchecker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

@Service
@Transactional
public class PhoneService {

    @Resource
    private PhoneDao phoneDao;

    @Resource
    private LogDao logDao;

    public String check(String phone, String requestId) {
        String result = "";

        try {
            Integer dbResult = phoneDao.phoneCheck(phone, requestId);

            switch (dbResult.intValue()) {
                case 0: result = "ACCEPT"; break;
                case 1: result = "CHALLENGE"; break;
                default: result = "DECLINE";
            }
        } finally {
            logDao.logRequest(phone, requestId, result);
        }

        return result;
    }
}
