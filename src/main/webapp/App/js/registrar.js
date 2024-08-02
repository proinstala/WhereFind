import { solicitudPost, setImageSelected, solicitudPost_modificada } from './comunes.mjs';
import { mostrarMensajeError, mostrarMensaje } from './alertasSweetAlert2.mjs';



$(document).ready(function () {
    validarFormulario("#frmRegistrarUsuario");

    const btnCancelar = document.querySelector('#btnCancelar');
    const btnFoto = document.querySelector('#btnFoto');
    const contenedorImg = document.querySelector('#imgUsuario');
    const inputHide64 = document.querySelector('#imagenUsuarioB64');
    const labelInputFoto = document.querySelector('#textoImagen');


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
        window.location.href = 'login.jsp';
    });

});


function validarFormulario(idForm) {
    $.validator.addMethod("passwordMatch", function(value, element) {
        // Comprueba si el valor del campo de confirmación coincide con el de la contraseña
        return value === $(idForm).find("input[name='passwordUsuario']").val();
    }, "Las contraseñas no coinciden.");

    $(idForm).validate({
        rules: {
            nombreUsuario: {
                required: true,
                maxlength: 20
            },
            passwordUsuario: {
                required: true,
                maxlength: 60,
                minlength: 4
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
                email: true
            },
            confirmarPasswordUsuario: {
                required: true,
                passwordMatch: true
            }
        },//Fin de reglas ----------------
        messages: {
            nombreUsuario: {
                required: "Debe introducir el nombre de usuario.",
                maxlength: "Longitud máx 20 caracteres."
            },
            passwordUsuario: {
                required: "Debe introducir el password de usuario.",
                maxlength: "Longitud máx 60 caracteres.",
                minlength: "Logitud minima de 4 caracteres."
            },
            nombreRealUsuario: {
                required: "Debe introducir su nombre.",
                maxlength: "Longitud máx 60 caracteres."
            },
            apellidoRealUsuario: {
                required: "Debe introducir sus apellidos.",
                maxlength: "Longitud máx 60 caracteres."
            },
            emailUsuario: {
                required: "Debe introducir su email.",
                email: "Debe introducir un email válido."
            },
            confirmarPasswordUsuario: {
                required: "Debe introducir el password de confirmación.",
                passwordMatch: "El password de confirmación y de usuario no coinciden."
            }

        },//Fin de msg  ------------------

        submitHandler: function () {
            /*
            const registrarCallBack = (response)  => {
                if (response.isError === 1) {
                    debugger;
                    mostrarMensajeAcceso("No se puede registrar", response.result, "error");
                } else {
                    debugger;
                    const acceso = () => window.location.replace(response.result);
                    mostrarMensajeAcceso(`Se ha creado correctamente el usuario de ${response.user.nombre}`, "Creado Usuario.", "success", (response.isUrl)? acceso : null);
                }
            };

            solicitudPost("api/identidad/create", registrarCallBack, idForm, true);
            */

            solicitudPost_modificada("api/identidad/create", idForm, true)
                .then(response => {
                    if (response.isError === 1) {
                        mostrarMensajeError("No se puede registrar", response.result, "error");
                    } else {
                        const acceso = () => window.location.replace(response.result);
                        mostrarMensaje(`Se ha creado correctamente el usuario de ${response.user.nombre}`, "Creado Usuario.", "success", (response.isUrl) ? acceso : null);
                    }
                })
                .catch(error => {
                    // Maneja el error aquí
                    console.error("Error:", error);
                    mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.", "error");
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
