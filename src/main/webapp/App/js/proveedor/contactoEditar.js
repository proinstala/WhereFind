import { solicitudGet, solicitudPut, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect, detectarCambiosFormulario } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';

const idSelectPuesto = "#puesto";
const idSelectProveedor = "#proveedor";
const idInputIdContacto = "#contacto_id";
const idInputNombre = "#nombre";
const idInputApellido = "#apellido";
const idInputTelefono = "#telefono";
const idInputEmail = "#email";

const idFormContacto = "#frmModificarContacto";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosContacto = "#btnDeshacerCambiosContacto";

let oldContacto;

$(document).ready(function () {
    const selectPuesto = document.querySelector(idSelectPuesto);
    const selectProveedor = document.querySelector(idSelectProveedor);
    const inputIdContacto = document.querySelector(idInputIdContacto);
    const inputNombre = document.querySelector(idInputNombre);
    const inputApellido = document.querySelector(idInputApellido);
    const inputTelefono = document.querySelector(idInputTelefono);
    const inputEmail = document.querySelector(idInputEmail);
    
    const btnDeshacerCambiosContacto = document.querySelector(idBtnDeshacerCambiosContacto);
    const btnCancelar = document.querySelector(idBtnCancelar);

    validarFormulario(idFormContacto);

    getContacto(inputIdContacto.value);

    btnDeshacerCambiosContacto.addEventListener('click', () => {
        fillFielsContacto(oldContacto);
    });

    btnCancelar.addEventListener('click', () => {
        if (document.referrer) {
            window.location.href = document.referrer;
        } else {
            // Fallback: vuelve a una página por defecto
            window.location.href = "proveedor/contactos";
        }
    });

});

function onDetectarCambiosModificarContacto(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosContacto).prop('disabled', !hayCambios);
}


function getContacto(idContacto) {
    solicitudGet(`api/contacto/contacto?idContacto=${idContacto}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    oldContacto = response.data;
                    fillFielsContacto(oldContacto);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


function fillFielsContacto(contacto) {
    const form = document.querySelector(idFormContacto);

    const inputNombre = form.querySelector(idInputNombre);
    const inputApellido = form.querySelector(idInputApellido);
    const inputTelefono = form.querySelector(idInputTelefono);
    const inputEmail = form.querySelector(idInputEmail);
    
    const selectPuesto = form.querySelector(idSelectPuesto);
    const selectProveedor = form.querySelector(idSelectProveedor);

    inputNombre.value = contacto.nombre;
    inputApellido.value = contacto.apellido ?? "";
    inputTelefono.value = contacto.telefono ?? "";
    inputEmail.value = contacto.email ?? "";
    
    const promesaPuesto = cargarInputSelect(selectPuesto, "api/puesto/puestos", '', contacto.puestoTrabajo.id, () => {});
   
    const promesaProveedor = cargarInputSelect(selectProveedor, "api/proveedor/proveedores", '', contacto.proveedor.id, () => {});
    
    Promise.all([promesaPuesto, promesaProveedor])
        .then(() => {
            onDetectarCambiosModificarContacto(false);
            detectarCambiosFormulario(idFormContacto, onDetectarCambiosModificarContacto);
        })
        .catch(error => {
            console.error("Error al cargar selects:", error);
        });
    
}

function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            nombre: {
                required: true,
                maxlength: 100
            },
            apellido: {
                maxlength: 100
            },
            proveedor: {
                required: true,
                min: 1,
                max: 999999
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                required: "Debe introducir el nombre del contacto.",
                maxlength: "Longitud máx 100 caracteres."
            },
            apellido: {
                maxlength: "Longitud máx 100 caracteres."
            },
            proveedor: {
                required: "Debe seleccionar un proveedor.",
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            mostrarMensajeOpcion("Modificar Contacto", '¿Quieres realmente modificar los datos?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const contactoIdInput = document.querySelector('#contacto_id');
                            const contactoId = contactoIdInput ? contactoIdInput.value : -1;

                            solicitudPut(`api/contacto/update/${contactoId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Modificado contacto.", `Se han modificado correctamente los datos del contacto ${response.data.id}`, "success");
                                            oldContacto = response.data;
                                            onDetectarCambiosModificarContacto(false);
                                            detectarCambiosFormulario(idFormContacto, onDetectarCambiosModificarContacto);
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
        //Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error
        }
    });//Fin Validate
}
