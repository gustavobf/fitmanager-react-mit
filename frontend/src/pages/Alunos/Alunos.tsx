import { Route, Routes } from 'react-router'
import ListaRegistros from '../../components/ListaRegistros/ListaRegistros'
import FormularioRegistro from '../../components/FormularioRegistro/FormularioRegistro'
import DetalhesRegistro from '../../components/DetalhesRegistro/DetalhesRegistro'
import PaginaNaoEncontrada from '../PaginaNaoEncontrada/PaginaNaoEncontrada'

export default function Alunos() {
  return (
    <Routes>
      <Route index element={<ListaRegistros recurso="alunos" />} />
      <Route path="novo" element={<FormularioRegistro key="new" recurso="alunos" />} />
      <Route path=":id" element={<DetalhesRegistro recurso="alunos" />} />
      <Route path=":id/editar" element={<FormularioRegistro key="edit" recurso="alunos" />} />
      <Route path="*" element={<PaginaNaoEncontrada />} />
    </Routes>
  )
}
