package com.android.regex;

/*
   The Computer Language Benchmarks Game
   https://salsa.debian.org/benchmarksgame-team/benchmarksgame/

   contributed by Francois Green
*/

import java.io.*;
import java.util.AbstractMap;
import java.util.Arrays;


import java9.util.stream.Collectors;
import java9.util.stream.StreamSupport;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java9.util.concurrent.CompletableFuture;
import java9.util.function.BiFunction;
import java9.util.stream.Stream;

public class RegexRedux {

    public void run(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        {
            byte[] buf = new byte[65536];
            int count;
            while ((count = in.read(buf)) > 0) {
                baos.write(buf, 0, count);
            }
        }
        final String input = baos.toString("US-ASCII");

        final int initialLength = input.length();

        final String sequence = input.replaceAll(">.*\n|\n", "");

        CompletableFuture<String> replacements = CompletableFuture.supplyAsync(() -> {
            final Map<String, String> iub = new LinkedHashMap<>();
            iub.put("tHa[Nt]", "<4>");
            iub.put("aND|caN|Ha[DS]|WaS", "<3>");
            iub.put("a[NSt]|BY", "<2>");
            iub.put("<[^>]*>", "|");
            iub.put("\\|[^|][^|]*\\|", "-");

            String buffer = sequence;
            for (Map.Entry<String, String> entry : iub.entrySet()) {
                buffer = Pattern.compile(entry.getKey()).matcher(buffer).replaceAll(entry.getValue());
            }
            return buffer;
        });

        final int codeLength = sequence.length();

        final List<String> variants = Arrays.asList("agggtaaa|tttaccct",
                "[cgt]gggtaaa|tttaccc[acg]",
                "a[act]ggtaaa|tttacc[agt]t",
                "ag[act]gtaaa|tttac[agt]ct",
                "agg[act]taaa|ttta[agt]cct",
                "aggg[acg]aaa|ttt[cgt]ccct",
                "agggt[cgt]aa|tt[acg]accct",
                "agggta[cgt]a|t[acg]taccct",
                "agggtaa[cgt]|[acg]ttaccct");

        BiFunction<String, String, Map.Entry<String, Long>> counts = (v, s) -> {
            Long count = Stream.of(s.split(v)).count() - 1; //Off by one
            return new AbstractMap.SimpleEntry<>(v, count);
        };

        final Map<String, Long> results = StreamSupport.parallelStream(variants)
                .map(variant -> counts.apply(variant, sequence))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (String variant : variants) {
            System.out.println(variant + " " + results.get(variant));
        }

        System.out.println();
        System.out.println(initialLength);
        System.out.println(codeLength);
        System.out.println(replacements.join().length());
    }
}
