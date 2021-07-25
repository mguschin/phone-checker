package ru.mguschin.phonenumberchecker.service;

public interface PhoneDao {

    String phoneCheck(String phone, String requestId);
}
