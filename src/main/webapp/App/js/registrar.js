import { solicitudPost } from './comunes.mjs';
import { mostrarMensajeAcceso } from './alertasSweetAlert2.mjs';

$(document).ready(function () {
    validarFormulario("#frmUser");
    
    const inputFoto = document.querySelector('#btnFoto');
    const contenedorImg = document.querySelector('#imgUsuario');
    const inputHide64 = document.querySelector('#imagenUsuarioB64');
    
    inputFoto.addEventListener('change', (e) => {
        obtenerImagen(e, contenedorImg, inputHide64);
    });
});

function obtenerImagen(event, contenedorImagen, inputHideImagen64) {
    const defaultFile = 'App/img/defaultUser.svg';
    
    if(event.target.files[0]) {
        console.log("Nombre del archivo:", event.target.files[0].name);
        console.log("Tipo de archivo:", event.target.files[0].type);
        console.log("Tamaño del archivo:", event.target.files[0].size, "bytes");
        console.log("Tamaño del archivo:", (event.target.files[0].size / 1024).toFixed(2), "KB");

        const reader = new FileReader(); //Crea un objeto FileReader para leer el contenido del archivo.
        reader.onload = function(e) {
            const base64String = e.target.result; // Obtén la URL de datos Base64
            //contenedorImagen.src = e.target.result; //Asigna el contenido del archivo como una URL de datos a la imagen.
            contenedorImagen.src = base64String; // Asigna el contenido del archivo como una URL de datos a la imagen.
            inputHideImagen64.value = base64String; // Establece el valor del input oculto con la representación Base64 de la imagen.
        };

        reader.readAsDataURL(event.target.files[0]); //Lee el contenido del archivo como una URL de datos.
        //contenedorImagen.style.display = 'block'; //Muestra la imagen al cambiar su display a 'block.
    } else {
        //imagen.src = '';
        //imagen.style.display = 'none'; //Oculta la imagen.
        
        //Si no se seleccionó ningún archivo, muestra la imagen por defecto.
        contenedorImagen.src = defaultFile; 
    }
}


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
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const registrarCallBack = (response)  => {
                if (response.isError === 1) {
                    mostrarMensajeAcceso("No se puede registrar", response.result, "error");
                } else {
                    const acceso = () => window.location.replace(response.result);
                    mostrarMensajeAcceso(`Se ha creado correctamente el usuario de ${response.user.nombre}`, "Creado Usuario.", "success", (response.isUrl)? acceso : null);
                }
            };

            solicitudPost("api/identidad/create", registrarCallBack, nombreForm, true);
        },
       // Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error

        },
        complete: function () {
            console.log("complete");
            formDisable(false, nombreForm);
        }
    });//Fin Validate
}
