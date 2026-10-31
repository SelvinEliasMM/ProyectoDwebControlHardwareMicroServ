-- =====================================================================================
-- SCRIPT 6: TOKEN UNICO DE SEGUIMIENTO PARA ORDENES (TICKET + QR)
-- =====================================================================================
USE [ERP_Hardware_DB];
GO

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO

-- columna del token
IF NOT EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('dbo.Ordenes_Trabajo') AND name = 'token_seguimiento')
BEGIN
    ALTER TABLE [dbo].[Ordenes_Trabajo] ADD [token_seguimiento] VARCHAR(20) NULL;
    PRINT '-> Columna token_seguimiento agregada.';
END
GO

-- token para ordenes que ya existian
UPDATE [dbo].[Ordenes_Trabajo]
SET [token_seguimiento] = 'TF-' + UPPER(LEFT(REPLACE(CONVERT(VARCHAR(36), NEWID()), '-', ''), 8))
WHERE [token_seguimiento] IS NULL;
GO

-- token unico (permite nulos)
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'UQ_Ordenes_TokenSeguimiento' AND object_id = OBJECT_ID('dbo.Ordenes_Trabajo'))
BEGIN
    CREATE UNIQUE NONCLUSTERED INDEX UQ_Ordenes_TokenSeguimiento
    ON [dbo].[Ordenes_Trabajo]([token_seguimiento])
    WHERE [token_seguimiento] IS NOT NULL;
    PRINT '-> Indice unico UQ_Ordenes_TokenSeguimiento creado.';
END
GO

SELECT [id_orden], [token_seguimiento], [estado_reparacion] FROM [dbo].[Ordenes_Trabajo];
GO

