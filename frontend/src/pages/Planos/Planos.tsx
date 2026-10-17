import { Route, Routes } from 'react-router'
import ListaRegistros from '../../components/ListaRegistros/ListaRegistros'
import FormularioRegistro from '../../components/FormularioRegistro/FormularioRegistro'
import DetalhesRegistro from '../../components/DetalhesRegistro/DetalhesRegistro'
import PaginaNaoEncontrada from '../PaginaNaoEncontrada/PaginaNaoEncontrada'

export default function Planos() {
  return (
    <Routes>
      <Route index element={<ListaRegistros recurso="planos" />} />
      <Route path="novo" element={<FormularioRegistro key="new" recurso="planos" />} />
      <Route path=":id" element={<DetalhesRegistro recurso="planos" />} />
      <Route path=":id/editar" element={<FormularioRegistro key="edit" recurso="planos" />} />
      <Route path="*" element={<PaginaNaoEncontrada />} />
    </Routes>
  )
}
