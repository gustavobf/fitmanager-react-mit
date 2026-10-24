import type { Recurso, Registro, Valor } from '../config/gestao/types'

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