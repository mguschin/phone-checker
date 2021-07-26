package ru.mguschin.phonenumberchecker.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MissingRequestValueException;

import javax.annotation.Resource;
import javax.validation.constraints.Pattern;
import javax.validation.ValidationException;

import ru.mguschin.phonenumberchecker.service.PhoneService;

@RestController
@RequestMapping("/api/v1/phone/")
@Validated
public class PhoneController {

    @Resource
    private PhoneService phoneService;

    @RequestMapping(value = "check", method = RequestMethod.GET)
    public String check(
            @RequestParam(name = "phone", required = true) @Pattern(regexp = "^7\\d{10}$", message = "phone must be in format 7XXXXXXXXXX") String phone,
            @RequestParam(name = "requestid", required = true) @Pattern(regexp = "^[a-zA-Z0-9]{1,20}$", message = "requestId must contain up to 20 chars") String requestId) {

        return phoneService.check(phone, requestId);
    }

    @ExceptionHandler({Exception.class, ValidationException.class, MissingRequestValueException.class})
    public ResponseEntity<String> handleException(Exception e) {

        if (e instanceof ValidationException || e instanceof MissingRequestValueException)
            return new ResponseEntity<>("Invalid parameters: " + e.getMessage(), HttpStatus.BAD_REQUEST);

        System.out.println(e.getMessage());

        return new ResponseEntity<>("Internal error.", HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
