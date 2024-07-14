package io.proinstala.wherefind.shared.controllers.actions;

public record ActionController(String fullUri, String uri, Object actionType, String[] parametros, ActionServer server) {

}
