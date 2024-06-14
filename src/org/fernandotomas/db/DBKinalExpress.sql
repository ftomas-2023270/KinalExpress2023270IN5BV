	ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'toor';
    drop database if exists DBKinalExpress;
create database if not exists DBKinalExpress;
use DBKinalExpress;
set global time_zone = '-6:00';

create table Clientes(
    codigoCliente int not null,
    NITCliente varchar(12) not null,
    nombreCliente varchar(50) not null,
    apellidoCliente varchar(50) not null,
    direccionCliente varchar(150),
    telefonoCliente varchar(12),
    correoCliente varchar(45),
    primary key PK_Clientes(codigoCliente));
    
create table Proveedores(
    codigoProveedor int not null,
    NITProveedor varchar(12) not null,
    nombreProveedor varchar(50)not null,
    apellidoProveedor varchar(50)not null,
    direccionProveedor varchar(150),
    razonSocial varchar(60),
    contactoPrincipal varchar(100),
    paginaWebProveedor varchar(50),
    primary key PK_Proveedores(codigoProveedor));
    
create table TipoProducto(
    codigoTipoProducto int not null,
    descripcion varchar(45),
    primary key PK_TipoProducto(codigoTipoProducto));
    
create table Compras(
    numeroDocumento int not null,
    fechaDocumento DATE,
    descripcion varchar(50),
    totalDocumento decimal(10,2),
    primary key PK_Compras(numeroDocumento)
);

create table CargoEmpleado(
    codigoCargoEmpleado int,
    nombreCargo varchar(45),
    descripcionCargo varchar(100),
    primary key PK_CargoEmpleado(codigoCargoEmpleado)
);
    
create table EmailProveedor(
    codigoEmailProveedor int not null,
    emailProveedor varchar(50)not null,
    descripcion varchar(100) not null,
    Proveedores_codigoProveedores int not null,
    primary key PK_EmailProveedor(codigoEmailProveedor),
    foreign key PK_Proveedores_EmailProveedores(Proveedores_codigoProveedores)
    references Proveedores(codigoProveedor)
);
    
create table TelefonoProveedor(
	codigoTelefonoProveedor int not null,
    numeroPrincipal varchar(15) not null,
    numeroSecundario varchar(15) not null,
    observaciones varchar(45) not null,
    Proveedores_codigoProveedores int not null,
    primary key PK_TelegonoProveedor(codigoTelefonoProveedor),
    foreign key FK_Proveedores_TelefonoProveedor(Proveedores_codigoProveedores)
    references  Proveedores(codigoProveedor)
);

create table Productos(
	codigoProducto varchar(15),
	descripcionProducto varchar(45),
	precioUnitario decimal(10,2),
	precioDocena decimal(10,2),
	precioMayor decimal(10,2),
	imagenProducto varchar(45),
	existencia int,
    codigoTipoProducto int,
    codigoProveedor int,
    primary key PK_codigoProducto (codigoProducto),
    constraint FK_Productos_TipoProducto foreign key Productos(codigoTipoProducto) 
    references TipoProducto(codigoTipoProducto) on delete cascade,
    constraint FK_Productos_Proveedores foreign key Productos(codigoProveedor) 
    references Proveedores(codigoProveedor) on delete cascade
);

create table Empleados(
	codigoEmpleado int not null,
    nombreEmpleado varchar(50) not null,
    apellidoEmpleado varchar(50) not null,
    sueldo decimal(10,2) not null,
    direccion varchar(150) not null,
    turno varchar(15) not null,
	cargoEmpleado_codigoCargoEmpleado int,
    primary key PK_Empleados (codigoEmpleado),
    constraint FK_CargoEmpleados_Empleados foreign key Empleados(cargoEmpleado_codigoCargoEmpleado)
    references CargoEmpleado(codigoCargoEmpleado)
);

create table Factura(
	numeroFactura int not null,
    estado varchar(50) not null,
    totalFactura decimal(10,2) ,
    fechaFactura varchar(45) not null,
    Clientes_codigoCliente int not null,
    Empleados_codigoEmpleado int not null,
    primary key PK_Factura(numeroFactura),
    constraint FK_Clientes_Factura foreign key Factura(Clientes_codigoCliente) 
    references Clientes(codigoCliente)on  delete cascade,
    constraint FK_Empleados_Factura foreign key Factura(Empleados_codigoEmpleado) 
    references Empleados(codigoEmpleado) on delete cascade
);

create table DetalleFactura(
	codigoDetalleFactura int not null,
    precioUnitario decimal(10,2) not null,
    cantidad int,
    Factura_numeroFactura int not null,
    Productos_codigoProducto varchar(15)not null,
    primary Key PK_DetalleFactura(codigoDetalleFactura),
	constraint FK_Factura_DetalleFactura foreign key DetalleFactura(Factura_numeroFactura) 
    references Factura(numeroFactura)on  delete cascade,
    constraint FK_Productos_DetalleFactura foreign key DetalleFactura(Productos_codigoProducto) 
    references Productos(codigoProducto) on delete cascade
);

create table DetalleCompra(
	codigoDetalleCompra int not null,
    precioUnitario decimal(10,2) ,
    cantidad int,
    Productos_codigoProducto varchar(15)not null,
    Compras_numeroDocumento int not null,
    primary Key PK_DetalleFactura(codigoDetalleCompra),
    constraint FK_Productos_DetalleCompra foreign key DetalleCompra(Productos_codigoProducto) 
    references Productos(codigoProducto) on delete cascade,
	constraint FK_Compras_DetalleCompra foreign key DetalleCompra(Compras_numeroDocumento) 
    references COmpras(numeroDocumento)on  delete cascade
);

-- ------------------------------Procedimientos Almacenados-------------------------------------------------
-- ------------------------------Clientes-------------------------------------------------------------------
-- ------------------------------Agregar Clientes------------------------------------------------------------------

