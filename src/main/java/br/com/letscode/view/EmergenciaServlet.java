package br.com.letscode.view;

import br.com.letscode.dominio.CustomMessage;
import br.com.letscode.dominio.Paciente;
import br.com.letscode.service.MedicamentoService;
import br.com.letscode.service.PacienteService;
import com.google.gson.Gson;
import jakarta.inject.Inject;
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
import java.util.Optional;

@WebServlet(name = "EmergenciaServlet", urlPatterns = "/emergencia")
public class EmergenciaServlet extends HttpServlet {

    public static final String PACIENTES_SESSION = "pacientes";

    @Inject
    private PacienteService pacienteService;

    @Inject
    private MedicamentoService medicamentoService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader br = request.getReader();
        String line = "";
        StringBuilder conteudo = new StringBuilder();
        Gson gson = new Gson();
        while ((line = br.readLine()) != null) {
            conteudo.append(line);
        }
        Paciente pacienteRequest = gson.fromJson(conteudo.toString(), Paciente.class);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter print = response.getWriter();
        String resposta;

        if (pacienteRequest.getNome() == null || pacienteRequest.getCpf() == null) {
            CustomMessage message = new CustomMessage(HttpServletResponse.SC_BAD_REQUEST, "Invalid Parameter");
            response.setStatus(message.getStatus());
            resposta = gson.toJson(message);
        } else {
            HttpSession session = request.getSession(true);
            List<Paciente> pacientes;
            if((pacientes = (List<Paciente>) session.getAttribute(PACIENTES_SESSION)) == null) {
                pacientes = new ArrayList<>();
            }
            pacientes.add(pacienteService.inserirPaciente(pacienteRequest));
            session.setAttribute(PACIENTES_SESSION, pacientes);

            pacienteService.inserirPaciente(pacienteRequest);
            resposta = gson.toJson(pacienteRequest);
        }
        print.write(resposta);
        print.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String cpfPesquisa = request.getParameter("cpf");
        HttpSession session = request.getSession();
        List<Paciente> pacientes = (List<Paciente>) session.getAttribute(PACIENTES_SESSION);
        Gson gson = new Gson();
        PrintWriter printWriter = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if(cpfPesquisa != null) {
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
}
