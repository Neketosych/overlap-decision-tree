package com.overlap.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Overlap {

    @JsonProperty("_id")
    String id;

    String firstName;
    String middleName;
    String lastName;

    String mobilePhone;

    Integer dulType;
    String dulNumber;

    String oldDulNumber;

    String brDate;

    List<SimilarClient> clients;
}