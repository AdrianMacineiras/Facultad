-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------

DROP TABLE Reserva;
DROP TABLE Espectaculo;

-- --------------------------------- Espectaculo ------------------------------------
CREATE TABLE Espectaculo ( idEspectaculo BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) COLLATE latin1_bin NOT NULL,
    duracion INTEGER NOT NULL,
    descripcion VARCHAR(1024) COLLATE latin1_bin NOT NULL,
    fechaEspectaculo DATETIME NOT NULL,
    fechaLimReserva DATETIME NOT NULL,
    maxEntradas INTEGER NOT NULL,
    precioReal FLOAT NOT NULL,
    precioDescontado FLOAT NOT NULL,
    comisionVenta FLOAT NOT NULL,
    numEntradasRest INTEGER NOT NULL,
    CONSTRAINT EspectaculoPK PRIMARY KEY(idEspectaculo), 
    CONSTRAINT validFechaLimReserva CHECK (fechaLimReserva < fechaEspectaculo),
    CONSTRAINT validDuracion CHECK ( duracion > 0 ),
    CONSTRAINT validPrecioReal CHECK ( precioReal > 0  ),
    CONSTRAINT validPrecioDescontado CHECK ( precioDescontado >= 0 AND precioDescontado <= precioReal ),
    CONSTRAINT validMaxEntradas CHECK ( maxEntradas > 0  ),
    CONSTRAINT validNumEntradasRest CHECK ( numEntradasRest >= 0  ),
    CONSTRAINT validComisionVenta CHECK ( comisionVenta >= 0  )) ENGINE = InnoDB;

-- --------------------------------- Reserva ------------------------------------

CREATE TABLE Reserva ( idReserva BIGINT NOT NULL AUTO_INCREMENT,
    idEspectaculo BIGINT NOT NULL,
    email VARCHAR(255) COLLATE latin1_bin NOT NULL,
    numTarjetaCredito VARCHAR(16) COLLATE latin1_bin NOT NULL,
    precio FLOAT NOT NULL,
    fechaReserva DATETIME NOT NULL,
	numEntradasTotal INTEGER NOT NULL,
	comprobar BOOLEAN,
    CONSTRAINT ReservaPK PRIMARY KEY(idReserva),
    CONSTRAINT validPrecio CHECK (precio > 0),
    CONSTRAINT numEntradasTotal CHECK (numEntradasTotal > 0),
    CONSTRAINT EspectaculoReservaIdFK FOREIGN KEY(idEspectaculo)
        REFERENCES Espectaculo(idEspectaculo) ON DELETE CASCADE ) ENGINE = InnoDB;


		
		