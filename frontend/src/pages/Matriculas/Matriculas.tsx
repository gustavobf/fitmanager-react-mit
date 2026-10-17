import { Route, Routes } from 'react-router'
import ListaRegistros from '../../components/ListaRegistros/ListaRegistros'
import FormularioRegistro from '../../components/FormularioRegistro/FormularioRegistro'
import DetalhesRegistro from '../../components/DetalhesRegistro/DetalhesRegistro'
import PaginaNaoEncontrada from '../PaginaNaoEncontrada/PaginaNaoEncontrada'

export default function Matriculas() {
  return (
    <Routes>
      <Route index element={<ListaRegistros recurso="matriculas" />} />
      <Route path="novo" element={<FormularioRegistro key="new" recurso="matriculas" />} />
      <Route path=":id" element={<DetalhesRegistro recurso="matriculas" />} />
      <Route path=":id/editar" element={<FormularioRegistro key="edit" recurso="matriculas" />} />
      <Route path="*" element={<PaginaNaoEncontrada />} />
    </Routes>
  )
}
