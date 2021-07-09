package br.com.letscode.service.impl;

import br.com.letscode.dao.EmergenciaDao;
import br.com.letscode.dominio.Medicamento;
import br.com.letscode.excecoes.PacienteNaoEncontradoExcpetion;
import br.com.letscode.service.PacienteService;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;

public class PacienteServiceImpl implements PacienteService {

    @Inject
    private EmergenciaDao emergenciaDao;

    @Override
    public List<Medicamento> consultaPaciente(String cpf) throws IOException {
        List<Medicamento> medicamentos = emergenciaDao.findByCpf(cpf);
        if(medicamentos.isEmpty()) {
            throw new PacienteNaoEncontradoExcpetion("Paciente com o cpf " + cpf + " não encontrado");
        }
        return medicamentos;
    }

}