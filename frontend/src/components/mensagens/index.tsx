import type { ReactNode } from 'react'
import {
  Alert,
  AlertTitle,
  Button,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from '@mui/material'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { ApiError as ErroApi } from '../../services/http'
import { excluirRegistro } from '../../services/api'
import type { Registro, Recurso } from '../../config/gestao/types'
import { recursos } from '../../config/gestao/recursos'
import { useNotification } from '../../app/contexts'
import './styles.css'

export function AvisoErro({
  erro,
  tentarNovamente,
}: {
  erro: Error
  tentarNovamente?: () => void
}) {
  return (
    <Alert
      severity="error"
      action={
        tentarNovamente ? (
          <Button color="inherit" onClick={tentarNovamente}>
            Tentar novamente
          </Button>
        ) : undefined
      }
    >
      <AlertTitle>Não foi possível concluir</AlertTitle>
      {erro.message}
      {erro instanceof ErroApi && erro.details.length > 0 && (
        <ul>
          {erro.details.map((detalhe, indice) => (
            <li key={`${indice}-${detalhe}`}>{detalhe}</li>
          ))}
        </ul>
      )}
    </Alert>
  )
}

export function Carregando() {
  return (
    <div className="estado-carregamento" role="status">
      <CircularProgress size={26} />
      <span>Carregando dados...</span>
    </div>
  )
}

export function CabecalhoPagina({
  titulo,
  descricao,
  acao,
}: {
  titulo: string
  descricao?: string
  acao?: ReactNode
}) {
  return (
    <div className="cabecalho-pagina">
      <div>
        <h1>{titulo}</h1>
        {descricao && <p>{descricao}</p>}
      </div>
      {acao && <div className="acao-cabecalho">{acao}</div>}
    </div>
  )
}

export function ConfirmacaoExclusao({
  recurso,
  registro,
  aoFechar,
  aoExcluir,
}: {
  recurso: Recurso
  registro: Registro | null
  aoFechar: () => void
  aoExcluir?: () => void
}) {
  const cliente = useQueryClient()
  const notificar = useNotification()
  const mutacao = useMutation<void, Error, boolean>({
    mutationFn: (apagarHistorico) => excluirRegistro(recurso, registro!.id, apagarHistorico),
    onSuccess: () => {
      // Não consulta novamente o detalhe excluído: essa busca retornaria um erro 404.
      void cliente.invalidateQueries({
        queryKey: ['registro', recurso, String(registro!.id)],
        exact: true,
        refetchType: 'none',
      })
      void cliente.invalidateQueries({
        predicate: ({ queryKey: chaveConsulta }) => chaveConsulta[0] !== 'registro',
      })
      notificar(`${recursos[recurso].singular} excluído com sucesso.`)
      aoFechar()
      aoExcluir?.()
    },
  })
  const confirmarExclusaoHistorico =
    recurso === 'alunos' && mutacao.error instanceof ErroApi && mutacao.error.status === 409
  return (
    <Dialog
      open={!!registro}
      onClose={mutacao.isPending ? undefined : aoFechar}
      fullWidth
      maxWidth="xs"
      aria-labelledby="titulo-exclusao"
    >
      <DialogTitle id="titulo-exclusao">
        Excluir {recursos[recurso].singular.toLowerCase()}?
      </DialogTitle>
      <DialogContent>
        <DialogContentText>{`O registro ${registro?.nome ? `"${registro.nome}"` : `#${registro?.id}`} será excluído permanentemente. Esta ação não pode ser desfeita.`}</DialogContentText>
        {mutacao.error && (
          <div className="espaco-mensagem">
            {confirmarExclusaoHistorico ? (
              <Alert severity="warning">
                <AlertTitle>Histórico encontrado</AlertTitle>
                Ao continuar, o aluno e todas as matrículas e agendamentos relacionados serão
                apagados permanentemente. Essa ação não pode ser desfeita.
              </Alert>
            ) : (
              <AvisoErro erro={mutacao.error} />
            )}
          </div>
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={aoFechar} disabled={mutacao.isPending}>
          Cancelar
        </Button>
        <Button
          color="error"
          variant="contained"
          disabled={mutacao.isPending}
          onClick={() => mutacao.mutate(confirmarExclusaoHistorico)}
        >
          {mutacao.isPending
            ? 'Excluindo...'
            : confirmarExclusaoHistorico
              ? 'Excluir aluno e histórico'
              : 'Excluir registro'}
        </Button>
      </DialogActions>
    </Dialog>
  )
}
