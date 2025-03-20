import { solicitudGet, solicitudPut, getDatosForm, addRowSelected, fillInputSelect, cargarInputSelect, observeRowSelectedChange, deleteRowSelectedTable } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs?v=20241021_184300';

const idInputNombre = "#nombre";
const idFormBusquedaProvincia = "frmBuscarProvincia";
const idTablaProvincias = "#tablaProvincias";
const idBtnBuscar = "#btnBuscar";
const idBtnModificar = "#btnModificar";
const idBtnCrear = "#btnCrear";
const idBtnEliminar = "#btnEliminar";
const idImputUserRol = "#userRol";

// Configuración de las urls
//const URL_MODIFICAR_PROVINCIA = "direccion/edit";

document.addEventListener("DOMContentLoaded", function () {
    const formBusquedaProvincias = document.querySelector(idFormBusquedaProvincia);
    const tablaProvincias = document.querySelector(idTablaProvincias);
    const btnBuscar = document.querySelector(idBtnBuscar);
    
    validarFormulario(idFormBusquedaProvincia);
    
    //Dispara el evento de clic en el botón para que haga una busqueda inicial.
    //btnBuscar.click();
});

function validarFormulario(idForm) {
    debugger;
    $(idForm).validate({
        rules: {
            nombre: {
                required: false,
                maxlength: 100
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                maxlength: "Longitud máx 100 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            debugger;
            const formData = getDatosForm(idForm);
            const url = `api/provincia/find_provincias?${formData}`;

            solicitudGet(url, idSelectLocalidad, false)
                .then(response => {
                    if (response.isError === 1) {
                        mostrarMensajeError("Se ha producido un error", response.result);
                    } else {
                        rellenarTablaProvincias(response.data);
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


/**
 * Función que rellena una tabla HTML con las provincias proporcionadas.
 * @param {Array} provincias - Un array de objetos de provincias que contiene los datos para cada fila de la tabla.
 */
function rellenarTablaProvincias(provincias) {
    const tablaProvincias = document.querySelector(idTablaProvincias);
    const cuerpoTablaProvincias = tablaProvincias.querySelector('tbody');
    const inputUserRol = document.querySelector(idImputUserRol);  //ADmin o User

    tablaProvincias.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de provincias
    let filasHTML = provincias.map(provincia => {
        return `<tr id="${provincia.id}">
                <td>${provincia.id}</td>
                <td>${provincia.nombre}</td>
                </tr>`;
    }).join('');


    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaProvincias.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaProvincias);
}
