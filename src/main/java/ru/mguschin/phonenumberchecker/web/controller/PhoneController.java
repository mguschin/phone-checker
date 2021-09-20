package ru.mguschin.phonenumberchecker.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MissingRequestValueException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
import javax.validation.ValidationException;

import ru.mguschin.phonenumberchecker.service.CheckResult;
import ru.mguschin.phonenumberchecker.service.PhoneService;
import ru.mguschin.phonenumberchecker.service.LogService;

@RestController
@RequestMapping("/api/v1/phone/")
@Validated
public class PhoneController {
    private static final Logger logger = LoggerFactory.getLogger(PhoneController.class);

    private final PhoneService phoneService;
    private final LogService logService;

    @Autowired
    PhoneController (PhoneService phoneService, LogService logService) {
        this.phoneService = phoneService;
        this.logService = logService;
    }

    @RequestMapping(value = "check", method = RequestMethod.GET)
    public CheckResultDTO check(
            @RequestParam(name = "phone", required = true) @Pattern(regexp = "^7\\d{10}$", message = "phone must be in format 7XXXXXXXXXX") String phone,
            @RequestParam(name = "requestid", required = true) @Pattern(regexp = "^[a-zA-Z0-9]{1,20}$", message = "requestId must contain up to 20 chars") String requestId) {

        CheckResult result = CheckResult.DECLINE;

        try {
            result = phoneService.check(phone, requestId);
        } finally{
            logger.info("Request [id={} phone={}] Response [result={}]", requestId, phone, result);

            try {
                logService.logRequest(phone, requestId, result);
            } catch (Exception e) {
                logger.error("Unable to log request [id={} phone={}] Response [result={}] - {}", requestId, phone, result, e.getMessage());
            }
        }

        CheckResultDTO dto = new CheckResultDTO();

        dto.setResult(result);

        return dto;
    }

    @ExceptionHandler({Exception.class, ValidationException.class, MissingRequestValueException.class})
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request,Exception e) {

        ErrorResponse response = new ErrorResponse();

        if (e instanceof ValidationException || e instanceof MissingRequestValueException) {

            logger.error("Invalid parameters [{}] - {}", request.getQueryString(), e.getMessage());

            response.setErrorCode(1);
            response.setErrorMessage("Invalid parameters: " + e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        logger.error("Internal error [" + request.getQueryString() + "].", e);

        response.setErrorCode(500);
        response.setErrorMessage("Internal error.");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
