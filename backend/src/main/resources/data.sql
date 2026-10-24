-- Dados de demonstração inseridos somente quando cada registro ainda não existe.
INSERT INTO alunos (nome, email, telefone, data_nascimento, ativo, criado_em, atualizado_em)
SELECT 'Ana Souza', 'ana.souza@example.com', '+55 21 99999-0001', DATE '1998-05-14', TRUE,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM alunos WHERE email = 'ana.souza@example.com');

INSERT INTO alunos (nome, email, telefone, data_nascimento, ativo, criado_em, atualizado_em)
SELECT 'Bruno Costa', 'bruno.costa@example.com', '+55 21 99999-0002', DATE '1992-08-03', TRUE,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM alunos WHERE email = 'bruno.costa@example.com');

INSERT INTO alunos (nome, email, telefone, data_nascimento, ativo, criado_em, atualizado_em)
SELECT 'Camila Oliveira', 'camila.oliveira@example.com', '+55 11 99999-0003', DATE '2001-02-21', TRUE,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM alunos WHERE email = 'camila.oliveira@example.com');

INSERT INTO alunos (nome, email, telefone, data_nascimento, ativo, criado_em, atualizado_em)
SELECT 'Diego Martins', 'diego.martins@example.com', '+55 31 99999-0004', DATE '1989-11-09', FALSE,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM alunos WHERE email = 'diego.martins@example.com');

INSERT INTO alunos (nome, email, telefone, data_nascimento, ativo, criado_em, atualizado_em)
SELECT 'Fernanda Lima', 'fernanda.lima@example.com', NULL, DATE '1995-07-30', TRUE,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM alunos WHERE email = 'fernanda.lima@example.com');

INSERT INTO planos (nome, descricao, duracao_em_dias, preco, ativo, criado_em, atualizado_em)
SELECT 'Plano Mensal', 'Acesso à academia por 30 dias', 30, 129.90, TRUE,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM planos WHERE nome = 'Plano Mensal');

INSERT INTO planos (nome, descricao, duracao_em_dias, preco, ativo, criado_em, atualizado_em)
SELECT 'Plano Trimestral', 'Acesso à academia por 90 dias', 90, 349.90, TRUE,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM planos WHERE nome = 'Plano Trimestral');

INSERT INTO planos (nome, descricao, duracao_em_dias, preco, ativo, criado_em, atualizado_em)
SELECT 'Plano Anual', 'Acesso à academia por 365 dias', 365, 1199.90, TRUE,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM planos WHERE nome = 'Plano Anual');

INSERT INTO planos (nome, descricao, duracao_em_dias, preco, ativo, criado_em, atualizado_em)
SELECT 'Plano Experimental', 'Plano indisponível para novas matrículas', 14, 79.90, FALSE,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM planos WHERE nome = 'Plano Experimental');

INSERT INTO instrutores (nome, email, telefone, especialidade, ativo, criado_em, atualizado_em)
SELECT 'Carlos Lima', 'carlos.lima@example.com', '+55 11 98888-0001', 'Musculação', TRUE,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM instrutores WHERE email = 'carlos.lima@example.com');

INSERT INTO instrutores (nome, email, telefone, especialidade, ativo, criado_em, atualizado_em)
SELECT 'Juliana Rocha', 'juliana.rocha@example.com', '+55 21 98888-0002', 'Pilates', TRUE,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM instrutores WHERE email = 'juliana.rocha@example.com');

INSERT INTO instrutores (nome, email, telefone, especialidade, ativo, criado_em, atualizado_em)
SELECT 'Rafael Mendes', 'rafael.mendes@example.com', '+55 31 98888-0003', 'Treinamento funcional', TRUE,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM instrutores WHERE email = 'rafael.mendes@example.com');

INSERT INTO instrutores (nome, email, telefone, especialidade, ativo, criado_em, atualizado_em)
SELECT 'Mariana Alves', 'mariana.alves@example.com', NULL, 'Yoga', FALSE,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM instrutores WHERE email = 'mariana.alves@example.com');

