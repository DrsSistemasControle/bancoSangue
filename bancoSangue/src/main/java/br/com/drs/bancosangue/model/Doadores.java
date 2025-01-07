package br.com.drs.bancosangue.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import br.com.drs.bancosangue.model.enuns.Sexo;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Doadores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String cpf;

    private String rg;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty("data_nasc")
    private LocalDate data_nasc;

    private Sexo sexo;

    private String mae;

    private String pai;

    private String email;

    private String cep;

    private String endereco;

    private String numero;

    private String bairro;

    private String cidade;

    private String estado;

    private String telefone_fixo;

    private String celular;

    private float altura;

    private float peso;

    @JsonProperty("tipo_sanguineo")
    private String tipo_sanguineo;
}
