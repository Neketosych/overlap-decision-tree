package com.overlap.prepare;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overlap.entity.Overlap;
import com.overlap.entity.SimilarClient;
import com.overlap.entity.TechnicalFeature;
import com.overlap.prepare.parser.TechnicalFeatureParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Prepare {

    public static void main(String[] args) throws IOException {
        String content = FileUtils.readFileToString(
                new File("src/main/resources/overlap/ufr_zpps.overlap 05-10 07 2019.json"), StandardCharsets.UTF_8);

        content = content.replaceAll("NumberInt\\(", "")
                .replaceAll("\\),", ",")
                .replaceAll("ISODate\\(", "")
                .replaceAll("\\)", "")
                .replaceAll("}[\\n\\s]+\\{", "},{")
                .replaceAll("T21:00:00\\.000\\+0000", "");

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        Overlap[] overlaps = mapper.readValue(content, Overlap[].class);

        System.out.println(Arrays.toString(overlaps));

        System.out.println();
        TechnicalFeatureParser parser = new TechnicalFeatureParser();
        List<TechnicalFeature> features = new ArrayList<>();
        for (Overlap o: overlaps) {
            if (o.getClients().isEmpty()) {
                continue;
            }
            for(SimilarClient c: o.getClients()) {
                try {
                    TechnicalFeature f = parser.parse(o, c);
                    System.out.println(o);
                    System.out.println(c);
                    System.out.println(f);
                    System.out.println("---------\n");
                    features.add(f);
                } catch (Exception e) {
                    log.error("", e);
                    break;
                }
            }
        }
        mapper.writeValue(new File("src/main/resources/overlap/train.json"), features);
    }
}