INSERT INTO matriculas (aluno_id, plano_id, data_inicio, data_fim, status, criado_em)
SELECT aluno.id, plano.id, DATEADD('DAY', -25, CURRENT_DATE), DATEADD('DAY', 4, CURRENT_DATE),
       'ATIVA', CURRENT_TIMESTAMP
FROM alunos aluno, planos plano
WHERE aluno.email = 'ana.souza@example.com'
  AND plano.nome = 'Plano Mensal'
  AND NOT EXISTS (SELECT 1 FROM matriculas WHERE aluno_id = aluno.id AND status = 'ATIVA');

INSERT INTO matriculas (aluno_id, plano_id, data_inicio, data_fim, status, criado_em)
SELECT aluno.id, plano.id, DATEADD('DAY', -60, CURRENT_DATE), DATEADD('DAY', 304, CURRENT_DATE),
       'ATIVA', CURRENT_TIMESTAMP
FROM alunos aluno, planos plano
WHERE aluno.email = 'bruno.costa@example.com'
  AND plano.nome = 'Plano Anual'
  AND NOT EXISTS (SELECT 1 FROM matriculas WHERE aluno_id = aluno.id AND status = 'ATIVA');

INSERT INTO matriculas (aluno_id, plano_id, data_inicio, data_fim, status, criado_em)
SELECT aluno.id, plano.id, DATEADD('DAY', -15, CURRENT_DATE), DATEADD('DAY', 74, CURRENT_DATE),
       'ATIVA', CURRENT_TIMESTAMP
FROM alunos aluno, planos plano
WHERE aluno.email = 'camila.oliveira@example.com'
  AND plano.nome = 'Plano Trimestral'
  AND NOT EXISTS (SELECT 1 FROM matriculas WHERE aluno_id = aluno.id AND status = 'ATIVA');

INSERT INTO matriculas (aluno_id, plano_id, data_inicio, data_fim, status, criado_em)
SELECT aluno.id, plano.id, DATEADD('DAY', -60, CURRENT_DATE), DATEADD('DAY', -31, CURRENT_DATE),
       'CANCELADA', CURRENT_TIMESTAMP
FROM alunos aluno, planos plano
WHERE aluno.email = 'diego.martins@example.com'
  AND plano.nome = 'Plano Mensal'
  AND NOT EXISTS (SELECT 1 FROM matriculas WHERE aluno_id = aluno.id AND status = 'CANCELADA');

INSERT INTO matriculas (aluno_id, plano_id, data_inicio, data_fim, status, criado_em)
SELECT aluno.id, plano.id, DATEADD('DAY', -120, CURRENT_DATE), DATEADD('DAY', -31, CURRENT_DATE),
       'ENCERRADA', CURRENT_TIMESTAMP
FROM alunos aluno, planos plano
WHERE aluno.email = 'fernanda.lima@example.com'
  AND plano.nome = 'Plano Trimestral'
  AND NOT EXISTS (SELECT 1 FROM matriculas WHERE aluno_id = aluno.id AND status = 'ENCERRADA');

INSERT INTO agendamentos
    (aluno_id, instrutor_id, data_hora, duracao_em_minutos, observacoes, status, criado_em, atualizado_em)
SELECT aluno.id, instrutor.id,
       DATEADD('HOUR', 18, CURRENT_DATE) AT TIME ZONE 'America/Sao_Paulo',
       60, 'Treino de força - membros superiores', 'AGENDADO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM alunos aluno, instrutores instrutor
WHERE aluno.email = 'ana.souza@example.com'
  AND instrutor.email = 'carlos.lima@example.com'
  AND NOT EXISTS (SELECT 1 FROM agendamentos WHERE observacoes = 'Treino de força - membros superiores');

INSERT INTO agendamentos
    (aluno_id, instrutor_id, data_hora, duracao_em_minutos, observacoes, status, criado_em, atualizado_em)
