package br.com.letscode.dao;

import br.com.letscode.dominio.Paciente;
import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PacienteDaoImpl implements PacienteDao {

    private final String caminho = "pacientes.csv";

    public Path path;

    @PostConstruct
    public void init() {
        try {
            path = Paths.get(caminho);
            if (!path.toFile().exists()) {
                Files.createFile(path);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public Paciente inserirArquivo(Paciente paciente) throws IOException {
        write(format(paciente));
        return paciente;
    }

    private void write(String clienteStr) throws IOException {
        try(BufferedWriter bf = Files.newBufferedWriter(path, StandardOpenOption.APPEND)){
            bf.flush();
            bf.write(clienteStr);
        }
    }

    @Override
    public List<Paciente> getAll() throws IOException {
        List<Paciente> pacientes;
        try (BufferedReader br = Files.newBufferedReader(path)){
            pacientes = br.lines().filter(Objects::nonNull).filter(Predicate.not(String::isEmpty)).map(this::convert).collect(Collectors.toList());
        }
        return pacientes;
    }

    @Override
    public Optional<Paciente> findByCpf(String cpf) throws IOException {
        List<Paciente> pacientes = getAll();
        return  pacientes.stream().filter(cliente -> cliente.getCpf().equals(cpf)).findFirst();
    }

    @Override
    public Paciente alterarArquivo(Paciente paciente, String identificador) throws IOException {
        List<Paciente> pacientes = getAll();
        Optional<Paciente> optionalPaciente = pacientes.stream()
                .filter(pacienteSearch -> pacienteSearch.getIdentificador().equals(identificador)).findFirst();
        if(optionalPaciente.isPresent()) {
            optionalPaciente.get().setNome(paciente.getNome());
            reescreverArquivo(pacientes);
            return optionalPaciente.get();
        }
        return paciente;
    }

    private void reescreverArquivo(List<Paciente> pacientes) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Paciente pacienteBuilder: pacientes) {
            builder.append(format(pacienteBuilder));
        }
        write(builder.toString());
    }

    @Override
    public void removerItemArquivo(String identificador) throws IOException {
        List<Paciente> pacientes = getAll();
        List<Paciente> pacienteResultante = new ArrayList<>();
        for (Paciente paciente : pacientes){
            if(!paciente.getIdentificador().equals(identificador)){
                pacienteResultante.add(paciente);
            }
        }
        eraseContent();
        reescreverArquivo(pacienteResultante);
    }

    private String format(Paciente paciente) {
        return String.format("%s;%s;%s \r\n", paciente.getIdentificador(), paciente.getCpf(), paciente.getNome());
    }

    private Paciente convert(String linha) {
        StringTokenizer token = new StringTokenizer(linha, ";");
        return Paciente.builder()
                .identificador(token.nextToken())
                .cpf(token.nextToken())
                .nome(token.nextToken())
                .build();
    }

    public void eraseContent() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path);
        writer.write("");
        writer.flush();
    }
}
