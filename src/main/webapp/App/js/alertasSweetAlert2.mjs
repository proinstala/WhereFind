
/*
 * Documentación oficial de sweetalert2.
 * 
 * https://sweetalert2.github.io/
*/

/**
 * Muestra un mensaje utilizando SweetAlert2.
 *
 * @param {string} titulo - El título del mensaje.
 * @param {string} contenido - El contenido del mensaje.
 * @param {string} icon - El tipo de ícono a mostrar ('success', 'error', 'warning', 'info', 'question').
 * @param {function} callback - Una función de retorno que se ejecuta cuando se confirma el mensaje.
 * @param {int} timerAutoClosed - El tiempo en milisegundos para que el mensaje de alerta se autocierre.
 */
const messageBox = (titulo = "titulo", contenido = "contenido", icon = "success", callback = null, timerAutoClosed = null) => {
    Swal.fire({
        title: titulo,
        html: `<span class="swal-contenido">${contenido}</span>`,
        icon: icon,
        confirmButtonText: 'Aceptar',
        timer: timerAutoClosed,

        customClass: {
            title: 'swal-titulo',
            confirmButton: 'swal-boton',
            popup: 'custom-popup'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const selectedValue = result.value;
            //console.log('value:', selectedValue);
        }

        if (typeof callback === "function") {
            callback();
        }
    });
};

/**
 * Muestra un mensaje de acceso utilizando SweetAlert2.
 *
 * @param {string} titulo - El título del mensaje.
 * @param {string} contenido - El contenido del mensaje.
 * @param {string} icon - El tipo de ícono a mostrar ('success', 'error', 'warning', 'info', 'question').
 * @param {function} callback - Una función de retorno que se ejecuta cuando se confirma el mensaje.
 */
const mostrarMensaje = (titulo = "titulo", contenido = "contenido", icon = "success", callback = null) => {
    messageBox(titulo, contenido, icon, callback, 3000);
};


/**
 * Muestra un mensaje de advertencia utilizando SweetAlert2.
 *
 * @param {string} titulo - El título del mensaje.
 * @param {string} contenido - El contenido del mensaje.
 * @param {string} icon - El tipo de ícono a mostrar ('success', 'error', 'warning', 'info', 'question').
 * @param {function} callback - Una función de retorno que se ejecuta cuando se confirma el mensaje.
 */
const mostrarMensajeAdvertencia = (titulo = "titulo", contenido = "contenido", icon = "warning", callback = null) => {
    messageBox(titulo, contenido, icon, callback, null);
};


/**
 * Muestra un mensaje de advertencia utilizando SweetAlert2.
 *
 * @param {string} titulo - El título del mensaje.
 * @param {string} contenido - El contenido del mensaje.
 * @param {string} icon - El tipo de ícono a mostrar ('success', 'error', 'warning', 'info', 'question').
 * @param {function} callback - Una función de retorno que se ejecuta cuando se confirma el mensaje.
 */
const mostrarMensajeError = (titulo = "titulo", contenido = "contenido", icon = "error", callback = null) => {
    messageBox(titulo, contenido, icon, callback, null);
};


/**
 * Muestra un mensaje con las opciones de aceptar o rechazar la pregunta utilizando SweetAlert2.
 *
 * @param {string} titulo - El título del mensaje.
 * @param {string} contenido - El contenido del mensaje.
 */
const mostrarMensajeOpcion = (titulo = "titulo", contenido = "contenido") => {
    return Swal.fire({
        title: titulo,
        html: `<span class="swal-contenido">${contenido}</span>`,
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Aceptar',
        cancelButtonText: 'Cancelar',
        
        customClass: {
            title: 'swal-titulo',
            confirmButton: 'swal-boton',
            cancelButton: 'swal-boton-cancel',
            popup: 'custom-popup'
        }
    });
};

const mostrarLoading = (mensaje = "Por favor espere.") => {
    let sweet_loader = '<div class="sweet_loader"><svg viewBox="0 0 140 140" width="140" height="140"><g class="outline"><path d="m 70 28 a 1 1 0 0 0 0 84 a 1 1 0 0 0 0 -84" stroke="rgba(0,0,0,0.1)" stroke-width="4" fill="none" stroke-linecap="round" stroke-linejoin="round"></path></g><g class="circle"><path d="m 70 28 a 1 1 0 0 0 0 84 a 1 1 0 0 0 0 -84" stroke="#71BBFF" stroke-width="4" fill="none" stroke-linecap="round" stroke-linejoin="round" stroke-dashoffset="200" stroke-dasharray="300"></path></g></svg></div>';
    swal.fire({
        html: '<h4>' + mensaje + '</h4>' + sweet_loader,
        allowOutsideClick: false,
        allowEscapeKey: false,
        showConfirmButton: false
    });
};

const ocultarLoading = () => {
    setTimeout(() => {
        swal.close();
    
    }, 400); 
};



export { mostrarMensaje, mostrarMensajeAdvertencia, mostrarMensajeError, mostrarMensajeOpcion, mostrarLoading, ocultarLoading };