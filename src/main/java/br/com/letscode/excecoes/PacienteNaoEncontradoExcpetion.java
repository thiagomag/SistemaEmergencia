package br.com.letscode.excecoes;

public class PacienteNaoEncontradoExcpetion extends RuntimeException {
    public PacienteNaoEncontradoExcpetion(String message){
        super(message);
    }
}
