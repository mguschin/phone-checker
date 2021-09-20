package ru.mguschin.phonenumberchecker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mguschin.phonenumberchecker.web.controller.PhoneController;

import javax.annotation.Resource;

import java.lang.InterruptedException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class PhoneService {

    private static final Logger logger = LoggerFactory.getLogger(PhoneService.class);

    private final PhoneDao phoneDao;
    private final ExecutorService executor;

    enum TaskResult {
        FOUND, NOT_FOUND;
    }

    class Task implements Callable<TaskResult>
    {
        private final ListTable listTable;
        private final String phone;
        private final String requestId;

        public Task(ListTable listTable, String phone, String requestId) {
            this.listTable = listTable;
            this.phone = phone;
            this.requestId = requestId;
        }

        @Override
        public TaskResult call() throws Exception {
            TaskResult r = null;

            try {
                Integer check = phoneDao.phoneCheck(listTable, phone, requestId);

                logger.info("Task [table={} phone={} requestId={}] returned {}.", listTable.getTableName(), phone, requestId, check);

                r = (check > 0) ? TaskResult.FOUND : TaskResult.NOT_FOUND;
            } catch (Exception e) {
                r = TaskResult.NOT_FOUND;

                logger.error("Task [table={} phone={} requestId={}] returned an error - {}", listTable.getTableName(), phone, requestId, e.getMessage());
            }

            return r;
        }
    }

    @Autowired
    PhoneService (PhoneDao phoneDao, ExecutorService executor) {
        this.phoneDao = phoneDao;
        this.executor = executor;
    }

    public CheckResult check(String phone, String requestId) {
        List<Task> taskList = new ArrayList<Task>();

        taskList.add(new Task(ListTable.LIST1, phone, requestId));
        taskList.add(new Task(ListTable.LIST2, phone.substring(1), requestId));

        List<Future<TaskResult>> resultList = null;

        try {
            resultList = executor.invokeAll(taskList, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Thread was interrupted - " + e.getMessage());
        }

        CheckResult result = CheckResult.DECLINE;

        try {
            if (resultList.get(0).get() == TaskResult.NOT_FOUND && resultList.get(1).get() == TaskResult.NOT_FOUND) {
                result = CheckResult.ACCEPT;
            } else if (resultList.get(0).get() == TaskResult.NOT_FOUND && resultList.get(1).get() == TaskResult.FOUND ||
                       resultList.get(0).get() == TaskResult.FOUND && resultList.get(1).get() == TaskResult.NOT_FOUND
                    ) {
                result = CheckResult.CHALLENGE;
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Thread was interrupted - " + e.getMessage());

            throw new RuntimeException("Check failed: " + e.getMessage());
        }

        return result;
    }
}
