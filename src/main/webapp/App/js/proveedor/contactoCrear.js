
import {solicitudPost, fillInputSelect, cargarInputSelect, vaciarSelect, detectarCambiosFormulario, resetCamposForm } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';

const idSelectPuesto = "#puesto";
const idSelectProveedor = "#proveedor";
const idInputNombre = "#nombre";
const idInputApellido = "#apellido";
const idInputTelefono = "#telefono";
const idInputEmail = "#email";
const idFormContacto = "#frmCrearContacto";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosContacto = "#btnDeshacerCambiosContacto";


$(document).ready(function () {
    const selectPuesto = document.querySelector(idSelectPuesto);
    const selectProveedor = document.querySelector(idSelectProveedor);
    const btnDeshacerCambiosContacto = document.querySelector(idBtnDeshacerCambiosContacto);
    const btnCancelar = document.querySelector(idBtnCancelar);

    //Carga el select provicia.
    const promesaCargaSelectPuesto = cargarInputSelect(selectPuesto, "api/puesto/puestos", 'Seleccione un puesto', false, () => {});
    
    //Carga el select provicia.
    const promesaCargaSelectProveedor = cargarInputSelect(selectProveedor, "api/proveedor/proveedores", 'Seleccione un proveedor', false, () => {});
    
    Promise.all([promesaCargaSelectPuesto, promesaCargaSelectProveedor])
        .then(() => {
            onDetectarCambiosCrearContacto(false);
            detectarCambiosFormulario(idFormContacto, onDetectarCambiosCrearContacto);
        })
        .catch(error => {
            console.error("Error al cargar selects:", error);
        });

    validarFormulario(idFormContacto);


    btnCancelar.addEventListener('click', () => {
        //window.location.href = "proveedor/contactos";
        if (document.referrer) {
            window.location.href = document.referrer;
        } else {
            // Fallback: vuelve a una página por defecto
            window.location.href = "proveedor/contactos";
        }
    });

    btnDeshacerCambiosContacto.addEventListener('click', () => {
        resetCamposForm(idFormContacto);
        
        onDetectarCambiosCrearContacto(false);
        detectarCambiosFormulario(idFormContacto, onDetectarCambiosCrearContacto);
    });

});


function onDetectarCambiosCrearContacto(hayCambios) {
    $("#btnGuardar").prop('disabled', !hayCambios);
    $("#btnDeshacerCambiosContacto").prop('disabled', !hayCambios);
}


function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            nombre: {
                required: true,
                maxlength: 100
            },
            apellido: {
                required: true,
                maxlength: 100
            },
            telefono: {
                maxlength: 20
            },
            email: {
                maxlength: 100
            },
            puesto: {
                required: true,
                min: 1,
                max: 999999
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
                required: "Debe introducir el apellido del contacto.",
                maxlength: "Longitud máx 100 caracteres."
            },
            telefono: {
                required: "Debe introducir el telefono del contacto.",
                maxlength: "Longitud máx 20 caracteres."
            },
            email: {
                required: "Debe introducir el email del contacto.",
                maxlength: "Longitud máx 100 caracteres."
            },
            puesto: {
                required: "Debe seleccionar un puesto.",
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            },
            proveedor: {
                required: "Debe seleccionar un proveedor.",
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const contactoJSON = getContactoJson();
            solicitudPost(`api/contacto/create`, idForm, true, contactoJSON)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("No se puede crear los datos", response.result);
                        } else {
                            const redireccionar = () => {
                                //window.location.href = "proveedor/contactos";
                                if (document.referrer) {
                                    window.location.href = document.referrer;
                                } else {
                                    // Fallback: vuelve a una página por defecto
                                    window.location.href = "proveedor/contactos";
                                }
                            };
                            
                            mostrarMensaje("Contacto Creado.", `Se ha creado el contacto con id "${response.data.id}" correctamente`, "success", redireccionar);
                        }
                    })
                    .catch(error => {
                        // Maneja el error aquí
                        console.error("Error:", error);
                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                    });
        },
        //Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error
        }
    });//Fin Validate
}

function getContactoJson() {
    // Guardar referencias de los elementos del DOM
    const selectPuesto = document.querySelector(idSelectPuesto);
    const selectProveedor = document.querySelector(idSelectProveedor);
    const inputNombre = document.querySelector(idInputNombre);
    const inputApellido = document.querySelector(idInputApellido);
    const inputTelefono = document.querySelector(idInputTelefono);
    const inputEmail = document.querySelector(idInputEmail);

    // Construcción del objeto JSON con validación del código postal
    const contactoJSON = {
        id: "0",
        nombre: inputNombre.value.trim(),
        apellido: inputApellido.value.trim(),
        puestoTrabajo: {
            id: selectPuesto.value,
            nombre: selectPuesto.selectedOptions[0]?.textContent || "" // Usar ?. para evitar errores si no hay opción seleccionada
        },
        telefono: inputTelefono.value.trim(),
        email: inputEmail.value.trim(),
        activo: true,
        proveedor: {
            id: selectProveedor.value,
            nombre: selectProveedor.selectedOptions[0]?.textContent || "", // Usar ?. para evitar errores si no hay opción seleccionada
            descripcion: "",
            paginaWeb: "",
            activo: true,
            direccion: null
        }
    };

    // Crear los datos en formato de URL usando URLSearchParams
    const data = new URLSearchParams({
        contactoJSON: JSON.stringify(contactoJSON)
    }).toString();

    return data;
}



