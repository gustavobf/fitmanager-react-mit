package com.gustavofigueiredo.fitmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI fitManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FitManager - API de Gestão de Academia")
                        .version("1.0.0")
                        .description("""
                                API REST para administrar alunos, planos, matrículas, instrutores,
                                agendamentos e indicadores de uma academia.

                                **Formato:** JSON; datas são ISO 8601. Horários de agendamento e
                                filtros de período são interpretados no horário local de
                                `America/Sao_Paulo`, sem necessidade de enviar deslocamento de fuso.
                                Por exemplo: `2026-10-20T18:00:00`.

                                **Paginação:** `pagina` começa em zero; `tamanho` aceita de 1 a 100
                                e usa 20 como padrão.

                                **Erros:** respostas de erro seguem o formato `codigo`, `mensagem`,
                                `detalhes` e `dataHora`.
                                """))
                .servers(List.of(new Server()
                        .url("http://localhost:8080")
                        .description("Ambiente local de desenvolvimento")));
    }
}