delimiter $$
	create procedure sp_AgregarClientes (in codigoCliente int ,in NITCliente varchar(12),in nombreCliente varchar(50),in apellidoCliente varchar(50),
	in direccionCliente varchar(150) ,in telefonoCliente varchar(12),in correoCliente varchar(45) ) 
		begin
			Insert into Clientes(codigoCliente, NITCliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente)
            values(codigoCliente, NITCliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente);
		End$$
delimiter ;

--  -------------------------Listar Clientes-----------------------------------------------
delimiter $$
	create procedure sp_ListarClientes()
		begin
			select C.codigoCliente, 
			C.NITCliente, 
			C.nombreCliente, 
			C.apellidoCliente, 
			C.direccionCliente, 
			C.telefonoCliente, 
			C.correoCliente 
			from Clientes C;
		end $$
delimiter ;


--  -------------------------Buscar Clientes-----------------------------------------------
delimiter $$
	create procedure sp_BuscarClientes(in id int)
		begin
			select C.codigoCliente, 
			C.NITCliente, 
			C.nombreCliente, 
			C.apellidoCliente, 
			C.direccionCliente, 
			C.telefonoCliente, 
			C.correoCliente 
			from Clientes C
			where id = C.codigoCliente;
		end $$
delimiter ;


--  -------------------------Eliminar Clientes-----------------------------------------------
delimiter $$
	create procedure sp_EliminarClientes(in id int)
		begin
			delete from Clientes 
			where id = codigoCliente;
		end $$
delimiter ;


--  -------------------------Editar Clientes-----------------------------------------------
delimiter $$
create procedure sp_EditarClientes (in codCliente int ,in nCliente varchar(12),in noCliente varchar(50),in apCliente varchar(50),
	in direcCliente varchar(150) ,in telCliente varchar(12),in corrCliente varchar(45) ) 
		begin
			update Clientes 
			set NITCliente=nCliente, 
			nombreCliente=noCliente, 
			apellidoCliente=apCliente,
			direccionCliente=direcCliente, 
			telefonoCliente=telCliente, 
			correoCliente=corrCliente 
			where codigoCliente =codCliente;
	End$$
delimiter ;


-- ------------------------------Proveedores-------------------------------------------------------------------
-- ------------------------------Agregar Proveedores------------------------------------------------------------------

delimiter $$
	create procedure sp_AgregarProveedores(in codigoProveedor int ,in NITProveedor varchar(12),in nombreProveedor varchar(50),in apellidoProveedor varchar(50),
	in direccionProveedor varchar(150) ,in razonSocial varchar(60),in contactoPrincipal varchar(100),in paginaWebProveedor varchar(50) ) 
		begin
			Insert into proveedores(codigoProveedor, NITProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWebProveedor)
            values(codigoProveedor, NITProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWebProveedor);
		End$$
delimiter ;

--  -------------------------Listar Proveedores-----------------------------------------------
delimiter $$
	create procedure sp_ListarProveedores()
		begin
			select codigoProveedor,
			NITProveedor,
			nombreProveedor,
			apellidoProveedor,
			direccionProveedor,
			razonSocial,
			contactoPrincipal,
			paginaWebProveedor
			from Proveedores ;
		end $$
delimiter ;


--  -------------------------Buscar  Proveedores-----------------------------------------------
delimiter $$
	create procedure sp_BuscarProveedores(in id int)
		begin
			select
			P.NITProveedor,
			P.nombreProveedor,
			P.apellidoProveedor,
			P.direccionProveedor,
			P.razonSocial,
			P.contactoPrincipal,
			P.paginaWebProveedor
			from Proceedores P
			where id =  P.codigoProveedor;
		end $$
delimiter ;


--  -------------------------Eliminar Proveedores-----------------------------------------------
delimiter $$
	create procedure sp_EliminarProveedores(in id int)
		begin
			delete from Proveedores 
			where id =  codigoProveedor;
		end $$
delimiter ;


--  -------------------------Editar Proveedores-----------------------------------------------
delimiter $$
create procedure sp_EditarProveedores (in codProveedor int ,in NProveedor varchar(12),in noProveedor varchar(50),in apProveedor varchar(50),
	in direcProveedor varchar(150) ,in rSocial varchar(60),in contactPrincipal varchar(100),in pagWebProveedor varchar(50) ) 
		begin
			update proveedores 
			set NITProveedor=NProveedor,
			nombreProveedor=noProveedor,
			apellidoProveedor=apProveedor,
			direccionProveedor=direcProveedor,
			razonSocial=rSocial,
			contactoPrincipal=contactPrincipal,
			paginaWebProveedor=pagWebProveedor
			where codigoProveedor =codProveedor;
		End$$
delimiter ;


-- -----------------------------CargoEmpleados-------------------------------------------
-- ------------------------------Agregar CargoEmpleados------------------------------------------------------------------

delimiter $$
	create procedure sp_AgregarCargoEmp(in codigoCargoEmpleado int ,in nombreCargo varchar(45),in descripcionCargo varchar(100) ) 
		begin
			Insert into cargoEmpleado(codigoCargoEmpleado,nombreCargo,descripcionCargo)
            values(codigoCargoEmpleado,nombreCargo,descripcionCargo);
		End$$
delimiter ;
--  -------------------------Listar CargoEmpleados-----------------------------------------------
delimiter $$
	create procedure sp_ListarCargoEmp()
		begin
			select codigoCargoEmpleado,
			nombreCargo,
			descripcionCargo
			from cargoEmpleado;
		end $$
delimiter ;


--  -------------------------Buscar CargoEmpleados-----------------------------------------------
delimiter $$
	create procedure sp_BuscarCargoEmp(in id int)
		begin
			select nombreCargo,
			descripcionCargo
			from cargoEmpleado
			where id = codigoCargoEmpleado;
		end $$
delimiter ;


