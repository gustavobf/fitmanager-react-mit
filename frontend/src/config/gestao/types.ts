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

export interface ConfiguracaoRecurso {
  titulo: string
  singular: string
  descricao: string
  campos: CampoFormulario[]
  colunas: { chave: string; rotulo: string }[]
  situacoes?: string[]
  referencias: Recurso[]
}