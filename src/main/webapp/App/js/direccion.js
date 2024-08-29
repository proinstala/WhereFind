
import { solicitudGet, getDatosForm, addRowSelected } from './comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from './alertasSweetAlert2.mjs';

const idSelectProvincia = "#provincia";
const idSelectLocalidad = "#localidad";
const idFormBusquedaDireccion = "#frmBuscarDireccion";
const idTablaDirecciones = "#tablaDireciones";
const idBtnBuscar = "#btnBuscar";

// Configuración de las urls
const URL_MODIFICAR_DIRECCION = "direccion/edit";

$(document).ready(function () {
    const selectProvincia = document.querySelector(idSelectProvincia);
    const selectlocalidad = document.querySelector(idSelectLocalidad);
    const formBusquedaDireccion = document.querySelector(idFormBusquedaDireccion);
    const btnBuscar = document.querySelector(idBtnBuscar);
    
    //Llama a la función para cargar las provincias en el select
    cargarInputSelectProvincia(selectProvincia, selectlocalidad);
    
    validarFormulario(idFormBusquedaDireccion);
    
    //Dispara el evento de clic en el botón
    btnBuscar.click();
});


function cargarInputSelectProvincia(nodoInputSelectProvincia, selectlocalidad) {
    solicitudGet("api/provincia/provincias", idSelectProvincia, false)
        .then(response => {
        if (response.isError === 1) {
            mostrarMensajeError("Se ha producido un error", response.result);
        } else {
            fillInputSelect(nodoInputSelectProvincia, response.data, 'Todas');
            
            nodoInputSelectProvincia.addEventListener('change', (e) => {
                const optionSelected = e.target.selectedOptions[0];
                
                const jsonProvincia = {
                    id: optionSelected.value,
                    nombre: optionSelected.textContent 
                };
                
                const jsonProvinciaString = JSON.stringify(jsonProvincia);
                const encodedJsonProvincia = encodeURIComponent(jsonProvinciaString);
                
                cargarInputSelectLocalidad(selectlocalidad, encodedJsonProvincia);
            });
        }
    })
    .catch(error => {
        // Maneja el error aquí
        console.error("Error:", error);
        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
    });
}

function cargarInputSelectLocalidad(selectlocalidad, jsonProvincia) {
    const url = `api/localidad/localidades?jsonProvincia=${jsonProvincia}`;
    solicitudGet(url, idSelectLocalidad, false)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    fillInputSelect(selectlocalidad, response.data, 'Todas');
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


function fillInputSelect(nodeInputSelect, datos, optionGenerico) {
    
    /*
     * OTRA FORMA:
    // Crear el contenido HTML con todas las opciones
    let optionsHTML = datos.map(dato => `<option value="${dato.id}">${dato.nombre}</option>`).join('');

    // Asignar el contenido HTML directamente al select, reemplazando cualquier contenido existente
    nodeInputSelect.innerHTML = optionsHTML;
    */
    
    if(optionGenerico) {
        nodeInputSelect.innerHTML = `<option value="${-1}">${optionGenerico}</option>`;
    } else {
        nodeInputSelect.innerHTML = '';
    }
    
    const fragment = document.createDocumentFragment(); // Crear un fragmento de documento.
    datos.forEach((dato) => {
        const elementOption = document.createElement('OPTION');
        elementOption.setAttribute('value', dato.id);
        elementOption.textContent = dato.nombre;
        fragment.appendChild(elementOption);
    });
    
    nodeInputSelect.appendChild(fragment);
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
    //Selecciona el cuerpo del <tbody> de la tabla utilizando el ID de la tabla
    const cuerpoTablaDirecciones = document.querySelector(`${idTablaDirecciones} tbody`);
    console.log(direcciones);
    //Crear el contenido HTML de todas las filas a partir de los datos de direcciones
    let filasHTML = direcciones.map(direccion => {
        return `<tr id="${direccion.id}">
                <td>${direccion.id}</td>
                <td>${direccion.calle}</td>
                <td>${direccion.numero}</td>
                <td>${(direccion.codigoPostal !== 0) ? direccion.codigoPostal : '' }</td>
                <td>${direccion.localidad.nombre}</td>
                <td>${direccion.localidad.provincia.nombre}</td>
                </tr>`;
    }).join('');
    
    
    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaDirecciones.innerHTML = filasHTML;
    
    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaDirecciones);
}
