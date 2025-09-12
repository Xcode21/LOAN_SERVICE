SELECT
    s.id, s.monto, s.plazo, s.email, s.fecha_creacion,
    tp.nombre as tipo_prestamo, tp.tasa_interes,
    e.nombre as estado
FROM solicitud s
         INNER JOIN tipo_prestamo tp ON s.id_prestamo = tp.id
         INNER JOIN estado e ON s.id_estado = e.id
WHERE s.email = :email AND s.id_estado = 2