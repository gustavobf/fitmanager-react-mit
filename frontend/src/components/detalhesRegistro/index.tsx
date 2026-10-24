import { useState } from 'react'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Link, useNavigate, useParams } from 'react-router'
import { Button, Chip, MenuItem, TextField } from '@mui/material'
import { ArrowBack, DeleteOutlined, EditOutlined } from '@mui/icons-material'
import { ConfirmacaoExclusao, AvisoErro, Carregando, CabecalhoPagina } from '../mensagens'
import { useNotification } from '../../app/contexts'
import { alterarSituacao, buscarRegistro, useReferencias } from '../../services/api'
import { recursos } from '../../config/gestao/recursos'
import type { Recurso } from '../../config/gestao/types'
import { formatarValor, rotuloSituacao } from '../../utils/formatacao'
import './styles.css'

export default function DetalhesRegistro({ recurso }: { recurso: Recurso }) {
  const { id } = useParams()
  const configuracao = recursos[recurso]
  const navegar = useNavigate()
  const notificar = useNotification()
  const cliente = useQueryClient()
  const [excluindo, definirExcluindo] = useState(false)
  const consulta = useQuery({
    queryKey: ['registro', recurso, id],
    queryFn: ({ signal: sinal }) => buscarRegistro(recurso, id!, sinal),
  })
  const referencias = useReferencias(configuracao.referencias)
  const mutacao = useMutation({
    mutationFn: (situacao: string) => alterarSituacao(recurso, id!, situacao),
    onSuccess: () => {
      void cliente.invalidateQueries()
      notificar('Situação atualizada com sucesso.')
    },
  })
  if (consulta.isPending) return <Carregando />
  if (consulta.error)
    return (
      <AvisoErro
        erro={consulta.error}
        tentarNovamente={() => {
          void consulta.refetch()
        }}
      />
    )
  const registro = consulta.data
  const campos = [
    ...configuracao.campos,
    ...(recurso === 'matriculas' ? [{ nome: 'dataFim', rotulo: 'Vencimento' }] : []),
  ]
  return (
    <>
      <Button
        component={Link}
        to={`/${recurso}`}
        startIcon={<ArrowBack />}
        className="botao-voltar"
      >
        {configuracao.titulo}
      </Button>
      <CabecalhoPagina
        titulo={registro.nome ? String(registro.nome) : `${configuracao.singular} #${id}`}
        descricao={`Registro #${id}`}
        acao={
          <Button
            component={Link}
            to={`/${recurso}/${id}/editar`}
            variant="outlined"
            startIcon={<EditOutlined />}
          >
            {recurso === 'matriculas' ? 'Alterar situação' : 'Editar cadastro'}
          </Button>
        }
      />
      {referencias.erro && (
        <AvisoErro erro={referencias.erro} tentarNovamente={referencias.tentarNovamente} />
      )}
      {mutacao.error && <AvisoErro erro={mutacao.error} />}
      <section className="secao-detalhes">
        <div className="titulo-secao">
          <h2>Informações gerais</h2>
          {typeof registro.ativo === 'boolean' && (
            <Chip
              size="small"
              variant="outlined"
              color={registro.ativo ? 'success' : 'default'}
              label={registro.ativo ? 'Ativo' : 'Inativo'}
            />
          )}
        </div>
        <dl className="grade-detalhes">
          {campos
            .filter(({ nome }) => nome !== 'ativo')
            .map(({ nome, rotulo }) => (
              <div key={nome}>
                <dt>{rotulo}</dt>
                <dd>{formatarValor(nome, registro[nome], referencias.dados)}</dd>
              </div>
            ))}
        </dl>
        {configuracao.situacoes && (
          <div className="situacao-registro">
            <TextField
              select
              label="Situação"
              value={registro.status ?? ''}
              disabled={mutacao.isPending}
              onChange={(evento) => mutacao.mutate(evento.target.value)}
            >
              {configuracao.situacoes.map((situacao) => (
                <MenuItem key={situacao} value={situacao}>
                  {rotuloSituacao(situacao)}
                </MenuItem>
              ))}
            </TextField>
          </div>
        )}
        <div className="metadados">
          {registro.criadoEm && (
            <span>Criado em {formatarValor('criadoEm', registro.criadoEm)}</span>
          )}
          {registro.atualizadoEm && (
            <span>Atualizado em {formatarValor('atualizadoEm', registro.atualizadoEm)}</span>
          )}
        </div>
      </section>
      {recurso === 'alunos' && (
        <div className="acoes-relacionadas">
          <Button component={Link} to="/matriculas">
            Ver matrículas
          </Button>
          <Button component={Link} to="/agendamentos">
            Ver agendamentos
          </Button>
        </div>
      )}
      {recurso !== 'matriculas' && (
        <div className="area-exclusao">
          <div>
            <h2>Excluir cadastro</h2>
            <p>A exclusao permanente depende da ausencia de vínculos no histórico.</p>
          </div>
          <Button
            color="error"
            variant="outlined"
            startIcon={<DeleteOutlined />}
            onClick={() => definirExcluindo(true)}
          >
            Excluir
          </Button>
        </div>
      )}
      {excluindo && (
        <ConfirmacaoExclusao
          recurso={recurso}
          registro={registro}
          aoFechar={() => definirExcluindo(false)}
          aoExcluir={() => navegar(`/${recurso}`, { replace: true })}
        />
      )}
    </>
  )
}
