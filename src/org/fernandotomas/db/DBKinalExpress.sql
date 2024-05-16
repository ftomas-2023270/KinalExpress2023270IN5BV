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
    
create table EmailProveedor(
    codigoEmailProveedor int not null,
    emailProveedor varchar(50)not null,
    descripcion varchar(100) not null,
    Proveedores_codigoProveedores int not null,
    primary key PK_EmailProveedor(codigoEmailProveedor),
    foreign key PK_Proveedores_EmailProveedores(Proveedores_codigoProveedores) references Proveedores(codigoProveedor));
    
create table TelefonoProveedor(
	codigoTelefonoProveedor int not null,
    numeroPrincipal varchar(15) not null,
    numeroSecundario varchar(15) not null,
    observaciones varchar(45) not null,
    Proveedores_codigoProveedores int not null,
    primary key PK_TelegonoProveedor(codigoTelefonoProveedor),
    foreign key FK_Proveedores_TelefonoProveedor(Proveedores_codigoProveedores)references  Proveedores(codigoProveedor));
	
    
    
    
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

--  -------------------------Buscar Clientes-----------------------------------------------
delimiter $$
	create procedure sp_EliminarClientes(in id int)
    begin
		delete from Clientes 
        where id = codigoCliente;
	end $$
delimiter ;


--  -------------------------Editar Clientes-----------------------------------------------
delimiter $$
create procedure sp_EditarClientes(in codCliente int ,in nCliente varchar(12),in noCliente varchar(50),in apCliente varchar(50),
	in direcCliente varchar(150) ,in telCliente varchar(12),in corrCliente varchar(45) ) 
		begin
        update Clientes C
        set C.NITCliente=nCliente, 
        C.nombreCliente=noCliente, 
        C.apellidoCliente=apCliente,
        C.direccionCliente=direcCliente, 
        C.telefonoCliente=telCliente, 
        C.correoCliente=corrCliente 
        where C.codigoCliente =codCliente;
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
		select P.codigoProveedor,
        P.NITProveedor,
        P.nombreProveedor,
        P.apellidoProveedor,
        P.direccionProveedor,
        P.razonSocial,
        P.contactoPrincipal,
        P.paginaWebProveedor
        from Proceedores P;
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
        update proveedores P
        set P.NITProveedor=NProveedor,
        P.nombreProveedor=noProveedor,
        P.apellidoProveedor=apProveedor,
        P.direccionProveedor=direcProveedor,
        P.razonSocial=rSocial,
        P.contactoPrincipal=contactPrincipal,
        P.paginaWebProveedor=pagWebProveedor
        where P.codigoProveedor =codCliente;
	End$$
delimiter ;

set global time_zone= '-6:00';