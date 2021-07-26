package ru.mguschin.phonenumberchecker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

@Service
@Transactional
public class PhoneService {

    @Resource
    private PhoneDao phoneDao;

    public String check(String phone, String requestId) {

        return phoneDao.phoneCheck(phone, requestId);
    }
}
