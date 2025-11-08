-- ============================================
-- INOVA GURUPI - Dados de Exemplo
-- ============================================

-- ============================================
-- USUÁRIOS - ACADÊMICOS
-- ============================================
INSERT INTO Usuario (id, nome, email, cpf, data_nascimento, tipo_usuario, pontuacao, data_cadastro, ativo, senha) VALUES
(1, 'Dr. Carlos Silva', 'carlos.silva@email.com', '12345678901', '1985-03-15', 1, 50, '2024-01-10 10:00:00', true, '123'),
(2, 'Prof. Maria Santos', 'maria.santos@email.com', '12345678902', '1982-07-22', 1, 75, '2024-01-11 09:30:00', true, '123'),
(3, 'João Pedro Almeida', 'joao.almeida@email.com', '12345678903', '1998-11-05', 1, 30, '2024-01-15 14:20:00', true, '123');

INSERT INTO Academico (id, instituicao, curso, lattes, area_atuacao, vinculo_professor) VALUES
(1, 'UnirG - Centro Universitário', 'Engenharia Civil', 'http://lattes.cnpq.br/1234567890', 'Infraestrutura Urbana', true),
(2, 'IFTO - Instituto Federal do Tocantins', 'Ciência da Computação', 'http://lattes.cnpq.br/0987654321', 'Tecnologia e Inovação', true),
(3, 'UnirG - Centro Universitário', 'Administração', NULL, 'Gestão Pública', false);

-- ============================================
-- USUÁRIOS - CIDADÃOS
-- ============================================
INSERT INTO Usuario (id, nome, email, cpf, data_nascimento, tipo_usuario, pontuacao, data_cadastro, ativo, senha) VALUES
(4, 'Ana Paula Costa', 'ana.costa@email.com', '12345678904', '1990-05-10', 2, 25, '2024-01-20 11:00:00', true, '123'),
(5, 'Pedro Henrique Lima', 'pedro.lima@email.com', '12345678905', '1995-08-30', 2, 15, '2024-01-21 16:45:00', true, '123'),
(6, 'Juliana Ferreira', 'juliana.ferreira@email.com', '12345678906', '1988-12-18', 2, 40, '2024-01-22 08:30:00', true, '123'),
(7, 'Roberto Carlos Souza', 'roberto.souza@email.com', '12345678907', '1992-04-25', 2, 20, '2024-01-23 13:15:00', true, '123');

INSERT INTO Cidadao (id, bairro, interesses_areas) VALUES
(4, 'Centro', 'Mobilidade,Saúde'),
(5, 'Setor Oeste', 'Educação,Cultura'),
(6, 'Waldir Lins', 'Meio Ambiente,Infraestrutura'),
(7, 'São José', 'Segurança,Saúde');

-- ============================================
-- USUÁRIOS - GESTORES PREFEITURA
-- ============================================
INSERT INTO Usuario (id, nome, email, cpf, data_nascimento, tipo_usuario, pontuacao, data_cadastro, ativo, senha) VALUES
(8, 'Dra. Mariana Oliveira', 'mariana.oliveira@gurupi.to.gov.br', '12345678908', '1978-02-14', 3, 0, '2024-01-05 09:00:00', true, '123'),
(9, 'Eng. Fernando Alves', 'fernando.alves@gurupi.to.gov.br', '12345678909', '1975-09-20', 3, 0, '2024-01-05 09:00:00', true, '123');

INSERT INTO GestorPrefeitura (id, secretaria, cargo, nivel_acesso) VALUES
(8, 'Secretaria de Saúde', 'Secretária Adjunta', 2),
(9, 'Secretaria de Infraestrutura', 'Diretor de Projetos', 2);

