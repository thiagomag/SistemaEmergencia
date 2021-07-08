package br.com.letscode.service;

import br.com.letscode.dominio.Medicamento;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MedicamentoService {

    Medicamento inserirMedicamento(Medicamento medicamento) throws IOException;
    Optional<Medicamento> consultaMedicamento(String principioAtivo) throws IOException;
    List<Medicamento> listAll() throws IOException;
    Medicamento alterar(Medicamento medicamento, String identificador) throws IOException;

    void remover(String identificador) throws IOException;
}
