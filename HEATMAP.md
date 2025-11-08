# 🗺️ Sistema de Mapa de Calor de Votos

Sistema de visualização geográfica dos votos por localização dos cidadãos (baseado em CEP).

## 📋 Funcionalidades

- **Geocodificação automática**: Converte CEP dos cidadãos em coordenadas (lat/lng)
- **Agregação inteligente**: Agrupa votos próximos geograficamente
- **Normalização logarítmica**: Calcula peso dos pontos em escala 0-1
- **Cache de geocodificação**: Evita chamadas repetidas às APIs externas
- **Filtro por projeto**: Visualize votos de projetos específicos

## 🚀 Endpoints Criados

### 1. Mapa de Calor Geral
```
GET /heatmap?precision={3-5}
```

Retorna mapa de calor com **todos os votos**.

**Parâmetros:**
- `precision` (opcional): Precisão da agregação (3-5, padrão: 3)
  - `3` = Baixa (~100m) - Agrupa mais votos
  - `4` = Média (~10m) - Recomendado
  - `5` = Alta (~1m) - Mais detalhado

**Exemplo:**
```bash
curl http://localhost:8080/heatmap?precision=4
```

**Resposta:**
```json
[
  {
    "lat": -11.729,
    "lng": -49.068,
    "weight": 0.85
  },
  {
    "lat": -11.735,
    "lng": -49.075,
    "weight": 0.42
  }
]
```

### 2. Mapa de Calor por Projeto
```
GET /heatmap/projeto/{projetoId}?precision={3-5}
```

Retorna mapa de calor para um **projeto específico**.

**Exemplo:**
```bash
curl http://localhost:8080/heatmap/projeto/1?precision=4
```

## 🎨 Visualização Frontend

Acesse: `http://localhost:8080/heatmap-example.html`

Mapa interativo com:
- ✅ Visualização em tempo real
- ✅ Filtro por projeto
- ✅ Ajuste de precisão
- ✅ Estatísticas dos dados

## 🔧 Como Funciona

### 1. **Geocodificação (GeocodingService)**

```java
CepLocationDTO location = geocodingService.geocodeCep("77400000");
// Retorna: lat: -11.7289, lng: -49.0683
```

**Processo:**
1. Consulta **ViaCEP** para obter endereço completo
2. Usa **Nominatim (OpenStreetMap)** para geocodificar
3. Fallback: centro da cidade se endereço específico não for encontrado
4. Cache em memória para performance

### 2. **Agregação de Votos (HeatmapService)**

```java
List<HeatPointDTO> points = heatmapService.generateHeatmap(projetoId, precision);
```

**Processo:**
1. Busca todos os votos (ou filtrados por projeto)
2. Para cada voto:
   - Geocodifica o CEP do cidadão
   - Arredonda coordenadas (agregação)
   - Conta votos por localização
3. Normaliza pesos usando escala logarítmica
4. Retorna pontos para o mapa de calor

### 3. **Normalização de Peso**

```java
weight = Math.log(voteCount + 1) / Math.log(maxVotes + 1)
```

- Escala logarítmica evita que áreas com muitos votos dominem o mapa
- Valores entre 0.0 (sem votos) e 1.0 (máximo)

## 📊 Exemplos de Uso

### Frontend com Leaflet.js

```html
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<script src="https://unpkg.com/leaflet.heat@0.2.0/dist/leaflet-heat.js"></script>

<script>
  const map = L.map('map').setView([-11.7289, -49.0683], 13);
  
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
  
  fetch('/heatmap?precision=4')
    .then(r => r.json())
    .then(data => {
      const heatData = data.map(p => [p.lat, p.lng, p.weight]);
      L.heatLayer(heatData, {
        radius: 25,
        blur: 15,
        maxZoom: 17
      }).addTo(map);
    });
</script>
```

### Backend com Java

```java
@Inject
HeatmapService heatmapService;

// Mapa geral
List<HeatPointDTO> allVotes = heatmapService.generateGeneralHeatmap();

// Por projeto
List<HeatPointDTO> projectVotes = heatmapService.generateHeatmapForProjeto(1L);
```

## 🔐 Segurança e Performance

### Cache de Geocodificação
- **Evita**: Chamadas repetidas às APIs externas
- **Benefício**: Resposta instantânea para CEPs já consultados
- **Limpar cache**: `geocodingService.clearCache()`

### Rate Limiting
- **Nominatim**: 1 requisição por segundo (implementado)
- **ViaCEP**: Sem limite oficial, mas use com moderação

### Fallbacks
1. Endereço completo (rua + bairro + cidade)
2. Cidade + Estado
3. Coordenadas padrão (centro de Gurupi)

## 🎯 Casos de Uso

### 1. Dashboard de Análise
Visualize geograficamente onde estão os apoiadores de cada projeto.

### 2. Planejamento Urbano
Identifique regiões da cidade mais engajadas com a plataforma.

### 3. Segmentação
Direcione ações específicas para bairros com mais ou menos participação.

### 4. Relatórios
Exporte dados geográficos para apresentações e relatórios.

## 📝 Estrutura de Dados

### HeatPointDTO
```java
{
  "lat": BigDecimal,     // Latitude
  "lng": BigDecimal,     // Longitude  
  "weight": Double       // Peso normalizado (0.0 - 1.0)
}
```

### CepLocationDTO
```java
{
  "cep": String,
  "latitude": BigDecimal,
  "longitude": BigDecimal,
  "logradouro": String,
  "bairro": String,
  "localidade": String,
  "uf": String
}
```

## 🐛 Troubleshooting

### "CEP não encontrado"
- Verifique se o CEP tem 8 dígitos
- Teste manualmente em: https://viacep.com.br/

### "Erro ao geocodificar"
- Verifique conexão com internet
- APIs externas (ViaCEP, Nominatim) podem estar offline
- Fallback usa coordenadas padrão de Gurupi

### Cache não funciona
- Reinicie a aplicação para limpar cache
- Em produção, considere usar Redis para cache persistente

## 🚀 Melhorias Futuras

- [ ] Cache persistente (Redis)
- [ ] Geocodificação em batch (assíncrona)
- [ ] Suporte a outros países
- [ ] API de geocodificação reversa
- [ ] Exportação de dados (CSV, GeoJSON)
- [ ] Clustering de pontos próximos

## 📚 Dependências Externas

- **ViaCEP**: API gratuita de consulta de CEPs brasileiros
- **Nominatim**: Geocodificação do OpenStreetMap (gratuita)
- **Leaflet.js**: Biblioteca de mapas (frontend)
- **Leaflet.heat**: Plugin de mapa de calor (frontend)

## 📞 Suporte

Em caso de dúvidas sobre o sistema de mapa de calor, consulte a documentação das APIs:
- ViaCEP: https://viacep.com.br/
- Nominatim: https://nominatim.org/
- Leaflet: https://leafletjs.com/

