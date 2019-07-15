package com.overlap.prepare.parser.distance;

import com.overlap.entity.Overlap;
import com.overlap.entity.SimilarClient;
import com.overlap.entity.TechnicalFeature;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class MobilePhoneDistance implements DistanceExtractor {

    LevenshteinDistance lDistance = new LevenshteinDistance();

    @Override
    public void extract(Overlap o, SimilarClient c, TechnicalFeature f) {
        String phone = o.getMobilePhone().replaceAll("[^\\d]+","");
        String cphone = c.getMobilePhone().replaceAll("[^\\d]+","");

        if (StringUtils.isBlank(phone) || StringUtils.isBlank(cphone)) {
            f.setPhoneDistance(-1.0);
        } else {
            f.setPhoneDistance(Double.valueOf(lDistance.apply(phone, cphone)));
        }
    }
}
