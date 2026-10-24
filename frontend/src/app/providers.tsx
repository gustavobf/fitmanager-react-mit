import { useEffect, useState } from 'react'
import type { ReactNode } from 'react'
import { Alert, CssBaseline, Snackbar, ThemeProvider, createTheme } from '@mui/material'
import { ptBR } from '@mui/material/locale'
import { QueryClient, QueryClientProvider, useQueryClient } from '@tanstack/react-query'
import { Provider as ReduxProvider, useSelector } from 'react-redux'
import { ApiError as ErroApi } from '../services/http'
import LimiteErro from '../components/limiteErro'
import { NotificationContext } from './contexts'
import { store } from './store'
import type { RootState } from './store'

// Redux mantém o acesso; React Query mantém o cache remoto, sem duplicar os registros na store.
const queryClient = new QueryClient({
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

const theme = createTheme(
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
  const acessoLiberado = useSelector((estado: RootState) => estado.acesso.acessoLiberado)
  const queryClient = useQueryClient()

  // A saída no Redux também descarta o cache remoto do usuário anterior.
  useEffect(() => {
    if (!acessoLiberado) queryClient.clear()
  }, [acessoLiberado, queryClient])

  return null
}

export default function Providers({ children }: { children: ReactNode }) {
  const [message, setMessage] = useState('')
  return (
    <ReduxProvider store={store}>
      <QueryClientProvider client={queryClient}>
        <SincronizarAcesso />
        <ThemeProvider theme={theme}>
          <CssBaseline />
          <NotificationContext.Provider value={setMessage}>
            <LimiteErro>{children}</LimiteErro>
            <Snackbar
              open={!!message}
              autoHideDuration={5000}
              onClose={() => setMessage('')}
              anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
            >
              <Alert onClose={() => setMessage('')} severity="success" variant="filled">
                {message}
              </Alert>
            </Snackbar>
          </NotificationContext.Provider>
        </ThemeProvider>
      </QueryClientProvider>
    </ReduxProvider>
  )
}
