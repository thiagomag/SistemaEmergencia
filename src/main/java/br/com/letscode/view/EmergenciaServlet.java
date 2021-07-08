package br.com.letscode.view;

import br.com.letscode.dominio.CustomMessage;
import br.com.letscode.dominio.Medicamento;
import br.com.letscode.excecoes.PacienteJaExisteException;
import br.com.letscode.service.MedicamentoService;
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

@WebServlet(name = "EmergenciaServlet", urlPatterns = "/emergencia")
public class EmergenciaServlet extends HttpServlet {

    public static final String MEDICAMENTOS_SESSION = "medicamentos";

    @Inject
    private MedicamentoService medicamentoService;

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
        Medicamento medicamentoRequest = gson.fromJson(conteudo.toString(), Medicamento.class);
        PrintWriter print = prepareResponse(response);
        String resposta;
        if (medicamentoRequest.getPaciente().getNome() == null || medicamentoRequest.getPaciente().getCpf() == null) {
            CustomMessage message = new CustomMessage(HttpServletResponse.SC_BAD_REQUEST, "Invalid Parameter");
            response.setStatus(message.getStatus());
            resposta = gson.toJson(message);
        } else {
            try {
                HttpSession session = request.getSession(true);
                medicamentoService.inserirMedicamento(medicamentoRequest);
                List<Medicamento> medicamentos = medicamentoService.listAll();
                session.setAttribute(MEDICAMENTOS_SESSION, medicamentos);
                resposta = gson.toJson(medicamentos);

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
        String line;
        StringBuilder conteudo = new StringBuilder();

        while (null != (line = br.readLine())) {
            conteudo.append(line);
        }
        return conteudo;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String cpfPesquisa = request.getParameter("cpf");
        final String nomePesquisa = request.getParameter("Principio Ativo");
        HttpSession session = request.getSession();
        List<Medicamento> medicamentos = new ArrayList<>();
        if (Objects.nonNull(session.getAttribute(MEDICAMENTOS_SESSION))) {
            medicamentos.addAll((List<Medicamento>) session.getAttribute(MEDICAMENTOS_SESSION));
        } else {
            medicamentos.addAll(medicamentoService.listAll());
        }
        PrintWriter printWriter = prepareResponse(response);
        if (cpfPesquisa != null) {
            Optional<Medicamento> optionalPaciente = pacienteService.consultaPaciente(cpfPesquisa);
            if (optionalPaciente.isPresent()) {
                printWriter.write(gson.toJson(optionalPaciente.get()));
            } else {
                naoEncontradoMessage(response, printWriter);
            }
        } else if (nomePesquisa != null) {
            Optional<Medicamento> optionalMedicamento = medicamentoService.consultaMedicamento(nomePesquisa);
            if (optionalMedicamento.isPresent()) {
                printWriter.write(gson.toJson(optionalMedicamento.get()));
            } else {
                naoEncontradoMessage(response, printWriter);
            }
        } else {
            printWriter.write(gson.toJson(medicamentos));
        }
        printWriter.close();
    }

    private void naoEncontradoMessage(HttpServletResponse response, PrintWriter printWriter) {
        CustomMessage message = CustomMessage.builder()
                .status(404)
                .message("Conteúdo não encontrado")
                .build();
        response.setStatus(404);
        printWriter.write(gson.toJson(message));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder conteudo = getBody(request);
        String identificador = request.getParameter("identificador");

        PrintWriter printWriter = prepareResponse(response);
        String resposta;
        if (Objects.isNull(identificador)) {
            resposta = erroMessage(response);
        } else {
            Medicamento medicamento = gson.fromJson(conteudo.toString(), Medicamento.class);
            resposta = gson.toJson(medicamentoService.alterar(medicamento, identificador));
            request.getSession().setAttribute(MEDICAMENTOS_SESSION, medicamentoService.listAll());
        }
        printWriter.write(resposta);
        printWriter.close();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String identificador = request.getParameter("identificador");
        PrintWriter printWriter = prepareResponse(response);
        String resposta;
        if(Objects.isNull(identificador)){
            resposta = erroMessage(response);
        }else {
            medicamentoService.remover(identificador);
            resposta = gson.toJson(new CustomMessage(204, "cliente removido"));
            request.getSession().setAttribute(MEDICAMENTOS_SESSION, medicamentoService.listAll());
        }
        printWriter.write(resposta);
        printWriter.close();
    }

    private String erroMessage(HttpServletResponse response) {
        response.setStatus(400);
        return gson.toJson(new CustomMessage(400, "identificador não informado"));
    }
}