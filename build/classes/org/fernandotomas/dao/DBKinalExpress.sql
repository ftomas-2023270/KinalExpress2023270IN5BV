drop database if exists DBKinalExpress;
create database if not exists DBKinalExpress;
use DBKinalExpress;

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
    primary key PK_Compras(numeroDocumento));

create table CargoEmpleado(
    codigoCargoEmpleado int,
    nombreCargo varchar(45),
    descripcionCargo varchar(45),
    primary key PK_CargoEmpleado(codigoCargoEmpleado));
    
    
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
	create procedure sp_AgregarCargoEmp(in codigoCargoEmpleado int ,in nombreCargo varchar(45),in descripcionCargo varchar(45) ) 
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
create procedure sp_EditarCargoEmp(in _codigoCargoEmpleado int ,in _nombreCargo varchar(45),in _descripcionCargo varchar(45)) 
		begin
			update CargoEmpleado 
			set nombreCargo=_nombreCargo,
			descripcionCargo=_descripcionCargo
			where _codigoCargoEmpleado = codigoCargoEmpleado;
		End$$
delimiter ;

call sp_EditarCargoEmp(101,'Programador','Desarrollador de aplicaciones')
-- -----------------------------TipoProducto-------------------------------------------
-- ------------------------------Agregar CargoEmpleados------------------------------------------------------------------

delimiter $$
	create procedure sp_AgregarTipoProducto(in codigoTipoProducto int , in descripcion varchar(45) ) 
		begin
			Insert into TipoProducto(codigoTipoProducto,descripcion)
			values(codigoTipoProducto,descripcion);
		End$$
delimiter ;

--  -------------------------Listar CargoEmpleados-----------------------------------------------
delimiter $$
	create procedure sp_ListarTipoProducto()
		begin
			select codigoTipoProducto,
			descripcion
			from TipoProducto;
		end $$
delimiter ;


--  -------------------------Buscar CargoEmpleados-----------------------------------------------
delimiter $$
	create procedure sp_BuscarTipoProducto(in id int)
		begin
			select descripcion
			from TipoProducto
			where id = codigoTipoProducto;
		end $$
delimiter ;


--  -------------------------Eliminar CargoEmpleados-----------------------------------------------
delimiter $$
	create procedure sp_EliminarTipoProducto(in id int)
		begin
			delete from TipoProducto 
			where id = codigoTipoProducto;
		end $$
delimiter ;


--  -------------------------Editar CargoEmpleados-----------------------------------------------
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
