INSERT INTO produtos (nome, descricao, qtd, valor, data_compra)
SELECT 'Notebook Dell Inspiron 15', 'Notebook 15.6 polegadas, 16GB RAM, SSD 512GB', 8, 3899.90, '2026-01-12'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE nome = 'Notebook Dell Inspiron 15');

INSERT INTO produtos (nome, descricao, qtd, valor, data_compra)
SELECT 'Monitor LG 24 IPS', 'Monitor 24 polegadas Full HD com painel IPS', 15, 899.00, '2026-01-10'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE nome = 'Monitor LG 24 IPS');

INSERT INTO produtos (nome, descricao, qtd, valor, data_compra)
SELECT 'Teclado Mecanico Redragon Kumara', 'Teclado ABNT2 switch blue com iluminacao', 20, 249.90, '2026-01-08'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE nome = 'Teclado Mecanico Redragon Kumara');

INSERT INTO produtos (nome, descricao, qtd, valor, data_compra)
SELECT 'Mouse Logitech G203', 'Mouse gamer com sensor de 8000 DPI', 30, 159.90, '2026-01-08'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE nome = 'Mouse Logitech G203');

INSERT INTO produtos (nome, descricao, qtd, valor, data_compra)
SELECT 'Headset HyperX Cloud Stinger', 'Headset com microfone e conector P2', 12, 299.90, '2026-01-15'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE nome = 'Headset HyperX Cloud Stinger');

INSERT INTO produtos (nome, descricao, qtd, valor, data_compra)
SELECT 'Cadeira Ergonomica Presidente', 'Cadeira com ajuste de altura e apoio lombar', 6, 1199.00, '2026-01-18'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE nome = 'Cadeira Ergonomica Presidente');

INSERT INTO produtos (nome, descricao, qtd, valor, data_compra)
SELECT 'Webcam Logitech C920', 'Webcam Full HD 1080p com microfone duplo', 10, 459.90, '2026-01-20'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE nome = 'Webcam Logitech C920');

INSERT INTO produtos (nome, descricao, qtd, valor, data_compra)
SELECT 'SSD Kingston NV2 1TB', 'SSD NVMe PCIe 4.0 para alta performance', 18, 429.90, '2026-01-22'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE nome = 'SSD Kingston NV2 1TB');

INSERT INTO produtos (nome, descricao, qtd, valor, data_compra)
SELECT 'Impressora HP Laser 107w', 'Impressora laser monocromatica com Wi-Fi', 5, 1099.00, '2026-01-25'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE nome = 'Impressora HP Laser 107w');

INSERT INTO produtos (nome, descricao, qtd, valor, data_compra)
SELECT 'Roteador TP-Link Archer AX53', 'Roteador Wi-Fi 6 dual band gigabit', 9, 579.90, '2026-01-28'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE nome = 'Roteador TP-Link Archer AX53');