--  -------------------------Eliminar CargoEmpleados-----------------------------------------------
delimiter $$
	create procedure sp_EliminarCargoEmp(in id int)
		begin
			delete from dbkinalexpress.cargoempleado 
			where id = codigoCargoEmpleado;
		end $$
delimiter ;

--  -------------------------Editar CargoEmpleados-----------------------------------------------
delimiter $$
create procedure sp_EditarCargoEmp(in _codigoCargoEmpleado int ,in _nombreCargo varchar(45),in _descripcionCargo varchar(100)) 
		begin
			update CargoEmpleado 
			set nombreCargo=_nombreCargo,
			descripcionCargo=_descripcionCargo
			where _codigoCargoEmpleado = codigoCargoEmpleado;
		End$$
delimiter ;

-- -----------------------------TipoProducto-------------------------------------------
-- ------------------------------Agregar TipoProducto------------------------------------------------------------------

delimiter $$
	create procedure sp_AgregarTipoProducto(in codigoTipoProducto int , in descripcion varchar(45) ) 
		begin
			Insert into TipoProducto(codigoTipoProducto,descripcion)
			values(codigoTipoProducto,descripcion);
		End$$
delimiter ;

--  -------------------------Listar TipoProducto-----------------------------------------------
delimiter $$
	create procedure sp_ListarTipoProducto()
		begin
			select codigoTipoProducto,
			descripcion
			from TipoProducto;
		end $$
delimiter ;


--  -------------------------Buscar TipoProducto-----------------------------------------------
delimiter $$
	create procedure sp_BuscarTipoProducto(in id int)
		begin
			select descripcion
			from TipoProducto
			where id = codigoTipoProducto;
		end $$
delimiter ;


--  -------------------------Eliminar TipoProducto-----------------------------------------------
delimiter $$
	create procedure sp_EliminarTipoProducto(in id int)
		begin
			delete from TipoProducto 
			where id = codigoTipoProducto;
		end $$
delimiter ;


--  -------------------------Editar TipoProducto-----------------------------------------------
delimiter $$
create procedure sp_EditarTipoProducto(in _codigoTipoProducto int ,in _descripcion varchar(45)) 
		begin
			update CargoEmpleado 
			set descripcion= _descripcion
			where _codigoTipoProducto = codigoTipoProducto;
		End$$
delimiter ;

-- -----------------------------Compras-------------------------------------------
-- ------------------------------Agregar Compras------------------------------------------------------------------

delimiter $$
	create procedure sp_AgregarCompras(in numeroDocumento int ,in fechaDocumento date, in descripcion varchar(60), in totalDocumento decimal(10,2))  
		begin
			Insert into Compras(numeroDocumento,fechaDocumento,descripcion,totalDocumento)
			values(numeroDocumento,fechaDocumento,descripcion,totalDocumento);
	End$$
delimiter ;


--  -------------------------Listar Compras-----------------------------------------------
delimiter $$
	create procedure sp_ListarCompras()
		begin
				select numeroDocumento,
				fechaDocumento,
				descripcion,
				totalDocumento
				from Compras;
		end $$
delimiter ;


--  -------------------------Buscar Compras-----------------------------------------------
delimiter $$
	create procedure sp_BuscarCompras(in id int)
		begin
			select fechaDocumento,
				descripcion,
				totalDocumento 
				from Compras
				where id = numeroDocumento;
		end $$
delimiter ;


--  -------------------------Eliminar Compras-----------------------------------------------
delimiter $$
	create procedure sp_EliminarCompras(in id int)
		begin
			delete from Compras 
			where id = numeroDocumento;
		end $$
delimiter ;

--  -------------------------Editar Compras-----------------------------------------------
delimiter $$
create procedure sp_EditarCompras(in _numeroDocumento int ,in _fechaDocumento date, in _descripcion varchar(60), in _totalDocumento decimal(10,2)) 
		begin
			update Compras 
			set fechaDocumento=_fechaDocumento,
			descripcion= _descripcion,
			totalDocumento=_totalDocumento
			where _numeroDocumento = numeroDocumento;
	End$$
delimiter ;


-- -----------------------------Productos-------------------------------------------
-- ------------------------------Agregar Productos------------------------------------------------------------------

delimiter $$
	create procedure sp_AgregarProductos(in codigoProducto varchar(15),in descripcionProducto varchar(45),in precioUnitario decimal(10,2),in precioDocena decimal(10,2),
in precioMayor decimal(10,2),in existencia int,in codigoTipoProducto int,in codigoProveedor int)  
		begin
			Insert into Productos(codigoProducto , descripcionProducto ,precioUnitario , precioDocena ,
            precioMayor , existencia , codigoTipoProducto , codigoProveedor) 
            values ( codigoProducto , descripcionProducto ,precioUnitario , precioDocena ,
            precioMayor , existencia , codigoTipoProducto , codigoProveedor);
	End$$
delimiter ; 

--  -------------------------Listar Productos-----------------------------------------------
delimiter $$
	create procedure sp_ListarProductos()
		begin
				select codigoProducto,
                descripcionProducto, 
                precioUnitario, 
                precioDocena, 
                precioMayor, 
                existencia, 
                codigoTipoProducto, 
                codigoProveedor 
				from Productos;
		end $$
delimiter ; 

--  -------------------------Buscar Productos-----------------------------------------------
delimiter $$
	create procedure sp_BuscarProductos(in id int)
		begin
			select descripcionProducto, 
                precioUnitario, 
                precioDocena, 
                precioMayor, 
                existencia, 
                codigoTipoProducto, 
                codigoProveedor 
				from Productos
				where id = codigoProducto ;
		end $$
delimiter ; 

--  -------------------------Eliminar Productos-----------------------------------------------
delimiter $$
	create procedure sp_EliminarProductos(in id int)
		begin
			delete from Productos 
			where id = codigoProducto;
		end $$
delimiter ; 