SELECT aluno.id, instrutor.id,
       DATEADD('MINUTE', 1155, CURRENT_DATE) AT TIME ZONE 'America/Sao_Paulo',
       45, 'Avaliação física e treino', 'AGENDADO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM alunos aluno, instrutores instrutor
WHERE aluno.email = 'bruno.costa@example.com'
  AND instrutor.email = 'carlos.lima@example.com'
  AND NOT EXISTS (SELECT 1 FROM agendamentos WHERE observacoes = 'Avaliação física e treino');

INSERT INTO agendamentos
    (aluno_id, instrutor_id, data_hora, duracao_em_minutos, observacoes, status, criado_em, atualizado_em)
SELECT aluno.id, instrutor.id,
       DATEADD('MINUTE', 1110, CURRENT_DATE) AT TIME ZONE 'America/Sao_Paulo',
       50, 'Aula de pilates', 'AGENDADO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM alunos aluno, instrutores instrutor
WHERE aluno.email = 'camila.oliveira@example.com'
  AND instrutor.email = 'juliana.rocha@example.com'
  AND NOT EXISTS (SELECT 1 FROM agendamentos WHERE observacoes = 'Aula de pilates');

INSERT INTO agendamentos
    (aluno_id, instrutor_id, data_hora, duracao_em_minutos, observacoes, status, criado_em, atualizado_em)
SELECT aluno.id, instrutor.id,
       DATEADD('HOUR', 9, DATEADD('DAY', 1, CURRENT_DATE)) AT TIME ZONE 'America/Sao_Paulo',
       60, 'Circuito funcional', 'AGENDADO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM alunos aluno, instrutores instrutor
WHERE aluno.email = 'fernanda.lima@example.com'
  AND instrutor.email = 'rafael.mendes@example.com'
  AND NOT EXISTS (SELECT 1 FROM agendamentos WHERE observacoes = 'Circuito funcional');

INSERT INTO agendamentos
    (aluno_id, instrutor_id, data_hora, duracao_em_minutos, observacoes, status, criado_em, atualizado_em)
SELECT aluno.id, instrutor.id,
       DATEADD('HOUR', 17, DATEADD('DAY', -1, CURRENT_DATE)) AT TIME ZONE 'America/Sao_Paulo',
       50, 'Aula concluída', 'CONCLUIDO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM alunos aluno, instrutores instrutor
WHERE aluno.email = 'ana.souza@example.com'
  AND instrutor.email = 'juliana.rocha@example.com'
  AND NOT EXISTS (SELECT 1 FROM agendamentos WHERE observacoes = 'Aula concluída');

INSERT INTO agendamentos
    (aluno_id, instrutor_id, data_hora, duracao_em_minutos, observacoes, status, criado_em, atualizado_em)
SELECT aluno.id, instrutor.id,
       DATEADD('HOUR', 10, DATEADD('DAY', 2, CURRENT_DATE)) AT TIME ZONE 'America/Sao_Paulo',
       60, 'Sessão cancelada', 'CANCELADO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM alunos aluno, instrutores instrutor
WHERE aluno.email = 'bruno.costa@example.com'
  AND instrutor.email = 'rafael.mendes@example.com'
  AND NOT EXISTS (SELECT 1 FROM agendamentos WHERE observacoes = 'Sessão cancelada');

