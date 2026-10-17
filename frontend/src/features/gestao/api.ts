import { useQueries } from '@tanstack/react-query'
import { request as requisitar } from '../../lib/http'
import type { PaginaRegistros, Registro, Recurso, Valor } from './recursos'

export const listarRegistros = (
  recurso: Recurso,
  parametros: URLSearchParams,
  sinal?: AbortSignal,
) => requisitar<PaginaRegistros>(`/api/${recurso}?${parametros}`, { signal: sinal })
export const buscarRegistro = (recurso: Recurso, id: string, sinal?: AbortSignal) =>
  requisitar<Registro>(`/api/${recurso}/${id}`, { signal: sinal })
export const salvarRegistro = (recurso: Recurso, dadosEnvio: Record<string, Valor>, id?: string) =>
  requisitar<Registro>(`/api/${recurso}${id ? `/${id}` : ''}`, {
    method: id ? (recurso === 'matriculas' ? 'PATCH' : 'PUT') : 'POST',
    body: JSON.stringify(dadosEnvio),
  })
export const excluirRegistro = (recurso: Recurso, id: number) =>
  requisitar<void>(`/api/${recurso}/${id}`, { method: 'DELETE' })
export const alterarSituacao = (recurso: Recurso, id: string, situacao: string) =>
  requisitar<Registro>(`/api/${recurso}/${id}`, {
    method: 'PATCH',
    body: JSON.stringify({ status: situacao }),
  })

const listarTodosRegistros = async (recurso: Recurso, sinal?: AbortSignal) => {
  const primeiro = await listarRegistros(
    recurso,
    new URLSearchParams({ tamanho: '100', pagina: '0' }),
    sinal,
  )
  const registros = [...primeiro.conteudo]
  for (let pagina = 1; pagina < primeiro.totalPaginas; pagina += 1) {
    const proximo = await listarRegistros(
      recurso,
      new URLSearchParams({ tamanho: '100', pagina: String(pagina) }),
      sinal,
    )
    registros.push(...proximo.conteudo)
  }
  return registros
}

export const useReferencias = (nomes: Recurso[]) => {
  const consultas = useQueries({
    queries: nomes.map((nome) => ({
      queryKey: ['referencias', nome],
      queryFn: ({ signal: sinal }: { signal: AbortSignal }) => listarTodosRegistros(nome, sinal),
      staleTime: 60000,
    })),
  })
  return {
    dados: Object.fromEntries(
      nomes.map((nome, indice) => [nome, consultas[indice].data ?? []]),
    ) as Partial<Record<Recurso, Registro[]>>,
    carregando: consultas.some((consulta) => consulta.isPending),
    erro: consultas.find((consulta) => consulta.error)?.error,
    tentarNovamente: () =>
      consultas.forEach((consulta) => {
        void consulta.refetch()
      }),
  }
}
