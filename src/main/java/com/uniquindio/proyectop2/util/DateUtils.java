package com.uniquindio.proyectop2.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String formatearFecha(LocalDate fecha) {
        return fecha != null ? fecha.format(DATE_FORMATTER) : "";
    }

    public static String formatearFechaHora(LocalDateTime fechaHora) {
        return fechaHora != null ? fechaHora.format(DATETIME_FORMATTER) : "";
    }

    public static LocalDate parsearFecha(String fechaStr) {
        return LocalDate.parse(fechaStr, DATE_FORMATTER);
    }

    public static LocalDateTime parsearFechaHora(String fechaHoraStr) {
        return LocalDateTime.parse(fechaHoraStr, DATETIME_FORMATTER);
    }
}
