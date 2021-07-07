package br.com.letscode.service;

import br.com.letscode.dominio.Medicamento;

public interface MedicamentoService {

    Medicamento cadastrarMedicamento(String principioAtivo, String fabricante, int dosagem, int periodicidade);
    Medicamento consultaMedicamento(String principioAtivo, String fabricante);
}
