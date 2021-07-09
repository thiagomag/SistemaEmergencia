##Sistema de controle de pacientes e posologia

Sistema conta com cadastros simples do paciente (Nome e cpf) e cadastro de medicação.
Além de mostrar as medicações do paciente, sua dosagem e periodicidade.

Json para realizar o Post

Put apenas do principio ativo.

~~~Json
{
  "principioAtivo": "nome",
  "fabricante": "nome",
  "dosagem": 5,
  "periodicidade": 24,
  "horarioDose": {
      "date": {
        "year": 2021,
        "month": 7,
        "day": 8
      },
      "time": {
        "hour": 20,
        "minute": 0,
        "second": 0,
        "nano": 0
      }
  },
  "paciente": {
    "nome": "nome",
    "cpf": "123456789"
    }
}
~~~
Periodicidade em horas.