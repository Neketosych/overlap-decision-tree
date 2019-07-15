package com.overlap.prepare.parser;

import com.overlap.entity.Overlap;
import com.overlap.entity.SimilarClient;
import com.overlap.entity.TechnicalFeature;
import com.overlap.prepare.parser.distance.FIODistance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FIODistanceTest {

    FIODistance distance = new FIODistance();

    Overlap o;
    SimilarClient c;

    @Before
    public void setup() {
         o = new Overlap().setFirstName("").setLastName("");
         c = new SimilarClient().setFirstName("").setLastName("");
    }


    @Test
    public void lastNameEquals() {
        TechnicalFeature f = new TechnicalFeature();
        distance.extract(
                o.setLastName("иван"),
                c.setLastName("ИВАН"),
                f);

        assertEquals(1.0, f.getLastNameDistance(), 0.01);
    }

    @Test
    public void firstNameEquals() {
        TechnicalFeature f = new TechnicalFeature();
        distance.extract(
                o.setFirstName("иван"),
                c.setFirstName("ИВАН"),
                f);

        assertEquals(1.0, f.getFirstNameDistance(), 0.01);
    }

    @Test
    public void middleNameEquals() {
        TechnicalFeature f = new TechnicalFeature();
        distance.extract(
                o.setMiddleName("иван"),
                c.setMiddleName("ИВАН"),
                f);

        assertEquals(1.0, f.getMiddleNameDistance(), 0.01);
    }

    @Test
    public void middleNamePassedToFirstName() {
        TechnicalFeature f = new TechnicalFeature();
        distance.extract(
                o.setFirstName("иван иванович"),
                c.setMiddleName("ИВАНОВИЧ"),
                f);

        assertEquals(1.0, f.getMiddleNameDistance(), 0.01);
    }

    @Test
    public void valueScaledTo2() {
        TechnicalFeature f = new TechnicalFeature();
        distance.extract(
                o.setMiddleName("иван1"),
                c.setMiddleName("ИВАН"),
                f);

        assertEquals(0.96, f.getMiddleNameDistance(), 0.01);
    }
}