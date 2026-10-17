import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

export interface Usuario {
  nome: string
}

export interface EstadoAcesso {
  usuario: Usuario
  acessoLiberado: boolean
}

export const chaveUsuario = 'fitmanager.usuario'
export const chaveAcesso = 'fitmanager.acesso'

export const estadoInicialAcesso: EstadoAcesso = {
  usuario: { nome: 'Usuário' },
  acessoLiberado: false,
}

const fatiaAcesso = createSlice({
  name: 'acesso',
  initialState: estadoInicialAcesso,
  reducers: {
    entrar: (estado, acao: PayloadAction<string>) => {
      const nome = acao.payload.trim()
      if (!nome || nome.length > 80) return

      // O Immer do Redux Toolkit converte estas atribuições em atualizações imutáveis.
      estado.usuario = { nome }
      estado.acessoLiberado = true
    },
    sair: (estado) => {
      estado.acessoLiberado = false
    },
  },
})

export const { entrar, sair } = fatiaAcesso.actions
export default fatiaAcesso.reducer
