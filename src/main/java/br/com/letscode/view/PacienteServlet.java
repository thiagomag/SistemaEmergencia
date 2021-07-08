package br.com.letscode.view;

import br.com.letscode.dominio.CustomMessage;
import br.com.letscode.dominio.Paciente;
import br.com.letscode.excecoes.PacienteJaExisteException;
import br.com.letscode.service.PacienteService;
import com.google.gson.Gson;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@WebServlet(name = "EmergenciaServlet", urlPatterns = "/pacientes")
public class PacienteServlet extends HttpServlet {

    public static final String PACIENTES_SESSION = "pacientes";

    @Inject
    private PacienteService pacienteService;

    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder conteudo = getBody(request);
        Paciente pacienteRequest = gson.fromJson(conteudo.toString(), Paciente.class);
        PrintWriter print = prepareResponse(response);
        String resposta;
        if (pacienteRequest.getNome() == null || pacienteRequest.getCpf() == null) {
            CustomMessage message = new CustomMessage(HttpServletResponse.SC_BAD_REQUEST, "Invalid Parameter");
            response.setStatus(message.getStatus());
            resposta = gson.toJson(message);
        } else {
            try {
                HttpSession session = request.getSession(true);
                pacienteService.inserirPaciente(pacienteRequest);
                List<Paciente> pacientes = pacienteService.listAll();
                session.setAttribute(PACIENTES_SESSION, pacientes);
                resposta = gson.toJson(pacientes);

            } catch (PacienteJaExisteException pacienteJaExisteException) {
                response.setStatus(400);
                resposta = gson.toJson(new CustomMessage(400, pacienteJaExisteException.getMessage()));
            }
        }
        print.write(resposta);
        print.close();
    }

    private PrintWriter prepareResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        return response.getWriter();
    }

    private StringBuilder getBody(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        String line = "";
        StringBuilder conteudo = new StringBuilder();

        while (null != (line = br.readLine())) {
            conteudo.append(line);
        }
        return conteudo;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String cpfPesquisa = request.getParameter("cpf");
        HttpSession session = request.getSession();
        List<Paciente> pacientes = new ArrayList<>();
        if (Objects.nonNull(session.getAttribute(PACIENTES_SESSION))) {
            pacientes.addAll((List<Paciente>) session.getAttribute(PACIENTES_SESSION));
        } else {
            pacientes.addAll(pacienteService.listAll());
        }
        PrintWriter printWriter = prepareResponse(response);
        if (cpfPesquisa != null) {
            Optional<Paciente> optionalPaciente = pacienteService.consultaPaciente(cpfPesquisa);
            if (optionalPaciente.isPresent()) {
                printWriter.write(gson.toJson(optionalPaciente.get()));
            } else {
                CustomMessage message = CustomMessage.builder()
                        .status(404)
                        .message("Conteúdo não encontrado")
                        .build();
                response.setStatus(404);
                printWriter.write(gson.toJson(message));
            }
        } else {
            printWriter.write(gson.toJson(pacientes));
        }
        printWriter.close();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder conteudo = getBody(request);
        String identificador = request.getParameter("identificador");

        PrintWriter printWriter = prepareResponse(response);
        String resposta = "";
        if (Objects.isNull(identificador)) {
            resposta = erroMessage(response);
        } else {
            Paciente paciente = gson.fromJson(conteudo.toString(), Paciente.class);
            resposta = gson.toJson(pacienteService.alterar(paciente, identificador));
            request.getSession().setAttribute(PACIENTES_SESSION,pacienteService.listAll());
        }
        printWriter.write(resposta);
        printWriter.close();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String identificador = request.getParameter("identificador");
        PrintWriter printWriter = prepareResponse(response);
        String resposta;
        if(Objects.isNull(identificador)){
            resposta = erroMessage(response);
        }else {
            pacienteService.remover(identificador);
            resposta = gson.toJson(new CustomMessage(204, "cliente removido"));
            request.getSession().setAttribute(PACIENTES_SESSION, pacienteService.listAll());
        }
        printWriter.write(resposta);
        printWriter.close();
    }

    private String erroMessage(HttpServletResponse response) {
        response.setStatus(400);
        return gson.toJson(new CustomMessage(400, "identificador não informado"));
    }
}