
$(document).ready(function () {

    const btnEditarUsuario = document.querySelector('#btnEditarUsuario');
    const btnLogout = document.querySelector('#btnLogout');

    const btnDireccion = document.querySelector('#btnDireccion');
    const btnConfig = document.querySelector('#btnConfig-user');
    const btnConfigAdmin = document.querySelector('#btnConfig-admin');



    btnEditarUsuario.addEventListener('click', () => {
        window.location.href = 'account/modificar';
    });

    btnLogout.addEventListener('click', () => {
        window.location.href = 'api/identidad/logout';
    });

    btnDireccion.addEventListener('click', () => {
        window.location.href = ('direccion');
    });

    btnConfig?.addEventListener('click', () => {
        window.location.href = ('account');
    });

    btnConfigAdmin?.addEventListener('click', () => {
        window.location.href = ('admin');
    });


});
