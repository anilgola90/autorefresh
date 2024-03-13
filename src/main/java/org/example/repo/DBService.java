package org.example.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public  class DBService {

    static DBService dbService;

    private DBService(){
    }

    public static DBService getDBservice(){
        if(dbService == null){
            synchronized (DBService.class){
                if(dbService == null) {
                    return new DBService();
                }
                else{
                    return  dbService;
                }
            }
        }
        else {
            return dbService;
        }


    }

    public record Success(String emiratesId){}
    public record Failure(String emiratesId){}

    public static List<Success> demographicSuccessList = new ArrayList<>();
    public static List<Failure> demographicErrorList = new ArrayList<>();

    public static List<Success> familySuccessList = new ArrayList<>();
    public static List<Failure> familyErrorList = new ArrayList<>();

    public static List<Success> pointingSuccessList = new ArrayList<>();
    public static List<Failure> pointingErrorList = new ArrayList<>();



    public List<String> fetchScreens(){
        return List.of("demographic","family");
    }


    public List<String> fetchEmiratesForRefresh(){
        List<String> list = new ArrayList<>();
        for(int i=0;i<1000;i++){
            list.add(String.valueOf(i));
        }
        return list;
    }

    public static List<String> fetchSuccessfullList(){
        List<String> list = demographicSuccessList.stream()
                .filter(familySuccessList::contains)
                .map(str -> str.emiratesId)
                .toList();
        return list;
    }




}
