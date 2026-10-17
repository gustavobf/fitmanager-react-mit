import { useDeferredValue, useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { Link, useSearchParams } from 'react-router'
import {
  Button,
  Chip,
  IconButton,
  InputAdornment,
  MenuItem,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  TextField,
  Tooltip,
} from '@mui/material'
import {
  Add,
  ArrowForward,
  DeleteOutlined,
  EditOutlined,
  Refresh,
  Search,
} from '@mui/icons-material'
import { ConfirmacaoExclusao, AvisoErro, Carregando, CabecalhoPagina } from '../Mensagens/Mensagens'
import { listarRegistros, useReferencias } from '../../features/gestao/api'
import { formatarValor, recursos, rotuloSituacao } from '../../features/gestao/recursos'
import type { Registro, Recurso } from '../../features/gestao/recursos'
import './ListaRegistros.css'

export default function ListaRegistros({ recurso }: { recurso: Recurso }) {
  const configuracao = recursos[recurso]
  const [parametros, definirParametros] = useSearchParams()
  const [excluindo, definirExcluindo] = useState<Registro | null>(null)
  const busca = parametros.get('nome') ?? ''
  const filtro = parametros.get('status') ?? parametros.get('ativo') ?? ''
  const paginaSolicitada = Number(parametros.get('pagina') ?? 0)
  const pagina = Number.isInteger(paginaSolicitada) && paginaSolicitada >= 0 ? paginaSolicitada : 0
  const buscaAdiada = useDeferredValue(busca)
  const parametrosConsulta = new URLSearchParams({ pagina: String(pagina), tamanho: '10' })
  if (buscaAdiada && !configuracao.situacoes) parametrosConsulta.set('nome', buscaAdiada)
  if (filtro) parametrosConsulta.set(configuracao.situacoes ? 'status' : 'ativo', filtro)
  const consulta = useQuery({
    queryKey: ['registros', recurso, parametrosConsulta.toString()],
    queryFn: ({ signal: sinal }) => listarRegistros(recurso, parametrosConsulta, sinal),
  })
  const referencias = useReferencias(configuracao.referencias)
  const alterarFiltro = (chave: string, valor: string) =>
    definirParametros(
      (anterior) => {
        const proximo = new URLSearchParams(anterior)
        if (valor) proximo.set(chave, valor)
        else proximo.delete(chave)
        proximo.delete('pagina')
        return proximo
      },
      { replace: true },
    )

  return (
    <>
      <CabecalhoPagina
        titulo={configuracao.titulo}
        descricao={configuracao.descricao}
        acao={
          <Button variant="contained" startIcon={<Add />} component={Link} to={`/${recurso}/novo`}>
            Novo cadastro
          </Button>
        }
      />
      <div className="barra-filtros">
        {!configuracao.situacoes && (
          <TextField
            label="Buscar por nome"
            value={busca}
            onChange={(evento) => alterarFiltro('nome', evento.target.value)}
            slotProps={{
              input: {
                startAdornment: (
                  <InputAdornment position="start">
                    <Search fontSize="small" />
                  </InputAdornment>
                ),
              },
            }}
          />
        )}
        <TextField
          select
          label="Situação"
          value={filtro}
          onChange={(evento) =>
            alterarFiltro(configuracao.situacoes ? 'status' : 'ativo', evento.target.value)
          }
          className="filtro-situacao"
        >
          <MenuItem value="">Todas</MenuItem>
          {configuracao.situacoes
            ? configuracao.situacoes.map((situacao) => (
                <MenuItem value={situacao} key={situacao}>
                  {rotuloSituacao(situacao)}
                </MenuItem>
              ))
            : [
                <MenuItem key="true" value="true">
                  Ativos
                </MenuItem>,
                <MenuItem key="false" value="false">
                  Inativos
                </MenuItem>,
              ]}
        </TextField>
        <Tooltip title="Atualizar lista">
          <span>
            <IconButton
              aria-label="Atualizar lista"
              disabled={consulta.isFetching}
              onClick={() => {
                void consulta.refetch()
              }}
            >
              <Refresh fontSize="small" />
            </IconButton>
          </span>
        </Tooltip>
      </div>
      {referencias.erro && (
        <AvisoErro erro={referencias.erro} tentarNovamente={referencias.tentarNovamente} />
      )}
      {consulta.isPending ? (
        <Carregando />
      ) : consulta.error ? (
        <AvisoErro
          erro={consulta.error}
          tentarNovamente={() => {
            void consulta.refetch()
          }}
        />
      ) : (
        <section
          className="area-tabela"
          aria-label={`Lista de ${configuracao.titulo.toLowerCase()}`}
        >
          <div className="resumo-tabela">
            <span>
              {consulta.data.totalElementos}{' '}
              {consulta.data.totalElementos === 1 ? 'registro' : 'registros'}
            </span>
            {consulta.isFetching && <span>Atualizando...</span>}
          </div>
          {consulta.data.conteudo.length === 0 ? (
            <div className="estado-vazio">
              <h2>{pagina > 0 ? 'Nenhum registro nesta página' : 'Nenhum registro encontrado'}</h2>
              <p>
                {busca || filtro
                  ? 'Nenhum resultado corresponde aos filtros selecionados.'
                  : 'Os cadastros aparecerão aqui.'}
              </p>
              <Button onClick={() => definirParametros({})}>Limpar filtros</Button>
            </div>
          ) : (
            <TableContainer>
              <Table size="small">
                <TableHead>
                  <TableRow>
                    {configuracao.colunas.map(({ chave, rotulo }) => (
                      <TableCell key={chave}>{rotulo}</TableCell>
                    ))}
                    <TableCell align="right">Ações</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {consulta.data.conteudo.map((registro) => (
                    <TableRow key={registro.id} hover>
                      {configuracao.colunas.map(({ chave }, indice) => (
                        <TableCell key={chave}>
                          {chave === 'ativo' || chave === 'status' ? (
                            <Chip
                              size="small"
                              variant="outlined"
                              color={
                                registro.ativo === true ||
                                ['ATIVA', 'AGENDADO', 'CONCLUIDO'].includes(String(registro.status))
                                  ? 'success'
                                  : 'default'
                              }
                              label={formatarValor(chave, registro[chave])}
                            />
                          ) : indice === 0 ? (
                            <Link className="link-registro" to={`/${recurso}/${registro.id}`}>
                              {formatarValor(chave, registro[chave], referencias.dados)}
                            </Link>
                          ) : (
                            formatarValor(chave, registro[chave], referencias.dados)
                          )}
                        </TableCell>
                      ))}
                      <TableCell align="right">
                        <div className="acoes-linha">
                          <Tooltip title="Ver detalhes">
                            <IconButton
                              aria-label={`Ver detalhes do registro ${registro.id}`}
                              size="small"
                              component={Link}
                              to={`/${recurso}/${registro.id}`}
                            >
                              <ArrowForward fontSize="small" />
                            </IconButton>
                          </Tooltip>
                          <Tooltip title={recurso === 'matriculas' ? 'Alterar situação' : 'Editar'}>
                            <IconButton
                              aria-label={`Editar registro ${registro.id}`}
                              size="small"
                              component={Link}
                              to={`/${recurso}/${registro.id}/editar`}
                            >
                              <EditOutlined fontSize="small" />
                            </IconButton>
                          </Tooltip>
                          {recurso !== 'matriculas' && (
                            <Tooltip title="Excluir">
                              <IconButton
                                aria-label={`Excluir registro ${registro.id}`}
                                size="small"
                                onClick={() => definirExcluindo(registro)}
                              >
                                <DeleteOutlined fontSize="small" />
                              </IconButton>
                            </Tooltip>
                          )}
                        </div>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          )}
          <TablePagination
            component="div"
            count={consulta.data.totalElementos}
            page={pagina}
            rowsPerPage={10}
            rowsPerPageOptions={[10]}
            onPageChange={(_, proximaPagina) =>
              definirParametros((anterior) => {
                const proximo = new URLSearchParams(anterior)
                proximo.set('pagina', String(proximaPagina))
                return proximo
              })
            }
          />
        </section>
      )}
      {excluindo && (
        <ConfirmacaoExclusao
          recurso={recurso}
          registro={excluindo}
          aoFechar={() => definirExcluindo(null)}
        />
      )}
    </>
  )
}
