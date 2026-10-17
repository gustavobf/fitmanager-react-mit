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
import { ApiError as ErroApi } from '../../lib/http'
import { excluirRegistro } from '../../features/gestao/api'
import type { Registro, Recurso } from '../../features/gestao/recursos'
import { recursos } from '../../features/gestao/recursos'
import { useNotificacao } from '../../app/contextos'
import './Mensagens.css'

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
  const notificar = useNotificacao()
  const mutacao = useMutation({
    mutationFn: () => excluirRegistro(recurso, registro!.id),
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
            <AvisoErro erro={mutacao.error} />
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
          onClick={() => mutacao.mutate()}
        >
          {mutacao.isPending ? 'Excluindo...' : 'Excluir registro'}
        </Button>
      </DialogActions>
    </Dialog>
  )
}
