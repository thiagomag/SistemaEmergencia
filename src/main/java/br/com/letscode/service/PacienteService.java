package br.com.letscode.service;

import br.com.letscode.dominio.Paciente;

import java.io.IOException;

public interface PacienteService {

    Paciente inserirPaciente(Paciente paciente) throws IOException;
    Paciente consultaPaciente(String cpf);
}
