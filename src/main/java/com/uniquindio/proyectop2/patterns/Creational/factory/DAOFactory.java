package com.uniquindio.proyectop2.patterns.Creational.factory;

import com.uniquindio.proyectop2.dao.impl.AsientoDAOImpl;
import com.uniquindio.proyectop2.dao.impl.CompraDAOImpl;
import com.uniquindio.proyectop2.dao.impl.EntradaDAOImpl;
import com.uniquindio.proyectop2.dao.impl.EventoDAOImpl;
import com.uniquindio.proyectop2.dao.impl.MetodoPagoDAOImpl;
import com.uniquindio.proyectop2.dao.impl.RecintoDAOImpl;
import com.uniquindio.proyectop2.dao.impl.ServicioAdicionalDAOImpl;
import com.uniquindio.proyectop2.dao.impl.UsuarioDAOImpl;
import com.uniquindio.proyectop2.dao.impl.ZonaDAOImpl;
import com.uniquindio.proyectop2.dao.interfaces.AsientoDAO;
import com.uniquindio.proyectop2.dao.interfaces.CompraDAO;
import com.uniquindio.proyectop2.dao.interfaces.EntradaDAO;
import com.uniquindio.proyectop2.dao.interfaces.EventoDAO;
import com.uniquindio.proyectop2.dao.interfaces.MetodoPagoDAO;
import com.uniquindio.proyectop2.dao.interfaces.RecintoDAO;
import com.uniquindio.proyectop2.dao.interfaces.ServicioAdicionalDAO;
import com.uniquindio.proyectop2.dao.interfaces.UsuarioDAO;
import com.uniquindio.proyectop2.dao.interfaces.ZonaDAO;
import com.uniquindio.proyectop2.service.impl.AdminServiceImpl;
import com.uniquindio.proyectop2.service.impl.ReporteServiceImpl;
import com.uniquindio.proyectop2.service.interfaces.AdminService;
import com.uniquindio.proyectop2.service.interfaces.ReporteService;

public final class DAOFactory {

    private DAOFactory() {
    }

    public static UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAOImpl();
    }

    public static UsuarioDAO crearUsuarioDAO() {
        return getUsuarioDAO();
    }

    public static UsuarioDAO obtenerUsuarioDAO() {
        return getUsuarioDAO();
    }

    public static MetodoPagoDAO getMetodoPagoDAO() {
        return new MetodoPagoDAOImpl();
    }

    public static MetodoPagoDAO crearMetodoPagoDAO() {
        return getMetodoPagoDAO();
    }

    public static MetodoPagoDAO obtenerMetodoPagoDAO() {
        return getMetodoPagoDAO();
    }

    public static RecintoDAO getRecintoDAO() {
        return new RecintoDAOImpl();
    }

    public static RecintoDAO crearRecintoDAO() {
        return getRecintoDAO();
    }

    public static RecintoDAO obtenerRecintoDAO() {
        return getRecintoDAO();
    }

    public static EventoDAO getEventoDAO() {
        return new EventoDAOImpl();
    }

    public static EventoDAO crearEventoDAO() {
        return getEventoDAO();
    }

    public static EventoDAO obtenerEventoDAO() {
        return getEventoDAO();
    }

    public static ZonaDAO getZonaDAO() {
        return new ZonaDAOImpl();
    }

    public static ZonaDAO crearZonaDAO() {
        return getZonaDAO();
    }

    public static ZonaDAO obtenerZonaDAO() {
        return getZonaDAO();
    }

    public static AsientoDAO getAsientoDAO() {
        return new AsientoDAOImpl();
    }

    public static AsientoDAO crearAsientoDAO() {
        return getAsientoDAO();
    }

    public static AsientoDAO obtenerAsientoDAO() {
        return getAsientoDAO();
    }

    public static CompraDAO getCompraDAO() {
        return new CompraDAOImpl();
    }

    public static CompraDAO crearCompraDAO() {
        return getCompraDAO();
    }

    public static CompraDAO obtenerCompraDAO() {
        return getCompraDAO();
    }

    public static EntradaDAO getEntradaDAO() {
        return new EntradaDAOImpl();
    }

    public static EntradaDAO crearEntradaDAO() {
        return getEntradaDAO();
    }

    public static EntradaDAO obtenerEntradaDAO() {
        return getEntradaDAO();
    }

    public static ServicioAdicionalDAO getServicioAdicionalDAO() {
        return new ServicioAdicionalDAOImpl();
    }

    public static ServicioAdicionalDAO crearServicioAdicionalDAO() {
        return getServicioAdicionalDAO();
    }

    public static ServicioAdicionalDAO obtenerServicioAdicionalDAO() {
        return getServicioAdicionalDAO();
    }

    public static AdminService getAdminService() {
        return new AdminServiceImpl(getUsuarioDAO(), getRecintoDAO());
    }

    public static AdminService crearAdminService() {
        return getAdminService();
    }

    public static AdminService obtenerAdminService() {
        return getAdminService();
    }

    public static ReporteService getReporteService() {
        return new ReporteServiceImpl();
    }

    public static ReporteService crearReporteService() {
        return getReporteService();
    }

    public static ReporteService obtenerReporteService() {
        return getReporteService();
    }
}
