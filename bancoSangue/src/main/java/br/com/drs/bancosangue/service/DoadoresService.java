package br.com.drs.bancosangue.service;

import br.com.drs.bancosangue.model.Doadores;
import br.com.drs.bancosangue.util.CompatibilidadeSanguinea;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class DoadoresService extends GenericService<Doadores, Long> {

    @Value("${data.json.path}")
    private String caminhoArquivo;

    public void importarDados() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        objectMapper.registerModule(javaTimeModule);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Lê o arquivo data.json
        List<Doadores> doadores = objectMapper.readValue(
                new File(caminhoArquivo),
                new TypeReference<List<Doadores>>() {}
        );

        doadores.forEach(this::salvar);
    }

    public Map<String, Long> contarCandidatosPorEstado() {
        return listarTodos().stream()
                .collect(Collectors.groupingBy(Doadores::getEstado, Collectors.counting()));
    }

    public Map<String, String> calcularImcMedioPorFaixaEtaria() {
        LocalDate hoje = LocalDate.now();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        return listarTodos().stream()
                .collect(Collectors.groupingBy(
                        doador -> {
                            int idade = Period.between(doador.getData_nasc(), hoje).getYears();
                            int faixaInicio = (idade / 10) * 10;
                            int faixaFim = faixaInicio + 9;
                            return faixaInicio + " a " + faixaFim; // Ex.: "21 a 30"
                        },
                        Collectors.averagingDouble(doador -> doador.getPeso() / Math.pow(doador.getAltura(), 2))
                )).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> decimalFormat.format(entry.getValue())
                ));
    }

    public Map<String, String> calcularPercentualObesosPorSexo() {
        List<Doadores> doadores = listarTodos();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        return doadores.stream()
                .collect(Collectors.groupingBy(
                        doador -> doador.getSexo().name(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                lista -> {
                                    long total = lista.size();
                                    long obesos = lista.stream()
                                            .filter(d -> d.getPeso() / Math.pow(d.getAltura(), 2) > 30)
                                            .count();
                                    double percentual = total > 0 ? (obesos * 100.0) / total : 0.0;
                                    return decimalFormat.format(percentual) + "%";
                                }
                        )
                ));
    }

    public Map<String, String> calcularMediaIdadePorTipoSanguineo() {
        LocalDate hoje = LocalDate.now();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        return listarTodos().stream()
                .collect(Collectors.groupingBy(
                        Doadores::getTipo_sanguineo,
                        Collectors.averagingInt(doador -> Period.between(doador.getData_nasc(), hoje).getYears())
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> decimalFormat.format(entry.getValue())
                ));
    }

    public Map<String, Long> calcularQuantidadeDoadoresParaCadaTipoSanguineo() {
        LocalDate hoje = LocalDate.now();

        return listarTodos().stream()
                .filter(doador -> {
                    int idade = Period.between(doador.getData_nasc(), hoje).getYears();
                    return idade >= 16 && idade <= 69 && doador.getPeso() > 50; // Peso > 50Kg
                })
                .collect(Collectors.groupingBy(
                        doador -> doador.getTipo_sanguineo(),
                        Collectors.counting()
                ));
    }

    public Map<String, Long> calcularQuantidadeDoadoresPossiveisParaCadaReceptor() {
        return CompatibilidadeSanguinea.podeDoarPara.keySet().stream()
                .collect(Collectors.toMap(
                        receptor -> receptor,
                        receptor -> {
                            Set<String> tiposCompativeis = CompatibilidadeSanguinea.podeDoarPara(receptor);
                            return listarTodos().stream()
                                    .filter(doador -> tiposCompativeis.contains(doador.getTipo_sanguineo()))
                                    .filter(doador -> {
                                        // Verificar idade (16 a 69 anos) e peso
                                        int idade = Period.between(doador.getData_nasc(), LocalDate.now()).getYears();
                                        return idade >= 16 && idade <= 69 && doador.getPeso() > 50;
                                    })
                                    .count();
                        }
                ));
    }
}