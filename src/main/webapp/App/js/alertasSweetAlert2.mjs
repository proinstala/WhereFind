

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


const mostrarLoading = (mensaje = "Por favor espere.") => {
    var sweet_loader = '<div class="sweet_loader"><svg viewBox="0 0 140 140" width="140" height="140"><g class="outline"><path d="m 70 28 a 1 1 0 0 0 0 84 a 1 1 0 0 0 0 -84" stroke="rgba(0,0,0,0.1)" stroke-width="4" fill="none" stroke-linecap="round" stroke-linejoin="round"></path></g><g class="circle"><path d="m 70 28 a 1 1 0 0 0 0 84 a 1 1 0 0 0 0 -84" stroke="#71BBFF" stroke-width="4" fill="none" stroke-linecap="round" stroke-linejoin="round" stroke-dashoffset="200" stroke-dasharray="300"></path></g></svg></div>';
    swal.fire({
        html: '<h4>' + mensaje + '</h4>' + sweet_loader,
        allowOutsideClick: false,
        allowEscapeKey: false,
        showConfirmButton: false

    });
};

const ocultarLoading = () => {
    swal.close();
};



export { mostrarMensajeAcceso, mostrarLoading, ocultarLoading };