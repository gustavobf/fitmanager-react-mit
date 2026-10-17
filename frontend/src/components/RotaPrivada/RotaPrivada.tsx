import { Navigate, Outlet, useLocation } from 'react-router'
import { useSelector } from 'react-redux'
import type { EstadoAplicacao } from '../../app/store'

export default function RotaPrivada() {
  const acessoLiberado = useSelector((estado: EstadoAplicacao) => estado.acesso.acessoLiberado)
  const localizacao = useLocation()

  return acessoLiberado ? (
    <Outlet />
  ) : (
    <Navigate
      to="/login"
      state={{ origem: `${localizacao.pathname}${localizacao.search}${localizacao.hash}` }}
      replace
    />
  )
}
