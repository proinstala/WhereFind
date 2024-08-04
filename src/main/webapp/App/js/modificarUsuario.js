import { setImageSelected, solicitudPut } from './comunes.mjs';
import { mostrarMensaje, mostrarMensajeError } from './alertasSweetAlert2.mjs';

$(document).ready(function () {
    validarFormulario("#frmModificarUsuario");
    validarFormularioPassword("#frmModificarUsuario");
    
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
    
    //btnPassword.addEventListener('click', modificarPassword);
    btnPassword.addEventListener('click',() => mostrarContenedor(divFormPassword, divFormUsuario));
    btnUsuario.addEventListener('click',() => mostrarContenedor(divFormUsuario, divFormPassword));
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
                required: "Debe introducir el password de usuario.",
                maxlength: "Longitud máx 60 caracteres."
            },
            nuevoPassword: {
                required: "Debe introducir el nuevo password de usuario.",
                maxlength: "Longitud máx 60 caracteres.",
                minlength: "Logitud minima de 4 caracteres."
            },
            confirmPassword: {
                required: "Debe introducir el password de confirmación.",
                passwordMatch: "El password de confirmación y de usuario no coinciden."
            }
            
        },//Fin de msg  ------------------

        submitHandler: function () {
  
            const usuarioIdInput = document.querySelector('#usuario_id');
            const usuarioId = usuarioIdInput ? usuarioIdInput.value : -1;
          
            solicitudPut(`api/identidad/updatePassword/${usuarioId}`, idForm, true)
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


function modificarPassword() {
    Swal.fire({
        title: "Cambiar Password",
        html:`<div class="formulario_sw2">
<div class="form__input">
<input type="password" name="passwordUsuario" id="oldPassword" placeholder="Introduce tu password">
<label for="passwordUsuario">Password Usuario</label>
</div>
        
<div class="form__input">
<input type="password" name="passwordUsuario" id="newPassword" placeholder="Introduce tu password">
<label for="passwordUsuario">Nuevo Password</label>
</div>
<div class="form__input">
<input type="password" name="passwordUsuario" id="confirmPassword" placeholder="Introduce tu password">
<label for="passwordUsuario">Confirma Nuevo Password</label>
</div>
        </div>
`, 
        focusConfirm: false,
        showCancelButton: true,
        confirmButtonText: 'Aceptar',
        cancelButtonText: 'Cancelar',
        customClass: {
            title: 'swal-titulo',
            confirmButton: 'swal-boton',
            popup: 'custom-popup'
        },
        preConfirm: () => {
            return [
                document.getElementById("oldPassword").value,
                document.getElementById("newPassword").value,
                document.getElementById("confirmPassword").value
            ];
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const formValues = result.value;
            const [oldPassword, newPassword, confirmPassword] = formValues;
            console.log("Old Password:", oldPassword);
            console.log("New Password:", newPassword);
            console.log("Confirm Password:", confirmPassword);
        } else {
            console.log("User cancelled the input");
        }
    });
}

function mostrarContenedor(nodeMostrar, nodeOcultar) {
    const estiloNodeMostrar = window.getComputedStyle(nodeMostrar);
    
    // Verificar si el nodo a mostrar tiene display: none
    if (estiloNodeMostrar.display === 'none') {
        // Si es así, cambiar su display a grid
        nodeMostrar.style.display = 'grid';
        
        // Ocultar el nodo a ocultar
        nodeOcultar.style.display = 'none';
    }
}

