##### Nota sobre erros

Caso uma requisição seja inválida (p. ex. não existe, não tem autorização), será retornado um objeto RespostaSimplesDto com os dados do erro.

Caso o erro seja devido a campos inválidos, a "message" do RespostaSimplesDto vai conter os campos e seus respectivos problemas.

##### Nota sobre autenticação com JWT

A autenticação no sistema é feita através de JSON Web Token (JWT). 

Primeiro você autentica mandando as credenciais do usuário para ```/api/v1/public/auth/authenticate```. Isso deve retornar uma responsta contendo o JWT no campo Authentication de seu cabeçalho.

Agora, basta salvar o JWT obtido e incluí-lo no cabeçalho de todas as próximas requisições.

## Endpoints

Esta seção descreve os endpoints da API.

### Público

#### Autenticação

##### Autenticar

Usado para criar uma nova seção para um usuário. Você envia as credenciais e ele retorna o usuário (no corpo) e o JWT (no cabeçalho).

| POST | /api/v1/public/auth/authenticate |
| --- | --- |
| Corpo enviado (JSON) | CredenciaisDto |
| Corpo retornado (JSON) | UsuarioDto |
| Cabeçalho retornado | Authentication (contendo o JWT) |

#### ONGs

##### Ver uma ong

| GET | /api/v1/public/ongs/{id} |
| --- | --- |
| Corpo retornado (JSON) | OngDto |

##### Ver todas ongs

| GET | /api/v1/public/ongs |
| --- | --- |
| Parâmetro opcional | **nome** - *string*<br>Se presente, só retorna ONGs cujo nome contém a string especificada. |
| Parâmetro opcional | **areas** - *string (array)*<br>Se presente, só retorna ONGs que tenha pelo menos uma das áreas especificadas. |
| Corpo retornado (JSON) | OngDto (array) |

##### Cadastrar nova ong

| POST | /api/v1/public/ongs |
| --- | --- |
| Corpo enviado (JSON) | NovaOngDto |
| Corpo retornado (JSON) | NovaOngDto |

#### Voluntários

##### Ver um voluntário

| GET | /api/v1/public/voluntarios/{id} |
| --- | --- |
| Corpo retornado (JSON) | VoluntarioDto |

##### Ver todos voluntários

| GET | /api/v1/public/voluntarios |
| --- | --- |
| Corpo retornado (JSON) | VoluntarioDto (array) |

##### Criar voluntário

| POST | /api/v1/public/voluntarios |
| --- | --- |
| Corpo enviado (JSON) | NovoVoluntarioDto |
| Corpo retornado (JSON) | NovoVoluntarioDto |

#### Eventos

##### Ver uma evento

| GET | /api/v1/public/eventos/{id} |
| --- | --- |
| Corpo retornado (JSON) | EventoDto |

##### Ver todas eventos

| GET | /api/v1/public/eventos |
| --- | --- |
| Parâmetro opcional | **idOng** - *long*<br>Se presente, só retorna eventos cuja ONG tem o ID especificado. |
| Parâmetro opcional | **nomeOng** - *string*<br>Se presente, só retorna eventos cujo nome da ONG nome contém a string especificada. |
| Parâmetro opcional | **nome** - *string*<br>Se presente, só retorna eventos cujo nome contém a string especificada. |
| Parâmetro opcional | **areas** - *string (array)*<br>Se presente, só retorna eventos que tenham pelo menos uma das áreas especificadas. |
| Parâmetro opcional | **realizados** - *boolean*<br>Se presente e verdadeiro, só retorna eventos já realizados. Se presente e falso, só retorna eventos ainda não realizados. |
| Corpo retornado (JSON) | EventoDto (array) |

### Apenas para usuários autenticados

#### Autenticação

##### Desautenticar

Invalida a sessão do JWT enviado, caso ela exista.

| GET | /api/v1/public/auth/deauthenticate |
| --- | --- |
| Cabeçalho enviado | Authentication (contendo o JWT) |
| Parâmetro opcional | **all** - *boolean*<br>Se true, desautentica TODAS as sessões daquele usuário. |
| Corpo retornado (JSON) | RespostaSimplesDto |

##### Ver usuário atual

| GET | /api/v1/public/auth/whoami |
| --- | --- |
| Cabeçalho enviado | Authentication (contendo o JWT) |
| Corpo retornado (JSON) | UsuarioDto (se tiver autenticado) |

### Apenas para ONGs autenticadas

#### ONGs

##### Editar ong logada

Atualiza os dados da ONG que estiver autenticada. Só funciona se tiver autenticado e se o usuário for um ONG.
**NOTE** que isso substitui os dados anteriores da ONG, então todos os campos devem ser enviados, mesmo que não tenham sido modificados.
**NOTE** que isso invalida a sessão atual, forçando o usuário a relogar.

| PUT | /api/v1/ong |
| --- | --- |
| Cabeçalho enviado | Authentication (contendo o JWT) |
| Corpo enviado (JSON) | NovaOngDto |
| Corpo retornado (JSON) | NovaOngDto |

#### Eventos

##### Adicionar evento

| POST | /api/v1/ong/eventos |
| --- | --- |
| Cabeçalho enviado | Authentication (contendo o JWT) |
| Corpo enviado (JSON) | EventoDto |
| Corpo retornado (JSON) | EventoDto 

##### Atualizar evento

| PUT | /api/v1/ong/eventos/{id} |
| --- | --- |
| Cabeçalho enviado | Authentication (contendo o JWT) |
| Corpo enviado (JSON) | EventoDto |
| Corpo retornado (JSON) | EventoDto 

