import type { CampoFormulario, Valor } from '../config/gestao/types'

export const montarDadosEnvio = (campos: CampoFormulario[], valores: Record<string, Valor>) =>
  Object.fromEntries(
    campos.map(({ nome, tipo }) => {
      const original = valores[nome]
      const valor = typeof original === 'string' ? original.trim() : original
      return [
        nome,
        tipo === 'number' || nome.endsWith('Id')
          ? Number(valor)
          : tipo === 'boolean'
            ? Boolean(valor)
            : valor === ''
              ? null
              : valor,
      ]
    }),
  )