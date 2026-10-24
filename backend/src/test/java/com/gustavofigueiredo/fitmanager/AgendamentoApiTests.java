package com.gustavofigueiredo.fitmanager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavofigueiredo.fitmanager.repository.AgendamentoRepository;
import com.gustavofigueiredo.fitmanager.repository.AlunoRepository;
import com.gustavofigueiredo.fitmanager.repository.InstrutorRepository;
import com.gustavofigueiredo.fitmanager.repository.MatriculaRepository;
import com.gustavofigueiredo.fitmanager.repository.PlanoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.junit.jupiter.api.BeforeEach;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AgendamentoApiTests {
    @Autowired
    private Environment environment;

    @Autowired
    private AlunoRepository alunos;

    @Autowired
    private PlanoRepository planos;

    @Autowired
    private InstrutorRepository instrutores;

    @Autowired
    private MatriculaRepository matriculas;

    @Autowired
    private AgendamentoRepository agendamentos;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String token;

    @BeforeEach
    void autenticar() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl() + "/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"login\":\"qualquer-login\",\"senha\":\"qualquer-senha\"}"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), response.body());
        token = objectMapper.readTree(response.body()).get("token").asText();
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    void seedsDevelopmentDataInEveryTable() {
                assertTrue(alunos.count() >= 405);
        assertTrue(planos.count() >= 14);
        assertTrue(instrutores.count() >= 24);
                assertTrue(matriculas.count() >= 405);
                assertTrue(agendamentos.count() >= 1206);
    }

    @Test
    void allowsConfiguredFrontendOriginForCorsPreflight() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl() + "/api/agendamentos"))
                .header("Origin", "http://localhost:5173")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "content-type")
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("http://localhost:5173", response.headers().firstValue("Access-Control-Allow-Origin")
                .orElseThrow());
        assertTrue(response.headers().firstValue("Access-Control-Allow-Methods").orElseThrow().contains("POST"));
    }

        @Test
        void rejectsProtectedApiRequestsWithoutBearerToken() throws Exception {
                HttpResponse<String> response = httpClient.send(
                                HttpRequest.newBuilder(URI.create(baseUrl() + "/api/agendamentos")).GET().build(),
                                HttpResponse.BodyHandlers.ofString());

                assertEquals(401, response.statusCode());
                assertEquals("NAO_AUTORIZADO", objectMapper.readTree(response.body()).get("codigo").asText());
        }

    @Test
    void acceptsLocalSaoPauloTimeAndUpdatesAppointmentWithoutValidationConflict() throws Exception {
        String unique = UUID.randomUUID().toString();
        JsonNode aluno = post("/api/alunos", """
                {"nome":"Aluno Local","email":"aluno.%s@example.com"}
                """.formatted(unique), 201);
        JsonNode instrutor = post("/api/instrutores", """
                {"nome":"Instrutor Local","email":"instrutor.%s@example.com","especialidade":"Pilates"}
                """.formatted(unique), 201);
        JsonNode plano = post("/api/planos", """
                {"nome":"Plano Teste %s","duracaoEmDias":30,"preco":99.90}
                """.formatted(unique), 201);
        JsonNode matricula = post("/api/matriculas", """
                {"alunoId":%d,"planoId":%d,"dataInicio":"2026-10-17"}
                """.formatted(aluno.get("id").asLong(), plano.get("id").asLong()), 201);

        put("/api/alunos/" + aluno.get("id").asLong(), """
                {"nome":"Aluno Local Atualizado","email":"aluno.%s@example.com"}
                """.formatted(unique), 200);
        put("/api/instrutores/" + instrutor.get("id").asLong(), """
                {"nome":"Instrutor Local","email":"instrutor.%s@example.com","especialidade":"Pilates"}
                """.formatted(unique), 200);
        put("/api/planos/" + plano.get("id").asLong(), """
                {"nome":"Plano Teste %s","duracaoEmDias":30,"preco":99.90}
                """.formatted(unique), 200);
        patch("/api/matriculas/" + matricula.get("id").asLong(), """
                {"status":"ENCERRADA"}
                """, 200);

        String agendamento = """
                {"alunoId":%d,"instrutorId":%d,"dataHora":"2026-10-20T18:30:00","duracaoEmMinutos":45}
                """.formatted(aluno.get("id").asLong(), instrutor.get("id").asLong());
        JsonNode criado = post("/api/agendamentos", agendamento, 201);
        long id = criado.get("id").asLong();
        assertEquals("2026-10-20T18:30:00-03:00", criado.get("dataHora").asText());

        HttpResponse<String> consulta = get("/api/agendamentos?alunoId=" + aluno.get("id").asLong()
                + "&inicio=2026-10-20T18%3A00%3A00&fim=2026-10-20T19%3A00%3A00");
        assertEquals(200, consulta.statusCode());
        assertEquals(id, objectMapper.readTree(consulta.body()).get("conteudo").get(0).get("id").asLong());

        JsonNode atualizado = put("/api/agendamentos/" + id, """
                {"alunoId":%d,"instrutorId":%d,"dataHora":"2026-10-20T19:30:00","duracaoEmMinutos":45}
                """.formatted(aluno.get("id").asLong(), instrutor.get("id").asLong()), 200);
        assertEquals("2026-10-20T19:30:00-03:00", atualizado.get("dataHora").asText());

        JsonNode status = patch("/api/agendamentos/" + id, """
                {"status":"CONCLUIDO"}
                """, 200);
        assertEquals("CONCLUIDO", status.get("status").asText());
    }

    @Test
    void openApiDocumentsLocalTimeWithoutAnOffset() throws Exception {
        JsonNode docs = objectMapper.readTree(get("/v3/api-docs").body());
        assertTrue(docs.get("openapi").asText().startsWith("3.0"));
        assertTrue(docs.get("info").get("description").asText().contains("America/Sao_Paulo"));
        assertEquals("#/components/schemas/AgendamentoRequest",
                docs.get("paths").get("/api/agendamentos").get("post").get("requestBody")
                        .get("content").get("application/json").get("schema").get("$ref").asText());
        JsonNode dataHoraExample = docs.get("paths")
                .get("/api/agendamentos")
                .get("post")
                .get("requestBody")
                .get("content")
                .get("application/json")
                .get("examples")
                .get("Sessão funcional")
                .get("value")
                .get("dataHora");
        assertEquals("2026-10-20T18:00:00", dataHoraExample.asText());
    }

    @Test
    void validatesRequestBodiesFromTheApiContracts() throws Exception {
        JsonNode error = post("/api/alunos", """
                {"nome":"A","email":"invalido"}
                """, 400);
        assertEquals("REQUISICAO_INVALIDA", error.get("codigo").asText());
    }

    @Test
    void deletesStudentHistoryOnlyAfterExplicitConfirmation() throws Exception {
        String unique = UUID.randomUUID().toString();
        JsonNode aluno = post("/api/alunos", """
                {"nome":"Aluno com histórico","email":"aluno.%s@example.com"}
                """.formatted(unique), 201);
        JsonNode instrutor = post("/api/instrutores", """
                {"nome":"Instrutor histórico","email":"instrutor.%s@example.com","especialidade":"Pilates"}
                """.formatted(unique), 201);
        JsonNode plano = post("/api/planos", """
                {"nome":"Plano histórico %s","duracaoEmDias":30,"preco":99.90}
                """.formatted(unique), 201);
        long alunoId = aluno.get("id").asLong();
        post("/api/matriculas", """
                {"alunoId":%d,"planoId":%d,"dataInicio":"2026-10-17"}
                """.formatted(alunoId, plano.get("id").asLong()), 201);
        post("/api/agendamentos", """
                {"alunoId":%d,"instrutorId":%d,"dataHora":"2026-10-30T18:30:00","duracaoEmMinutos":45}
                """.formatted(alunoId, instrutor.get("id").asLong()), 201);

        HttpResponse<String> bloqueio = delete("/api/alunos/" + alunoId);
        assertEquals(409, bloqueio.statusCode());
        assertEquals("ALUNO_COM_HISTORICO", objectMapper.readTree(bloqueio.body()).get("codigo").asText());
        assertTrue(alunos.existsById(alunoId));

        HttpResponse<String> exclusao = delete("/api/alunos/" + alunoId + "?apagarHistorico=true");
        assertEquals(204, exclusao.statusCode());
        assertFalse(alunos.existsById(alunoId));
        assertFalse(matriculas.existsByAlunoId(alunoId));
        assertFalse(agendamentos.existsByAlunoId(alunoId));
    }

    private HttpResponse<String> delete(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl() + path))
                .header("Authorization", "Bearer " + token)
                .DELETE()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private JsonNode post(String path, String body, int expectedStatus) throws Exception {
        return request("POST", path, body, expectedStatus);
    }

    private JsonNode put(String path, String body, int expectedStatus) throws Exception {
        return request("PUT", path, body, expectedStatus);
    }

    private JsonNode patch(String path, String body, int expectedStatus) throws Exception {
        return request("PATCH", path, body, expectedStatus);
    }

    private JsonNode request(String method, String path, String body, int expectedStatus) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl() + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .method(method, HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(expectedStatus, response.statusCode(), response.body());
        return objectMapper.readTree(response.body());
    }

    private HttpResponse<String> get(String path) throws Exception {
        return httpClient.send(HttpRequest.newBuilder(URI.create(baseUrl() + path))
                .header("Authorization", "Bearer " + token).GET().build(),
                HttpResponse.BodyHandlers.ofString());
    }

    private String baseUrl() {
        return "http://localhost:" + environment.getProperty("local.server.port");
    }
}