-- ============================================
-- PROJETOS
-- ============================================
INSERT INTO Projeto (id, academico_id, titulo, resumo_popular, descricao_completa, objetivos, metodologia, resultados_esperados, orcamento_estimado, prazo_execucao, area_tematica, status, data_submissao, data_aprovacao, total_votos, total_visualizacoes, nota_tecnica, nota_popular, nota_final) VALUES
(1, 1, 'Sistema de Monitoramento de Tráfego Inteligente', 'Câmeras e sensores para reduzir congestionamentos no centro de Gurupi e melhorar o fluxo de veículos', 'Projeto visa implementar um sistema inteligente de monitoramento de tráfego utilizando câmeras e sensores IoT para coletar dados em tempo real sobre o fluxo de veículos', 'Reduzir congestionamentos; Melhorar mobilidade urbana; Fornecer dados para tomada de decisão', 'Instalação de sensores em pontos estratégicos; Desenvolvimento de plataforma de análise de dados; Integração com semáforos inteligentes', 'Redução de 30% nos congestionamentos; Economia de tempo no deslocamento; Dados para planejamento urbano', 150000.00, 12, 1, 3, '2024-02-01 10:00:00', '2024-02-10 14:30:00', 45, 234, 8.50, 4.50, 6.50),

(2, 2, 'Plataforma Digital de Telemedicina para Comunidades Rurais', 'Consultas médicas online para moradores da zona rural, facilitando acesso à saúde sem precisar ir até a cidade', 'Desenvolvimento de plataforma de telemedicina conectando médicos da cidade com moradores de áreas rurais, incluindo aplicativo mobile e sistema de agendamento', 'Ampliar acesso à saúde; Reduzir deslocamentos; Atendimento preventivo', 'Desenvolvimento de app mobile; Treinamento de agentes comunitários; Parceria com Secretaria de Saúde', 'Atendimento de 5000 pessoas no primeiro ano; Redução de 40% nas viagens para consultas básicas; Diagnósticos mais rápidos', 80000.00, 8, 2, 3, '2024-02-03 09:15:00', '2024-02-12 11:00:00', 89, 456, 9.00, 8.90, 8.95),

(3, 1, 'Revitalização da Praça Central com Energia Solar', 'Reforma da praça principal com iluminação solar, bicicletário e espaços verdes para lazer da comunidade', 'Projeto de revitalização completa da Praça Central incluindo novo paisagismo, iluminação LED com energia solar, ciclovia, academia ao ar livre e playground', 'Criar espaço de convivência; Promover sustentabilidade; Incentivar práticas saudáveis', 'Projeto arquitetônico participativo; Instalação de painéis solares; Plantio de árvores nativas; Construção de equipamentos', 'Economia de 100% na energia da praça; Aumento de 200% na frequência de visitantes; Redução de ilhas de calor', 250000.00, 10, 4, 3, '2024-02-05 16:00:00', '2024-02-15 10:20:00', 67, 389, 8.75, 6.70, 7.73),

(4, 3, 'Hub de Inovação e Empreendedorismo Gurupi', 'Espaço para empreendedores, startups e pequenos negócios com mentorias, capacitações e networking', 'Criação de hub físico de inovação com salas de coworking, auditório, laboratório maker, e programas de aceleração para startups locais', 'Fomentar empreendedorismo; Gerar empregos; Atrair investimentos; Desenvolver ecossistema inovador', 'Reforma de espaço público; Aquisição de equipamentos; Parcerias com SEBRAE e universidades; Programas de mentoria', 'Criação de 150 novos negócios em 2 anos; Geração de 300 empregos; Aumento de 50% na arrecadação via novos negócios', 180000.00, 18, 8, 2, '2024-02-08 14:30:00', NULL, 0, 145, NULL, NULL, NULL),

(5, 2, 'Aplicativo de Segurança Comunitária', 'App conectando moradores, PM e Guarda Civil para alertas de segurança em tempo real e mapa de ocorrências', 'Desenvolvimento de aplicativo mobile para comunicação direta entre população e forças de segurança, com geolocalização, botão de pânico e mapa colaborativo de ocorrências', 'Melhorar segurança pública; Agilizar atendimentos; Prevenir crimes; Fortalecer comunidade', 'Desenvolvimento mobile nativo; Integração com sistemas da PM; Treinamento de equipes; Campanha de engajamento', 'Redução de 25% no tempo de resposta a ocorrências; Aumento de 40% nas denúncias; Sensação de segurança ampliada', 120000.00, 10, 5, 1, '2024-02-12 11:00:00', NULL, 0, 78, NULL, NULL, NULL);

