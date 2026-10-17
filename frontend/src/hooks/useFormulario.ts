import { useState } from 'react'
import type { CampoFormulario, Valor } from '../features/gestao/recursos'

export const useFormulario = (inicial: Record<string, Valor>, campos: CampoFormulario[]) => {
  const [valores, definirValores] = useState(inicial)
  const [erros, definirErros] = useState<Record<string, string>>({})
  const definirValor = (nome: string, valor: Valor) => {
    definirValores((anterior) => ({ ...anterior, [nome]: valor }))
    definirErros((anterior) => ({ ...anterior, [nome]: '' }))
  }
  const validar = () => {
    const novosErros: Record<string, string> = {}
    campos.forEach(
      ({ nome, rotulo, tipo, obrigatorio, minimo, maximo, tamanhoMaximo, incremento }) => {
        const valor = valores[nome]
        const texto = String(valor ?? '').trim()
        if (obrigatorio && !texto) novosErros[nome] = `${rotulo} é obrigatório.`
        else if (texto && tipo === 'email' && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(texto))
          novosErros[nome] = 'Informe um e-mail válido.'
        else if (
          texto &&
          tipo === 'number' &&
          (!Number.isFinite(Number(valor)) ||
            (minimo !== undefined && Number(valor) < minimo) ||
            (maximo !== undefined && Number(valor) > maximo))
        )
          novosErros[nome] =
            `Informe um valor entre ${minimo ?? 0} e ${maximo ?? 'o limite permitido'}.`
        else if (
          texto &&
          tipo === 'number' &&
          incremento === undefined &&
          !Number.isInteger(Number(valor))
        )
          novosErros[nome] = 'Informe um número inteiro.'
        else if (texto && tipo !== 'number' && minimo !== undefined && texto.length < minimo)
          novosErros[nome] = `Use pelo menos ${minimo} caracteres.`
        else if (tamanhoMaximo && texto.length > tamanhoMaximo)
          novosErros[nome] = `Use até ${tamanhoMaximo} caracteres.`
        else if (
          nome === 'dataNascimento' &&
          texto &&
          texto >= new Date().toLocaleDateString('sv-SE', { timeZone: 'America/Sao_Paulo' })
        )
          novosErros[nome] = 'Informe uma data de nascimento no passado.'
      },
    )
    definirErros(novosErros)
    return Object.keys(novosErros).length === 0
  }
  return { valores, erros, definirValor, validar }
}