--  -------------------------Editar Productos-----------------------------------------------
delimiter $$
create procedure sp_EditarProductos(in _codigoProducto varchar(15),in _descripcionProducto varchar(45),in _precioUnitario decimal(10,2),in _precioDocena decimal(10,2),
in _precioMayor decimal(10,2),in _existencia int,in _codigoTipoProducto int,in _codigoProveedor int)  
		begin
			update Productos 
			set descripcionProducto=_descripcionProducto, 
                precioUnitario=_precioUnitario, 
                precioDocena=_precioDocena, 
                precioMayor=_precioMayor,
                existencia=_existencia, 
                codigoTipoProducto=_codigoTipoProducto, 
                codigoProveedor = _codigoProveedor
			where _codigoProducto = codigoProducto;
	End$$
delimiter ;

-- ------------------------------------------------Empleados ---------------------------------------------------------------------
-- ------------------------------------------------ Agregar Empleados ---------------------------------------------------------------------
Delimiter $$
	Create procedure sp_agregarEmpleados(in codigoEmpleado int  ,in nombreEmpleado varchar(50)  ,in apellidoEmpleado varchar(50) ,
    in sueldo decimal(10,2)  ,in direccion varchar(150),in turno varchar(15) ,in cargoEmpleado_codigoCargoEmpleado int)  
		begin
			Insert into Empleados(codigoEmpleado , nombreEmpleado ,apellidoEmpleado , sueldo ,
            direccion , turno , cargoEmpleado_codigoCargoEmpleado ) 
            values (codigoEmpleado , nombreEmpleado ,apellidoEmpleado , sueldo ,
            direccion , turno , cargoEmpleado_codigoCargoEmpleado ) ;
		End$$
Delimiter ;
-- -------------------------------------------------- Listar empleados ----------------------------------------------------------------------
Delimiter $$
Create procedure sp_listarEmpleados()
    Begin
        Select 
           codigoEmpleado, 
           nombreEmpleado, 
           apellidoEmpleado, 
           sueldo, 
           direccion, 
           turno, 
           cargoEmpleado_codigoCargoEmpleado 
        from Empleados ;
    End $$
Delimiter ;
-- -------------------------------------------  Buscar Empleado -----------------------------------------------------------------------------------
Delimiter $$
Create procedure sp_buscarEmpleados(in id int)
    Begin
        Select 
           nombreEmpleado, 
           apellidoEmpleado, 
           sueldo, 
           direccion, 
           turno, 
           cargoEmpleado_codigoCargoEmpleado 
        from Empleados 
        where id = codigoEmpleado ;
        End $$
Delimiter ;
-- ----------------------------------------------Editar Empleado -----------------------------------------------------------------------------------
Delimiter $$
Create procedure sp_editarEmpleado(in _codigoEmpleado int  ,in _nombreEmpleado varchar(50)  ,in _apellidoEmpleado varchar(50) ,
    in _sueldo decimal(10,2)  ,in _direccion varchar(150),in _turno varchar(15) ,in _cargoEmpleado_codigoCargoEmpleado int)  
    begin
        Update Empleados
            Set
            nombreEmpleado = _nombreEmpleado,
            apellidoEmpleado = _apellidoEmpleado,
            sueldo = _sueldo,
            direccion = _direccion,
            turno = _turno,
            cargoEmpleado_codigoCargoEmpleado = _cargoEmpleado_codigoCargoEmpleado
            where _codigoEmpleado = codigoEmpleado;
    End$$
Delimiter ;
-- -------------------------------------------------- Eliminar empleado ----------------------------------------------------------------------------------------
Delimiter $$
Create procedure sp_eliminarEmpleado(in id int)
    Begin
        Delete from Empleados 
			where id = codigoEmpleado;
    End$$
Delimiter ;


-- -----------------------------DetalleFacturas-------------------------------------------
-- ------------------------------Agregar DetalleFactura------------------------------------------------------------------

delimiter $$
	create procedure sp_AgregarDetalleFactura(in codigoDetalleFactura int,in precioUnitario decimal(10,2)  ,
in cantidad int,in Factura_numeroFactura int  ,in Productos_codigoProducto varchar(15) )
		begin
			Insert into DetalleFactura(codigoDetalleFactura, precioUnitario, cantidad, Factura_numeroFactura, Productos_codigoProducto)
			values(codigoDetalleFactura, precioUnitario, cantidad, Factura_numeroFactura, Productos_codigoProducto);
	End$$
delimiter ;

--  -------------------------Listar DetalleFacturas-----------------------------------------------
delimiter $$
	create procedure sp_ListarDetalleFactura()
		begin
				select codigoDetalleFactura,
				precioUnitario, 
                cantidad,
                Factura_numeroFactura, 
                Productos_codigoProducto 
				from DetalleFactura;
		end $$
delimiter ;

--  -------------------------Buscar DetalleFacturas-----------------------------------------------
delimiter $$
	create procedure sp_BuscarDetalleFactura(in id int)
		begin
			select precioUnitario, 
                cantidad,
                Factura_numeroFactura, 
                Productos_codigoProducto 
				from DetalleFactura
				where id = codigoDetalleFactura;
		end $$
delimiter ;

--  -------------------------Eliminar DetalleFactura-----------------------------------------------
delimiter $$
	create procedure sp_EliminarDetalleFactura(in id int)
		begin
			delete from DetalleFactura 
			where id = codigoDetalleFactura;
		end $$
delimiter ;

--  -------------------------Editar DetalleFactura-----------------------------------------------
delimiter $$
create procedure sp_EditarDetalleFactura(in _codigoDetalleFactura int,in _precioUnitario decimal(10,2)  ,
in _cantidad int,in _Factura_numeroFactura int  ,in _Productos_codigoProducto varchar(15) )
		begin
			update DetalleFactura 
			set precioUnitario=_precioUnitario,
            cantidad=_cantidad, 
            Factura_numeroFactura=_Factura_numeroFactura, 
            Productos_codigoProducto = _Productos_codigoProducto
			where codigoDetalleFactura = _codigoDetalleFactura;
	End$$
