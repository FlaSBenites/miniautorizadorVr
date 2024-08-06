# Mini autorizador

A VR processa todos os dias diversas transações de Vale Refeição e Vale Alimentação, entre outras.
De forma breve, as transações saem das maquininhas de cartão e chegam até uma de nossas aplicações, conhecida como *autorizador*, que realiza uma série de verificações e análises. Essas também são conhecidas como *regras de autorização*.

Ao final do processo, o autorizador toma uma decisão, aprovando ou não a transação:
* se aprovada, o valor da transação é debitado do saldo disponível do benefício, e informamos à maquininha que tudo ocorreu bem.
* senão, apenas informamos o que impede a transação de ser feita e o processo se encerra.

Sua tarefa será construir um *mini-autorizador*. Este será uma aplicação Spring Boot com interface totalmente REST que permita:

* a criação de cartões (todo cartão deverá ser criado com um saldo inicial de R$500,00)
* a obtenção de saldo do cartão
* a autorização de transações realizadas usando os cartões previamente criados como meio de pagamento

## Regras de autorização a serem implementadas

Uma transação pode ser autorizada se:
* o cartão existir
* a senha do cartão for a correta
* o cartão possuir saldo disponível

Caso uma dessas regras não ser atendida, a transação não será autorizada.

## Estrutura do Projeto

* MiniautorizadorVrApplication
* controller
     * CartaoController
     * TransacaoController
* dto
     * TransacaoRequest
* model
    * Cartao
* repository
    * CartaoRepository
* service
    * CartaoService
    * TransacaoService

