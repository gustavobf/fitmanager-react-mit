import { lazy, Suspense } from 'react'
import { BrowserRouter, Route, Routes } from 'react-router'
import Providers from './app/providers'
import Estrutura from './components/estrutura'
import RotaPrivada from './components/rotaPrivada'
import { Carregando } from './components/mensagens'
import PaginaNaoEncontrada from './pages/paginaNaoEncontrada'

const Acesso = lazy(() => import('./pages/acesso'))
const Painel = lazy(() => import('./pages/painel'))
const Alunos = lazy(() => import('./pages/alunos'))
const Planos = lazy(() => import('./pages/planos'))
const Instrutores = lazy(() => import('./pages/instrutores'))
const Matriculas = lazy(() => import('./pages/matriculas'))
const Agendamentos = lazy(() => import('./pages/agendamentos'))

export default function App() {
  return (
    <Providers>
      <BrowserRouter>
        <a href="#conteudo-principal" className="atalho-conteudo">
          Ir para o conteúdo
        </a>
        <Suspense fallback={<Carregando />}>
          <Routes>
            <Route path="/login" element={<Acesso />} />
            <Route element={<RotaPrivada />}>
              <Route element={<Estrutura />}>
                <Route index element={<Painel />} />
                <Route path="alunos/*" element={<Alunos />} />
                <Route path="planos/*" element={<Planos />} />
                <Route path="instrutores/*" element={<Instrutores />} />
                <Route path="matriculas/*" element={<Matriculas />} />
                <Route path="agendamentos/*" element={<Agendamentos />} />
                <Route path="*" element={<PaginaNaoEncontrada />} />
              </Route>
            </Route>
          </Routes>
        </Suspense>
      </BrowserRouter>
    </Providers>
  )
}
