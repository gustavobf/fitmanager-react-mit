export type Recurso = 'alunos' | 'instrutores' | 'planos' | 'matriculas' | 'agendamentos'
export type Valor = string | number | boolean | null
export type Registro = { id: number } & Record<string, Valor>
export interface PaginaRegistros {
  conteudo: Registro[]
  pagina: number
  tamanho: number
  totalElementos: number
  totalPaginas: number
}
export interface CampoFormulario {
  nome: string
  rotulo: string
  tipo?: 'email' | 'tel' | 'date' | 'datetime-local' | 'number' | 'textarea' | 'boolean' | 'select'
  obrigatorio?: boolean
  minimo?: number
  maximo?: number
  tamanhoMaximo?: number
  incremento?: number
  referencia?: Recurso
  opcoes?: string[]
}
interface ConfiguracaoRecurso {
  titulo: string
  singular: string
  descricao: string
  campos: CampoFormulario[]
  colunas: { chave: string; rotulo: string }[]
  situacoes?: string[]
  referencias: Recurso[]
}

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

export const rotuloSituacao = (valor: string) =>
  ({
    ATIVA: 'Ativa',
    ENCERRADA: 'Encerrada',
    CANCELADA: 'Cancelada',
    VENCIDA: 'Vencida',
    AGENDADO: 'Agendado',
    CONCLUIDO: 'Concluído',
    CANCELADO: 'Cancelado',
    NAO_COMPARECEU: 'Não compareceu',
  })[valor] ?? valor
export const ehRecurso = (valor: string | undefined): valor is Recurso =>
  !!valor && Object.hasOwn(recursos, valor)

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

export const montarDadosEnvio = (campos: CampoFormulario[], valores: Record<string, Valor>) =>
  Object.fromEntries(
    campos.map(({ nome, tipo }) => {
      const original = valores[nome]
      const valor = typeof original === 'string' ? original.trim() : original
      return [
        nome,
        tipo === 'number' || nome.endsWith('Id')
          ? Number(valor)
          : tipo === 'boolean'
            ? Boolean(valor)
            : valor === ''
              ? null
              : valor,
      ]
    }),
  )

export const obterDataHoraLocal = (valor: string) => {
  // O backend espera o horário local de São Paulo, sem offset no valor enviado pelo formulário.
  const partes = new Intl.DateTimeFormat('sv-SE', {
    timeZone: 'America/Sao_Paulo',
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    hourCycle: 'h23',
  })
    .format(new Date(valor))
    .split(' ')
  return `${partes[0]}T${partes[1]}`
}

export const formatarValor = (
  chave: string,
  valor: Valor | undefined,
  referencias: Partial<Record<Recurso, Registro[]>> = {},
): string => {
  if (valor === null || valor === undefined || valor === '') return 'Não informado'
  const referencia = (
    { alunoId: 'alunos', planoId: 'planos', instrutorId: 'instrutores' } as const
  )[chave as 'alunoId']
  if (referencia)
    return String(
      referencias[referencia]?.find(({ id }) => id === Number(valor))?.nome ?? `#${valor}`,
    )
  if (chave === 'preco')
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(
      Number(valor),
    )
  if (chave === 'duracaoEmDias') return `${valor} dias`
  if (chave === 'duracaoEmMinutos') return `${valor} min`
  if (chave === 'status') return rotuloSituacao(String(valor))
  if (typeof valor === 'boolean') return valor ? 'Ativo' : 'Inativo'
  if (/^(data|criadoEm|atualizadoEm)/.test(chave)) {
    const data = String(valor)
    return new Intl.DateTimeFormat('pt-BR', {
      dateStyle: 'short',
      ...(data.includes('T')
        ? { timeStyle: 'short' as const, timeZone: 'America/Sao_Paulo' }
        : { timeZone: 'UTC' }),
    }).format(new Date(data))
  }
  return String(valor)
}
