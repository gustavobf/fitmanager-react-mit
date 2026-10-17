import { useEffect, useState } from 'react'
import type { ReactNode } from 'react'
import { Alert, CssBaseline, Snackbar, ThemeProvider, createTheme } from '@mui/material'
import { ptBR } from '@mui/material/locale'
import { QueryClient, QueryClientProvider, useQueryClient } from '@tanstack/react-query'
import { Provider as ProvedorRedux, useSelector } from 'react-redux'
import { ApiError as ErroApi } from '../lib/http'
import LimiteErro from '../components/LimiteErro/LimiteErro'
import { ContextoNotificacao } from './contextos'
import { store } from './store'
import type { EstadoAplicacao } from './store'

// Redux mantém o acesso; React Query mantém o cache remoto, sem duplicar os registros na store.
const clienteConsultas = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 30000,
      refetchOnWindowFocus: true,
      retry: (tentativas, erro) =>
        !(erro instanceof ErroApi && erro.status >= 400 && erro.status < 500) && tentativas < 1,
    },
    mutations: { retry: false },
  },
})

const tema = createTheme(
  {
    palette: {
      primary: { main: '#17694f' },
      secondary: { main: '#485b65' },
      background: { default: '#f7f8fa', paper: '#ffffff' },
      text: { primary: '#20272c', secondary: '#667078' },
      divider: '#e5e8eb',
    },
    typography: {
      fontFamily: '"DM Sans", sans-serif',
      fontSize: 14,
      allVariants: { letterSpacing: 0 },
      button: { textTransform: 'none', fontWeight: 600 },
      h1: { fontSize: '1.75rem', fontWeight: 600 },
      h2: { fontSize: '1.125rem', fontWeight: 600 },
    },
    shape: { borderRadius: 6 },
    components: {
      MuiButton: {
        defaultProps: { disableElevation: true },
        styleOverrides: { root: { minHeight: 38 } },
      },
      MuiTextField: { defaultProps: { size: 'small', fullWidth: true } },
      MuiChip: { styleOverrides: { root: { borderRadius: 4, fontWeight: 500 } } },
      MuiTableCell: {
        styleOverrides: {
          head: {
            color: '#667078',
            backgroundColor: '#fafbfc',
            fontWeight: 500,
            whiteSpace: 'nowrap',
          },
          root: { padding: '16px 20px', borderColor: '#edf0f2' },
        },
      },
      MuiOutlinedInput: { styleOverrides: { root: { backgroundColor: '#fff' } } },
    },
  },
  ptBR,
)

function SincronizarAcesso() {
  const acessoLiberado = useSelector((estado: EstadoAplicacao) => estado.acesso.acessoLiberado)
  const cliente = useQueryClient()

  // A saída no Redux também descarta o cache remoto do usuário anterior.
  useEffect(() => {
    if (!acessoLiberado) cliente.clear()
  }, [acessoLiberado, cliente])

  return null
}

export default function Provedores({ children: filhos }: { children: ReactNode }) {
  const [mensagem, definirMensagem] = useState('')
  return (
    <ProvedorRedux store={store}>
      <QueryClientProvider client={clienteConsultas}>
        <SincronizarAcesso />
        <ThemeProvider theme={tema}>
          <CssBaseline />
          <ContextoNotificacao.Provider value={definirMensagem}>
            <LimiteErro>{filhos}</LimiteErro>
            <Snackbar
              open={!!mensagem}
              autoHideDuration={5000}
              onClose={() => definirMensagem('')}
              anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
            >
              <Alert onClose={() => definirMensagem('')} severity="success" variant="filled">
                {mensagem}
              </Alert>
            </Snackbar>
          </ContextoNotificacao.Provider>
        </ThemeProvider>
      </QueryClientProvider>
    </ProvedorRedux>
  )
}
