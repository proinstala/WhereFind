

/**
 * Muestra un mensaje de acceso utilizando SweetAlert2.
 *
 * @param {string} titulo - El título del mensaje.
 * @param {string} contenido - El contenido del mensaje.
 * @param {string} icon - El tipo de ícono a mostrar ('success', 'error', 'warning', 'info', 'question').
 * @param {function} callback - Una función de retorno que se ejecuta cuando se confirma el mensaje.
 */
const mostrarMensajeAcceso = (titulo = "titulo", contenido = "contenido", icon = "success", callback) => {
    Swal.fire({
        title: titulo,
        html: `<span class="swal-contenido">${contenido}</span>`,
        icon: icon,
        confirmButtonText: 'Aceptar',
        timer: 2000,

        customClass: {
            title: 'swal-titulo',
            confirmButton: 'swal-boton',
            popup: 'custom-popup'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const selectedValue = result.value;
            console.log('value:', selectedValue);
        }

        if (typeof callback === "function") {
            callback();
        }
    });
};

export { mostrarMensajeAcceso };