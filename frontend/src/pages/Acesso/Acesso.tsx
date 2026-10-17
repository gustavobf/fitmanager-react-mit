import { useState } from 'react'
import { Button, TextField } from '@mui/material'
import { ArrowForward, FitnessCenter } from '@mui/icons-material'
import { Navigate, useLocation } from 'react-router'
import { useDispatch, useSelector } from 'react-redux'
import { entrar } from '../../features/acesso/estadoAcesso'
import type { DespachoAplicacao, EstadoAplicacao } from '../../app/store'
import './Acesso.css'

export default function Acesso() {
  const { acessoLiberado, usuario } = useSelector((estado: EstadoAplicacao) => estado.acesso)
  const despachar = useDispatch<DespachoAplicacao>()
  const localizacao = useLocation()
  const [erroNome, definirErroNome] = useState('')
  const origem: unknown = localizacao.state?.origem
  const destino =
    typeof origem === 'string' &&
    origem.startsWith('/') &&
    !origem.startsWith('//') &&
    !origem.startsWith('/login')
      ? origem
      : '/'

  if (acessoLiberado) return <Navigate to={destino} replace />

  return (
    <main className="pagina-acesso" id="conteudo-principal">
      <div className="conteudo-acesso">
        <div className="marca">
          <FitnessCenter fontSize="small" />
          <span>
            fitmanager<span className="ponto-marca">.</span>
          </span>
        </div>
        <h1>Acesso ao painel</h1>
        <form
          noValidate
          onSubmit={(evento) => {
            evento.preventDefault()
            const dados = new FormData(evento.currentTarget)
            const nome = String(dados.get('nome') ?? '').trim()
            if (!nome || nome.length > 80) {
              definirErroNome(!nome ? 'Informe seu nome.' : 'Use até 80 caracteres.')
              return
            }
            despachar(entrar(nome))
          }}
        >
          <TextField
            name="nome"
            label="Seu nome"
            defaultValue={usuario.nome}
            required
            autoFocus
            autoComplete="name"
            error={!!erroNome}
            helperText={erroNome || ' '}
            onChange={() => definirErroNome('')}
            slotProps={{ htmlInput: { maxLength: 80 } }}
          />
          <Button type="submit" variant="contained" fullWidth endIcon={<ArrowForward />}>
            Entrar
          </Button>
        </form>
      </div>
    </main>
  )
}
