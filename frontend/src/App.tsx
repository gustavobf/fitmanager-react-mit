import { lazy, Suspense } from 'react'
import { BrowserRouter, Route, Routes } from 'react-router'
import Provedores from './app/Provedores'
import Estrutura from './components/Estrutura/Estrutura'
import RotaPrivada from './components/RotaPrivada/RotaPrivada'
import { Carregando } from './components/Mensagens/Mensagens'
import PaginaNaoEncontrada from './pages/PaginaNaoEncontrada/PaginaNaoEncontrada'

const Acesso = lazy(() => import('./pages/Acesso/Acesso'))
const Painel = lazy(() => import('./pages/Painel/Painel'))
const Alunos = lazy(() => import('./pages/Alunos/Alunos'))
const Planos = lazy(() => import('./pages/Planos/Planos'))
const Instrutores = lazy(() => import('./pages/Instrutores/Instrutores'))
const Matriculas = lazy(() => import('./pages/Matriculas/Matriculas'))
const Agendamentos = lazy(() => import('./pages/Agendamentos/Agendamentos'))

export default function App() {
  return (
    <Provedores>
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
    </Provedores>
  )
}
