package br.com.letscode.dao;

import br.com.letscode.dominio.Paciente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class PacienteDaoImpl implements PacienteDao {

    private final String caminho = "pacientes.csv";

    public final Path path = Paths.get(caminho);

    @Override
    public Paciente inserirArquivo(Paciente paciente) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(path)){
            bw.write(String.valueOf(format(paciente)));
        }
        return paciente;
    }

    @Override
    public List<Paciente> getAll() throws IOException {
        List<Paciente> pacientes;
        try (BufferedReader br = Files.newBufferedReader(path)){
            pacientes = br.lines().map(this::convert).collect(Collectors.toList());
        }
        return pacientes;
    }

    @Override
    public Optional<Paciente> findByCpf(String cpf) throws IOException {
        List<Paciente> pacientes = getAll();
        return  pacientes.stream().filter(cliente -> cliente.getCpf().equals(cpf)).findFirst();
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
}
