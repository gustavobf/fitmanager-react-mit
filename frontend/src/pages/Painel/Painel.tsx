import { useQuery } from '@tanstack/react-query'
import { Link } from 'react-router'
import { Button, Chip, IconButton, Tooltip } from '@mui/material'
import {
  Add,
  ArrowForward,
  CalendarMonthOutlined,
  GroupsOutlined,
  Refresh,
  SchoolOutlined,
  ThermostatOutlined,
  VerifiedUserOutlined,
} from '@mui/icons-material'
import { useSelector } from 'react-redux'
import type { EstadoAplicacao } from '../../app/store'
import { AvisoErro, Carregando, CabecalhoPagina } from '../../components/Mensagens/Mensagens'
import { request as requisitar } from '../../lib/http'
import { listarRegistros, useReferencias } from '../../features/gestao/api'
import { formatarValor } from '../../features/gestao/recursos'
import './Painel.css'

interface IndicadoresPainel {
  totalAlunos: number
  alunosAtivos: number
  totalInstrutores: number
  matriculasAtivas: number
  matriculasVencendo: number
  agendamentosHoje: number
}
interface DadosClima {
  current: { temperature_2m: number; weather_code: number }
  current_units: { temperature_2m: string }
}

function IndicadorClima() {
  const consulta = useQuery({
    queryKey: ['clima', 'rio-de-janeiro'],
    queryFn: ({ signal: sinal }) =>
      requisitar<DadosClima>(
        'https://api.open-meteo.com/v1/forecast?latitude=-22.9068&longitude=-43.1729&current=temperature_2m,weather_code&timezone=America%2FSao_Paulo',
        { signal: sinal },
      ),
    staleTime: 600000,
    refetchInterval: 600000,
    retry: false,
  })
  const codigo = consulta.data?.current.weather_code ?? 0
  const condicao =
    codigo === 0
      ? 'Céu limpo'
      : codigo < 4
        ? 'Parcialmente nublado'
        : codigo < 50
          ? 'Neblina'
          : codigo < 80
            ? 'Chuva'
            : 'Pancadas de chuva'
  return (
    <div className="indicador-clima">
      <ThermostatOutlined fontSize="small" />
      <div>
        <strong>Rio de Janeiro</strong>
        <span>
          {consulta.isPending
            ? 'Consultando clima...'
            : consulta.error
              ? 'Clima indisponível'
              : `${Math.round(consulta.data.current.temperature_2m)} ${consulta.data.current_units.temperature_2m} · ${condicao}`}
        </span>
      </div>
      <Tooltip title="Atualizar clima">
        <span>
          <IconButton
            size="small"
            aria-label="Atualizar clima"
            disabled={consulta.isFetching}
            onClick={() => {
              void consulta.refetch()
            }}
          >
            <Refresh fontSize="small" />
          </IconButton>
        </span>
      </Tooltip>
    </div>
  )
}