delimiter ;


-- -----------------------------DetalleCompra-------------------------------------------
-- ------------------------------Agregar DetalleCompra------------------------------------------------------------------

delimiter $$
	create procedure sp_AgregarDetalleCompra(in codigoDetalleFactura int,in precioUnitario decimal(10,2)  ,
in cantidad int,in Productos_codigoProducto varchar(15),in Compras_numeroDocumento int   )
		begin
			Insert into DetalleCompra(codigoDetalleFactura, precioUnitario, cantidad, Productos_codigoProducto,Compras_numeroDocumento )
			values(codigoDetalleFactura, precioUnitario, cantidad,Productos_codigoProducto,Compras_numeroDocumento);
	End$$
delimiter ;

--  -------------------------Listar DetalleFacturas-----------------------------------------------
delimiter $$
	create procedure sp_ListarDetalleCompra()
		begin
				select codigoDetalleCompra,
				precioUnitario, 
                cantidad,
                Productos_codigoProducto, 
                Compras_numeroDocumento 
				from DetalleCompra;
		end $$
delimiter ;

--  -------------------------Buscar DetalleFacturas-----------------------------------------------
delimiter $$
	create procedure sp_BuscarDetalleCompra(in id int)
		begin
			select precioUnitario, 
                cantidad,
                Productos_codigoProducto,
                Compras_numeroDocumento
				from DetalleFactura
				where id = codigoDetalleCompra;
		end $$
delimiter ;

--  -------------------------Eliminar DetalleFactura-----------------------------------------------
delimiter $$
	create procedure sp_EliminarDetalleCompra(in id int)
		begin
			delete from DetalleCompra 
			where id = codigoDetalleCompra;
		end $$
delimiter ;

--  -------------------------Editar DetalleFactura-----------------------------------------------
delimiter $$
create procedure sp_EditarDetalleCompra(in _codigoDetalleFactura int,in _precioUnitario decimal(10,2)  ,
in _cantidad int,in _Productos_codigoProducto varchar(15),in _Compras_numeroDocumento int   )
		begin
			update DetalleCompra 
			set precioUnitario=_precioUnitario,
            cantidad=_cantidad, 
            Productos_codigoProducto = _Productos_codigoProducto,
            _Compras_numeroDocumento = __Compras_numeroDocumento
			where codigoDetalleCompra = _codigoDetalleCompra;
	End$$
delimiter ;



-- -----------------------------emailProveedor-------------------------------------------
-- ------------------------------Agregar emailProveedor------------------------------------------------------------------

delimiter $$
	create procedure sp_AgregarEmailProveedor(in codigoEmailProveedor int,in emailProveedor varchar(50), in  descripcion varchar(100) ,
    in Proveedores_codigoProveedores int)
		begin
			Insert into emailProveedor( codigoEmailProveedor, emailProveedor, descripcion, Proveedores_codigoProveedores )
			values( codigoEmailProveedor, emailProveedor, descripcion, Proveedores_codigoProveedores );
	End$$
delimiter ;

--  -------------------------Listar emailProveedor-----------------------------------------------
delimiter $$
	create procedure sp_ListarEmailProveedor()
		begin
				select codigoEmailProveedor,
				emailProveedor, 
                descripcion, 
                Proveedores_codigoProveedores
				from EmailProveedor;
		end $$
delimiter ;

--  -------------------------Buscar emailProveedor-----------------------------------------------
delimiter $$
	create procedure sp_BuscarEmailProveedor(in id int)
		begin
			select emailProveedor, 
            descripcion, 
            Proveedores_codigoProveedores
				from EmailProveedor
				where id = codigoEmailProveedor;
		end $$
delimiter ;

--  -------------------------Eliminar emailProveedor-----------------------------------------------
delimiter $$
	create procedure sp_EliminarEmailProveedor(in id int)
		begin
			delete from EmailProveedor 
			where id = codigoEmailProveedor;
		end $$
delimiter ;

--  -------------------------Editar emailProveedor-----------------------------------------------
delimiter $$
create procedure sp_EditarEmailProveedor(in _codigoEmailProveedor int,in _emailProveedor varchar(50), in  _descripcion varchar(100) ,
    in _Proveedores_codigoProveedores int)
		begin
			update EmailProveedor 
			set  
            emailProveedor = _emailProveedor, 
            descripcion=_descripcion, 
            Proveedores_codigoProveedores=_Proveedores_codigoProveedores
			where codigoEmailProveedor = _codigoEmailProveedor;
	End$$
delimiter ;

-- -----------------------------TelefonoProveedor-------------------------------------------
-- ------------------------------Agregar TelefonoProveedor------------------------------------------------------------------

delimiter $$
	create procedure sp_AgregarTelefonoProveedor(in codigoTelefonoProveedor int , in  numeroPrincipal varchar(15),in numeroSecundario varchar(15) ,
   in observaciones varchar(45) , in Proveedores_codigoProveedores int)
		begin
			Insert into TelefonoProveedor( codigoTelefonoProveedor, numeroPrincipal, numeroSecundario, observaciones, Proveedores_codigoProveedores )
			values( codigoTelefonoProveedor, numeroPrincipal, numeroSecundario, observaciones, Proveedores_codigoProveedores );
	End$$
delimiter ;

--  -------------------------Listar emailProveedor-----------------------------------------------
delimiter $$
	create procedure sp_ListarTelefonoProveedor()
		begin
				select codigoTelefonoProveedor, 
                numeroPrincipal, 
                numeroSecundario, 
                observaciones, 
                Proveedores_codigoProveedores
				from TelefonoProveedor;
		end $$
delimiter ;

--  -------------------------Buscar emailProveedor-----------------------------------------------
delimiter $$
	create procedure sp_BuscarTelefonoProveedor(in id int)
		begin
			select numeroPrincipal, 
                numeroSecundario, 
                observaciones, 
                Proveedores_codigoProveedores
				from TelefonoProveedor
				where id = codigoTelefonoProveedor;
		end $$
