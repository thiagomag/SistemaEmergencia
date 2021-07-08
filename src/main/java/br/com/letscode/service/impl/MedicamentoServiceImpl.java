package br.com.letscode.service.impl;

import br.com.letscode.dao.EmergenciaDao;
import br.com.letscode.dominio.Medicamento;
import br.com.letscode.excecoes.PacienteJaExisteException;
import br.com.letscode.service.MedicamentoService;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MedicamentoServiceImpl implements MedicamentoService {

    @Inject
    private EmergenciaDao emergenciaDao;

    @Override
    public Medicamento inserirMedicamento(Medicamento medicamento) throws IOException {
        if(emergenciaDao.findByName(medicamento.getPrincipioAtivo()).isPresent()) {
            throw new PacienteJaExisteException("Medicamento com o principio ativo " + medicamento.getPrincipioAtivo() + " já existe no sistema.");
        }
        medicamento.setIdentificador(UUID.randomUUID().toString());
        return emergenciaDao.inserirArquivo(medicamento);
    }

    @Override
    public List<Medicamento> listAll() throws IOException {
        return emergenciaDao.getAll();
    }

    @Override
    public Optional<Medicamento> consultaMedicamento(String principioAtivo) throws IOException {
        return emergenciaDao.findByName(principioAtivo);
    }

    @Override
    public Medicamento alterar(Medicamento medicamento, String identificador) throws IOException {
        return emergenciaDao.alterarArquivo(medicamento, identificador);
    }

    @Override
    public void remover(String identificador) throws IOException {
        emergenciaDao.removerItemArquivo(identificador);
    }
}
