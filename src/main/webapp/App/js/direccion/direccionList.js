
import { solicitudGet, solicitudPut, getDatosForm, addRowSelected, fillInputSelect, cargarInputSelect, observeRowSelectedChange, deleteRowSelectedTable } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs?v=20241021_184300';

const idSelectProvincia = "#provincia";
const idSelectLocalidad = "#localidad";
const idFormBusquedaDireccion = "#frmBuscarDireccion";
const idTablaDirecciones = "#tablaDireciones";
const idBtnBuscar = "#btnBuscar";
const idBtnModificar = "#btnModificar";
const idBtnCrear = "#btnCrear";
const idBtnEliminar = "#btnEliminar";
const idBtnCancelar = "#btnCancelar";
const idImputUserRol = "#userRol";

// Configuración de las urls
const URL_MODIFICAR_DIRECCION = "direccion/edit";

$(document).ready(function () {
    const selectProvincia = document.querySelector(idSelectProvincia);
    const selectLocalidad = document.querySelector(idSelectLocalidad);
    const formBusquedaDireccion = document.querySelector(idFormBusquedaDireccion);
    const tablaDirecciones = document.querySelector(idTablaDirecciones);
    const btnBuscar = document.querySelector(idBtnBuscar);
    const btnCrear = document.querySelector(idBtnCrear);
    const btnModificar = document.querySelector(idBtnModificar);
    const btnEliminar = document.querySelector(idBtnEliminar);
    const btnCancelar = document.querySelector(idBtnCancelar);


    //Añade un evento de cambio al select de provincias y actualiza el select de localidades según la provincia seleccionada.
    const cargaInputSelectLocalidad = () => {
        selectProvincia.addEventListener('change', (e) => {
            const optionSelected = e.target.selectedOptions[0]; //Obtiene la opción seleccionada del select.

            //Crea un objeto con la información de la provincia seleccionada (id y nombre).
            const jsonProvincia = {
                id: optionSelected.value,
                nombre: optionSelected.textContent
            };

            const jsonProvinciaString = JSON.stringify(jsonProvincia); //Convierte el objeto jsonProvincia a una cadena JSON.
            const encodedJsonProvincia = encodeURIComponent(jsonProvinciaString); //Codifica la cadena JSON para que sea segura al incluirla en la URL.

            //Llama a cargarInputSelect para llenar el select de localidades con los datos obtenidos del select provincia.
            cargarInputSelect(selectLocalidad, `api/localidad/localidades?jsonProvincia=${encodedJsonProvincia}`, 'Todas');
        });
    };

    cargarInputSelect(selectProvincia, "api/provincia/provincias", 'Todas', false, cargaInputSelectLocalidad);

    validarFormulario(idFormBusquedaDireccion);

    //Dispara el evento de clic en el botón para que haga una busqueda inicial.
    btnBuscar.click();

    observeRowSelectedChange(tablaDirecciones, onDetectarFilaSeleccionada);

    btnModificar.addEventListener('click', () => {
        const idDireccion = tablaDirecciones.getAttribute('data-rowselected'); //data-rowSelected
        window.location.href = (`direccion/direcciones/edit/${idDireccion}`);
    });

    btnCrear.addEventListener('click', () => {
        window.location.href = (`direccion/direcciones/crear`);
    });

    btnEliminar.addEventListener('click', () => {
        const idDireccion = tablaDirecciones.getAttribute('data-rowselected'); //data-rowSelected
        borrarDireccion(idDireccion);
    });
    
    btnCancelar.addEventListener('click', () => {
        window.location.href = "direccion";
    });
});

function onDetectarFilaSeleccionada(hayFilaSeleccionada) {
    $("#btnEliminar").prop('disabled', !hayFilaSeleccionada);
    $("#btnModificar").prop('disabled', !hayFilaSeleccionada);
}


function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            calle: {
                required: false,
                maxlength: 100
            },
            provincia: {
                required: false,
                min: -1
            },
            localidad: {
                required: false,
                min: -1
            }
        },//Fin de reglas ----------------
        messages: {
            calle: {
                maxlength: "Longitud máx 100 caracteres."
            },
            provincia: {
                min: "Seleccion no valida."
            },
            localidad: {
                min: "Seleccion no valida."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const formData = getDatosForm(idForm);
            const url = `api/direccion/finddirecciones?${formData}`;

            solicitudGet(url, idSelectLocalidad, false)
                .then(response => {
                    if (response.isError === 1) {
                        mostrarMensajeError("Se ha producido un error", response.result);
                    } else {
                        rellenarTablaDirecciones(response.data);
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
 * Función que rellena una tabla HTML con las direcciones proporcionadas.
 * @param {Array} direcciones - Un array de objetos de direcciones que contiene los datos para cada fila de la tabla.
 */
function rellenarTablaDirecciones(direcciones) {
    const tablaDirecciones = document.querySelector(idTablaDirecciones);
    const cuerpoTablaDirecciones = tablaDirecciones.querySelector('tbody');
    const inputUserRol = document.querySelector(idImputUserRol);  //ADmin o User

    tablaDirecciones.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de direcciones
    let filasHTML = direcciones.map(direccion => {
        return `<tr id="${direccion.id}">
                <td>${direccion.id}</td>
                <td>${direccion.calle}</td>
                <td>${direccion.numero}</td>
                <td>${(direccion.codigoPostal !== 0 && direccion.codigoPostal !== undefined) ? direccion.codigoPostal : '' }</td>
                <td>${direccion.localidad.nombre}</td>
                <td>${direccion.localidad.provincia.nombre}</td>
                </tr>`;
    }).join('');


    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaDirecciones.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaDirecciones);
}

function borrarDireccion(direccionId) {
    mostrarMensajeOpcion("Borrar Dirección", `¿Quieres realmente borrar los datos de la dirección con id ${direccionId}?`)
                    .then((result) => {
                        if (result.isConfirmed) {
                            solicitudPut(`api/direccion/delete/${direccionId}`, "", true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede borrar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Dirección Borrada.", `Se han borrado correctamente los datos de la dirección.`, "success");

                                            //Elimina la fila seleccionada de la tabla.
                                            deleteRowSelectedTable(idTablaDirecciones);
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
}

