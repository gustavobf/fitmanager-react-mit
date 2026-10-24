import { configureStore } from '@reduxjs/toolkit'
import accessReducer, {
  chaveAcesso,
  chaveUsuario,
  estadoInicialAcesso,
} from './acesso/estadoAcesso'
import type { Usuario } from './acesso/estadoAcesso'

const lerUsuario = (): Usuario => {
  try {
    const salvo: unknown = JSON.parse(localStorage.getItem(chaveUsuario) ?? 'null')
    if (salvo && typeof salvo === 'object' && 'nome' in salvo && typeof salvo.nome === 'string') {
      const nome = salvo.nome.trim()
      if (nome && nome.length <= 80) return { nome }
    }
  } catch {
    return estadoInicialAcesso.usuario
  }
  return estadoInicialAcesso.usuario
}

const lerToken = () => {
  try {
    return sessionStorage.getItem(chaveAcesso)
  } catch {
    return null
  }
}

const tokenSalvo = lerToken()

export const store = configureStore({
  reducer: { acesso: accessReducer },
  preloadedState: {
    acesso: {
      usuario: lerUsuario(),
      acessoLiberado: Boolean(tokenSalvo),
      token: tokenSalvo,
    },
  },
})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch

const salvarUsuario = (usuario: Usuario) => {
  try {
    localStorage.setItem(chaveUsuario, JSON.stringify(usuario))
  } catch {
    return
  }
}

const salvarToken = (token: string | null) => {
  try {
    if (token) sessionStorage.setItem(chaveAcesso, token)
    else sessionStorage.removeItem(chaveAcesso)
  } catch {
    return
  }
}

// A persistência fica fora dos reducers: nome dura no navegador e acesso somente na aba.
let acessoAnterior = store.getState().acesso
store.subscribe(() => {
  const acessoAtual = store.getState().acesso
  if (acessoAtual === acessoAnterior) return
  acessoAnterior = acessoAtual
  salvarUsuario(acessoAtual.usuario)
  salvarToken(acessoAtual.token)
})
