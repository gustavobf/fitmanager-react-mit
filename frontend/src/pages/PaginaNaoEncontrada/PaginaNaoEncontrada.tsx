import { Link } from 'react-router'
import { Button } from '@mui/material'
import { CabecalhoPagina } from '../../components/Mensagens/Mensagens'

export default function PaginaNaoEncontrada() {
  return (
    <>
      <CabecalhoPagina
        titulo="Página não encontrada"
        descricao="O endereco solicitado não existe."
      />
      <Button component={Link} to="/">
        Voltar ao painel
      </Button>
    </>
  )
}
