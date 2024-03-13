package org.example.service;

import org.apache.commons.lang3.RandomUtils;
import org.example.Demographic;
import org.example.repo.DBService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

public class DemographicService {
    DBService    dbService = DBService.getDBservice();
    public  Void fetchDemographic(List<String> list) throws InterruptedException {
        List<StructuredTaskScope.Subtask<Demographic>> resultList = new ArrayList<>();
        try(StructuredTaskScope<Demographic> scope = new StructuredTaskScope<>()){
            for(String emirateId : list){
                resultList.add(scope.fork(() -> getDemographic(emirateId)));
            }
            scope.join();
            for(StructuredTaskScope.Subtask<Demographic> task : resultList){
                if(task.state() == StructuredTaskScope.Subtask.State.SUCCESS){
                    dbService.demographicSuccessList.add(new DBService.Success(task.get().emiratesId));
                }
                if(task.state() == StructuredTaskScope.Subtask.State.FAILED){
                    dbService.demographicErrorList.add(new DBService.Failure(task.exception().getMessage()));
                }
            }
        }
        return null;
    }


    public record Response(String emirateId){}

    public  Demographic getDemographic(String emirate) {
        // here is the business logic
        int number = RandomUtils.nextInt(1000,5000);
        if(number >= 2000 && number <= 3000){
           throw new IllegalStateException(emirate);
        }
        sleep(number);
        return new Demographic(emirate);
    }

    public  void sleep(int number)  {
        try {
            Thread.sleep(number);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
