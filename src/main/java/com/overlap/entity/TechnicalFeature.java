package com.overlap.entity;

import lombok.Data;

@Data
public class TechnicalFeature {

    String dfid;
    String rno;
    String cus;

    double firstNameDistance;
    double middleNameDistance;
    double lastNameDistance;

    Double phoneDistance;

    double brDateMistakes;
    double dulEquals;



    double result;
}