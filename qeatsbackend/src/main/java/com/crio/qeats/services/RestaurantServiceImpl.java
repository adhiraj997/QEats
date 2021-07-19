
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import com.crio.qeats.repositoryservices.RestaurantRepositoryServiceDummyImpl;
import com.crio.qeats.repositoryservices.RestaurantRepositoryServiceImpl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;
  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;


  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(
      GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {

    Double latitude = getRestaurantsRequest.getLatitude();
    Double longitude = getRestaurantsRequest.getLongitude();
    
    Double servingRadius = getServingRadius(currentTime);
    // LocalTime peakStart1 = LocalTime.parse("07:59:59");
    // LocalTime peakEnd1 = LocalTime.parse("10:00:01");
    
    // LocalTime peakStart2 = LocalTime.parse("12:59:59");
    // LocalTime peakEnd2 = LocalTime.parse("14:00:01");

    // LocalTime peakStart3 = LocalTime.parse("18:59:59");
    // LocalTime peakEnd3 = LocalTime.parse("21:00:01");

    // if ((currentTime.isAfter(peakStart1) && currentTime.isBefore(peakEnd1)) 
    //     ||
    //     (currentTime.isAfter(peakStart2) && currentTime.isBefore(peakEnd2)) 
    //     ||
    //     (currentTime.isAfter(peakStart3) && currentTime.isBefore(peakEnd3))
    // ) {
    //   radius = peakHoursServingRadiusInKms;
    // } else {
    //   radius = normalHoursServingRadiusInKms;
    // }

    List<Restaurant> restaurantList = restaurantRepositoryService
        .findAllRestaurantsCloseBy(latitude, longitude, currentTime, servingRadius);
    

    GetRestaurantsResponse getRestaurantsResponse = 
        new GetRestaurantsResponse(restaurantList);
    
    return getRestaurantsResponse;
  }





  // TODO: CRIO_TASK_MODULE_RESTAURANTSEARCH
  // Implement findRestaurantsBySearchQuery. The request object has the search string.
  // We have to combine results from multiple sources:
  // 1. Restaurants by name (exact and inexact)
  // 2. Restaurants by cuisines (also called attributes)
  // 3. Restaurants by food items it serves
  // 4. Restaurants by food item attributes (spicy, sweet, etc)
  // Remember, a restaurant must be present only once in the resulting list.
  // Check RestaurantService.java file for the interface contract.
  @Override
  public GetRestaurantsResponse findRestaurantsBySearchQuery(
      GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {

    String searchString = getRestaurantsRequest.getSearchFor();
    Double latitude = getRestaurantsRequest.getLatitude();
    Double longitude = getRestaurantsRequest.getLongitude();
    Double servingRadius = getServingRadius(currentTime);

    GetRestaurantsResponse getRestaurantsResponse = new GetRestaurantsResponse(new ArrayList<>());

    if (!searchString.isEmpty()) {
      List<Restaurant> restaurantListByName = restaurantRepositoryService.findRestaurantsByName(
          latitude, longitude, searchString, currentTime, servingRadius);

      List<Restaurant> restaurantListByAttribute = restaurantRepositoryService
          .findRestaurantsByAttributes(
          latitude, longitude, searchString, currentTime, servingRadius);

      List<Restaurant> restaurantList = Stream.concat(
          restaurantListByName.stream(), restaurantListByAttribute.stream())
          .collect(Collectors.toList());

      Set<Restaurant> set = new LinkedHashSet<>(restaurantList);

      //create a list again from above set 
      List<Restaurant> restaurantListUnique = new ArrayList<>(set);

      getRestaurantsResponse = 
          new GetRestaurantsResponse(restaurantListUnique); 
    }
    

    return getRestaurantsResponse;
  }

  //utility function 
  Double getServingRadius(LocalTime currentTime) {
    Double radius;
    LocalTime peakStart1 = LocalTime.parse("07:59:59");
    LocalTime peakEnd1 = LocalTime.parse("10:00:01");
    
    LocalTime peakStart2 = LocalTime.parse("12:59:59");
    LocalTime peakEnd2 = LocalTime.parse("14:00:01");

    LocalTime peakStart3 = LocalTime.parse("18:59:59");
    LocalTime peakEnd3 = LocalTime.parse("21:00:01");

    if ((currentTime.isAfter(peakStart1) && currentTime.isBefore(peakEnd1)) 
        ||
        (currentTime.isAfter(peakStart2) && currentTime.isBefore(peakEnd2)) 
        ||
        (currentTime.isAfter(peakStart3) && currentTime.isBefore(peakEnd3))
    ) {
      radius = peakHoursServingRadiusInKms;
    } else {
      radius = normalHoursServingRadiusInKms;
    }
    return radius;
  }

}