delimiter ;

--  -------------------------Eliminar emailProveedor-----------------------------------------------
delimiter $$
	create procedure sp_EliminarTelefonoProveedor(in id int)
		begin
			delete from TelefonoProveedor 
			where id = codigoTelefonoProveedor;
		end $$
delimiter ;

--  -------------------------Editar emailProveedor-----------------------------------------------
delimiter $$
create procedure sp_EditarTelefonoProveedor(in _codigoTelefonoProveedor int , in  _numeroPrincipal varchar(15),in _numeroSecundario varchar(15) ,
   in _observaciones varchar(45) , in _Proveedores_codigoProveedores int)
		begin
			update TelefonoProveedor 
			set  
            numeroPrincipal=_numeroPrincipal, 
			numeroSecundario=_numeroSecundario, 
			observaciones=_observaciones, 
			Proveedores_codigoProveedores=_Proveedores_codigoProveedores
			where codigoTelefonoProveedor = _codigoTelefonoProveedor;
	End$$
delimiter ;

-- -----------------------------Factura -------------------------------------------
-- ------------------------------Agregar Factura------------------------------------------------------------------

delimiter $$
	create procedure sp_AgregarFactura(in numeroFactura int,in estado varchar(50),in totalFactura decimal(10,2),in fechaFactura varchar(45),
  in Clientes_codigoCliente int, in Empleados_codigoEmpleado int)
		begin
			Insert into Factura( numeroFactura, estado, totalFactura, fechaFactura, Clientes_codigoCliente, Empleados_codigoEmpleado )
			values( numeroFactura, estado, totalFactura, fechaFactura, Clientes_codigoCliente, Empleados_codigoEmpleado );
	End$$
delimiter ;

--  -------------------------Listar TelefonoProveedor-----------------------------------------------
delimiter $$
	create procedure sp_ListarFactura()
		begin
				select 
                numeroFactura, 
                estado, 
                totalFactura, 
                fechaFactura, 
                Clientes_codigoCliente, 
                Empleados_codigoEmpleado
				from Factura;
		end $$
delimiter ;

--  -------------------------Buscar TelefonoProveedor-----------------------------------------------
delimiter $$
	create procedure sp_BuscarFactura(in id int)
		begin
			select estado, 
                totalFactura, 
                fechaFactura, 
                Clientes_codigoCliente, 
                Empleados_codigoEmpleado
				from Factura
				where id = numeroFactura;
		end $$
delimiter ;

--  -------------------------Eliminar TelefonoProveedor-----------------------------------------------
delimiter $$
	create procedure sp_EliminarFactura(in id int)
		begin
			delete from Factura 
			where id = codigoFactura;
		end $$
delimiter ;

--  -------------------------Editar TelefonoProveedor-----------------------------------------------
delimiter $$
create procedure sp_EditarFactura(in _numeroFactura int,in _estado varchar(50),in _totalFactura decimal(10,2),in _fechaFactura varchar(45),
  in _Clientes_codigoCliente int, in _Empleados_codigoEmpleado int)
		begin
			update Factura 
				set  
				estado=_estado, 
                totalFactura=_totalFactura, 
                fechaFactura=_fechaFactura, 
                Clientes_codigoCliente=_Clientes_codigoCliente, 
                Empleados_codigoEmpleado=_Empleados_codigoEmpleado
				where numeroFactura = _numeroFactura;
	End$$
delimiter ;

CALL sp_AgregarClientes(1, '123456789012', 'Juan', 'Pérez', 'Calle Falsa 123', '5551234567', 'juan.perez@example.com');
CALL sp_AgregarClientes(2, '987654321098', 'Ana', 'García', 'Avenida Siempre Viva 742', '5559876543', 'ana.garcia@example.com');
CALL sp_AgregarClientes(3, '123123123123', 'Luis', 'Martínez', 'Boulevard de los Sueños Rotos 5', '5551239876', 'luis.martinez@example.com');
CALL sp_AgregarClientes(4, '321321321321', 'María', 'López', 'Calle del Olvido 23', '5553216548', 'maria.lopez@example.com');
CALL sp_AgregarClientes(5, '456456456456', 'Carlos', 'Rodríguez', 'Paseo de la Reforma 100', '5554567891', 'carlos.rodriguez@example.com');
CALL sp_AgregarClientes(6, '654654654654', 'Elena', 'Sánchez', 'Plaza de la Constitución 1', '5556543214', 'elena.sanchez@example.com');
CALL sp_AgregarClientes(7, '789789789789', 'Pedro', 'González', 'Callejón del Beso 12', '5557894563', 'pedro.gonzalez@example.com');
CALL sp_AgregarClientes(8, '987987987987', 'Sofía', 'Fernández', 'Camino Real 45', '5559873217', 'sofia.fernandez@example.com');
CALL sp_AgregarClientes(9, '741741741741', 'David', 'Jiménez', 'Ruta del Sol 6', '5557418529', 'david.jimenez@example.com');
CALL sp_AgregarClientes(10, '852852852852', 'Laura', 'Hernández', 'Vía Láctea 89', '5558529634', 'laura.hernandez@example.com');

