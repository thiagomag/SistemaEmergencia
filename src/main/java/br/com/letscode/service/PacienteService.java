package br.com.letscode.service;

import br.com.letscode.dominio.Paciente;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PacienteService {

    Paciente inserirPaciente(Paciente paciente) throws IOException;
    Optional<Paciente> consultaPaciente(String cpf) throws IOException;
    List<Paciente> listAll() throws IOException;
    Paciente alterar(Paciente paciente, String identificador) throws IOException;
    void remover(String identificador) throws IOException;
}