INSERT INTO alunos (nome, email, telefone, data_nascimento, ativo, criado_em, atualizado_em)
SELECT CASE MOD(amostra.x - 1, 20)
           WHEN 0 THEN 'Ana'
           WHEN 1 THEN 'Beatriz'
           WHEN 2 THEN 'Bruno'
           WHEN 3 THEN 'Camila'
           WHEN 4 THEN 'Daniel'
           WHEN 5 THEN 'Eduardo'
           WHEN 6 THEN 'Fernanda'
           WHEN 7 THEN 'Gabriel'
           WHEN 8 THEN 'Juliana'
       WHEN 9 THEN 'Rafael'
       WHEN 10 THEN 'Amanda'
       WHEN 11 THEN 'Bianca'
       WHEN 12 THEN 'Caio'
       WHEN 13 THEN 'Carolina'
       WHEN 14 THEN 'Felipe'
       WHEN 15 THEN 'Isabela'
       WHEN 16 THEN 'Lucas'
       WHEN 17 THEN 'Mariana'
       WHEN 18 THEN 'Thiago'
       ELSE 'Vanessa'
       END || ' ' ||
     CASE FLOOR((amostra.x - 1) / 20)
           WHEN 0 THEN 'Almeida'
           WHEN 1 THEN 'Barros'
           WHEN 2 THEN 'Carvalho'
           WHEN 3 THEN 'Dias'
           WHEN 4 THEN 'Fernandes'
           WHEN 5 THEN 'Gonçalves'
           WHEN 6 THEN 'Lima'
           WHEN 7 THEN 'Martins'
           WHEN 8 THEN 'Nascimento'
       WHEN 9 THEN 'Oliveira'
       WHEN 10 THEN 'Pereira'
       WHEN 11 THEN 'Ribeiro'
       WHEN 12 THEN 'Rocha'
       WHEN 13 THEN 'Santos'
       WHEN 14 THEN 'Costa'
       WHEN 15 THEN 'Souza'
       WHEN 16 THEN 'Araújo'
       WHEN 17 THEN 'Barbosa'
       WHEN 18 THEN 'Cardoso'
       ELSE 'Teixeira'
       END,
       'amostra.aluno.' || amostra.x || '@example.com',
     CASE WHEN MOD(amostra.x, 13) = 0 THEN NULL ELSE
       '+55 ' || CASE MOD(amostra.x - 1, 10)
         WHEN 0 THEN '11'
         WHEN 1 THEN '21'
         WHEN 2 THEN '31'
         WHEN 3 THEN '41'
         WHEN 4 THEN '51'
         WHEN 5 THEN '61'
         WHEN 6 THEN '71'
         WHEN 7 THEN '81'
         WHEN 8 THEN '85'
         ELSE '91'
       END || ' 9' || RIGHT('0000' || CAST(MOD(amostra.x * 137, 10000) AS VARCHAR), 4)
         || '-' || RIGHT('0000' || CAST(MOD(amostra.x * 251, 10000) AS VARCHAR), 4)
     END,
     DATEADD('DAY', -(6570 + MOD(amostra.x * 173, 17000)), CURRENT_DATE),
     CASE WHEN MOD(amostra.x, 12) = 0 THEN FALSE ELSE TRUE END,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM SYSTEM_RANGE(1, 400) amostra
WHERE NOT EXISTS (
    SELECT 1 FROM alunos
    WHERE email = 'amostra.aluno.' || amostra.x || '@example.com'
);

UPDATE alunos aluno
SET nome = (
    SELECT CASE MOD(amostra.x - 1, 20)
               WHEN 0 THEN 'Ana'
               WHEN 1 THEN 'Beatriz'
               WHEN 2 THEN 'Bruno'
               WHEN 3 THEN 'Camila'
               WHEN 4 THEN 'Daniel'
               WHEN 5 THEN 'Eduardo'
               WHEN 6 THEN 'Fernanda'
               WHEN 7 THEN 'Gabriel'
               WHEN 8 THEN 'Juliana'
           WHEN 9 THEN 'Rafael'
           WHEN 10 THEN 'Amanda'
           WHEN 11 THEN 'Bianca'
           WHEN 12 THEN 'Caio'
           WHEN 13 THEN 'Carolina'
           WHEN 14 THEN 'Felipe'
           WHEN 15 THEN 'Isabela'
           WHEN 16 THEN 'Lucas'
           WHEN 17 THEN 'Mariana'
           WHEN 18 THEN 'Thiago'
           ELSE 'Vanessa'
           END || ' ' ||
         CASE FLOOR((amostra.x - 1) / 20)
               WHEN 0 THEN 'Almeida'
               WHEN 1 THEN 'Barros'
               WHEN 2 THEN 'Carvalho'
               WHEN 3 THEN 'Dias'
               WHEN 4 THEN 'Fernandes'
               WHEN 5 THEN 'Gonçalves'
               WHEN 6 THEN 'Lima'
               WHEN 7 THEN 'Martins'
               WHEN 8 THEN 'Nascimento'
           WHEN 9 THEN 'Oliveira'
           WHEN 10 THEN 'Pereira'
           WHEN 11 THEN 'Ribeiro'
           WHEN 12 THEN 'Rocha'
           WHEN 13 THEN 'Santos'
           WHEN 14 THEN 'Costa'
           WHEN 15 THEN 'Souza'
           WHEN 16 THEN 'Araújo'
           WHEN 17 THEN 'Barbosa'
           WHEN 18 THEN 'Cardoso'
           ELSE 'Teixeira'
           END
    FROM SYSTEM_RANGE(1, 400) amostra
    WHERE aluno.email = 'amostra.aluno.' || amostra.x || '@example.com'
)
WHERE aluno.email LIKE 'amostra.aluno.%@example.com';

