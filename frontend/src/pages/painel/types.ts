export interface IndicadoresPainel {
  totalAlunos: number
  alunosAtivos: number
  totalInstrutores: number
  matriculasAtivas: number
  matriculasVencendo: number
  agendamentosHoje: number
}

export interface DadosClima {
  current: { temperature_2m: number; weather_code: number }
  current_units: { temperature_2m: string }
}