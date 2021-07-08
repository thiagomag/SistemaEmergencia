package br.com.letscode.excecoes;

public class PacienteJaExisteException extends RuntimeException {
    public PacienteJaExisteException(String message){
        super(message);
    }
}
