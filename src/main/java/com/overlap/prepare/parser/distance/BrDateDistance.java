package com.overlap.prepare.parser.distance;

import com.overlap.entity.Overlap;
import com.overlap.entity.SimilarClient;
import com.overlap.entity.TechnicalFeature;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class BrDateDistance implements DistanceExtractor {

    LevenshteinDistance l = new LevenshteinDistance();

    @Override
    public void extract(Overlap o, SimilarClient c, TechnicalFeature f) {
        String brDate = o.getBrDate().replaceAll("[^\\d]","");
        String cBrDate = c.getBirthDate().replaceAll("[^\\d]","");
        f.setBrDateMistakes(l.apply(brDate, cBrDate));
    }
}