-- ============================================
-- VOTOS
-- ============================================
INSERT INTO Voto (id, projeto_id, cidadao_id, data_voto, ip_address) VALUES
(1, 1, 4, '2024-02-11 08:30:00', '192.168.1.10'),
(2, 1, 5, '2024-02-11 09:45:00', '192.168.1.11'),
(3, 1, 6, '2024-02-11 10:15:00', '192.168.1.12'),
(4, 2, 4, '2024-02-13 14:20:00', '192.168.1.10'),
(5, 2, 5, '2024-02-13 15:30:00', '192.168.1.11'),
(6, 2, 6, '2024-02-13 16:00:00', '192.168.1.12'),
(7, 2, 7, '2024-02-13 17:45:00', '192.168.1.13'),
(8, 3, 4, '2024-02-16 09:00:00', '192.168.1.10'),
(9, 3, 5, '2024-02-16 10:30:00', '192.168.1.11');

-- ============================================
-- AVALIAÇÕES TÉCNICAS
-- ============================================
INSERT INTO AvaliacaoTecnica (id, projeto_id, gestor_id, nota, criterio_viabilidade, criterio_impacto, criterio_inovacao, criterio_orcamento, justificativa, data_avaliacao) VALUES
(1, 1, 8, 8.50, 8.0, 9.0, 8.5, 8.5, 'Projeto bem estruturado com viabilidade técnica comprovada. Impacto significativo na mobilidade urbana.', '2024-02-09 10:30:00'),
(2, 1, 9, 8.50, 8.5, 8.5, 8.0, 9.0, 'Proposta inovadora e com orçamento realista. Recomendo aprovação com prioridade média.', '2024-02-09 14:00:00'),
(3, 2, 8, 9.00, 9.0, 10.0, 8.5, 8.5, 'Excelente projeto com potencial de transformação do acesso à saúde. Altamente recomendado.', '2024-02-11 09:00:00'),
(4, 2, 9, 9.00, 8.5, 9.5, 9.0, 9.0, 'Projeto com forte embasamento técnico e grande relevância social. Aprovação recomendada.', '2024-02-11 15:30:00'),
(5, 3, 8, 8.75, 9.0, 8.5, 8.0, 9.5, 'Projeto sustentável e bem planejado. Impacto positivo na qualidade de vida.', '2024-02-14 11:00:00'),
(6, 3, 9, 8.75, 9.5, 8.0, 8.0, 9.5, 'Boa relação custo-benefício. Alinhado com objetivos de sustentabilidade da prefeitura.', '2024-02-14 16:20:00');

-- ============================================
-- COMENTÁRIOS
-- ============================================
INSERT INTO Comentario (id, projeto_id, cidadao_id, texto, data_comentario, editado) VALUES
(1, 1, 4, 'Excelente ideia! O trânsito no centro está cada vez pior, precisamos urgentemente de soluções inteligentes.', '2024-02-11 09:00:00', false),
(2, 1, 5, 'Apoio totalmente! Será que pode incluir também dados sobre estacionamentos disponíveis?', '2024-02-11 10:00:00', false),
(3, 2, 6, 'Minha família mora na zona rural e sempre tem dificuldade para consultas. Esse projeto vai ajudar muito!', '2024-02-13 15:00:00', false),
(4, 2, 7, 'Projeto maravilhoso! Minha mãe é idosa e tem dificuldade de locomoção. A telemedicina seria perfeita.', '2024-02-13 18:00:00', false),
(5, 3, 4, 'A praça está abandonada há anos. Fico feliz em ver um projeto de revitalização com energia limpa!', '2024-02-16 10:00:00', false);

