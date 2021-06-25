package com.wit.farmo.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AmazonObj {
           @SerializedName(value = "ASIN")
           String asin;
           String title;
           String price;
           String listPrice;
           String imageUrl;
           String detailPageURL;
           String rating;
           String totalReviews;
           String subtitle;
           String isPrimeEligible;
}