INSERT INTO instrutores (nome, email, telefone, especialidade, ativo, criado_em, atualizado_em)
SELECT CASE MOD(amostra.x - 1, 10)
           WHEN 0 THEN 'André'
           WHEN 1 THEN 'Carolina'
           WHEN 2 THEN 'Felipe'
           WHEN 3 THEN 'Isabela'
           WHEN 4 THEN 'João'
           WHEN 5 THEN 'Larissa'
           WHEN 6 THEN 'Marcelo'
           WHEN 7 THEN 'Patrícia'
           WHEN 8 THEN 'Rodrigo'
           ELSE 'Tatiane'
       END || ' ' ||
       CASE FLOOR((amostra.x - 1) / 10)
           WHEN 0 THEN 'Azevedo'
           WHEN 1 THEN 'Campos'
           ELSE 'Ribeiro'
       END,
       'amostra.instrutor.' || amostra.x || '@example.com',
       '+55 21 98800-' || RIGHT('0000' || CAST(amostra.x AS VARCHAR), 4),
       CASE MOD(amostra.x, 5)
           WHEN 0 THEN 'Musculação'
           WHEN 1 THEN 'Pilates'
           WHEN 2 THEN 'Treinamento funcional'
           WHEN 3 THEN 'Yoga'
           ELSE 'Natação'
       END,
       CASE WHEN MOD(amostra.x, 8) = 0 THEN FALSE ELSE TRUE END,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM SYSTEM_RANGE(1, 20) amostra
WHERE NOT EXISTS (
    SELECT 1 FROM instrutores
    WHERE email = 'amostra.instrutor.' || amostra.x || '@example.com'
);

UPDATE instrutores instrutor
SET nome = (
    SELECT CASE MOD(amostra.x - 1, 10)
               WHEN 0 THEN 'André'
               WHEN 1 THEN 'Carolina'
               WHEN 2 THEN 'Felipe'
               WHEN 3 THEN 'Isabela'
               WHEN 4 THEN 'João'
               WHEN 5 THEN 'Larissa'
               WHEN 6 THEN 'Marcelo'
               WHEN 7 THEN 'Patrícia'
               WHEN 8 THEN 'Rodrigo'
               ELSE 'Tatiane'
           END || ' ' ||
           CASE FLOOR((amostra.x - 1) / 10)
               WHEN 0 THEN 'Azevedo'
               WHEN 1 THEN 'Campos'
               ELSE 'Ribeiro'
           END
    FROM SYSTEM_RANGE(1, 20) amostra
    WHERE instrutor.email = 'amostra.instrutor.' || amostra.x || '@example.com'
)
WHERE instrutor.email LIKE 'amostra.instrutor.%@example.com';

