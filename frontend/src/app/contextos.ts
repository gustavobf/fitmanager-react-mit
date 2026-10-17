import { createContext, useContext } from 'react'

export const ContextoNotificacao = createContext<(mensagem: string) => void>(() => undefined)
export const useNotificacao = () => useContext(ContextoNotificacao)
