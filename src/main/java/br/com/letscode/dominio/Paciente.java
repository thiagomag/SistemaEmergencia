package br.com.letscode.dominio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    private String identificador;
    private String nome;
    private String cpf;
    private List<Medicamento> medicamentos;
}
