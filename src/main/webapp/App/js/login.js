
import { mostrarMensajeAcceso } from './alertasSweetAlert2.mjs';

$(document).ready(function () {

    const form = document.getElementById("frmLogin");
    //form.addEventListener("submit", loginSubmit);

    validarFormulario("#frmLogin");
});

function validarFormulario(nombreForm) {
    $(nombreForm).validate({
        rules: {
            nombreUsuario: {
                required: true,
                maxlength: 20
            },
            passwordUsuario: {
                required: true,
                maxlength: 60
            }

        },//Fin de reglas ----------------
        messages: {
            nombreUsuario: {
                required: "Debe introducir el nombre de usuario.",
                maxlength: "Longitud máx 20 caracteres."
            },
            passwordUsuario: {
                required: "Debe introducir el password de usuario.",
                maxlength: "Longitud máx 60 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            solicitudLoginF("api/identidad/login", getDatosForm());
        },
       // Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error

        },
        complete: function () {
            console.log("complete");
        }
    });//Fin Validate
}

/**
 * Obtiene los datos del formulario.
 *
 * @returns {string} - Los datos del formulario serializados.
 */
function getDatosForm() {
    const formData = $("#frmLogin").serialize();
    return formData;
}

function loginSubmit(event) {
    event.preventDefault();

    let formData = "nombreUsuario=" + $("#nombreUsuario").val() + "&passwordUsuario=" + $("#passwordUsuario").val();

    let request = new XMLHttpRequest();
    request.open("POST", "api/identidad/login");
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    request.send(formData);

    request.onload = () => {
        if (request.status == 200) {
            let myArr = JSON.parse(request.responseText);
            if (myArr != null) {
                if (myArr.iserror) {
                    mostrarError(myArr.result);
                }
                else {
                    window.location = myArr.result;
                }
            }
        }
    }

    request.onerror = () => {
        mostrarError("No se ha podido realizar la acción por un error en el servidor.");
    }
    
    return false;
}

function mostrarError(msg) {
    const msgError = document.getElementById("info-error");
    if (msgError) {
        msgError.innerHTML = msg;
        msgError.style.display = "block";
    }
}

/**
 * Realiza una solicitud de login utilizando jQuery AJAX.
 *
 * @param {string} url - La URL a la que se enviará la solicitud.
 * @param {string} data - Los datos a enviar en la solicitud.
 */
function solicitudLogin(url, data) {
    $.ajax({
        url: url,
        type: 'POST',
        data: data,
        //Antes del envio
        beforeSend: function () {
            
        },
        success: function (response) {
            //debugger;
            //console.log(response);

            if(response.isError === 1) {
                mostrarMensajeAcceso("Acceso Denegado ", response.result, "error");
            } else {
                const acceso = () => window.location.replace(response.result);
                mostrarMensajeAcceso("Bienvenido", "Acceso Permitido.", "success", acceso);
            }
        },
        //Funcion error de respuesta    
        error: function (jqXHR, textStatus, errorThrown) {
            //errorAjax(jqXHR, textStatus, errorThrown, urlServlet);
            console.log("error -----------!!!!!!");
            console.log(jqXHR);
            console.log(textStatus);
            console.log(errorThrown);
        },
        complete: function () {
           
        }
    });
}


/**
 * Realiza una solicitud de login utilizando fetch.
 *
 * @param {string} url - La URL a la que se enviará la solicitud.
 * @param {string} data - Los datos a enviar en la solicitud.
 */
async function solicitudLoginF(url, data) {
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: data
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();

        if (result.isError === 1) {
            mostrarMensajeAcceso("Acceso Denegado", result.result, "error");
        } else {
            const acceso = () => window.location.replace(result.result);
            console.log(result);
            //mostrarMensajeAcceso(`Bienvenido ${result.user.userName}`, "Acceso Permitido.", "success", acceso);
        }
    } catch (error) {
        console.error("Error:", error);
        mostrarMensajeAcceso("Error", "No se ha podido realizar la acción por un error en el servidor.", "error");
    } finally {
        console.log("complete");
    }
}


/**
 * Realiza una solicitud de login utilizando fetch.
 *
 * @param {string} url - La URL a la que se enviará la solicitud.
 * @param {string} data - Los datos a enviar en la solicitud.
 */
function solicitudLoginF2(url, data) {
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: data
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(response => {
        if (response.isError === 1) {
            mostrarMensajeAcceso("Acceso Denegado", response.result, "error");
        } else {
            const acceso = () => window.location.replace(response.result);
            mostrarMensajeAcceso("Bienvenido", "Acceso Permitido.", "success", acceso);
        }
    })
    .catch(error => {
        console.error("Error:", error);
        mostrarMensajeAcceso("Error", "No se ha podido realizar la acción por un error en el servidor.", "error");
    })
    .finally(() => {
        console.log("complete");
    });
}

