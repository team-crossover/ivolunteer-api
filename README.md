# ivolunteer-api

Backend do projeto do Trabalho de Desenvolvimento Web

## API Endpoints

Esta sessão descreve os endpoints da API. Para ver mais sobre os objetos retornados, veja a sessão 'API Objects' abaixo.

##### Nota sobre erros

Caso uma requisição seja inválida (p. ex. não existe, não tem autorização), será retornado um objeto RespostaSimplesDto com os dados do erro.

Caso o erro seja devido a campos inválidos, a "message" do RespostaSimplesDto vai conter os campos e seus respectivos problemas.

##### Nota sobre autenticação com JWT

A autenticação no sistema é feita através de JSON Web Token (JWT). 

Primeiro você autentica mandando as credenciais do usuário para ```/api/v1/public/auth/authenticate```. Isso deve retornar uma responsta contendo o JWT no campo Authentication de seu cabeçalho.

Agora, basta salvar o JWT obtido e incluí-lo no cabeçalho de todas as próximas requisições.

### Autenticação

#### Autenticar

Usado para criar uma nova seção para um usuário. Você envia as credenciais e ele retorna o usuário (no corpo) e o JWT (no cabeçalho).

| URL | /api/v1/public/auth/authenticate |
| --- | --- |
| Tipo | POST 
| Corpo enviado (JSON) | CredenciaisDto |
| Corpo retornado (JSON) | UsuarioDto |
| Cabeçalho retornado | Authentication (contendo o JWT) |

#### Desautenticar

Invalida a sessão do JWT enviado, caso ela exista.

| URL | /api/v1/public/auth/deauthenticate |
| --- | --- |
| Tipo | GET 
| Parâmetro opcional | **all**<br>*boolean*<br>Se true, desautentica TODAS as sessões daquele usuário. |
| Cabeçalho enviado | Authentication (contento o JWT) |
| Corpo retornado (JSON) | RespostaSimplesDto |

#### Ver usuário atual

| URL | /api/v1/public/auth/whoami |
| --- | --- |
| Tipo | GET 
| Cabeçalho enviado | Authentication (contento o JWT) |
| Corpo retornado (JSON) | UsuarioDto se tiver autenticado|

### Usuários

#### Criar voluntário

| URL | /api/v1/public/users/create/voluntario |
| --- | --- |
| Tipo | POST 
| Corpo enviado (JSON) | NovoVoluntarioDto |
| Corpo retornado (JSON) | UsuarioDto |

#### Criar ong

| URL | /api/v1/public/users/create/ong |
| --- | --- |
| Tipo | POST 
| Corpo enviado (JSON) | NovaOngDto |
| Corpo retornado (JSON) | UsuarioDto |

## API Objects

Esta sessão descreve os possíveis objetos retornados pela API.

### RespostaSimplesDto

Resposta genérica dada em certas situações, como erros.

- timestamp
- status
- error
- message
- path

### CredenciaisDto

- username
- senha

### UsuarioDto

- id
- username
- tipo ("ADMIN", "ONG", "VOLUNTARIO")
- idOng (se for ONG)
- idVoluntario (se for voluntario)

### NovoVoluntarioDto

- username
- senha
- nome
- email
- dataNascimento
- areasInteressadas (array)

### NovaOngDto

- username
- senha
- nome
- descricao
- doacao
- dataFundacao ("MM/dd/yyyy")
- areas (array)
- telefone
- email (tem que ser válido)
- urlFacebook
- urlWebsite
- endereco (EnderecoDto)

### EnderecoDto

- uf
- cidade
- cep
- bairro
- complemento1
- complemento2