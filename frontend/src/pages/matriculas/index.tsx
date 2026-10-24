import { Route, Routes } from 'react-router'
import ListaRegistros from '../../components/listaRegistros'
import FormularioRegistro from '../../components/formularioRegistro'
import DetalhesRegistro from '../../components/detalhesRegistro'
import PaginaNaoEncontrada from '../paginaNaoEncontrada'

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
