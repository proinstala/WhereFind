import { setImageSelected, solicitudPut, resetCamposForm, detectarCambiosFormulario } from './comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from './alertasSweetAlert2.mjs';



function onDetectarCambiosModificarUsuario(hayCambios) {
    $("#btnGuardar").prop('disabled', !hayCambios);
    $("#btnCancelar").prop('disabled', !hayCambios);
}

function onDetectarCambiosModificarPassword(hayCambios) {
    $("#btnGuardarPassword").prop('disabled', !hayCambios);
    $("#btnCancelarPassword").prop('disabled', !hayCambios);
}


$(document).ready(function () {
    validarFormulario("#frmModificarUsuario");
    validarFormularioPassword("#frmModificarPassword");

    detectarCambiosFormulario("#frmModificarUsuario", onDetectarCambiosModificarUsuario);
    detectarCambiosFormulario("#frmModificarPassword", onDetectarCambiosModificarPassword);

    const btnCancelar = document.querySelector('#btnCancelar');
    const btnPassword = document.querySelector('#btnPassword');
    const btnUsuario = document.querySelector('#btnUsuario');

    const btnFoto = document.querySelector('#btnFoto');
    const contenedorImg = document.querySelector('#imgUsuario');
    const inputHide64 = document.querySelector('#imagenUsuarioB64');
    const labelInputFoto = document.querySelector('#textoImagen');

    const divFormUsuario = document.querySelector('#form_usuario');
    const divFormPassword = document.querySelector('#form_password');


    btnFoto.addEventListener('change', (e) => {
        const defaultUserImg = contenedorImg.src;
        const fileImg = e.target.files[0];

        //Establece la imagen seleccionada.
        setImageSelected(fileImg, contenedorImg, inputHide64, defaultUserImg, 2)
            .then((result) => {
                if (result) {
                    console.log("Imagen establecida correctamente.");
                    labelInputFoto.textContent = result;
                } else {
                    console.log("No se ha establecido la imagen.");
                }
            })
            .catch((error) => {
                // Maneja cualquier error que ocurra durante la validación o el proceso de establecer la imagen
                console.error('Error:', error);
                btnFoto.value = '';
            });
    });

    btnCancelar.addEventListener('click', () => {
        //window.location.href = 'login.jsp';
    });

    btnPassword.addEventListener('click',() => mostrarContenedor(divFormPassword, divFormUsuario, 'grid'));
    btnUsuario.addEventListener('click',() => mostrarContenedor(divFormUsuario, divFormPassword, 'grid'));
});


function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            nombreUsuario: {
                required: true,
                maxlength: 20
            },
            nombreRealUsuario: {
                required: true,
                maxlength: 100
            },
            apellidoRealUsuario: {
                required: true,
                maxlength: 100
            },
            emailUsuario: {
                required: true,
                maxlength: 200,
                email: true
            }
        },//Fin de reglas ----------------
        messages: {
            nombreUsuario: {
                required: "Debe introducir el nombre de usuario.",
                maxlength: "Longitud máx 20 caracteres."
            },
            nombreRealUsuario: {
                required: "Debe introducir su nombre.",
                maxlength: "Longitud máx 100 caracteres."
            },
            apellidoRealUsuario: {
                required: "Debe introducir sus apellidos.",
                maxlength: "Longitud máx 100 caracteres."
            },
            emailUsuario: {
                required: "Debe introducir su email.",
                maxlength: "Longitud máx 200 caracteres.",
                email: "Debe introducir un email válido."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            mostrarMensajeOpcion("Modificar Usuario", '¿Quieres realmente modificar los datos?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const usuarioIdInput = document.querySelector('#usuario_id');
                            const usuarioId = usuarioIdInput ? usuarioIdInput.value : -1;

                            solicitudPut(`api/identidad/update/${usuarioId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            //const acceso = () => window.location.replace(response.result);
                                            mostrarMensaje(`Se han modificado correctamente los datos el usuario de ${response.user.nombre}`, "Modificado Usuario.", "success");
                                        }
                                    })
                                    .catch(error => {
                                        // Maneja el error aquí
                                        console.error("Error:", error);
                                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                                    });
                        } else if (result.isDenied) {
                            //denegado
                        } else if (result.isDismissed) {
                            //cancelado
                        }
                    });
        },
       // Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error

        },
        complete: function () {
            console.log("complete");
            formDisable(false, idForm);
        }
    });//Fin Validate
}

function validarFormularioPassword(idForm) {
    $.validator.addMethod("passwordMatch", function(value, element) {
        // Comprueba si el valor del campo de confirmación coincide con el de la contraseña
        return value === $(idForm).find("input[name='nuevoPassword']").val();
    }, "Las contraseñas no coinciden.");

    $(idForm).validate({
        rules: {
            passwordUsuario: {
                required: true,
                maxlength: 60
            },
            nuevoPassword: {
                required: true,
                maxlength: 60,
                minlength: 4
            },
            confirmPassword: {
                required: true,
                passwordMatch: true
            }

        },//Fin de reglas ----------------
        messages: {
            passwordUsuario: {
                required: "Este campo es requerido.",
                maxlength: "Longitud máx 60 caracteres."
            },
            nuevoPassword: {
                required: "Este campo es requerido.",
                maxlength: "Longitud máx 60 caracteres.",
                minlength: "Logitud minima de 4 caracteres."
            },
            confirmPassword: {
                required: "Este campo es requerido.",
                passwordMatch: "El password de confirmación es erroneo."
            }

        },//Fin de msg  ------------------

        submitHandler: function () {
            mostrarMensajeOpcion("Modificar Password Usuario", '¿Quieres realmente modificar el password de usuario?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const usuarioIdInput = document.querySelector('#passwordUsuario_id');
                            const usuarioId = usuarioIdInput ? usuarioIdInput.value : -1;

                            solicitudPut(`api/identidad/updatePassword/${usuarioId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            //const acceso = () => window.location.replace(response.result);
                                            mostrarMensaje(`Se han modificado correctamente los datos el usuario de ${response.user.nombre}`, "Modificado Usuario.", "success");
                                            resetCamposForm(idForm);
                                        }
                                    })
                                    .catch(error => {
                                        // Maneja el error aquí
                                        console.error("Error:", error);
                                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                                    });
                        } else if (result.isDenied) {
                            //denegado
                        } else if (result.isDismissed) {
                            //cancelado
                        }
                    });
        },
       // Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error

        },
        complete: function () {
            console.log("complete");
            formDisable(false, idForm);
        }
    });//Fin Validate
}

function mostrarContenedor(nodeMostrar, nodeOcultar, display) {
    const estiloNodeMostrar = window.getComputedStyle(nodeMostrar);

    // Verificar si el nodo a mostrar tiene display: none
    if (estiloNodeMostrar.display === 'none') {
        // Si es así, cambiar su display a grid
        nodeMostrar.style.display = display;

        // Ocultar el nodo a ocultar
        nodeOcultar.style.display = 'none';
    }
}

