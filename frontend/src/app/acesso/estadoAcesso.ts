import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

export interface Usuario {
  nome: string
}

export interface EstadoAcesso {
  usuario: Usuario
  acessoLiberado: boolean
  token: string | null
}

export interface CredenciaisAcesso {
  nome: string
  token: string
}

export const chaveUsuario = 'fitmanager.usuario'
export const chaveAcesso = 'fitmanager.token'

export const estadoInicialAcesso: EstadoAcesso = {
  usuario: { nome: 'Usuário' },
  acessoLiberado: false,
  token: null,
}

const accessSlice = createSlice({
  name: 'acesso',
  initialState: estadoInicialAcesso,
  reducers: {
    entrar: (estado, acao: PayloadAction<CredenciaisAcesso>) => {
      // O Immer do Redux Toolkit converte estas atribuições em atualizações imutáveis.
      estado.usuario = { nome: acao.payload.nome }
      estado.acessoLiberado = true
      estado.token = acao.payload.token
    },
    sair: (estado) => {
      estado.acessoLiberado = false
      estado.token = null
    },
  },
})

export const { entrar, sair } = accessSlice.actions
export default accessSlice.reducer