CALL sp_AgregarProveedores(1, '123456789012', 'Juan', 'Pérez', 'Calle Falsa 123', 'Proveedores S.A.', 'Juan Pérez', 'www.proveedoressa.com');
CALL sp_AgregarProveedores(2, '987654321098', 'Ana', 'García', 'Avenida Siempre Viva 742', 'Distribuciones Ana', 'Ana García', 'www.distribucionesana.com');
CALL sp_AgregarProveedores(3, '123123123123', 'Luis', 'Martínez', 'Boulevard de los Sueños Rotos 5', 'Martínez y Cía.', 'Luis Martínez', 'www.martinezycia.com');
CALL sp_AgregarProveedores(4, '321321321321', 'María', 'López', 'Calle del Olvido 23', 'Suministros López', 'María López', 'www.suministroslopez.com');
CALL sp_AgregarProveedores(5, '456456456456', 'Carlos', 'Rodríguez', 'Paseo de la Reforma 100', 'Rodríguez Proveeduría', 'Carlos Rodríguez', 'www.rodriguezproveeduria.com');
CALL sp_AgregarProveedores(6, '654654654654', 'Elena', 'Sánchez', 'Plaza de la Constitución 1', 'Sánchez y Asociados', 'Elena Sánchez', 'www.sanchezyasociados.com');
CALL sp_AgregarProveedores(7, '789789789789', 'Pedro', 'González', 'Callejón del Beso 12', 'González Distribuciones', 'Pedro González', 'www.gonzalezdistribuciones.com');
CALL sp_AgregarProveedores(8, '987987987987', 'Sofía', 'Fernández', 'Camino Real 45', 'Fernández Proveedores', 'Sofía Fernández', 'www.fernandezproveedores.com');
CALL sp_AgregarProveedores(9, '741741741741', 'David', 'Jiménez', 'Ruta del Sol 6', 'Jiménez Suministros', 'David Jiménez', 'www.jimenezsuministros.com');
CALL sp_AgregarProveedores(10, '852852852852', 'Laura', 'Hernández', 'Vía Láctea 89', 'Hernández y Cía.', 'Laura Hernández', 'www.hernandezycia.com');

CALL sp_AgregarCargoEmp(1, 'Gerente General', 'Responsable de la dirección general de la empresa.');
CALL sp_AgregarCargoEmp(2, 'Subgerente', 'Asiste al gerente general y supervisa áreas específicas.');
CALL sp_AgregarCargoEmp(3, 'Jefe de Ventas', 'Dirige el equipo de ventas y estrategias comerciales.');
CALL sp_AgregarCargoEmp(4, 'Analista Financiero', 'Encargado del análisis financiero y presupuestos.');
CALL sp_AgregarCargoEmp(5, 'Coordinador de Recursos Humanos', 'Gestión y coordinación del personal y nómina.');
CALL sp_AgregarCargoEmp(6, 'Desarrollador de Software', 'Desarrollo y mantenimiento de sistemas de software.');
CALL sp_AgregarCargoEmp(7, 'Ingeniero de Sistemas', 'Planificación y gestión de infraestructura de TI.');
CALL sp_AgregarCargoEmp(8, 'Representante de Servicio al Cliente', 'Atención y soporte a los clientes de la empresa.');
CALL sp_AgregarCargoEmp(9, 'Marketing Digital', 'Estrategias y gestión de campañas de marketing digital.');
CALL sp_AgregarCargoEmp(10, 'Contador', 'Manejo de la contabilidad y registros financieros de la empresa.');

CALL sp_AgregarTipoProducto(1, 'Electrónica');
CALL sp_AgregarTipoProducto(2, 'Alimentos');
CALL sp_AgregarTipoProducto(3, 'Ropa');
CALL sp_AgregarTipoProducto(4, 'Muebles');
CALL sp_AgregarTipoProducto(5, 'Libros');
CALL sp_AgregarTipoProducto(6, 'Juguetes');
CALL sp_AgregarTipoProducto(7, 'Herramientas');
CALL sp_AgregarTipoProducto(8, 'Artículos de Oficina');
CALL sp_AgregarTipoProducto(9, 'Cosméticos');
CALL sp_AgregarTipoProducto(10, 'Deportes');

CALL sp_AgregarCompras(1001, '2024-01-15', 'Compra de equipos de oficina', 1250.75);
CALL sp_AgregarCompras(1002, '2024-01-20', 'Compra de materiales de construcción', 5800.50);
CALL sp_AgregarCompras(1003, '2024-01-25', 'Compra de muebles para la sala de reuniones', 2340.90);
CALL sp_AgregarCompras(1004, '2024-02-01', 'Compra de insumos de limpieza', 450.00);
CALL sp_AgregarCompras(1005, '2024-02-10', 'Compra de productos electrónicos', 6750.30);
CALL sp_AgregarCompras(1006, '2024-02-15', 'Compra de software de gestión', 3200.45);
CALL sp_AgregarCompras(1007, '2024-02-20', 'Compra de alimentos para eventos', 1200.00);
CALL sp_AgregarCompras(1008, '2024-03-01', 'Compra de uniformes para el personal', 1950.80);
CALL sp_AgregarCompras(1009, '2024-03-05', 'Compra de materiales de oficina', 890.25);
CALL sp_AgregarCompras(1010, '2024-03-10', 'Compra de equipos de telecomunicaciones', 4890.60);

CALL sp_AgregarProductos('P001', 'Laptop Dell', 750.00, 9000.00, 680.00, 50, 1, 1);
CALL sp_AgregarProductos('P002', 'Smartphone Samsung', 500.00, 6000.00, 450.00, 100, 1, 2);
CALL sp_AgregarProductos('P003', 'Camisa Hombre', 25.00, 270.00, 22.00, 200, 3, 3);
CALL sp_AgregarProductos('P004', 'Escritorio Oficina', 150.00, 1700.00, 130.00, 30, 4, 4);
CALL sp_AgregarProductos('P005', 'Libro de Programación', 30.00, 350.00, 28.00, 80, 5, 5);
CALL sp_AgregarProductos('P006', 'Juguete Educativo', 15.00, 160.00, 13.00, 150, 6, 6);
CALL sp_AgregarProductos('P007', 'Martillo', 10.00, 110.00, 9.00, 60, 7, 7);
CALL sp_AgregarProductos('P008', 'Papel de Oficina', 3.00, 30.00, 2.50, 500, 8, 8);
CALL sp_AgregarProductos('P009', 'Set de Maquillaje', 20.00, 220.00, 18.00, 100, 9, 9);
CALL sp_AgregarProductos('P010', 'Balón de Fútbol', 25.00, 270.00, 22.00, 120, 10, 10);

