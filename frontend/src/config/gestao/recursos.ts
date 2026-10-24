import type { CampoFormulario, ConfiguracaoRecurso, Recurso } from './types'

const camposPessoa: CampoFormulario[] = [
  { nome: 'nome', rotulo: 'Nome completo', obrigatorio: true, minimo: 2, tamanhoMaximo: 120 },
  { nome: 'email', rotulo: 'E-mail', tipo: 'email', obrigatorio: true, tamanhoMaximo: 160 },
  { nome: 'telefone', rotulo: 'Telefone', tipo: 'tel', tamanhoMaximo: 25 },
]
const campoAtivo: CampoFormulario = { nome: 'ativo', rotulo: 'Cadastro ativo', tipo: 'boolean' }
const colunasPessoa = [
  { chave: 'nome', rotulo: 'Nome' },
  { chave: 'email', rotulo: 'E-mail' },
  { chave: 'telefone', rotulo: 'Telefone' },
]

export const recursos: Record<Recurso, ConfiguracaoRecurso> = {
  alunos: {
    titulo: 'Alunos',
    singular: 'Aluno',
    descricao: 'Cadastros e contatos dos alunos.',
    campos: [
      ...camposPessoa,
      { nome: 'dataNascimento', rotulo: 'Data de nascimento', tipo: 'date' },
      campoAtivo,
    ],
    colunas: [...colunasPessoa, { chave: 'ativo', rotulo: 'Situação' }],
    referencias: [],
  },
  instrutores: {
    titulo: 'Instrutores',
    singular: 'Instrutor',
    descricao: 'Profissionais que acompanham cada treino.',
    campos: [
      ...camposPessoa,
      { nome: 'especialidade', rotulo: 'Especialidade', obrigatorio: true, tamanhoMaximo: 100 },
      campoAtivo,
    ],
    colunas: [
      { chave: 'nome', rotulo: 'Nome' },
      { chave: 'especialidade', rotulo: 'Especialidade' },
      { chave: 'email', rotulo: 'E-mail' },
      { chave: 'ativo', rotulo: 'Situação' },
    ],
    referencias: [],
  },
  planos: {
    titulo: 'Planos',
    singular: 'Plano',
    descricao: 'Valores e condições de acesso à academia.',
    campos: [
      { nome: 'nome', rotulo: 'Nome do plano', obrigatorio: true, minimo: 2, tamanhoMaximo: 80 },
      { nome: 'descricao', rotulo: 'Descrição', tipo: 'textarea', tamanhoMaximo: 500 },
      {
        nome: 'duracaoEmDias',
        rotulo: 'Duração em dias',
        tipo: 'number',
        obrigatorio: true,
        minimo: 1,
        maximo: 730,
      },
      {
        nome: 'preco',
        rotulo: 'Preço (R$)',
        tipo: 'number',
        obrigatorio: true,
        minimo: 0,
        incremento: 0.01,
      },
      campoAtivo,
    ],
    colunas: [
      { chave: 'nome', rotulo: 'Plano' },
      { chave: 'duracaoEmDias', rotulo: 'Duração' },
      { chave: 'preco', rotulo: 'Valor' },
      { chave: 'ativo', rotulo: 'Situação' },
    ],
    referencias: [],
  },
  matriculas: {
    titulo: 'Matrículas',
    singular: 'Matrícula',
    descricao: 'Vínculos entre alunos e planos da academia.',
    campos: [
      { nome: 'alunoId', rotulo: 'Aluno', tipo: 'select', referencia: 'alunos', obrigatorio: true },
      { nome: 'planoId', rotulo: 'Plano', tipo: 'select', referencia: 'planos', obrigatorio: true },
      { nome: 'dataInicio', rotulo: 'Data de início', tipo: 'date', obrigatorio: true },
    ],
    colunas: [
      { chave: 'alunoId', rotulo: 'Aluno' },
      { chave: 'planoId', rotulo: 'Plano' },
      { chave: 'dataInicio', rotulo: 'Início' },
      { chave: 'dataFim', rotulo: 'Vencimento' },
      { chave: 'status', rotulo: 'Situação' },
    ],
    situacoes: ['ATIVA', 'ENCERRADA', 'CANCELADA', 'VENCIDA'],
    referencias: ['alunos', 'planos'],
  },
  agendamentos: {
    titulo: 'Agendamentos',
    singular: 'Agendamento',
    descricao: 'Organize aulas e sessões de treinamento.',
    campos: [
      { nome: 'alunoId', rotulo: 'Aluno', tipo: 'select', referencia: 'alunos', obrigatorio: true },
      {
        nome: 'instrutorId',
        rotulo: 'Instrutor',
        tipo: 'select',
        referencia: 'instrutores',
        obrigatorio: true,
      },
      {
        nome: 'dataHora',
        rotulo: 'Data e hora (São Paulo)',
        tipo: 'datetime-local',
        obrigatorio: true,
      },
      {
        nome: 'duracaoEmMinutos',
        rotulo: 'Duração em minutos',
        tipo: 'number',
        obrigatorio: true,
        minimo: 15,
        maximo: 240,
      },
      { nome: 'observacoes', rotulo: 'Observações', tipo: 'textarea', tamanhoMaximo: 500 },
    ],
    colunas: [
      { chave: 'dataHora', rotulo: 'Data e hora' },
      { chave: 'alunoId', rotulo: 'Aluno' },
      { chave: 'instrutorId', rotulo: 'Instrutor' },
      { chave: 'duracaoEmMinutos', rotulo: 'Duração' },
      { chave: 'status', rotulo: 'Situação' },
    ],
    situacoes: ['AGENDADO', 'CONCLUIDO', 'CANCELADO', 'NAO_COMPARECEU'],
    referencias: ['alunos', 'instrutores'],
  },
}

export const obterCamposEditaveis = (recurso: Recurso, editando: boolean): CampoFormulario[] =>
  recurso === 'matriculas' && editando
    ? [
        {
          nome: 'status',
          rotulo: 'Situação da matrícula',
          tipo: 'select',
          obrigatorio: true,
          opcoes: recursos.matriculas.situacoes,
        },
      ]
    : recursos[recurso].campos