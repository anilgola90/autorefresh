package org.example.service;

import org.apache.commons.lang3.RandomUtils;
import org.example.Demographic;
import org.example.Pointing;
import org.example.repo.DBService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

public class PointingService {

    DBService    dbService = DBService.getDBservice();
    public  Void pointing(List<String> pointingList) throws InterruptedException {
        List<StructuredTaskScope.Subtask<Pointing>> resultList = new ArrayList<>();
        try(StructuredTaskScope scope = new StructuredTaskScope<Void>()){
            for(String emirateId : pointingList){
                resultList.add(scope.fork(() -> calculatePointing(emirateId)));
            }
            scope.join();
            for(StructuredTaskScope.Subtask<Pointing> task : resultList){
                if(task.state() == StructuredTaskScope.Subtask.State.SUCCESS){
                    dbService.pointingSuccessList.add(new DBService.Success(task.get().emiratesId));
                }
                if(task.state() == StructuredTaskScope.Subtask.State.FAILED){
                    dbService.pointingErrorList.add(new DBService.Failure(task.exception().getMessage()));
                }
            }
        }
        return null;
    }

    public  Pointing calculatePointing(String emirateId)  {
        // here is the business logic
        int number = RandomUtils.nextInt(1000,5000);
        if(number >= 2000 && number <= 3000){
            throw new IllegalStateException(emirateId);
        }
        return new Pointing(emirateId);
    }

    public  void sleep(int number)  {
        try {
            Thread.sleep(number);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
