package br.com.drs.bancosangue.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CompatibilidadeSanguinea {

    public static final Map<String, Set<String>> podeDoarPara = new HashMap<>();

    private static final Map<String, Set<String>> podeReceberDe = new HashMap<>();

    static {
        podeDoarPara.put("A+", Set.of("A+", "AB+"));
        podeDoarPara.put("A-", Set.of("A+", "A-", "AB+", "AB-"));
        podeDoarPara.put("B+", Set.of("B+", "AB+"));
        podeDoarPara.put("B-", Set.of("B+", "B-", "AB+", "AB-"));
        podeDoarPara.put("AB+", Set.of("AB+"));
        podeDoarPara.put("AB-", Set.of("AB+", "AB-"));
        podeDoarPara.put("O+", Set.of("A+", "B+", "O+", "AB+"));
        podeDoarPara.put("O-", Set.of("A+", "B+", "O+", "AB+", "A-", "B-", "O-", "AB-"));

        podeReceberDe.put("A+", Set.of("A+", "A-", "O+", "O-"));
        podeReceberDe.put("A-", Set.of("A-", "O-"));
        podeReceberDe.put("B+", Set.of("B+", "B-", "O+", "O-"));
        podeReceberDe.put("B-", Set.of("B-", "O-"));
        podeReceberDe.put("AB+", Set.of("A+", "B+", "O+", "AB+", "A-", "B-", "O-", "AB-"));
        podeReceberDe.put("AB-", Set.of("A-", "B-", "O-", "AB-"));
        podeReceberDe.put("O+", Set.of("O+", "O-"));
        podeReceberDe.put("O-", Set.of("O-"));
    }

    public static Set<String> podeDoarPara(String tipoSanguineo) {
        return podeDoarPara.getOrDefault(tipoSanguineo, Set.of());
    }

    public static Set<String> podeReceberDe(String tipoSanguineo) {
        return podeReceberDe.getOrDefault(tipoSanguineo, Set.of());
    }
}
