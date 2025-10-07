
$(document).ready(function () {

    const btnEditarUsuario = document.querySelector('#btnEditarUsuario');
    const btnLogout = document.querySelector('#btnLogout');

    const btnAlmacen = document.querySelector('#btnAlmacen');
    const btnArticulo = document.querySelector('#btnArticulo');
    const btnDireccion = document.querySelector('#btnDireccion');
    const btnProveedor = document.querySelector('#btnProveedor');
    const btnConfig = document.querySelector('#btnConfig-user');
    const btnConfigAdmin = document.querySelector('#btnConfig-admin');


    btnEditarUsuario.addEventListener('click', () => {
        window.location.href = 'account/modificar';
    });

    btnLogout.addEventListener('click', () => {
        window.location.href = 'api/identidad/logout';
    });
    
    btnAlmacen.addEventListener('click', () => {
        window.location.href = ('almacen');
    });
    
    btnArticulo.addEventListener('click', () => {
        window.location.href = ('articulo');
    });

    btnDireccion.addEventListener('click', () => {
        window.location.href = ('direccion');
    });
    
    btnProveedor.addEventListener('click', () => {
        window.location.href = ('proveedor');
    });

    btnConfig?.addEventListener('click', () => {
        window.location.href = ('account');
    });

    btnConfigAdmin?.addEventListener('click', () => {
        window.location.href = ('admin');
    });


});
