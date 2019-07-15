package com.overlap.prepare.parser.distance;

import com.overlap.entity.Overlap;
import com.overlap.entity.SimilarClient;
import com.overlap.entity.TechnicalFeature;

public interface DistanceExtractor {

    void extract(Overlap o, SimilarClient c, TechnicalFeature f);
}