UPDATE planos plano
SET nome = (
    SELECT CASE amostra.x
               WHEN 1 THEN 'Plano Essencial'
               WHEN 2 THEN 'Plano Condicionamento 90 Dias'
               WHEN 3 THEN 'Plano Força e Saúde'
               WHEN 4 THEN 'Plano Treino Funcional'
               WHEN 5 THEN 'Plano Bem-Estar Total'
               WHEN 6 THEN 'Plano Performance'
               WHEN 7 THEN 'Plano Pilates Essencial'
               WHEN 8 THEN 'Plano Yoga e Mobilidade'
               WHEN 9 THEN 'Plano Semestral'
               ELSE 'Plano Anual Completo'
           END
    FROM SYSTEM_RANGE(1, 10) amostra
    WHERE plano.nome = 'Plano de amostra ' || amostra.x
)
WHERE plano.nome LIKE 'Plano de amostra %';

INSERT INTO planos (nome, descricao, duracao_em_dias, preco, ativo, criado_em, atualizado_em)
SELECT CASE amostra.x
           WHEN 1 THEN 'Plano Essencial'
           WHEN 2 THEN 'Plano Condicionamento 90 Dias'
           WHEN 3 THEN 'Plano Força e Saúde'
           WHEN 4 THEN 'Plano Treino Funcional'
           WHEN 5 THEN 'Plano Bem-Estar Total'
           WHEN 6 THEN 'Plano Performance'
           WHEN 7 THEN 'Plano Pilates Essencial'
           WHEN 8 THEN 'Plano Yoga e Mobilidade'
           WHEN 9 THEN 'Plano Semestral'
           ELSE 'Plano Anual Completo'
       END,
       'Acesso à academia com atividades e acompanhamento conforme o plano.',
       CASE MOD(amostra.x, 4)
           WHEN 0 THEN 30
           WHEN 1 THEN 90
           WHEN 2 THEN 180
           ELSE 365
       END,
       CAST(79.90 + (amostra.x * 13.50) AS DECIMAL(10, 2)),
       CASE WHEN MOD(amostra.x, 5) = 0 THEN FALSE ELSE TRUE END,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM SYSTEM_RANGE(1, 10) amostra
WHERE NOT EXISTS (
    SELECT 1 FROM planos
    WHERE nome = CASE amostra.x
        WHEN 1 THEN 'Plano Essencial'
        WHEN 2 THEN 'Plano Condicionamento 90 Dias'
        WHEN 3 THEN 'Plano Força e Saúde'
        WHEN 4 THEN 'Plano Treino Funcional'
        WHEN 5 THEN 'Plano Bem-Estar Total'
        WHEN 6 THEN 'Plano Performance'
        WHEN 7 THEN 'Plano Pilates Essencial'
        WHEN 8 THEN 'Plano Yoga e Mobilidade'
        WHEN 9 THEN 'Plano Semestral'
        ELSE 'Plano Anual Completo'
    END
);

INSERT INTO matriculas (aluno_id, plano_id, data_inicio, data_fim, status, criado_em)
SELECT aluno.id, plano.id,
    CASE WHEN aluno.ativo AND plano.ativo AND MOD(amostra.x, 10) NOT IN (0, 1, 2)
      THEN DATEADD('DAY', -MOD(amostra.x * 7, plano.duracao_em_dias), CURRENT_DATE)
      ELSE DATEADD('DAY', -(plano.duracao_em_dias + 30 + MOD(amostra.x * 17, 365)), CURRENT_DATE)
    END,
    DATEADD('DAY', plano.duracao_em_dias - 1,
     CASE WHEN aluno.ativo AND plano.ativo AND MOD(amostra.x, 10) NOT IN (0, 1, 2)
       THEN DATEADD('DAY', -MOD(amostra.x * 7, plano.duracao_em_dias), CURRENT_DATE)
       ELSE DATEADD('DAY', -(plano.duracao_em_dias + 30 + MOD(amostra.x * 17, 365)), CURRENT_DATE)
     END),
    CASE WHEN NOT aluno.ativo OR NOT plano.ativo THEN 'ENCERRADA'
      WHEN MOD(amostra.x, 10) = 0 THEN 'CANCELADA'
      WHEN MOD(amostra.x, 10) = 1 THEN 'ENCERRADA'
      WHEN MOD(amostra.x, 10) = 2 THEN 'VENCIDA'
      ELSE 'ATIVA'
    END,
       CURRENT_TIMESTAMP
