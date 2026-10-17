import { configureStore } from '@reduxjs/toolkit'
import redutorAcesso, {
  chaveAcesso,
  chaveUsuario,
  estadoInicialAcesso,
} from '../features/acesso/estadoAcesso'
import type { Usuario } from '../features/acesso/estadoAcesso'

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

const lerAcesso = () => {
  try {
    return sessionStorage.getItem(chaveAcesso) === 'liberado'
  } catch {
    return false
  }
}

export const store = configureStore({
  reducer: { acesso: redutorAcesso },
  preloadedState: {
    acesso: { usuario: lerUsuario(), acessoLiberado: lerAcesso() },
  },
})

export type EstadoAplicacao = ReturnType<typeof store.getState>
export type DespachoAplicacao = typeof store.dispatch

const salvarUsuario = (usuario: Usuario) => {
  try {
    localStorage.setItem(chaveUsuario, JSON.stringify(usuario))
  } catch {
    return
  }
}

const salvarAcesso = (acessoLiberado: boolean) => {
  try {
    if (acessoLiberado) sessionStorage.setItem(chaveAcesso, 'liberado')
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
  salvarAcesso(acessoAtual.acessoLiberado)
})
