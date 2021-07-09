package br.com.letscode.dao;

import br.com.letscode.dominio.Medicamento;
import br.com.letscode.dominio.Paciente;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface EmergenciaDao {

    Medicamento inserirArquivo(Medicamento medicamento) throws IOException;
    List<Medicamento> getAll() throws IOException;
    List<Medicamento> findByCpf(String cpf) throws IOException;
    Optional<Medicamento> findByName(String principioAtivo) throws IOException;
    Optional<Medicamento> findFirstByCpf(String cpf) throws IOException;
    List<Medicamento> findByPrincipioAtivo(String principioAtivo) throws IOException;
    Medicamento alterarArquivo(Medicamento medicamento, String identificador) throws IOException;
    void removerItemArquivo(String identificador) throws IOException;
}
