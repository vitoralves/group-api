# group-api
API REST para agrupar uma lista não ordenada de produtos através de um endpoint que recebe uma lista de produtos e parâmetros de agrupamento e ordenação.

A lista pode ser filtrada, agrupada e ordenada por qualquer atributo da classe Produto, a execução será feita nessa ordem. Caso nenhum parâmetro for enviado será agrupado por padrão na seguinte ordem de prioridade (EAN iguais, Título com 70% ou mais de semelhança e mesma marca) e ordenado por estoque em ordem decrescente e preço em ordem crescente.

# Exemplo de requisição
No exemplo abaixo quero somento produtos com número EAN = 7898100848355 agrupados por estoque e ordenados por preço em ordem crescente.

https://group-api.herokuapp.com/product?filter=ean:7898100848355&group=stock&order=price:asc

No parâmetro filter deve ser enviado o campo do produto e o valor que deve ser filtrado separados por ":".
O group deve receber o campo para agrupar valores semelhantes.
O order deve receber o campo que deseja-se agrupar e a ordenação (asc/desc).

Nenhum parâmetro é obrigatório!

# Acesso aos endpoints 

A API está disponível para testes na nuvem do Heroku e para esse fim o swagger também está habilitado e pode ser consultado pela URL https://group-api.herokuapp.com/swagger-ui.html

Endpoint para enviar uma lista e produtos. [POST] https://group-api.herokuapp.com/product

# Execução 

O arquivo compilado para execução está na raiz do projeto group-api-0.0.1-SNAPSHOT.jar e pode ser executado com o comando java - jar -Dserver.port=${PORTA PADRAO = 8080} group-api-0.0.1-SNAPSHOT.jar



