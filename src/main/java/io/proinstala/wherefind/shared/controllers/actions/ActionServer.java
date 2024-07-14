package io.proinstala.wherefind.shared.controllers.actions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public record ActionServer(HttpServletRequest request, HttpServletResponse response) {

}
