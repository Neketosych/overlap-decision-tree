package com.overlap.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimilarClient {
    String rno;
    String cus;

    String firstName;
    String middleName;
    String lastName;

    String mobilePhone;

    String birthDate;

    List<CustomerIdentityCard> cards;


    String result;
}