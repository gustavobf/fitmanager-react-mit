import { useState } from 'react'
import { Link, NavLink, Outlet } from 'react-router'
import { Avatar, Drawer, IconButton, Tooltip } from '@mui/material'
import {
  CalendarMonthOutlined,
  DashboardOutlined,
  FitnessCenter,
  GroupsOutlined,
  Logout,
  Menu,
  SchoolOutlined,
  SellOutlined,
  VerifiedUserOutlined,
} from '@mui/icons-material'
import { useDispatch, useSelector } from 'react-redux'
import { sair } from '../../app/acesso/estadoAcesso'
import type { AppDispatch, RootState } from '../../app/store'
import './styles.css'

const itensMenu = [
  { caminho: '/', titulo: 'Visão geral', icone: DashboardOutlined },
  { caminho: '/alunos', titulo: 'Alunos', icone: GroupsOutlined },
  { caminho: '/instrutores', titulo: 'Instrutores', icone: SchoolOutlined },
  { caminho: '/planos', titulo: 'Planos', icone: SellOutlined },
  { caminho: '/matriculas', titulo: 'Matrículas', icone: VerifiedUserOutlined },
  { caminho: '/agendamentos', titulo: 'Agendamentos', icone: CalendarMonthOutlined },
]

export default function Estrutura() {
  const [aberto, definirAberto] = useState(false)
  const usuario = useSelector((estado: RootState) => estado.acesso.usuario)
  const dispatch = useDispatch<AppDispatch>()
  const menuLateral = (
    <>
      <Link to="/" className="marca" onClick={() => definirAberto(false)}>
        <FitnessCenter fontSize="small" />
        <span>
          fitmanager<span className="ponto-marca">.</span>
        </span>
      </Link>
      <div className="rotulo-navegacao">GERENCIAMENTO</div>
      <nav aria-label="Navegação principal">
        {itensMenu.map(({ caminho, titulo, icone: Icone }) => (
          <NavLink
            key={caminho}
            to={caminho}
            end={caminho === '/'}
            className={({ isActive: estaAtivo }) => `item-navegacao${estaAtivo ? ' ativo' : ''}`}
            onClick={() => definirAberto(false)}
          >
            <Icone fontSize="small" />
            <span>{titulo}</span>
          </NavLink>
        ))}
      </nav>
    </>
  )
  return (
    <div className="estrutura-aplicacao">
      <aside className="menu-lateral-computador">{menuLateral}</aside>
      <Drawer
        open={aberto}
        onClose={() => definirAberto(false)}
        sx={{ '& .MuiDrawer-paper': { width: 236, padding: '28px 18px' } }}
      >
        {menuLateral}
      </Drawer>
      <div className="area-trabalho">
        <header className="cabecalho-aplicacao">
          <div className="inicio-cabecalho">
            <IconButton
              aria-label="Abrir menu"
              className="menu-movel"
              onClick={() => definirAberto(true)}
            >
              <Menu />
            </IconButton>
            <span>Gestão da academia</span>
          </div>
          <div className="menu-usuario">
            <Avatar
              sx={{ width: 30, height: 30, bgcolor: '#edf3f0', color: '#17694f', fontSize: 13 }}
            >
              {usuario.nome.slice(0, 1).toUpperCase()}
            </Avatar>
            <div className="dados-usuario">
              <strong>{usuario.nome}</strong>
              <span>Administrador</span>
            </div>
            <Tooltip title="Sair">
              <IconButton size="small" aria-label="Sair" onClick={() => dispatch(sair())}>
                <Logout fontSize="small" />
              </IconButton>
            </Tooltip>
          </div>
        </header>
        <main className="conteudo-principal" id="conteudo-principal">
          <Outlet />
        </main>
        <footer className="rodape-aplicacao">
          <span>FitManager · Gestão da academia</span>
        </footer>
      </div>
    </div>
  )
}
