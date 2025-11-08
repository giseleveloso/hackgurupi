-- ============================================
-- SCRIPT PARA RESETAR O BANCO DE DADOS
-- ============================================
-- ⚠️ ATENÇÃO: Este script APAGA TODOS OS DADOS!
-- Use apenas se quiser começar do zero.
-- ============================================

-- Desabilitar verificação de foreign keys temporariamente
SET FOREIGN_KEY_CHECKS = 0;

-- Dropar todas as tabelas
DROP TABLE IF EXISTS Pontuacao;
DROP TABLE IF EXISTS RepositorioDados;
DROP TABLE IF EXISTS Desafio;
DROP TABLE IF EXISTS Comentario;
DROP TABLE IF EXISTS AvaliacaoTecnica;
DROP TABLE IF EXISTS Voto;
DROP TABLE IF EXISTS Projeto;
DROP TABLE IF EXISTS ProjetoDesafio;
DROP TABLE IF EXISTS AnexoProjeto;
DROP TABLE IF EXISTS RelatorioProgresso;
DROP TABLE IF EXISTS Notificacao;
DROP TABLE IF EXISTS GestorPrefeitura;
DROP TABLE IF EXISTS Cidadao;
DROP TABLE IF EXISTS Academico;
DROP TABLE IF EXISTS Usuario;

-- Reabilitar verificação de foreign keys
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- APÓS EXECUTAR ESTE SCRIPT:
-- 1. Reinicie a aplicação - ela vai recriar as tabelas
-- 2. Execute o import.sql manualmente OU
-- 3. Use a API para cadastrar dados
-- ============================================

