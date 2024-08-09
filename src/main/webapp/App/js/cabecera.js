
$(document).ready(function () {

    const btnEditarUsuario = document.querySelector('#btnEditarUsuario');



    btnEditarUsuario.addEventListener('click', () => {
        window.location.href = 'account/modificar';
    });
});
