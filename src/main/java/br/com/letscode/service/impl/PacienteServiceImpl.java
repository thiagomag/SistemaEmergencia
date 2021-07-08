package br.com.letscode.service.impl;

import br.com.letscode.dao.PacienteDao;
import br.com.letscode.dominio.Paciente;
import br.com.letscode.excecoes.PacienteJaExisteException;
import br.com.letscode.service.PacienteService;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PacienteServiceImpl implements PacienteService {

    @Inject
    private PacienteDao pacienteDao;

    @Override
    public Paciente inserirPaciente(Paciente paciente) throws IOException {
        if(pacienteDao.findByCpf(paciente.getCpf()).isPresent()) {
            throw new PacienteJaExisteException("Paciente com o cpf " + paciente.getCpf() + " já existe no sistema.");
        }
        paciente.setIdentificador(UUID.randomUUID().toString());
        return pacienteDao.inserirArquivo(paciente);
    }

    @Override
    public List<Paciente> listAll() throws IOException {
        return pacienteDao.getAll();
    }

    @Override
    public Optional<Paciente> consultaPaciente(String cpf) throws IOException {
        return pacienteDao.findByCpf(cpf);
    }

    @Override
    public Paciente alterar(Paciente paciente, String identificador) throws IOException {
        return pacienteDao.alterarArquivo(paciente, identificador);
    }

    @Override
    public void remover(String identificador) throws IOException {
        pacienteDao.removerItemArquivo(identificador);
    }

}