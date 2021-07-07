package br.com.letscode.dao;

import br.com.letscode.dominio.Paciente;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PacienteDao {

    Paciente inserirArquivo(Paciente paciente) throws IOException;
    List<Paciente> getAll() throws IOException;
    Optional<Paciente> findByCpf(String cpf) throws IOException;
}
