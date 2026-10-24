import { useState } from 'react'
import { Button, TextField } from '@mui/material'
import { ArrowForward, FitnessCenter } from '@mui/icons-material'
import { Navigate, useLocation } from 'react-router'
import { useDispatch, useSelector } from 'react-redux'
import { entrar } from '../../app/acesso/estadoAcesso'
import type { AppDispatch, RootState } from '../../app/store'
import { fazerLogin } from '../../services/api'
import './styles.css'

export default function Acesso() {
  const acessoLiberado = useSelector((estado: RootState) => estado.acesso.acessoLiberado)
  const dispatch = useDispatch<AppDispatch>()
  const localizacao = useLocation()
  const [erroLogin, definirErroLogin] = useState('')
  const [carregando, definirCarregando] = useState(false)
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
          onSubmit={async (evento) => {
            evento.preventDefault()
            const dados = new FormData(evento.currentTarget)
            const login = String(dados.get('login') ?? '').trim()
            const senha = String(dados.get('senha') ?? '')
            if (!login || !senha.trim()) {
              definirErroLogin('Informe seu login e sua senha.')
              return
            }
            definirCarregando(true)
            definirErroLogin('')
            try {
              const resposta = await fazerLogin(login, senha)
              dispatch(entrar({ nome: resposta.nome, token: resposta.token }))
            } catch (erro) {
              definirErroLogin(
                erro instanceof Error ? erro.message : 'Não foi possível entrar. Tente novamente.',
              )
            } finally {
              definirCarregando(false)
            }
          }}
        >
          <TextField
            name="login"
            label="Login"
            required
            autoFocus
            autoComplete="username"
            error={!!erroLogin}
            helperText={erroLogin || ' '}
            onChange={() => definirErroLogin('')}
          />
          <TextField
            name="senha"
            label="Senha"
            type="password"
            required
            autoComplete="current-password"
            error={!!erroLogin}
            onChange={() => definirErroLogin('')}
          />
          <Button
            type="submit"
            variant="contained"
            fullWidth
            endIcon={<ArrowForward />}
            disabled={carregando}
          >
            {carregando ? 'Entrando...' : 'Entrar'}
          </Button>
        </form>
      </div>
    </main>
  )
}
