package br.com.letscode.service.impl;

import br.com.letscode.dao.PacienteDao;
import br.com.letscode.dominio.Paciente;
import br.com.letscode.service.PacienteService;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class PacienteServiceImpl implements PacienteService {

    @Inject
    private PacienteDao pacienteDao;

    @Override
    public Paciente inserirPaciente(Paciente paciente) throws IOException {
        paciente.setIdentificador(UUID.randomUUID().toString());
        pacienteDao.inserirArquivo(paciente);
        return paciente;
    }

    @Override
    public Optional<Paciente> consultaPaciente(String cpf) throws IOException {
        return pacienteDao.findByCpf(cpf).stream().filter(paciente1 -> paciente1.getCpf().equals(cpf)).findFirst();
    }
}