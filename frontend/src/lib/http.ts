export class ApiError extends Error {
  readonly status: number
  readonly details: string[]

  constructor(message: string, status = 0, details: string[] = []) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.details = details
  }
}

export const request = async <Result>(
  path: string,
  options: RequestInit = {},
  timeout = 15000,
): Promise<Result> => {
  const controller = new AbortController()
  const abort = () => controller.abort(options.signal?.reason)
  if (options.signal?.aborted) abort()
  else options.signal?.addEventListener('abort', abort, { once: true })
  let timer: ReturnType<typeof setTimeout> | undefined
  // Promise.race limita a espera; o AbortController também interrompe o fetch pendente.
  const deadline = new Promise<never>((_, reject) => {
    timer = setTimeout(() => {
      reject(new ApiError('O servidor demorou a responder. Tente novamente.', 408))
      controller.abort()
    }, timeout)
  })

  const execute = async (): Promise<Result> => {
    const baseUrl = (import.meta.env.VITE_API_URL ?? '').trim().replace(/\/+$/, '')
    const url = path.startsWith('/') ? `${baseUrl}${path}` : path
    const response = await fetch(url, {
      ...options,
      headers: {
        Accept: 'application/json',
        ...(options.body ? { 'Content-Type': 'application/json' } : {}),
        ...options.headers,
      },
      signal: controller.signal,
    })
    if (!response.ok) {
      const error = (await response.json().catch(() => null)) as {
        mensagem?: string
        detalhes?: string[]
      } | null
      throw new ApiError(
        error?.mensagem ?? `Não foi possível concluir a operação (${response.status}).`,
        response.status,
        error?.detalhes ?? [],
      )
    }
    if (response.status === 204) return undefined as Result
    return response.json() as Promise<Result>
  }

  try {
    return await Promise.race([execute(), deadline])
  } catch (error) {
    if (error instanceof ApiError || controller.signal.aborted) throw error
    throw new ApiError(
      'Não foi possível conectar ao servidor. Verifique se o backend está em execução.',
    )
  } finally {
    clearTimeout(timer)
    options.signal?.removeEventListener('abort', abort)
  }
}
