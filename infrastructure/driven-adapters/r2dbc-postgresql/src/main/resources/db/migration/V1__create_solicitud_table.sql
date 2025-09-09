CREATE TABLE if not exists estado (
                        id INTEGER PRIMARY KEY,
                        nombre VARCHAR(255) NOT NULL,
                        descripcion VARCHAR(255),
                        fecha_creacion TIMESTAMP NOT NULL,
                        fecha_actualizacion TIMESTAMP NOT NULL
);

CREATE TABLE if not exists tipo_prestamo (
                               id INTEGER PRIMARY KEY,
                               nombre VARCHAR(255) NOT NULL,
                               monto_minimo NUMERIC(19,2) NOT NULL,
                               monto_maximo NUMERIC(19,2) NOT NULL,
                               tasa_interes NUMERIC(5,4) NOT NULL,
                               validacion_automatica BOOLEAN NOT NULL,
                               fecha_creacion TIMESTAMP NOT NULL,
                               fecha_actualizacion TIMESTAMP NOT NULL
);

CREATE TABLE if not exists solicitud (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           monto NUMERIC(19,2) NOT NULL,
                           plazo INTEGER NOT NULL,
                           email VARCHAR(255) NOT NULL,
                           documento VARCHAR(255) NOT NULL,
                           id_estado INTEGER NOT NULL REFERENCES estado(id),
                           id_prestamo INTEGER NOT NULL REFERENCES tipo_prestamo(id),
                            fecha_creacion TIMESTAMP NOT NULL,
                            fecha_actualizacion TIMESTAMP NOT NULL
);

-- Índices para mejorar el rendimiento de las FK
--CREATE INDEX idx_solicitud_estado ON solicitud(id_estado);
--CREATE INDEX idx_solicitud_prestamo ON solicitud(id_prestamo);