##### Deletar evento

| DELETE | /api/v1/ong/eventos/{id} |
| --- | --- |
| Cabeçalho enviado | Authentication (contendo o JWT) |
| Corpo enviado (JSON) | EventoDto
| Corpo retornado (JSON) | RespostaSimplesDto 

### Apenas para Voluntários autenticadas

#### Voluntários

##### Editar voluntário logado

Atualiza os dados do voluntário que estiver autenticado. Só funciona se tiver autenticado e se o usuário for um voluntário.
**NOTE** que isso substitui os dados anteriores do voluntário, então todos os campos devem ser enviados, mesmo que não tenham sido modificados.
**NOTE** que isso invalida a sessão atual, forçando o usuário a relogar.

| PUT | /api/v1/voluntario |
| --- | --- |
| Cabeçalho enviado | Authentication (contendo o JWT) |
| Corpo enviado (JSON) | NovoVoluntarioDto |
| Corpo retornado (JSON) | NovoVoluntarioDto |

#### ONGs

##### Seguir/deixar de seguir ONG

Define se o voluntário logado está seguindo ou não a ONG com o ID especificado.

| POST | /api/v1/voluntario/ongs/{idOng}/seguir |
| --- | --- |
| Cabeçalho enviado | Authentication (contendo o JWT) |
| Parâmetro obrigatório | **valor** - *boolean*<br>'true' (para seguir)<br>ou 'false' (para deixar de seguir) |
| Corpo retornado (JSON) | OngDto |

##### Confirmar/desconfirmar interesse em evento

Define se o voluntário logado confirmou interesse ou não no evento com o ID especificado.

| POST | /api/v1/voluntario/eventos/{idEvento}/confirmar |
| --- | --- |
| Cabeçalho enviado | Authentication (contendo o JWT) |
| Parâmetro obrigatório | **valor** - *boolean*<br>'true' (confirmar presença)<br>ou 'false' (desconfirmar presença) |
| Corpo retornado (JSON) | EventoDto |

-----

## API Objects

Esta sessão descreve os possíveis objetos retornados pela API.

### RespostaSimplesDto

Resposta genérica dada em certas situações, como erros.

| Campo | Tipo | Notas |
| --- | --- | --- |
| timestamp | - | - |
| status | - | - |
| error | - | - |
| message | - | - |
| path | - | - |

### CredenciaisDto

Usado pra autenticação.

| Campo | Tipo | Notas |
| --- | --- | --- |
| username | string | - |
| senha | string | - |

### UsuarioDto

Representa um usuário (ong, voluntário ou admin).
Usado geralmente pra saber quem está autenticado.

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | long | - |
| username | string | - |
| tipo | string | ADMIN, ONG ou VOLUNTARIO 
| idOng | long | Omitido se não for ong |
| idVoluntario | long | Omitido se não for voluntário |
| jwt | string | JWT recebido ao autenticar |

### NovoVoluntarioDto

Usado pra criar e atualizar Voluntarios.

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | long | - |
| username | string | Not blank |
| senha | string | Not blank |
| nome | string | Not blank |
| email | string | - |
| dataNascimento | string | MM/dd/yyyy 
| areasInteressadas | string (array) | - |

### VoluntarioDto

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | long | - |
| nome | string | Not blank |
| email | string | - |
| dataNascimento | string | MM/dd/yyyy 
| dataCriacao| string | MM/dd/yyyy HH:mm
| areasInteressadas | string (array) | - |
| idsOngsSeguidas | long (array) | - |
| idsEventosConfirmados | long (array) | - |

### NovaOngDto

Usado pra criar e atualizar ONGs.

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | long | - |
| username | string | Not blank |
| senha | string | Not blank |
| nome | string | Not blank |
| descricao | string | Not blank |
| doacoes | string | - |
| dataFundacao | string | MM/dd/yyyy 
| areas | string (array) | - |
| telefone | string | - |
| email | string | - |
| urlFacebook | string | - |
| urlWebsite | string | - |
| endereco | EnderecoDto | - |

### OngDto

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | long | - |
| nome | string | Not blank |
| descricao | string | Not blank |
| doacoes | string | - |
| dataCriacao| string | MM/dd/yyyy HH:mm
| dataFundacao | string | MM/dd/yyyy 
| areas | string (array) | - |
| telefone | string | - |
| email | string | - |
| urlFacebook | string | - |
| urlWebsite | string | - |
| endereco | EnderecoDto | - |
| idsEventos | long (array) | IDs dos eventos da ong |
| idsSeguidores | long (array) | IDs dos voluntarios seguindo a ong |

### EventoDto

| Campo | Tipo | Notas |
| --- | --- | --- |
| id | long | - |
| idOng | long | - |
| nome | string | Not blank |
| descricao | string | Not blank |
| local | EnderecoDto | Not null<br>Onde o evento vai acontecer |
| dataRealizacao| string | MM/dd/yyyy HH:mm<br>Quando o evento vai acontecer
| dataCriacao| string | MM/dd/yyyy HH:mm
| areas | string (array) | - |
| idsVoluntariosConfirmados | long (array) | - |

### EnderecoDto

Usado em ONGs e eventos.

| Campo | Tipo | Notas |
| --- | --- | --- |
| uf | string | Not blank |
| cidade | string | Not blank |
| cep | string | Not blank |
| bairro | string | Not blank |
| complemento1 | string | Not blank |
| complemento2 | string | - |
