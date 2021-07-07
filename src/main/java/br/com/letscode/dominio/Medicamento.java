package br.com.letscode.dominio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicamento {

    private String principioAtivo;
    private String fabricante;
    private int dosagem;
    private int periodicidade;
}
