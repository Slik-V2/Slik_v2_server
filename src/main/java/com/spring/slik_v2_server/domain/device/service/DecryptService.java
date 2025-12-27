package com.spring.slik_v2_server.domain.device.service;

import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import com.spring.slik_v2_server.domain.fingerprint.repository.FingerPrintRepository;
import info.debatty.java.stringsimilarity.NGram;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class DecryptService {

    private final FingerPrintRepository fingerPrintRepository;
    private final NGram nGram = new NGram(2);
    private Map<String, List<FingerPrint>> lsh = new HashMap<>();
    private static final int NGRAM_SIZE = 2;

    @PostConstruct
    public void buildLSHIndex() {
        List<FingerPrint> allFilgerdata = fingerPrintRepository.findAll();
        for(FingerPrint finger : allFilgerdata) {
            Set<String> ngrams = extractNGram(finger.getEncrypted_template(), NGRAM_SIZE);

            for(String ngram : ngrams) {
                lsh.computeIfAbsent(ngram, k -> new ArrayList<>()).add(finger);
            }
        }
    }

    private Set<String> extractNGram(String text, int n) {
        Set<String> ngrams = new HashSet<>();
        if(text == null || text.length() < n) {
            return ngrams;
        }

        for (int i = 0; i<= text.length() - n; i++) {
            String ngram = text.substring(i, i+n);
            ngrams.add(ngram);
        }
        return ngrams;
    }

    public FingerPrint ContrastRatioComparator(String input) {
        Set<String> inputNgrams = extractNGram(input, NGRAM_SIZE);

        Set<FingerPrint> candidates = new HashSet<>();

        for(String ngram : inputNgrams) {
            List<FingerPrint> bucket = lsh.get(ngram);
            if (bucket != null) {
                candidates.addAll(bucket);
            }
        }

        FingerPrint bestMacth = null;
        double minDistance = Double.MAX_VALUE;

        for (FingerPrint fingerPrint : candidates) {
            double dis = nGram.distance(input, fingerPrint.getEncrypted_template());

            if (dis < minDistance) {
                minDistance = dis;
                bestMacth = fingerPrint;
            }
        }

        if (minDistance > 0.9) {
            return null;
        }
        return bestMacth;
    }
}
