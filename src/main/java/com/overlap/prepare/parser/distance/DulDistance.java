package com.overlap.prepare.parser.distance;

import com.overlap.entity.CustomerIdentityCard;
import com.overlap.entity.Overlap;
import com.overlap.entity.SimilarClient;
import com.overlap.entity.TechnicalFeature;
import org.apache.commons.lang.StringUtils;

import java.util.Optional;

public class DulDistance implements DistanceExtractor {

    @Override
    public void extract(Overlap o, SimilarClient c, TechnicalFeature f) {
        Integer type = o.getDulType();
        String number = fixNumber(o.getDulNumber());
        String oldNumber = fixNumber(o.getDulNumber());

        Optional<CustomerIdentityCard> card = c.getCards().stream()
                .filter(i -> i.getUcd().equals(type))
                .peek(i -> i.setNumber(fixNumber(i.getNumber())))
                .filter(i -> i.getNumber().equals(number) || i.getNumber().equals(oldNumber))
                .findFirst();

        if (card.isPresent()) {
            f.setDulEquals(1.0);
        } else {
            f.setDulEquals(0.0);
        }

    }

    private String fixNumber(String source) {
        return StringUtils.lowerCase(StringUtils.deleteWhitespace(source));
    }
}