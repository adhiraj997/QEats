/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.models;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Java class that maps to Mongo collection.
@Data
@Document(collection = "restaurants")
@NoArgsConstructor
public class RestaurantEntity {

  @Id
  private String id;

  @NotNull
  private String restaurantId;

  @NotNull
  private String name;

  @NotNull
  private String city;

  @NotNull
  private String imageUrl;

  @NotNull
  private Double latitude;

  @NotNull
  private Double longitude;

  @NotNull
  private String opensAt;

  @NotNull
  private String closesAt;

  @NotNull
  private List<String> attributes = new ArrayList<>();

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((restaurantId == null) ? 0 : restaurantId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof RestaurantEntity)) {
      return false;
    }
    RestaurantEntity other = (RestaurantEntity) obj;
    
    if (restaurantId == null) {
      if (other.restaurantId != null) {
        return false;
      }
    } else if (!restaurantId.equals(other.restaurantId)) {
      return false;
    }
    
    return true;
  }

}

