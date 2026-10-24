import { Route, Routes } from 'react-router'
import ListaRegistros from '../../components/listaRegistros'
import FormularioRegistro from '../../components/formularioRegistro'
import DetalhesRegistro from '../../components/detalhesRegistro'
import PaginaNaoEncontrada from '../paginaNaoEncontrada'

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
