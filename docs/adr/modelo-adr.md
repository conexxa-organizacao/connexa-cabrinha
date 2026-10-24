# ADR-<001> — <Adoção da stack Java e React para o projeto>
- Situação: Aceita.
- Data: 2026-10-21
- Participantes: Luccas, Juan, Marco, Aaron e Vitória.

## Contexto
O Connexa busca facilitar a formação de grupos de estudo, especialmente para alunos do 1º ciclo e do período noturno, além de dar visibilidade à coordenação. O projeto conta com cinco integrantes, prazo de um semestre e diferentes níveis de conhecimento técnico. Sem integração com o sistema da faculdade, deverá validar e-mails institucionais, proteger senhas e garantir acessibilidade, uso em celulares e conformidade com a LGPD.

## Decisão
Monólito Modular (Camadas verticais);
Utilizando Java + React.

## Alternativas consideradas
Consideramos usar outras ferramentas como .NET que já possui ferramentas integradas que nos ajudariam em nosso trabalho ou até mesmo C# por ser uma linguagem muito estruturada, que ajuda a identificar erros durante a escrita do código, mas escolhemos Java, por conta de nossa equipe possuir maior conhecimento e experiência com essa linguagem.

## Consequências
- Positivas: Além de ser a stack que a equipe mais tem conhecimento, o Monólito Modular é mais adequado para modelos de projetos pequenos. Com isso, o fatiamento vertical contribui para uma maior organização do código/arquivo.
- Negativas: Por se tratar de um monólito modular, o código é em apenas um bloco e pode ter problemas de coesão e acoplamento. Dificulta no trabalho de revisão e implementação de novas ferramentas.
- Como verificar: Vamos utilizar testes de integração com JUnit 5 para garantir que as diferentes partes do sistema funcionem corretamente em conjunto, incluindo as regras de negócio e o acesso ao banco de dados.
