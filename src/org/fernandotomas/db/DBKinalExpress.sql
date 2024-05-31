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

call sp_AgregarClientes(1, '123456-7', 'Juan', 'García', 'Calle 123, Ciudad A', '123456789', 'juan.garcia@example.com');
call sp_AgregarClientes(2, '987654-3', 'María', 'López', 'Avenida 456, Ciudad B', '987654321', 'maria.lopez@example.com');
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

call sp_ListarClientes();

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

call sp_BuscarClientes(2);

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

call sp_EditarClientes(1,'85698-5','Eladio','Carrion','Humacao','45369858','SouceBoyz@gcode.com')

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
call sp_AgregarProveedores(1001, '123456789','ProveedorCorp','S.A.','Calle Ficticia 123','ProveedorCorp S.A.', 'Juan Pérez', 'www.proveedorcorp.com');

call sp_AgregarProveedores(2002, '987654321', 'Suministros XYZ', 'Ltda.', 'Avenida Principal 456', 'Suministros XYZ Ltda.', 'María García', 'www.suministrosxyz.com');

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

call sp_ListarClientes();

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

call sp_BuscarClientes(2);

--  -------------------------Eliminar Proveedores-----------------------------------------------
delimiter $$
	create procedure sp_EliminarProveedores(in id int)
		begin
			delete from Proveedores 
			where id =  codigoProveedor;
		end $$
delimiter ;

call sp_EliminarProveedores(1);

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

call sp_ListarProveedores();

-- -----------------------------CargoEmpleados-------------------------------------------
-- ------------------------------Agregar CargoEmpleados------------------------------------------------------------------

delimiter $$
	create procedure sp_AgregarCargoEmp(in codigoCargoEmpleado int ,in nombreCargo varchar(45),in descripcionCargo varchar(100) ) 
		begin
			Insert into cargoEmpleado(codigoCargoEmpleado,nombreCargo,descripcionCargo)
            values(codigoCargoEmpleado,nombreCargo,descripcionCargo);
		End$$
delimiter ;
call sp_AgregarCargoEmp(102, 'Desarrollador', 'Responsable de desarrollar aplicaciones');
call sp_AgregarCargoEmp(101, 'Gerente', 'Responsable de gestionar equipos y proyectos');

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

call sp_ListarCargoEmp();

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

call sp_BuscarCargoEmp(101);

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

call sp_EditarCargoEmp(101,'Programador','Desarrollador de aplicaciones')
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

call sp_AgregarCompras(123456, '2024-05-10', 'Compra de materiales', 500.00);
call sp_AgregarCompras(789012, '2024-05-09', 'Compra de equipo', 1200.00);

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

CALL sp_ListarCompras();

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
CALL sp_BuscarCompras(123456);


--  -------------------------Eliminar Compras-----------------------------------------------
delimiter $$
	create procedure sp_EliminarCompras(in id int)
		begin
			delete from Compras 
			where id = numeroDocumento;
		end $$
delimiter ;
CALL sp_EliminarCompras(789012);


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

CALL sp_EditarCompras(123456, '2024-05-10', 'Compra de materiales de construcción', 550.00);

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
			Insert into Productos(codigoProducto , descripcionProducto ,precioUnitario , precioDocena ,
            precioMayor , imagenProducto , existencia , codigoTipoProducto , codigoProveedor) 
            values ( codigoProducto , descripcionProducto ,precioUnitario , precioDocena ,
            precioMayor , imagenProducto , existencia , codigoTipoProducto , codigoProveedor);
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
