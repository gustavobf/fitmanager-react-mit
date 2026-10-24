import { Navigate, Outlet, useLocation } from 'react-router'
import { useSelector } from 'react-redux'
import type { RootState } from '../../app/store'

export default function RotaPrivada() {
  const acessoLiberado = useSelector((estado: RootState) => estado.acesso.acessoLiberado)
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
