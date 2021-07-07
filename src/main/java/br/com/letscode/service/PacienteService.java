package br.com.letscode.service;

import br.com.letscode.dominio.Paciente;

import java.io.IOException;
import java.util.Optional;

public interface PacienteService {

    Paciente inserirPaciente(Paciente paciente) throws IOException;
    Optional<Paciente> consultaPaciente(String cpf) throws IOException;
}