CALL sp_AgregarEmailProveedor(1, 'contacto@proveedoressa.com', 'Correo principal de contacto', 1);
CALL sp_AgregarEmailProveedor(2, 'ventas@distribucionesana.com', 'Correo para ventas', 2);
CALL sp_AgregarEmailProveedor(3, 'soporte@martinezycia.com', 'Correo de soporte técnico', 3);
CALL sp_AgregarEmailProveedor(4, 'info@suministroslopez.com', 'Correo de información general', 4);
CALL sp_AgregarEmailProveedor(5, 'pedidos@rodriguezproveeduria.com', 'Correo para pedidos', 5);
CALL sp_AgregarEmailProveedor(6, 'admin@sanchezyasociados.com', 'Correo administrativo', 6);
CALL sp_AgregarEmailProveedor(7, 'servicio@gonzalezdistribuciones.com', 'Correo de servicio al cliente', 7);
CALL sp_AgregarEmailProveedor(8, 'compras@fernandezproveedores.com', 'Correo para compras', 8);
CALL sp_AgregarEmailProveedor(9, 'marketing@jimenezsuministros.com', 'Correo para marketing', 9);
CALL sp_AgregarEmailProveedor(10, 'contacto@hernandezycia.com', 'Correo principal de contacto', 10);

CALL sp_AgregarTelefonoProveedor(1, '555-1234567', '555-7654321', 'Teléfonos de contacto principal', 1);
CALL sp_AgregarTelefonoProveedor(2, '555-2345678', '555-8765432', 'Teléfonos de ventas y atención al cliente', 2);
CALL sp_AgregarTelefonoProveedor(3, '555-3456789', '555-9876543', 'Soporte técnico y administración', 3);
CALL sp_AgregarTelefonoProveedor(4, '555-4567890', '555-0987654', 'Contacto para pedidos e información', 4);
CALL sp_AgregarTelefonoProveedor(5, '555-5678901', '555-1098765', 'Líneas de atención al cliente', 5);
CALL sp_AgregarTelefonoProveedor(6, '555-6789012', '555-2109876', 'Números de atención y soporte', 6);
CALL sp_AgregarTelefonoProveedor(7, '555-7890123', '555-3210987', 'Teléfonos de servicio y atención', 7);
CALL sp_AgregarTelefonoProveedor(8, '555-8901234', '555-4321098', 'Líneas de contacto para compras', 8);
CALL sp_AgregarTelefonoProveedor(9, '555-9012345', '555-5432109', 'Teléfonos para marketing y ventas', 9);
CALL sp_AgregarTelefonoProveedor(10, '555-0123456', '555-6543210', 'Contactos principales y secundarios', 10);

CALL sp_agregarEmpleados(1, 'María', 'García', 2800.50, 'Avenida Central 456', 'Tarde', 2);
CALL sp_agregarEmpleados(2, 'Carlos', 'Martínez', 3200.75, 'Calle 7 de Abril 789', 'Mañana', 3);
CALL sp_agregarEmpleados(3, 'Ana', 'López', 2500.00, 'Avenida Principal 321', 'Noche', 1);
CALL sp_agregarEmpleados(4, 'Pedro', 'Sánchez', 3000.00, 'Calle del Bosque 45', 'Tarde', 4);
CALL sp_agregarEmpleados(5, 'Laura', 'Ramírez', 2700.25, 'Avenida Libertad 890', 'Mañana', 2);
CALL sp_agregarEmpleados(6, 'Roberto', 'Gómez', 2900.80, 'Calle Rosales 67', 'Tarde', 3);
CALL sp_agregarEmpleados(7, 'Sofía', 'Hernández', 2600.50, 'Avenida del Sol 123', 'Noche', 1);
CALL sp_agregarEmpleados(8, 'Juan', 'Torres', 3100.00, 'Calle Central 456', 'Mañana', 4);
CALL sp_agregarEmpleados(19, 'Marcela', 'Díaz', 2850.75, 'Avenida Primavera 789', 'Tarde', 2);
CALL sp_agregarEmpleados(10, 'Luis', 'Martí', 2950.60, 'Calle Mayor 567', 'Noche', 3);


CALL sp_AgregarFactura(1, 'Pagada', 1500.00, '2024-01-10', 1, 1);
CALL sp_AgregarFactura(2, 'Pendiente', 2800.50, '2024-01-15', 2, 2);
CALL sp_AgregarFactura(3, 'Pagada', 990.75, '2024-01-20', 3, 3);
CALL sp_AgregarFactura(4, 'Pagada', 450.25, '2024-02-01', 4, 4);
CALL sp_AgregarFactura(5, 'Pendiente', 3200.80, '2024-02-05', 5, 5);
CALL sp_AgregarFactura(6, 'Pagada', 1750.30, '2024-02-10', 6, 6);
CALL sp_AgregarFactura(7, 'Pendiente', 210.50, '2024-02-15', 7, 7);
CALL sp_AgregarFactura(8, 'Pagada', 890.00, '2024-03-01', 8, 8);
CALL sp_AgregarFactura(9, 'Pagada', 4200.60, '2024-03-05', 9, 9);
CALL sp_AgregarFactura(10, 'Pendiente', 760.45, '2024-03-10', 10, 10);

INSERT INTO DetalleFactura (codigoDetalleFactura, precioUnitario, cantidad, Factura_numeroFactura, Productos_codigoProducto) VALUES
(1, 15.50, 2, 1, 'P001'),
(2, 22.75, 1, 1, 'P002'),
(3, 9.99, 5, 2, 'P003'),
(4, 45.00, 3, 2, 'P004'),
(5, 33.33, 4, 3, 'P005'),
(6, 18.50, 6, 3, 'P006'),
(7, 27.75, 2, 4, 'P007'),
(8, 5.99, 10, 4, 'P008'),
(9, 100.00, 1, 5, 'P009'),
(10, 12.50, 8, 5, 'P010');

