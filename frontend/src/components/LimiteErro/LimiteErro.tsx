import { Component } from 'react'
import type { ErrorInfo, ReactNode } from 'react'
import { Button } from '@mui/material'
import { Refresh } from '@mui/icons-material'
import './LimiteErro.css'

interface PropriedadesLimiteErro {
  children: ReactNode
}

interface EstadoLimiteErro {
  falhou: boolean
}

export default class LimiteErro extends Component<PropriedadesLimiteErro, EstadoLimiteErro> {
  state: EstadoLimiteErro = { falhou: false }

  // O limite usa o ciclo de vida de classe para capturar falhas de renderização dos descendentes.
  static getDerivedStateFromError(): EstadoLimiteErro {
    return { falhou: true }
  }

  componentDidCatch(erro: Error, informacoes: ErrorInfo) {
    console.error('Erro de renderização no FitManager:', erro, informacoes.componentStack)
  }

  render() {
    if (this.state.falhou) {
      return (
        <section className="limite-erro" role="alert">
          <h1>Não foi possível exibir a página</h1>
          <p>Ocorreu um erro inesperado ao carregar a aplicação.</p>
          <Button
            variant="contained"
            startIcon={<Refresh />}
            onClick={() => window.location.reload()}
          >
            Recarregar página
          </Button>
        </section>
      )
    }

    return this.props.children
  }
}