-- ============================================
-- DESAFIOS
-- ============================================
INSERT INTO Desafio (id, titulo, descricao, area_tematica, status, orcamento_disponivel, data_inicio, data_fim, prioridade) VALUES
(1, 'Redução do Desperdício de Água', 'Como podemos reduzir o desperdício de água potável em áreas urbanas de Gurupi através de soluções tecnológicas e educacionais?', 4, 1, 100000.00, '2024-03-01', '2024-08-31', 5),
(2, 'Mobilidade Universitária Sustentável', 'Desenvolver soluções de transporte sustentável entre os campi universitários e principais bairros da cidade', 1, 1, 80000.00, '2024-03-15', '2024-09-30', 4),
(3, 'Digitalização dos Serviços Públicos', 'Criar plataforma unificada para acesso digital aos serviços públicos municipais, reduzindo filas e burocracia', 8, 3, 200000.00, '2024-06-01', '2024-12-31', 5);

-- ============================================
-- REPOSITÓRIO DE DADOS
-- ============================================
INSERT INTO RepositorioDados (id, nome, descricao, url, fonte, categoria_dados, ultima_atualizacao, ativo) VALUES
(1, 'Censo IBGE Gurupi 2022', 'Dados demográficos, sociais e econômicos da população de Gurupi', 'https://cidades.ibge.gov.br/brasil/to/gurupi', 'IBGE', 'Demografia', '2023-12-15', true),
(2, 'DATASUS - Saúde Pública TO', 'Indicadores de saúde pública do Tocantins e Gurupi', 'http://www2.datasus.gov.br/', 'Ministério da Saúde', 'Saúde', '2024-01-20', true),
(3, 'Censo Escolar Gurupi', 'Dados sobre matrículas, infraestrutura e desempenho das escolas', 'https://www.gov.br/inep/pt-br/areas-de-atuacao/pesquisas-estatisticas-e-indicadores/censo-escolar', 'INEP', 'Educação', '2023-11-30', true),
(4, 'Portal da Transparência Gurupi', 'Dados sobre orçamento, licitações e gastos públicos municipais', 'https://gurupi.to.gov.br/transparencia', 'Prefeitura de Gurupi', 'Gestão Pública', '2024-02-01', true),
(5, 'Observatório de Segurança Pública TO', 'Estatísticas criminais e indicadores de segurança', 'https://www.ssp.to.gov.br/', 'SSP-TO', 'Segurança', '2024-01-15', true);

-- ============================================
-- PONTUAÇÕES
-- ============================================
INSERT INTO Pontuacao (id, usuario_id, pontos, tipo_acao, descricao, data_obtencao) VALUES
(1, 1, 20, 'PROJETO_APROVADO', 'Projeto Sistema de Monitoramento de Tráfego aprovado', '2024-02-10 14:30:00'),
(2, 1, 10, 'PROJETO_VOTADO', 'Seu projeto recebeu 10 votos', '2024-02-11 18:00:00'),
(3, 2, 20, 'PROJETO_APROVADO', 'Projeto Telemedicina aprovado', '2024-02-12 11:00:00'),
(4, 2, 10, 'PROJETO_VOTADO', 'Seu projeto recebeu 10 votos', '2024-02-13 20:00:00'),
(5, 4, 5, 'VOTO', 'Votou no projeto: Sistema de Monitoramento de Tráfego', '2024-02-11 08:30:00'),
(6, 4, 2, 'COMENTARIO', 'Comentou no projeto: Sistema de Monitoramento de Tráfego', '2024-02-11 09:00:00'),
(7, 4, 5, 'VOTO', 'Votou no projeto: Telemedicina', '2024-02-13 14:20:00'),
(8, 5, 5, 'VOTO', 'Votou no projeto: Sistema de Monitoramento de Tráfego', '2024-02-11 09:45:00'),
(9, 6, 5, 'VOTO', 'Votou no projeto: Telemedicina', '2024-02-13 16:00:00'),
(10, 6, 2, 'COMENTARIO', 'Comentou no projeto: Telemedicina', '2024-02-13 15:00:00');
