
import { solicitudGet, getDatosForm } from './comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from './alertasSweetAlert2.mjs';

const idSelectProvincia = "#provincia";
const idSelectLocalidad = "#localidad";
const idFormBusquedaDireccion = "#frmBuscarDireccion";
const idTablaDirecciones = "#tablaDireciones";
const idBtnBuscar = "#btnBuscar";

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

function rellenarTablaDirecciones(direcciones) {
    
    const cuerpoTablaDirecciones = document.querySelector(`${idTablaDirecciones} tbody`);
    
    //Crear el contenido HTML con todas las opciones
    let filasHTML = direcciones.map(direccion => {
        return `<tr id="${direccion.id}">
                <td data-name="id">${direccion.id}</td>
                <td data-name="calle">${direccion.calle}</td>
                <td data-name="numero">${direccion.numero}</td>
                <td data-name="codigoPostal">${(direccion.codigoPostal !== 0) ? direccion.codigoPostal : '' }</td>
                <td data-name="localidad" data-value="${direccion.localidad.id}">${direccion.localidad.nombre}</td>
                <td data-name="provincia" data-value="${direccion.localidad.provincia.id}">${direccion.localidad.provincia.nombre}</td>
                </tr>`;
    }).join('');
    
    // Asignar el contenido HTML directamente al select, reemplazando cualquier contenido existente
    cuerpoTablaDirecciones.innerHTML = filasHTML;
}

function rellenarTablaDirecciones2(direcciones) {
    const cuerpoTablaDirecciones = document.querySelector(`${idTablaDirecciones} tbody`);
    
    // Crear un fragmento de documento para almacenar las filas
    const fragment = document.createDocumentFragment();
    
    // Iterar sobre cada dirección para crear las filas
    direcciones.forEach(direccion => {
        // Crear el elemento tr para cada dirección
        const tr = document.createElement('tr');
        tr.id = direccion.id;
        
        // Crear y añadir cada celda td
        const tdId = document.createElement('td');
        tdId.setAttribute('data-name', 'id');
        tdId.textContent = direccion.id;
        tr.appendChild(tdId);

        const tdCalle = document.createElement('td');
        tdCalle.setAttribute('data-name', 'calle');
        tdCalle.textContent = direccion.calle;
        tr.appendChild(tdCalle);

        const tdNumero = document.createElement('td');
        tdNumero.setAttribute('data-name', 'numero');
        tdNumero.textContent = direccion.numero;
        tr.appendChild(tdNumero);

        const tdCodigoPostal = document.createElement('td');
        tdCodigoPostal.setAttribute('data-name', 'codigoPostal');
        tdCodigoPostal.textContent = (direccion.codigoPostal !== 0) ? direccion.codigoPostal : '';
        tr.appendChild(tdCodigoPostal);

        const tdLocalidad = document.createElement('td');
        tdLocalidad.setAttribute('data-name', 'localidad');
        tdLocalidad.setAttribute('data-value', direccion.localidad.id);
        tdLocalidad.textContent = direccion.localidad.nombre;
        tr.appendChild(tdLocalidad);

        const tdProvincia = document.createElement('td');
        tdProvincia.setAttribute('data-name', 'provincia');
        tdProvincia.setAttribute('data-value', direccion.localidad.provincia.id);
        tdProvincia.textContent = direccion.localidad.provincia.nombre;
        tr.appendChild(tdProvincia);

        // Añadir el elemento tr al fragmento
        fragment.appendChild(tr);
    });
    
    // Limpiar el contenido actual del tbody
    cuerpoTablaDirecciones.innerHTML = '';
    
    // Añadir el fragmento al tbody de la tabla
    cuerpoTablaDirecciones.appendChild(fragment);
}

