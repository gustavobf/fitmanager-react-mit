import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Link, useNavigate, useParams } from 'react-router'
import { Alert, Button, Checkbox, FormControlLabel, MenuItem, TextField } from '@mui/material'
import { ArrowBack, SaveOutlined } from '@mui/icons-material'
import { AvisoErro, Carregando, CabecalhoPagina } from '../mensagens'
import { useNotification } from '../../app/contexts'
import { useFormulario } from './useFormulario'
import { buscarRegistro, salvarRegistro, useReferencias } from '../../services/api'
import { obterCamposEditaveis, recursos } from '../../config/gestao/recursos'
import type { Registro, Recurso, Valor } from '../../config/gestao/types'
import { obterDataHoraLocal, rotuloSituacao } from '../../utils/formatacao'
import { montarDadosEnvio } from '../../utils/formulario'
import './styles.css'

function ConteudoFormulario({
  recurso,
  registro,
  id,
}: {
  recurso: Recurso
  registro?: Registro
  id?: string
}) {
  const campos = obterCamposEditaveis(recurso, !!id)
  const referencias = useReferencias(
    recurso === 'matriculas' && id ? [] : recursos[recurso].referencias,
  )
  const inicial = Object.fromEntries(
    campos.map(({ nome, tipo }) => {
      let valor: Valor =
        registro?.[nome] ?? (tipo === 'boolean' ? true : nome === 'duracaoEmMinutos' ? 60 : '')
      if (nome === 'dataHora' && valor) valor = obterDataHoraLocal(String(valor))
      return [nome, valor]
    }),
  )
  const formulario = useFormulario(inicial, campos)
  const navegar = useNavigate()
  const cliente = useQueryClient()
  const notificar = useNotification()
  const mutacao = useMutation({
    mutationFn: () => salvarRegistro(recurso, montarDadosEnvio(campos, formulario.valores), id),
    onSuccess: async (salvo) => {
      await cliente.invalidateQueries()
      const operacao = id
        ? recurso === 'matriculas'
          ? 'atualizada'
          : 'atualizado'
        : recurso === 'matriculas'
          ? 'cadastrada'
          : 'cadastrado'
      notificar(`${recursos[recurso].singular} ${operacao} com sucesso.`)
      navegar(`/${recurso}/${salvo.id}`, { replace: true })
    },
  })

  return (
    <>
      {referencias.erro && (
        <AvisoErro erro={referencias.erro} tentarNovamente={referencias.tentarNovamente} />
      )}
      {mutacao.error && (
        <div className="espaco-mensagem">
          <AvisoErro erro={mutacao.error} />
        </div>
      )}
      <form
        className="formulario-registro"
        noValidate
        onSubmit={(evento) => {
          evento.preventDefault()
          if (formulario.validar()) mutacao.mutate()
        }}
      >
        <fieldset disabled={mutacao.isPending || referencias.carregando || !!referencias.erro}>
          <legend>{id ? 'Dados do cadastro' : 'Informações do cadastro'}</legend>
          <div className="grade-formulario">
            {campos.map((campo) => {
              const {
                nome,
                rotulo,
                tipo,
                obrigatorio,
                referencia,
                opcoes,
                minimo,
                maximo,
                tamanhoMaximo,
                incremento,
              } = campo
              if (tipo === 'boolean')
                return (
                  <div className="largura-total" key={nome}>
                    <FormControlLabel
                      control={
                        <Checkbox
                          checked={Boolean(formulario.valores[nome])}
                          onChange={(evento) =>
                            formulario.definirValor(nome, evento.target.checked)
                          }
                        />
                      }
                      label={rotulo}
                    />
                  </div>
                )
              const opcoesDisponiveis = referencia ? (referencias.dados[referencia] ?? []) : []
              return (
                <TextField
                  key={nome}
                  id={`campo-${nome}`}
                  name={nome}
                  label={rotulo}
                  required={obrigatorio}
                  value={formulario.valores[nome] ?? ''}
                  onChange={(evento) => formulario.definirValor(nome, evento.target.value)}
                  type={tipo === 'select' || tipo === 'textarea' ? undefined : (tipo ?? 'text')}
                  select={tipo === 'select'}
                  multiline={tipo === 'textarea'}
                  minRows={tipo === 'textarea' ? 3 : undefined}
                  error={!!formulario.erros[nome]}
                  helperText={
                    formulario.erros[nome] ||
                    (referencia &&
                    !referencias.carregando &&
                    opcoesDisponiveis.filter((item) => item.ativo).length === 0
                      ? 'Nenhum cadastro ativo disponível.'
                      : ' ')
                  }
                  className={tipo === 'textarea' ? 'largura-total' : undefined}
                  slotProps={{
                    inputLabel:
                      tipo === 'date' || tipo === 'datetime-local' ? { shrink: true } : undefined,
                    htmlInput: {
                      min: tipo === 'number' ? minimo : undefined,
                      max:
                        tipo === 'number'
                          ? maximo
                          : nome === 'dataNascimento'
                            ? new Date().toLocaleDateString('sv-SE', {
                                timeZone: 'America/Sao_Paulo',
                              })
                            : undefined,
                      minLength: tipo !== 'number' ? minimo : undefined,
                      maxLength: tamanhoMaximo,
                      step: incremento ?? (tipo === 'number' ? 1 : undefined),
                    },
                  }}
                >
                  {tipo === 'select' && (
                    <MenuItem value="" disabled>
                      Selecione
                    </MenuItem>
                  )}
                  {opcoes?.map((opcao) => (
                    <MenuItem key={opcao} value={opcao}>
                      {rotuloSituacao(opcao)}
                    </MenuItem>
                  ))}
                  {opcoesDisponiveis
                    .filter((item) => item.ativo || item.id === Number(formulario.valores[nome]))
                    .map((item) => (
                      <MenuItem key={item.id} value={item.id}>
                        {String(item.nome)}
                        {!item.ativo ? ' (inativo)' : ''}
                      </MenuItem>
                    ))}
                </TextField>
              )
            })}
          </div>
        </fieldset>
        {recurso === 'matriculas' && !id && (
          <Alert severity="info" icon={false}>
            O vencimento será calculado conforme a duração do plano escolhido.
          </Alert>
        )}
        <div className="acoes-formulario">
          <Button
            component={Link}
            to={id ? `/${recurso}/${id}` : `/${recurso}`}
            disabled={mutacao.isPending}
          >
            Cancelar
          </Button>
          <Button
            type="submit"
            variant="contained"
            startIcon={<SaveOutlined />}
            disabled={mutacao.isPending || referencias.carregando || !!referencias.erro}
          >
            {mutacao.isPending ? 'Salvando...' : 'Salvar cadastro'}
          </Button>
        </div>
      </form>
    </>
  )
}

export default function FormularioRegistro({ recurso }: { recurso: Recurso }) {
  const { id } = useParams()
  const consulta = useQuery({
    queryKey: ['registro', recurso, id],
    queryFn: ({ signal: sinal }) => buscarRegistro(recurso, id!, sinal),
    enabled: !!id,
  })
  return (
    <>
      <Button
        component={Link}
        to={id ? `/${recurso}/${id}` : `/${recurso}`}
        startIcon={<ArrowBack />}
        className="botao-voltar"
      >
        Voltar
      </Button>
      <CabecalhoPagina
        titulo={`${id ? (recurso === 'matriculas' ? 'Atualizar' : 'Editar') : 'Novo cadastro de'} ${recursos[recurso].singular.toLowerCase()}`}
      />
      {id && consulta.isPending ? (
        <Carregando />
      ) : id && consulta.error ? (
        <AvisoErro
          erro={consulta.error}
          tentarNovamente={() => {
            void consulta.refetch()
          }}
        />
      ) : (
        <ConteudoFormulario
          key={`${recurso}-${id ?? 'new'}`}
          recurso={recurso}
          id={id}
          registro={consulta.data}
        />
      )}
    </>
  )
}
