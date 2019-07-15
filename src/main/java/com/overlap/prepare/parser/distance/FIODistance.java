package com.overlap.prepare.parser.distance;

import com.google.common.base.Splitter;
import com.overlap.entity.Overlap;
import com.overlap.entity.SimilarClient;
import com.overlap.entity.TechnicalFeature;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FIODistance implements DistanceExtractor {

    JaroWinklerDistance distance = new JaroWinklerDistance();

    @Override
    public void extract(Overlap o, SimilarClient c, TechnicalFeature f) {
        f.setLastNameDistance(scaled(distance.apply(fixName(o.getLastName()), fixName(c.getLastName()))));

        String firstName = fixName(o.getFirstName());
        String cFirstName = fixName(c.getFirstName());

        String middleName = fixName(o.getMiddleName());
        String cmiddleName = fixName(c.getMiddleName());

        Set<String> firstNameVariants = new HashSet<>();
        firstNameVariants.add(firstName);
        Set<String> middleNameVariants = new HashSet<>();
        if (StringUtils.isBlank(middleName)) {
            List<String> splitted = Splitter.on(" ").omitEmptyStrings().trimResults().splitToList(firstName);
            if (splitted.size() == 2) {
                firstNameVariants.addAll(splitted);
                middleNameVariants.addAll(splitted);
            }
        } else {
            middleNameVariants.add(middleName);
        }


        Set<String> cFirstNameVariants = new HashSet<>();
        cFirstNameVariants.add(cFirstName);
        Set<String> cMiddleNameVariants = new HashSet<>();
        if (StringUtils.isBlank(cmiddleName)) {
            List<String> splitted = Splitter.on(" ").omitEmptyStrings().trimResults().splitToList(cFirstName);
            if (splitted.size() == 2) {
                cFirstNameVariants.addAll(splitted);
                cMiddleNameVariants.addAll(splitted);
            }
        } else {
            cMiddleNameVariants.add(cmiddleName);
        }



        f.setFirstNameDistance(maxSimilar(firstNameVariants, cFirstNameVariants));
        f.setMiddleNameDistance(maxSimilar(middleNameVariants, cMiddleNameVariants));
    }

    private String fixName(String source) {
        return StringUtils.lowerCase(StringUtils.strip(source));
    }

    private Double maxSimilar(Set<String> s1, Set<String> s2) {
        Double max = 0.0;
        for (String s11: s1) {
            for (String s22: s2) {
                double dis = distance.apply(s11, s22);
                if (dis > max) {
                    max = dis;
                }
            }
        }
        return scaled(max);
    }

    private double scaled(double source) {
        return BigDecimal.valueOf(source).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}