export default function Painel() {
  const usuario = useSelector((estado: EstadoAplicacao) => estado.acesso.usuario)
  const hoje = new Date().toLocaleDateString('sv-SE', { timeZone: 'America/Sao_Paulo' })
  const painel = useQuery({
    queryKey: ['painel'],
    queryFn: ({ signal: sinal }) => requisitar<IndicadoresPainel>('/api/painel', { signal: sinal }),
    refetchInterval: 60000,
  })
  const agenda = useQuery({
    queryKey: ['registros', 'agendamentos', 'hoje', hoje],
    queryFn: ({ signal: sinal }) =>
      listarRegistros(
        'agendamentos',
        new URLSearchParams({
          inicio: `${hoje}T00:00:00`,
          fim: `${hoje}T23:59:59`,
          tamanho: '100',
          status: 'AGENDADO',
        }),
        sinal,
      ),
    refetchInterval: 60000,
  })
  const referencias = useReferencias(['alunos', 'instrutores'])
  const indicadores = painel.data
    ? [
        {
          titulo: 'Alunos ativos',
          valor: painel.data.alunosAtivos,
          detalhe: `${painel.data.totalAlunos} alunos cadastrados`,
          icone: GroupsOutlined,
          destino: '/alunos',
        },
        {
          titulo: 'Matrículas ativas',
          valor: painel.data.matriculasAtivas,
          detalhe: 'Vínculos em andamento',
          icone: VerifiedUserOutlined,
          destino: '/matriculas',
        },
        {
          titulo: 'Instrutores',
          valor: painel.data.totalInstrutores,
          detalhe: 'Profissionais cadastrados',
          icone: SchoolOutlined,
          destino: '/instrutores',
        },
        {
          titulo: 'Agendamentos hoje',
          valor: painel.data.agendamentosHoje,
          detalhe: 'Sessões no dia de hoje',
          icone: CalendarMonthOutlined,
          destino: '/agendamentos',
        },
      ]
    : []
  return (
    <>
      <CabecalhoPagina
        titulo="Visão geral"
        descricao={`Olá, ${usuario.nome.split(' ')[0]}. Confira o dia da academia.`}
        acao={
          <Button component={Link} to="/alunos/novo" variant="contained" startIcon={<Add />}>
            Cadastrar aluno
          </Button>
        }
      />
      <div className="data-painel">
        <span>
          {new Intl.DateTimeFormat('pt-BR', {
            dateStyle: 'full',
            timeZone: 'America/Sao_Paulo',
          }).format(new Date())}
        </span>
        <Tooltip title="Atualizar indicadores">
          <IconButton
            aria-label="Atualizar indicadores"
            size="small"
            disabled={painel.isFetching}
            onClick={() => {
              void painel.refetch()
            }}
          >
            <Refresh fontSize="small" />
          </IconButton>
        </Tooltip>
      </div>
      {painel.isPending ? (
        <Carregando />
      ) : painel.error ? (
        <AvisoErro
          erro={painel.error}
          tentarNovamente={() => {
            void painel.refetch()
          }}
        />
      ) : (
        <>
          <div className="grade-indicadores">
            {indicadores.map(({ titulo, valor, detalhe, icone: Icone, destino }) => (
              <Link className="item-indicador" key={titulo} to={destino}>
                <div className="rotulo-indicador">
                  <span>{titulo}</span>
                  <Icone fontSize="small" />
                </div>
                <strong>{valor}</strong>
                <span className="detalhe-indicador">{detalhe}</span>
              </Link>
            ))}
          </div>
          <div className="resumo-vencimentos">
            <div>
              <span className="marcador-resumo" />
              <p>
                <strong>
                  {painel.data.matriculasVencendo}{' '}
                  {painel.data.matriculasVencendo === 1 ? 'matrícula vence' : 'matrículas vencem'}{' '}
                  nos próximos 7 dias
                </strong>
                <span>Acompanhe os vencimentos e mantenha os planos em dia.</span>
              </p>
            </div>
            <Button component={Link} to="/matriculas?status=ATIVA" endIcon={<ArrowForward />}>
              Ver matrículas
            </Button>
          </div>
        </>
      )}
      <section className="secao-agenda">
        <div className="titulo-secao">
          <h2>Agenda de hoje</h2>
          <Button component={Link} to="/agendamentos" endIcon={<ArrowForward />} size="small">
            Ver agenda
          </Button>
        </div>
        {referencias.erro && (
          <AvisoErro erro={referencias.erro} tentarNovamente={referencias.tentarNovamente} />
        )}
        {agenda.isPending ? (
          <Carregando />
        ) : agenda.error ? (
          <AvisoErro
            erro={agenda.error}
            tentarNovamente={() => {
              void agenda.refetch()
            }}
          />
        ) : agenda.data.conteudo.length === 0 ? (
          <div className="estado-vazio">
            <CalendarMonthOutlined color="disabled" />
            <h2>Agenda livre por aqui</h2>
            <p>Nenhuma sessão agendada para hoje.</p>
            <Button component={Link} to="/agendamentos/novo" startIcon={<Add />}>
              Novo agendamento
            </Button>
          </div>
        ) : (
          <>
            <div className="lista-agenda">
              {[...agenda.data.conteudo]
                .sort((primeiro, segundo) =>
                  String(primeiro.dataHora).localeCompare(String(segundo.dataHora)),
                )
                .slice(0, 5)
                .map((registro) => (
                  <Link
                    to={`/agendamentos/${registro.id}`}
                    key={registro.id}
                    className="linha-agenda"
                  >
                    <time>
                      {new Intl.DateTimeFormat('pt-BR', {
                        timeStyle: 'short',
                        timeZone: 'America/Sao_Paulo',
                      }).format(new Date(String(registro.dataHora)))}
                    </time>
                    <div>
                      <strong>
                        {formatarValor('alunoId', registro.alunoId, referencias.dados)}
                      </strong>
                      <span>
                        {formatarValor('instrutorId', registro.instrutorId, referencias.dados)} ·{' '}
                        {registro.duracaoEmMinutos} min
                      </span>
                    </div>
                    <Chip label="Agendado" size="small" variant="outlined" />
                    <ArrowForward fontSize="small" />
                  </Link>
                ))}
            </div>
            {agenda.data.totalElementos > 5 && (
              <p className="mais-registros">
                {agenda.data.totalElementos - 5} outros agendamentos na agenda.
              </p>
            )}
          </>
        )}
      </section>
      <div className="rodape-painel">
        <IndicadorClima />
        <a
          className="credito-fonte"
          href="https://open-meteo.com/"
          target="_blank"
          rel="noreferrer"
        >
          Clima: Open-Meteo
        </a>
      </div>
    </>
  )
}
