package br.com.letscode.service;

import br.com.letscode.dominio.Medicamento;

import java.io.IOException;
import java.util.List;

public interface MedicamentoService {

    Medicamento inserirMedicamento(Medicamento medicamento) throws IOException;
    List<Medicamento> consultaMedicamento(String principioAtivo) throws IOException;
    List<Medicamento> listAll() throws IOException;
    Medicamento alterar(Medicamento medicamento, String identificador) throws IOException;

    void remover(String identificador) throws IOException;
}
