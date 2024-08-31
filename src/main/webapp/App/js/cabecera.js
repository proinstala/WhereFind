
$(document).ready(function () {

    const btnEditarUsuario = document.querySelector('#btnEditarUsuario');
    const btnLogout = document.querySelector('#btnLogout');
    
    const btnDireccion = document.querySelector('#btnDireccion');

    btnEditarUsuario.addEventListener('click', () => {
        window.location.href = 'account/modificar';
    });
    
    btnLogout.addEventListener('click', () => {
        window.location.href = 'api/identidad/logout';
    });
    
    btnDireccion.addEventListener('click', () => {
        window.location.href = ('direccion/adminDireccion');
    });
});
