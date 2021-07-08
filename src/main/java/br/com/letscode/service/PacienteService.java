package br.com.letscode.service;

import br.com.letscode.dominio.Medicamento;

import java.io.IOException;
import java.util.List;

public interface PacienteService {

    List<Medicamento> consultaPaciente(String cpf) throws IOException;
}
