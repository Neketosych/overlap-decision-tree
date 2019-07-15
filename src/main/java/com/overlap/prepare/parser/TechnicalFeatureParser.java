package com.overlap.prepare.parser;

import com.overlap.entity.Overlap;
import com.overlap.entity.SimilarClient;
import com.overlap.entity.TechnicalFeature;
import com.overlap.prepare.parser.distance.BrDateDistance;
import com.overlap.prepare.parser.distance.DulDistance;
import com.overlap.prepare.parser.distance.FIODistance;
import com.overlap.prepare.parser.distance.MobilePhoneDistance;

public class TechnicalFeatureParser {

    FIODistance fioDistance = new FIODistance();
    BrDateDistance brDateDistance = new BrDateDistance();
    DulDistance dulDistance = new DulDistance();
    MobilePhoneDistance phoneDistance = new MobilePhoneDistance();

    public TechnicalFeature parse(Overlap o, SimilarClient c) {
        TechnicalFeature f = new TechnicalFeature();
        f.setDfid(o.getId());
        f.setRno(c.getRno());
        f.setCus(c.getCus());


        fioDistance.extract(o, c, f);
        brDateDistance.extract(o, c, f);
        dulDistance.extract(o, c, f);
        phoneDistance.extract(o, c, f);

        if (c.getResult().equalsIgnoreCase("ACCEPT")) {
            f.setResult(1.0);
        } else {
            f.setResult(0);
        }
        return f;
    }
}