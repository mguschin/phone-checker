package ru.mguschin.phonenumberchecker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

import java.lang.InterruptedException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class PhoneService {

    @Resource
    private PhoneDao phoneDao;

    @Resource
    private LogDao logDao;

    class Task implements Callable<Integer>
    {
        private final String source;
        private final String phone;
        private final String requestId;

        public Task(String source, String phone, String requestId) {
            this.source = source;
            this.phone = phone;
            this.requestId = requestId;
        }

        @Override
        public Integer call() throws Exception
        {
            try {
                return phoneDao.phoneCheck(source, phone, requestId);
            } catch (Exception e) {
                return new Integer(-1);
            }
        }
    }

    public String check(String phone, String requestId) {

        String result = "";

        ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);

        List<Task> taskList = new ArrayList<Task>();

        taskList.add(new Task("TABLE1", phone, requestId));
        taskList.add(new Task("TABLE2", phone.substring(1), requestId));

        List<Future<Integer>> resultList = null;

        try {
            resultList = executor.invokeAll(taskList, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        Integer resultA;
        Integer resultB;

        try {
            if (resultList != null) {
                resultA = resultList.get(0).get();
                resultB = resultList.get(1).get();
            } else {
                resultA = -1;
                resultB = -1;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();

            resultA = -1;
            resultB = -1;
        }

        if (resultA.intValue() >= 0 && resultB.intValue() >= 0) {
            switch (resultA.intValue() + resultB.intValue()) {
                case 0:
                    result = "ACCEPT";
                    break;
                case 1:
                    result = "CHALLENGE";
                    break;
                default:
                    result = "DECLINE";
            }
        } else {
            throw new RuntimeException("DB Query failed.");
        }

        return result;
    }

    @Transactional
    public void logRequest (String phone, String requestId, String result) {
        logDao.logRequest(phone, requestId, result);
    }
}
