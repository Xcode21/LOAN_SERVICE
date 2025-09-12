SELECT
    s.id, s.monto, s.plazo, s.email, s.fecha_creacion,
    tp.nombre as tipo_prestamo, tp.tasa_interes,
    e.nombre as estado
FROM solicitud s
         INNER JOIN tipo_prestamo tp ON s.id_prestamo = tp.id
         INNER JOIN estado e ON s.id_estado = e.id
WHERE s.id_estado = 1
  AND (:email::text IS NULL OR s.email ILIKE :email)
  AND (:tipoPrestamo::text IS NULL OR tp.nombre = :tipoPrestamo)
ORDER BY
    CASE WHEN :sortBy = 'monto' AND :sortDirection = 'ASC' THEN s.monto END ASC,
    CASE WHEN :sortBy = 'monto' AND :sortDirection = 'DESC' THEN s.monto END DESC,
    CASE WHEN :sortBy = 'plazo' AND :sortDirection = 'ASC' THEN s.plazo END ASC,
    CASE WHEN :sortBy = 'plazo' AND :sortDirection = 'DESC' THEN s.plazo END DESC,
    s.fecha_creacion DESC
    LIMIT :limit OFFSET :offset
