package br.com.letscode.service;

import br.com.letscode.dominio.Medicamento;

import java.io.IOException;
import java.util.Optional;

public interface PacienteService {

    Optional<Medicamento> consultaPaciente(String cpf) throws IOException;
}
