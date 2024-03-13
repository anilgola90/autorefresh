package org.example.service;

import org.apache.commons.lang3.RandomUtils;
import org.example.Family;
import org.example.repo.DBService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

public class FamilyService {

    DBService    dbService = DBService.getDBservice();

    public  Void fetchFamily(List<String> list) throws InterruptedException {
        List<StructuredTaskScope.Subtask<Family>> resultList = new ArrayList<>();
        try(StructuredTaskScope scope = new StructuredTaskScope<Family>()){
            for(String emirateId : list){
                resultList.add(scope.fork(() -> getFamily(emirateId)));
            }
            scope.join();
            for(StructuredTaskScope.Subtask<Family> task : resultList){
                if(task.state() == StructuredTaskScope.Subtask.State.SUCCESS){
                    // save this emirates id in success table
                    dbService.familySuccessList.add(new DBService.Success(task.get().emiratesId));
                }
                else if (task.state() == StructuredTaskScope.Subtask.State.FAILED){
                    // save this in failed emirates id list
                    dbService.familyErrorList.add(new DBService.Failure(task.exception().getMessage()));
                }
            }
        }
        return null;
    }

    public  Family getFamily(String emirate)  {
        // here is the business logic
        int number = RandomUtils.nextInt(1000,5000);
        if(number >= 2000 && number <= 3000){
            throw new IllegalStateException(emirate);
        }
        sleep(number);
        return new Family(emirate);
    }

    public  void sleep(int number)  {
        try {
            Thread.sleep(number);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
