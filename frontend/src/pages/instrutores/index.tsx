import { Route, Routes } from 'react-router'
import ListaRegistros from '../../components/listaRegistros'
import FormularioRegistro from '../../components/formularioRegistro'
import DetalhesRegistro from '../../components/detalhesRegistro'
import PaginaNaoEncontrada from '../paginaNaoEncontrada'

export default function Instrutores() {
  return (
    <Routes>
      <Route index element={<ListaRegistros recurso="instrutores" />} />
      <Route path="novo" element={<FormularioRegistro key="new" recurso="instrutores" />} />
      <Route path=":id" element={<DetalhesRegistro recurso="instrutores" />} />
      <Route path=":id/editar" element={<FormularioRegistro key="edit" recurso="instrutores" />} />
      <Route path="*" element={<PaginaNaoEncontrada />} />
    </Routes>
  )
}
