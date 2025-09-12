SELECT COUNT(*)
FROM solicitud s
         INNER JOIN tipo_prestamo tp ON s.id_prestamo = tp.id
WHERE s.id_estado = 1
  AND (:email::text IS NULL OR s.email ILIKE :email)
  AND (:tipoPrestamo::text IS NULL OR tp.nombre = :tipoPrestamo)