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
