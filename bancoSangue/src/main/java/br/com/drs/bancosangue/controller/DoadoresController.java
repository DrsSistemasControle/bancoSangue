package br.com.drs.bancosangue.controller;

import br.com.drs.bancosangue.model.Doadores;
import br.com.drs.bancosangue.service.DoadoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/doadores")
@CrossOrigin(origins = "http://localhost:3000")
public class DoadoresController {

    @Autowired
    private DoadoresService doadoresService;

    @PostMapping
    public Doadores salvar(@RequestBody Doadores doadores) {
        return doadoresService.salvar(doadores);
    }

    @GetMapping
    public List<Doadores> listarTodos() {
        return doadoresService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Doadores> buscarPorId(@PathVariable Long id) {
        return doadoresService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Doadores atualizar(@PathVariable Long id, @RequestBody Doadores doadores) {
        doadores.setId(id);
        return doadoresService.atualizar(doadores);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        doadoresService.deletarPorId(id);
    }

    @PostMapping("/importar")
    public ResponseEntity<?> importarDados() {
        try {
            doadoresService.importarDados();
            return ResponseEntity.ok("Dados importados com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao importar dados: " + e.getMessage());
        }
    }

    @GetMapping("/candidatos-por-estado")
    public Map<String, Long> contarCandidatosPorEstado() {
        return doadoresService.contarCandidatosPorEstado();
    }

    @GetMapping("/imc-medio-por-faixa-etaria")
    public ResponseEntity<?> getImcPorFaixaEtaria() {
        try {
            Map<String, String> resultados = doadoresService.calcularImcMedioPorFaixaEtaria();
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao calcular IMC");
        }
    }

    @GetMapping("/percentual-obesos-por-sexo")
    public Map<String, String> calcularPercentualObesosPorSexo() {
        return doadoresService.calcularPercentualObesosPorSexo();
    }

    @GetMapping("/media-idade-por-tipo-sanguineo")
    public ResponseEntity<?> getMediaIdadePorTipoSanguineo() {
        try {
            Map<String, String> resultados = doadoresService.calcularMediaIdadePorTipoSanguineo();
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao calcular média de idade");
        }
    }

    @GetMapping("/quantidade-doadores-por-receptor")
    public ResponseEntity<?> getQuantidadeDoadoresPorReceptor() {
        try {
            Map<String, Long> resultados = doadoresService.calcularQuantidadeDoadoresPossiveisParaCadaReceptor();
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao calcular quantidade de doadores");
        }
    }
}
