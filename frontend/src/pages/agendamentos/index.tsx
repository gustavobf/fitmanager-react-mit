import { Route, Routes } from 'react-router'
import ListaRegistros from '../../components/listaRegistros'
import FormularioRegistro from '../../components/formularioRegistro'
import DetalhesRegistro from '../../components/detalhesRegistro'
import PaginaNaoEncontrada from '../paginaNaoEncontrada'

export default function Agendamentos() {
  return (
    <Routes>
      <Route index element={<ListaRegistros recurso="agendamentos" />} />
      <Route path="novo" element={<FormularioRegistro key="new" recurso="agendamentos" />} />
      <Route path=":id" element={<DetalhesRegistro recurso="agendamentos" />} />
      <Route path=":id/editar" element={<FormularioRegistro key="edit" recurso="agendamentos" />} />
      <Route path="*" element={<PaginaNaoEncontrada />} />
    </Routes>
  )
}