FROM SYSTEM_RANGE(1, 400) amostra
JOIN alunos aluno
  ON aluno.email = 'amostra.aluno.' || amostra.x || '@example.com'
JOIN planos plano
  ON plano.nome = CASE (MOD(amostra.x - 1, 10) + 1)
      WHEN 1 THEN 'Plano Essencial'
      WHEN 2 THEN 'Plano Condicionamento 90 Dias'
      WHEN 3 THEN 'Plano Força e Saúde'
      WHEN 4 THEN 'Plano Treino Funcional'
      WHEN 5 THEN 'Plano Bem-Estar Total'
      WHEN 6 THEN 'Plano Performance'
      WHEN 7 THEN 'Plano Pilates Essencial'
      WHEN 8 THEN 'Plano Yoga e Mobilidade'
      WHEN 9 THEN 'Plano Semestral'
      ELSE 'Plano Anual Completo'
  END
WHERE NOT EXISTS (
    SELECT 1 FROM matriculas
    WHERE aluno_id = aluno.id
    AND plano_id = plano.id
);

INSERT INTO agendamentos
    (aluno_id, instrutor_id, data_hora, duracao_em_minutos, observacoes, status, criado_em, atualizado_em)
SELECT aluno.id, instrutor.id,
       DATEADD('MINUTE', 480 + MOD(amostra.x * 37, 600),
         DATEADD('DAY', MOD(amostra.x - 1, 61) - 15, CURRENT_DATE))
           AT TIME ZONE 'America/Sao_Paulo',
       CASE MOD(amostra.x, 4)
           WHEN 0 THEN 30
           WHEN 1 THEN 45
           WHEN 2 THEN 60
           ELSE 90
       END,
     CASE MOD(amostra.x, 8)
       WHEN 0 THEN 'Treino de força - membros superiores'
       WHEN 1 THEN 'Treino de força - membros inferiores'
       WHEN 2 THEN 'Avaliação física'
       WHEN 3 THEN 'Treinamento funcional'
       WHEN 4 THEN 'Pilates individual'
       WHEN 5 THEN 'Mobilidade e alongamento'
       WHEN 6 THEN 'Condicionamento cardiovascular'
       ELSE 'Reavaliação do treino'
     END || ' (ref. ' || RIGHT('0000' || CAST(amostra.x AS VARCHAR), 4) || ')',
     CASE WHEN MOD(amostra.x - 1, 61) - 15 < 0 THEN
       CASE MOD(amostra.x, 10)
         WHEN 0 THEN 'CANCELADO'
         WHEN 1 THEN 'NAO_COMPARECEU'
         ELSE 'CONCLUIDO'
       END
       WHEN MOD(amostra.x, 10) = 0 THEN 'CANCELADO'
       ELSE 'AGENDADO'
     END,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM SYSTEM_RANGE(1, 1200) amostra
JOIN alunos aluno
  ON aluno.email = 'amostra.aluno.' || (MOD(amostra.x - 1, 400) + 1) || '@example.com'
JOIN instrutores instrutor
  ON instrutor.email = 'amostra.instrutor.' || (MOD(amostra.x - 1, 20) + 1) || '@example.com'
WHERE NOT EXISTS (
    SELECT 1 FROM agendamentos
  WHERE observacoes = CASE MOD(amostra.x, 8)
    WHEN 0 THEN 'Treino de força - membros superiores'
    WHEN 1 THEN 'Treino de força - membros inferiores'
    WHEN 2 THEN 'Avaliação física'
    WHEN 3 THEN 'Treinamento funcional'
    WHEN 4 THEN 'Pilates individual'
    WHEN 5 THEN 'Mobilidade e alongamento'
    WHEN 6 THEN 'Condicionamento cardiovascular'
    ELSE 'Reavaliação do treino'
  END || ' (ref. ' || RIGHT('0000' || CAST(amostra.x AS VARCHAR), 4) || ')'
);
