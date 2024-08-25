
import { solicitudGet } from './comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from './alertasSweetAlert2.mjs';

const idSelectProvincia = "#provincia";
const idSelectLocalidad = "#localidad";

$(document).ready(function () {
    const selectProvincia = document.querySelector(idSelectProvincia);
    const selectlocalidad = document.querySelector(idSelectLocalidad);
    
    cargarInputSelectProvincia(selectProvincia, selectlocalidad);
});


function cargarInputSelectProvincia(nodoInputSelectProvincia, selectlocalidad) {
    solicitudGet("api/provincia/provincias", idSelectProvincia, false)
        .then(response => {
        if (response.isError === 1) {
            mostrarMensajeError("Se ha producido un error", response.result);
        } else {
            console.log(response);
            console.log(response.data);
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
                    console.log(response);
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


