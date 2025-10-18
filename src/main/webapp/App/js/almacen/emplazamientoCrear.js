
import {solicitudPost, setImageSelected, resetImg, fillInputSelect, cargarInputSelect, vaciarSelect, detectarCambiosFormulario, resetCamposForm } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG} from '../constantes.mjs';

const idSelectAlmacen = "#almacen";
const idSelectTipo = "#tipo";
const idInputNombre = "#nombre";
const idInputDescripcion = "#descripcion";
const idFormEmplazamiento = "#frmCrearEmplazamiento";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosEmplazamiento = "#btnDeshacerCambiosEmplazamiento";

$(document).ready(function () {
    const selectAlmacen = document.querySelector(idSelectAlmacen);
    const selectTipo = document.querySelector(idSelectTipo);
    const btnDeshacerCambiosEmplazamiento = document.querySelector(idBtnDeshacerCambiosEmplazamiento);
    const btnCancelar = document.querySelector(idBtnCancelar);

    //Carga el select almacen.
    const promesaCargaSelectTipo = cargarInputSelect(selectTipo, "api/tipo_emplazamiento/tipos_emplazamientos", 'Seleccione un tipo de emplazamiento', false, () => {});

    //Carga el select almacen.
    const promesaCargaSelectAlmacen = cargarInputSelect(selectAlmacen, "api/almacen/almacenes", 'Seleccione un almacén', false, () => {});
    
    Promise.all([promesaCargaSelectTipo, promesaCargaSelectAlmacen])
        .then(() => {
            onDetectarCambiosCrearEmplazamiento(false);
            detectarCambiosFormulario(idFormEmplazamiento, onDetectarCambiosCrearEmplazamiento);
        })
        .catch(error => {
            console.error("Error al cargar selects:", error);
        });

    validarFormulario(idFormEmplazamiento);
    
    btnCancelar.addEventListener('click', () => {
        if (document.referrer) {
            window.location.href = document.referrer;
        } else {
            // Fallback: vuelve a una página por defecto
            window.location.href = "almacen/emplazamientos";
        }
    });

    btnDeshacerCambiosEmplazamiento.addEventListener('click', () => {
        resetCamposForm(idFormEmplazamiento);
        
        onDetectarCambiosCrearEmplazamiento(false);
        detectarCambiosFormulario(idFormEmplazamiento, onDetectarCambiosCrearEmplazamiento);
    });
    
});


function onDetectarCambiosCrearEmplazamiento(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosEmplazamiento).prop('disabled', !hayCambios);
}


function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            nombre: {
                required: true,
                maxlength: 100
            },
            descripcion: {
                required: true,
                maxlength: 200
            },
            direccion: {
                number: true,
                min: -1,
                max: 999999
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                required: "Debe introducir el nombre del emplazamiento.",
                maxlength: "Longitud máx 100 caracteres."
            },
            descripcion: {
                required: "Debe introducir la descripcion del emplazamiento.",
                maxlength: "Longitud máx 200 caracteres."
            },
            tipo: {
                number: "Valor seleccionado no válido.",
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            },
            almacen: {
                number: "Valor seleccionado no válido.",
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const emplazamientoJSON = getEmplazamientoJson();
            solicitudPost(`api/emplazamiento/create`, idForm, true, emplazamientoJSON)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("No se puede crear los datos", response.result);
                        } else {
                            const redireccionar = () => window.location.href = "almacen/emplazamientos";
                            mostrarMensaje("Emplazamiento Creado.", `Se ha creado el emplazamiento con id "${response.data.id}" correctamente`, "success", redireccionar);
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

function getEmplazamientoJson() {
    // Guardar referencias de los elementos del DOM
    const selectTipo = document.querySelector(idSelectTipo);
    const selectAlmacen = document.querySelector(idSelectAlmacen);
    const inputNombre = document.querySelector(idInputNombre);
    const inputDescripcion = document.querySelector(idInputDescripcion);
    
    // Obtener el valor seleccionado de tipo
    const tipoId = parseInt(selectTipo.value, 10);
    
    // Obtener el valor seleccionado de almacen
    const almacenId = parseInt(selectAlmacen.value, 10);

    const emplazamientoJSON = {
        id: "0",
        nombre: inputNombre.value.trim(),
        descripcion: inputDescripcion.value.trim(),
        activo: true,
        tipoEmplazamiento: tipoId === -1 ? null : {
            id: tipoId,
            nombre: "",
            descripcion: ""
        },
        almacen: almacenId === -1 ? null : {
            id: almacenId,
            nombre: "",
            descripcion: "",
            direccion: null
        }
    };

    // Crear los datos en formato de URL usando URLSearchParams
    const data = new URLSearchParams({
        emplazamientoJSON: JSON.stringify(emplazamientoJSON)
    }).toString();

    return data;
}

