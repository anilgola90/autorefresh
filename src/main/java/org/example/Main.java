package org.example;

import org.example.repo.DBService;
import org.example.service.DemographicService;
import org.example.service.FamilyService;
import org.example.service.PointingService;

import java.util.*;
import java.util.concurrent.StructuredTaskScope;


public class Main {

    static DemographicService demographicService;
    static FamilyService familyService;
    static PointingService pointingService;
    static DBService dbService;


    public static void main(String[] args) throws InterruptedException {
         demographicService = new DemographicService();
         familyService = new FamilyService();
         pointingService = new PointingService();
         dbService = DBService.getDBservice();
         var screenList =  dbService.fetchScreens();
         var emiratesList = dbService.fetchEmiratesForRefresh();
         refresh(screenList,emiratesList);
         pointingService.pointing(dbService.fetchSuccessfullList());

         System.out.println("Processing finished:: Stats below::");
         System.out.println("Demographic SuccessList" + DBService.demographicSuccessList.size());
         System.out.println("Demographic ErrorList" + DBService.demographicErrorList.size());

         System.out.println("Family SuccessList" + DBService.familySuccessList.size());
         System.out.println("Family ErrorList" + DBService.familyErrorList.size());

         System.out.println("Pointing insertion list" + DBService.fetchSuccessfullList().size());
         System.out.println("Pointing SuccessList" + DBService.pointingSuccessList.size());
         System.out.println("Pointing ErrorList" + DBService.pointingErrorList.size());

    }

    public static Void refresh(List<String> screens, List<String> emiratesList) throws InterruptedException {
        try(StructuredTaskScope scope = new StructuredTaskScope<Void>()){
            if(screens.contains("demographic")){
                scope.fork(() -> demographicService.fetchDemographic(emiratesList));
            }
            if(screens.contains("family")){
                scope.fork(() -> familyService.fetchFamily(emiratesList));
            }
            scope.join();
        }
        return null;
    }
}